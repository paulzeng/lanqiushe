package cn.lanqiushe.ui;

import java.util.HashMap;

 










import com.lanqiushe.auth.SinaWeiBoManager;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

import cn.lanqiushe.entity.ChatMsg;
import cn.lanqiushe.entity.User;
import cn.lanqiushe.manager.LogManager;
import cn.lanqiushe.manager.TitleManager;
import cn.lanqiushe.manager.ToastManager;
import cn.lanqiushe.manager.UIManager;
import cn.lanqiushe.ui.RShareActivity.AuthListener;
import cn.lanqiushe.App;
import cn.lanqiushe.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;

public class MeSetActivity extends BaseActivity implements   
		OnClickListener, OnCheckedChangeListener {
	public static final int SINA = 0;
	public static final int PHONE = 1;
	public static final String FROM = "from";
	private BindReceiver bindReceiver;
	public static final String ACTION = "bind";
	//sina
	private TextView mSinaStatus,mPhoneStatus;
	private User user;
	private SsoHandler  mSsoHandler;
	
	private CheckBox mOffOn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.layout_set);
		TitleManager.showTitle(this, null, R.string.title_set,R.string.title_my, 0);
		super.onCreate(savedInstanceState);
	}
    @Override
    protected void findViews() {
    	mSinaStatus = (TextView) findViewById(R.id.item_set_sina_status_tv);
    	mPhoneStatus = (TextView) findViewById(R.id.item_set_phone_status_tv);
    	mOffOn = (CheckBox) findViewById(R.id.item_set_opencon_cb);
    	super.findViews();
    }
	@Override
	protected void init() {
		setBindInfo();
		 //用于接收手机或者微博的解绑信息
 		bindReceiver = new BindReceiver();
 		IntentFilter filter = new IntentFilter(ACTION);
 		registerReceiver(bindReceiver, filter);
		super.init();
	}
	
	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		mOffOn.setOnCheckedChangeListener(this);
		super.setListener();
	}
	
	/**
	 * 设置绑定的信息,相当于刷新异常界面
	 */
	private void setBindInfo() {
		App app =  (App)getApplication();
		user =app.getUser();
		if(user.isBindWeibo()){
			mSinaStatus.setText(R.string.bind);
		}else{
			mSinaStatus.setText(R.string.unbind);
		}
		if(user.isBindPhone()){
			mPhoneStatus.setText(R.string.bind);
		}else{
			mPhoneStatus.setText(R.string.unbind);
		}
	}

    
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.item_set_sina_rl: {
			// 新浪微博
			// 判断绑定没有
			if (user.isBindWeibo()) {
				bindWeiboAfterSwitch();
			} else {
				  mSsoHandler =  SinaWeiBoManager.authorize(this, new AuthListener());
			}
			break;
		}
		case R.id.item_set_phone_rl: {
			if(user.isBindPhone()){//绑定了
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put(FROM, PHONE);
				UIManager.switcher(this, MBContactsActivity.class, map);
			} else {//还没有绑定
				UIManager.switcher(this, SetBindPhoneActivity.class);
			}

			break;
		}
		case R.id.item_set_alertpwd_rl: {// 修改密码
			UIManager.switcher(this, SetAlterPwdActivity.class);
			break;
		}
		case R.id.item_set_aboutus_rl: {// 关于我们
			UIManager.switcher(this, SetAboutUsActivity.class);
			break;
		}

		case R.id.item_set_mm_rl: {// 联系客服
			HashMap<String, Object> map  =new HashMap<String, Object>();
			ChatMsg chat = new ChatMsg();
			chat.name = "客服";
			map.put("chat", chat);
			UIManager.switcher(this, ChatActivity.class,map);
			break;
		}

		case R.id.item_set_good_rl: {// 好评
			Intent intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("market://details?id=" + getPackageName()));
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;
		}
		case R.id.item_set_opencon_rl: {// 开启通讯录
		
			
			 
			break;
		}
		case R.id.set_exit_login_bt: {
			App app  = (App) getApplication();
			app.exit(App.EXIT_ACCOUNT);
			UIManager.switcher(this, LoginAndRegisterActivity.class);
			break;
		}

		}

	}
	private void bindWeiboAfterSwitch() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(FROM, SINA);
		UIManager.switcher(this, MBContactsActivity.class, map);
	}

	
	class AuthListener implements WeiboAuthListener {

		@Override
		public void onCancel() {
			LogManager.e("xx","onCancel");
		}

		@Override
		public void onComplete(Bundle values) {
			LogManager.e("xx","onComplete");
			Oauth2AccessToken mAccessToken = Oauth2AccessToken.parseAccessToken(values);
			if (mAccessToken.isSessionValid()) {
				//授权成功。更新用户信息，post数据给服务端
				App app = (App)getApplication();
				User user = app.getUser();
				user.expiresIn = String.valueOf(mAccessToken.getExpiresTime());
				user.accessToken = mAccessToken.getToken();
				user.uid = mAccessToken.getUid();
				app.setUser(user);
				setBindInfo();
				bindWeiboAfterSwitch();
				
			}else{
				ToastManager.show(MeSetActivity.this,R.string.toast_authorize_fail);
			}
		}

		@Override
		public void onWeiboException(WeiboException arg0) {
			LogManager.e("xx","WeiboException");
			ToastManager.show(MeSetActivity.this,R.string.toast_authorize_fail);
		}

	}
	private class BindReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
//			int unbindType = intent.getIntExtra(MBContactsActivity.UNBIND_TYPE,
//					-1);
//			TextView tv = null;
//			// 解绑成功
//			switch (unbindType) {
//			case MBContactsActivity.UNBIND_TYPE_SINA: {
//				tv = (TextView) findViewById(R.id.item_set_sina_status_tv);
//				break;
//			}
//
//			case MBContactsActivity.UNBIND_TYPE_PHONE:
//				tv = (TextView) findViewById(R.id.item_set_phone_status_tv);
//				break;
//
//			}
//			if (tv != null) {
//				tv.setText(R.string.unbind);
//			}
			
			
			setBindInfo();

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
	protected void onDestroy() {
		unregisterReceiver(bindReceiver);
		super.onDestroy();
	}
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(user.isBindPhone()){//绑定了
			 UIManager.switcher(this, MBContactsActivity.class);
		}else{
			Dialog dialog = UIManager.getCommWarnDialog(this, R.string.dialog_unbind_phone,new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					 
					UIManager.switcher(MeSetActivity.this, SetBindPhoneActivity.class);
				}
			});
			dialog.show();
		}
		
		
	}
}
