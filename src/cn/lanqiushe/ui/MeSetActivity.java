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
		 //���ڽ����ֻ�����΢���Ľ����Ϣ
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
	 * ���ð󶨵���Ϣ,�൱��ˢ���쳣����
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
			// ����΢��
			// �жϰ�û��
			if (user.isBindWeibo()) {
				bindWeiboAfterSwitch();
			} else {
				  mSsoHandler =  SinaWeiBoManager.authorize(this, new AuthListener());
			}
			break;
		}
		case R.id.item_set_phone_rl: {
			if(user.isBindPhone()){//����
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put(FROM, PHONE);
				UIManager.switcher(this, MBContactsActivity.class, map);
			} else {//��û�а�
				UIManager.switcher(this, SetBindPhoneActivity.class);
			}

			break;
		}
		case R.id.item_set_alertpwd_rl: {// �޸�����
			UIManager.switcher(this, SetAlterPwdActivity.class);
			break;
		}
		case R.id.item_set_aboutus_rl: {// ��������
			UIManager.switcher(this, SetAboutUsActivity.class);
			break;
		}

		case R.id.item_set_mm_rl: {// ��ϵ�ͷ�
			HashMap<String, Object> map  =new HashMap<String, Object>();
			ChatMsg chat = new ChatMsg();
			chat.name = "�ͷ�";
			map.put("chat", chat);
			UIManager.switcher(this, ChatActivity.class,map);
			break;
		}

		case R.id.item_set_good_rl: {// ����
			Intent intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("market://details?id=" + getPackageName()));
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;
		}
		case R.id.item_set_opencon_rl: {// ����ͨѶ¼
		
			
			 
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
				//��Ȩ�ɹ��������û���Ϣ��post���ݸ������
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
//			// ���ɹ�
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
	@Override
	protected void onDestroy() {
		unregisterReceiver(bindReceiver);
		super.onDestroy();
	}
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(user.isBindPhone()){//����
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
