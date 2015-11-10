package cn.lanqiushe.ui;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import com.lanqiushe.auth.SinaWeiBoManager;
import com.lanqiushe.auth.TencentManager;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;

import cn.lanqiushe.engine.DataService;
import cn.lanqiushe.entity.User;
import cn.lanqiushe.manager.ConstantManager;
import cn.lanqiushe.manager.LogManager;
import cn.lanqiushe.manager.TitleManager;
import cn.lanqiushe.manager.ToastManager;
import cn.lanqiushe.manager.UIManager;
import cn.lanqiushe.App;
import cn.lanqiushe.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class RShareActivity extends BaseActivity implements
		OnCheckedChangeListener {
	private int checkedId = R.id.share_wechat_rb;
	private Dialog loadDialog;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			UIManager.toggleDialog(loadDialog);
			switch (msg.what) {
			case ConstantManager.SUCCESS_2:
				// 登录成功会得到登录用户的信息！！
				UIManager.switcher(RShareActivity.this, MainActivity.class);
				break;
			case ConstantManager.FAIL_SERVER_ERROR:
			case ConstantManager.FAIL_NO_NET: {
				ToastManager.show(RShareActivity.this, R.string.toast_un_net);
				break;
			}
			case ConstantManager.LOADING_2: {
				loadDialog = UIManager.getLoadingDialog(RShareActivity.this);
				loadDialog.show();
				break;
			}
			}
		};
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.layout_register_share);

		TitleManager.showTitle(this, null, R.string.share, 0,
				R.string.title_skip);

		super.onCreate(savedInstanceState);
	}

	 

	@Override
	protected void setListener() {
		((RadioGroup) findViewById(R.id.share_rg))
				.setOnCheckedChangeListener(this);
		super.setListener();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_next_ll: {
 			HashMap<String, Object> map = new HashMap<String, Object>();
 			map.put("userId", getIntent().getStringExtra("userId")); 
 			map.put("loginType", 1);
 			DataService.getUserInfo(map, this, handler);
			break;
		}
		case R.id.register_share_bt: {
			share();
			break;
		}

		}
	}
    private SsoHandler  mSsoHandler;
	private void share(){
		if (checkedId == R.id.share_sina_rb) {
			if(!SinaWeiBoManager.isAuthorize(this)){
				mSsoHandler =SinaWeiBoManager.authorize(this, new AuthListener());
			}else{
				User user = ((App)getApplication()).getUser();
				Oauth2AccessToken accessToken = new Oauth2AccessToken(user.accessToken, user.expiresIn);
				StatusesAPI api = new StatusesAPI(accessToken);
				api.update("分享的内容", "", "", new ShareRequestListener());
			}
		} 
		if (checkedId == R.id.share_wechat_rb) {
			TencentManager.shareToWeChatFriends(this, "分享标题", "分享内容描述");
		}
	}
	
	
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		this.checkedId = checkedId;
	}

	class ShareRequestListener implements RequestListener {

		@Override
		public void onComplete(String arg0) {
			ToastManager
					.show(RShareActivity.this, R.string.toast_share_success);
			UIManager.switcher(RShareActivity.this, MainActivity.class);
		}

		@Override
		public void onWeiboException(WeiboException arg0) {
			ToastManager.show(RShareActivity.this, R.string.toast_share_fail);
		}

	}

	class AuthListener implements WeiboAuthListener {

		@Override
		public void onCancel() {
           LogManager.e("xx", "--------取消");
		}

		@Override
		public void onComplete(Bundle values) {
			  LogManager.e("xx", "--------chengong");
			Oauth2AccessToken mAccessToken = Oauth2AccessToken
					.parseAccessToken(values);
			if (mAccessToken.isSessionValid()) {
				App app = (App) getApplication();
				User user = app.getUser();
				user.expiresIn = String.valueOf(mAccessToken.getExpiresTime());
				user.accessToken = mAccessToken.getToken();
				 app.setUser(user);
				share();
			}else{
				ToastManager.show(RShareActivity.this,R.string.toast_authorize_fail);
			}
		}

		@Override
		public void onWeiboException(WeiboException arg0) {
			ToastManager.show(RShareActivity.this,R.string.toast_authorize_fail);
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

	@Override
	public void onBackPressed() {
		// 屏蔽返回按钮
		// super.onBackPressed();
	}

}
