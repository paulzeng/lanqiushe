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
 * QQ 有客户端通过， 无客户端还无法授权 weibo 无客户端授权通过该 有客户端通过
 * 
 * @author lee
 * 
 */
public class LoginAndRegisterActivity extends BaseActivity {
	// 新浪微博授权成功后返回的toketn
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
				if (msg.arg1 == ConstantManager.LOGIN_QQ) {// QQ 授权验证
					if (msg.obj instanceof User) {// 非第一次授权
						// 服务端返回当前登录用户的信息
						UIManager.switcher(LoginAndRegisterActivity.this,MainActivity.class);
					} else {// 第一次授权
							// 第一次授权那么就要获取用户的信息，
						UserInfo mInfo = new UserInfo(
								LoginAndRegisterActivity.this,
								mQQAuth.getQQToken());
						mInfo.getUserInfo(new QQRequestListener(
								"get_simple_userinfo"));
					}
				} else {// 新浪唯一性验证
					if (msg.obj instanceof User) {//非第一次授权
						UIManager.switcher(LoginAndRegisterActivity.this,
								MainActivity.class);
						//非第一次授权。但是还是要更新最新一次的token
						//存放数据,每次授权mAccessToken是会变的，但是uid 是和用户唯一绑定
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
		// 退出账号后回到这个界面来。
		if (!DataService.isNetworkConnected(getApplication())) {
			// 网络未连接
			UIManager.getCommWarnDialog(this, R.string.dialog_no_net,
					R.string.dialog_go_setting, new OnClickListener() {

						@Override
						public void onClick(View v) {
							// 去无线网络界面
							// startActivity(new
							// Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
							// 去wifi界面
							startActivity(new Intent(
									android.provider.Settings.ACTION_WIFI_SETTINGS));
						}
					}).show();
		}

		// UpdateManager manager = new
		// UpdateManager(LoginAndRegisterActivity.this);

		// 检查软件更新
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
			LogManager.e("xx", "----授权-----" + arg0);
			if (StringManager.isEmpty(api)) {// 如果为null, 表示是授权响应
				JSONObject obj = (JSONObject) arg0;
				String openid = obj.optString("openid");
				// 通过这个唯一openid判断以前是否授权过。
				DataService.verificationAuthorize(openid,
						ConstantManager.LOGIN_QQ,
						LoginAndRegisterActivity.this, handler);
			} else {
				// 这是获取用户信息
				LogManager.e("xx", "用户信息获取成功");
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

			LogManager.e("xx", "数据获取失败。。。。" + arg0.toString() + "");
		}

	}

	// -------------------------weibo ------------------------------------
	private SsoHandler mSsoHandler;

	/**
	 * 微博认证授权回调类。 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用
	 * {@link SsoHandler#authorizeCallBack} 后， 该回调才会被执行。 2. 非 SSO
	 * 授权时，当授权结束后，该回调就会被执行。 当授权成功后，请保存该 access_token、expires_in、uid 等信息到
	 * SharedPreferences 中。
	 */
	class AuthListener implements WeiboAuthListener {
		@Override
		public void onComplete(Bundle values) {
			LogManager.e("xx", "授权----" + values);
			// 从 Bundle 中解析 Token
			mAccessToken = Oauth2AccessToken.parseAccessToken(values);
			if (mAccessToken.isSessionValid()) {
				// SinaWeiBoManager.mAccessToken = mAccessToken;
				// 授权成功后。1、获得用户头像 2、用户昵称 3、第一次授权成功跳转到完善资料界面
				String uid = mAccessToken.getUid();
				
				// 验证该微博以前是否授权过。以前授权过。就不会填写个人资料了
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
				user.gender = "m".equals(weiboUserInfoJson.optString("gender")) ? "男" : "女";
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
	 * 当 SSO 授权 Activity 退出时，该函数被调用。
	 * 
	 * @see {@link Activity#onActivityResult}
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// SSO 授权回调
		// 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResult
		if (mSsoHandler != null) {
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

}
