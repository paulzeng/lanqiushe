package cn.lanqiushe.ui;

import java.util.HashMap;

import cn.lanqiushe.App;
import cn.lanqiushe.R;
import cn.lanqiushe.engine.DataService;
import cn.lanqiushe.entity.User;
import cn.lanqiushe.manager.ConstantManager;
import cn.lanqiushe.manager.StringManager;
import cn.lanqiushe.manager.TitleManager;
import cn.lanqiushe.manager.ToastManager;
import cn.lanqiushe.manager.UIManager;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;

public class ChangePhoneNumberActivity extends BaseActivity {
	private EditText mOldPhone, mNewPhone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.layout_change_phone);

		TitleManager.showTitle(this, null, R.string.title_change_phone,
				R.string.title_back, R.string.title_sure);

		super.onCreate(savedInstanceState);
	}

	@Override
	protected void init() {
		mOldPhone = (EditText) findViewById(R.id.change_phone_old_et);
		mNewPhone = (EditText) findViewById(R.id.change_phone_new_et);
		super.init();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_next_ll:
			String oldPhone = StringManager.getStringByET(mOldPhone);
			String newPhone = StringManager.getStringByET(mNewPhone);
			if (StringManager.isEmpty(oldPhone)) {
				ToastManager.show(this, R.string.toast_cellphone_empty);
				return;
			}
			if (StringManager.isBadCellphone(newPhone)) {
				ToastManager.show(this, R.string.toast_oldpwd_bad);
				return;
			}
			if (StringManager.isEmpty(newPhone)) {
				ToastManager.show(this, R.string.toast_cellphone_empty);
				return;
			}
			if (StringManager.isBadCellphone(newPhone)) {
				ToastManager.show(this, R.string.toast_newpwd_bad);
				return;
			}
			HashMap<String, Object> map  =  new HashMap<String, Object>();
			DataService.changePhone(map, this, handler);
			break;

		}
	}
	private Dialog loadDialog;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			UIManager.toggleDialog(loadDialog);
			switch (msg.what) {
			case ConstantManager.SUCCESS_1:
				App app = (App) getApplication();
				app.exit(App.EXIT_ACCOUNT);
				UIManager.switcher(ChangePhoneNumberActivity.this, LoginAndRegisterActivity.class);
				break;
			case ConstantManager.FAIL_CODE_ERROR:{
				ToastManager.show(ChangePhoneNumberActivity.this,(String)msg.obj);
				break;
			}
			case ConstantManager.FAIL_SERVER_ERROR:
			case ConstantManager.FAIL_NO_NET: {
				ToastManager.show(ChangePhoneNumberActivity.this, R.string.toast_un_net);
				break;
			}
			case ConstantManager.LOADING_1: {
				loadDialog = UIManager.getLoadingDialog(ChangePhoneNumberActivity.this);
				loadDialog.show();
				break;
			}
			}
		};
	};
}
