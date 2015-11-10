package cn.lanqiushe.ui;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.HashMap;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import com.lanqiushe.auth.Constants;
import com.lanqiushe.auth.SinaWeiBoManager;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQAuth;
import com.tencent.tauth.IRequestListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.tencent.utils.HttpUtils.HttpStatusException;
import com.tencent.utils.HttpUtils.NetworkUnavailableException;

import cn.lanqiushe.engine.DataService;
import cn.lanqiushe.entity.User;
import cn.lanqiushe.manager.ConstantManager;
import cn.lanqiushe.manager.LogManager;
import cn.lanqiushe.manager.StringManager;
import cn.lanqiushe.manager.ThreadPool;
import cn.lanqiushe.manager.ToastManager;
import cn.lanqiushe.manager.UIManager;
import cn.lanqiushe.App;
import cn.lanqiushe.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

/**
 * QQ �пͻ���ͨ���� �޿ͻ��˻��޷���Ȩ weibo �޿ͻ�����Ȩͨ���� �пͻ���ͨ��
 * 
 * @author lee
 * 
 */
public class LoginAndRegisterActivity extends BaseActivity {
	// ����΢����Ȩ�ɹ��󷵻ص�toketn
	private Oauth2AccessToken mAccessToken;
	private QQAuth mQQAuth;
	private Dialog loadDialog;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			UIManager.toggleDialog(loadDialog);
			switch (msg.what) {
			case ConstantManager.FAIL_SERVER_ERROR:
			case ConstantManager.FAIL_NO_NET:
				ToastManager.show(LoginAndRegisterActivity.this,
						R.string.toast_un_net);
				break;
			case ConstantManager.SUCCESS_1: {
				if (msg.arg1 == ConstantManager.LOGIN_QQ) {// QQ ��Ȩ��֤
					if (msg.obj instanceof User) {// �ǵ�һ����Ȩ
						// ����˷��ص�ǰ��¼�û�����Ϣ
						UIManager.switcher(LoginAndRegisterActivity.this,MainActivity.class);
					} else {// ��һ����Ȩ
							// ��һ����Ȩ��ô��Ҫ��ȡ�û�����Ϣ��
						UserInfo mInfo = new UserInfo(
								LoginAndRegisterActivity.this,
								mQQAuth.getQQToken());
						mInfo.getUserInfo(new QQRequestListener(
								"get_simple_userinfo"));
					}
				} else {// ����Ψһ����֤
					if (msg.obj instanceof User) {//�ǵ�һ����Ȩ
						UIManager.switcher(LoginAndRegisterActivity.this,
								MainActivity.class);
						//�ǵ�һ����Ȩ�����ǻ���Ҫ��������һ�ε�token
						//�������,ÿ����ȨmAccessToken�ǻ��ģ�����uid �Ǻ��û�Ψһ��
					} else {
						UsersAPI weiboUser = new UsersAPI(mAccessToken);
						weiboUser.show(Long.parseLong(mAccessToken.getUid()),
								new GetWeiboUserInfoListener());
					}
				}
				break;
			}
			case ConstantManager.LOADING_1:
				int resId = (Integer) msg.obj;
				loadDialog = UIManager.getLoadingDialog(
						LoginAndRegisterActivity.this,resId);
				loadDialog.show();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.layout_login_and_register);
		
	 
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void init() {
		// �˳��˺ź�ص������������
		if (!DataService.isNetworkConnected(getApplication())) {
			// ����δ����
			UIManager.getCommWarnDialog(this, R.string.dialog_no_net,
					R.string.dialog_go_setting, new OnClickListener() {

						@Override
						public void onClick(View v) {
							// ȥ�����������
							// startActivity(new
							// Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
							// ȥwifi����
							startActivity(new Intent(
									android.provider.Settings.ACTION_WIFI_SETTINGS));
						}
					}).show();
		}

		// UpdateManager manager = new
		// UpdateManager(LoginAndRegisterActivity.this);

		// ����������
		// manager.checkUpdate();

		super.init();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_and_register_sina_ll: {
			mSsoHandler = SinaWeiBoManager.authorize(this, new AuthListener());
			break;
		}
		case R.id.login_and_register_qq_ll: {
			QQlogin();
			break;
		}
		case R.id.login_and_register_login_bt: {
			UIManager.switcher(this, LoginActivity.class);
			break;
		}
		case R.id.login_and_register_register_bt: {
			HashMap<String, Object> map  =new HashMap<String, Object>();
			map.put("type", RInputCellphoneActivity.NEW_USER);
			UIManager.switcher(this, RInputCellphoneActivity.class,map);
			break;
		}

		}
	}

	// ------------------qq-------------------------

	private void QQlogin() {
		mQQAuth = QQAuth.createInstance(Constants.APP_ID,
				getApplicationContext());
		Tencent mTencent = Tencent.createInstance(Constants.APP_ID, this);
		if (!mQQAuth.isSessionValid()) {
			mTencent.login(this, "all", new QQRequestListener());
		}
	}

	class QQRequestListener implements IUiListener {
		private String api;

		public QQRequestListener() {
		}

		public QQRequestListener(String api) {
			this.api = api;
		}

		@Override
		public void onCancel() {
		}

		@Override
		public void onComplete(Object arg0) {
			LogManager.e("xx", "----��Ȩ-----" + arg0);
			if (StringManager.isEmpty(api)) {// ���Ϊnull, ��ʾ����Ȩ��Ӧ
				JSONObject obj = (JSONObject) arg0;
				String openid = obj.optString("openid");
				// ͨ�����Ψһopenid�ж���ǰ�Ƿ���Ȩ����
				DataService.verificationAuthorize(openid,
						ConstantManager.LOGIN_QQ,
						LoginAndRegisterActivity.this, handler);
			} else {
				// ���ǻ�ȡ�û���Ϣ
				LogManager.e("xx", "�û���Ϣ��ȡ�ɹ�");
				JSONObject qqUserInfoJson = (JSONObject) arg0;
				User user = new User();
				user.nickName = qqUserInfoJson.optString("nickname");
				// user.portrait = qqUserInfoJson.optString("");
				user.portrait = "http://ubmcmm.baidustatic.com/media/v1/0f0002shjSFoh-mXNXP8pf.jpg";
				user.gender = qqUserInfoJson.optString("gender");
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("user", user);
				UIManager.switcher(LoginAndRegisterActivity.this,RPerfectDataActivity.class, map);
			}
		}

		@Override
		public void onError(UiError arg0) {

			LogManager.e("xx", "���ݻ�ȡʧ�ܡ�������" + arg0.toString() + "");
		}

	}

	// -------------------------weibo ------------------------------------
	private SsoHandler mSsoHandler;

	/**
	 * ΢����֤��Ȩ�ص��ࡣ 1. SSO ��Ȩʱ����Ҫ�� {@link #onActivityResult} �е���
	 * {@link SsoHandler#authorizeCallBack} �� �ûص��Żᱻִ�С� 2. �� SSO
	 * ��Ȩʱ������Ȩ�����󣬸ûص��ͻᱻִ�С� ����Ȩ�ɹ����뱣��� access_token��expires_in��uid ����Ϣ��
	 * SharedPreferences �С�
	 */
	class AuthListener implements WeiboAuthListener {
		@Override
		public void onComplete(Bundle values) {
			LogManager.e("xx", "��Ȩ----" + values);
			// �� Bundle �н��� Token
			mAccessToken = Oauth2AccessToken.parseAccessToken(values);
			if (mAccessToken.isSessionValid()) {
				// SinaWeiBoManager.mAccessToken = mAccessToken;
				// ��Ȩ�ɹ���1������û�ͷ�� 2���û��ǳ� 3����һ����Ȩ�ɹ���ת���������Ͻ���
				String uid = mAccessToken.getUid();
				
				// ��֤��΢����ǰ�Ƿ���Ȩ������ǰ��Ȩ�����Ͳ�����д����������
				DataService.verificationAuthorize(uid,
						ConstantManager.LOGIN_WEIBO,
						LoginAndRegisterActivity.this, handler);
			} else {
				ToastManager.show(LoginAndRegisterActivity.this,R.string.toast_authorize_fail);
			}
		}

		@Override
		public void onCancel() {
		}

		@Override
		public void onWeiboException(WeiboException e) {
			ToastManager.show(LoginAndRegisterActivity.this,
					R.string.toast_authorize_fail);
		}
	}

	class GetWeiboUserInfoListener implements RequestListener {

		@Override
		public void onComplete(String arg0) {
			try {
				LogManager.e("xx", "userinfo----" + arg0);
				JSONObject weiboUserInfoJson = new JSONObject(arg0);
				User user = new User();
				user.nickName = weiboUserInfoJson.optString("screen_name");
				user.portrait = weiboUserInfoJson.optString("profile_image_url");
				user.gender = "m".equals(weiboUserInfoJson.optString("gender")) ? "��" : "Ů";
				user.expiresIn = String.valueOf(mAccessToken.getExpiresTime());
				user.accessToken = mAccessToken.getToken();
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("user", user);
				UIManager.switcher(LoginAndRegisterActivity.this,RPerfectDataActivity.class, map);

			} catch (JSONException e) {
				ToastManager.show(LoginAndRegisterActivity.this,R.string.toast_authorize_fail);
			}

		}

		@Override
		public void onWeiboException(WeiboException arg0) {
			// TODO Auto-generated method stub
			ToastManager.show(LoginAndRegisterActivity.this,
					R.string.toast_authorize_fail);
		}

	}

	/**
	 * �� SSO ��Ȩ Activity �˳�ʱ���ú��������á�
	 * 
	 * @see {@link Activity#onActivityResult}
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// SSO ��Ȩ�ص�
		// ��Ҫ������ SSO ��½�� Activity ������д onActivityResult
		if (mSsoHandler != null) {
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

}
