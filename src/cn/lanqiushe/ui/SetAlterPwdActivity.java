package cn.lanqiushe.ui;

import java.util.HashMap;

import cn.lanqiushe.engine.DataService;
import cn.lanqiushe.manager.ConstantManager;
import cn.lanqiushe.manager.StringManager;
import cn.lanqiushe.manager.TitleManager;
import cn.lanqiushe.manager.ToastManager;
import cn.lanqiushe.manager.UIManager;

import cn.lanqiushe.App;
import cn.lanqiushe.R;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class SetAlterPwdActivity extends BaseActivity {
	private EditText mOldPwdET, mNewPwdET;
	private ImageView mOldLockIV, mNewLockIV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setContentView(R.layout.layout_set_alter_pwd);
		TitleManager.showTitle(this, null, R.string.title_alert_pwd,
				R.string.title_back, R.string.title_finish);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void init() {
		mOldPwdET = (EditText) findViewById(R.id.alert_pwd_old_et);
		mNewPwdET = (EditText) findViewById(R.id.alert_pwd_new_et);
		mOldLockIV = (ImageView) findViewById(R.id.set_alter_old_lock_iv);
		mNewLockIV = (ImageView) findViewById(R.id.set_alter_new_lock_iv);
		super.init();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.set_alter_old_lock_rl:
			StringManager.togglePwd(mOldPwdET, mOldLockIV);
			break;
		case R.id.set_alter_new_lock_rl:
			StringManager.togglePwd(mNewPwdET, mNewLockIV);
			break;
		case R.id.title_next_ll: {
			String oldPwd = StringManager.getStringByET(mOldPwdET);
			String newPwd = StringManager.getStringByET(mNewPwdET);
			if (StringManager.isEmpty(oldPwd)) {
				ToastManager.show(this, R.string.toast_oldpwd_empty);
				return;
			}
			if (StringManager.isBadPwd(newPwd)) {
				ToastManager.show(this, R.string.toast_oldpwd_bad);
				return;
			}
			if (StringManager.isEmpty(newPwd)) {
				ToastManager.show(this, R.string.toast_newpwd_empty);
				return;
			}
			if (StringManager.isBadPwd(newPwd)) {
				ToastManager.show(this, R.string.toast_newpwd_bad);
				return;
			}
            HashMap<String, Object> map  = new HashMap<String, Object>();
            App app =  (App)getApplication() ;
            map.put("userId", app.getUser().userId);
            map.put("forderPwd",oldPwd);
            map.put("newPwd", newPwd);
			DataService.alertPwd(map, this, handler);
			break;
		}
		}

	}
	private Dialog loadDialog;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			UIManager.toggleDialog(loadDialog);
			switch (msg.what) {
			case ConstantManager.SUCCESS_1:
				 App app = (App) getApplication();
				 app.exit(App.EXIT_ALERT_PWD);
				 UIManager.switcher(SetAlterPwdActivity.this, LoginAndRegisterActivity.class);
				 break;

			case 	ConstantManager.FAIL_CODE_ERROR:{
				String text = (String) msg.obj;
				ToastManager.show(SetAlterPwdActivity.this, text);
				break;
			}
			case ConstantManager.FAIL_SERVER_ERROR:
			case ConstantManager.FAIL_NO_NET: {
				ToastManager.show(SetAlterPwdActivity.this, R.string.toast_un_net);
				break;
			}
			case ConstantManager.LOADING_1: {
				loadDialog = UIManager.getLoadingDialog(SetAlterPwdActivity.this);
				loadDialog.show();
				break;
			}
			}
		};
	}; 
}
