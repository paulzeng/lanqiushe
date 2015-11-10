 
package cn.lanqiushe.ui;

import cn.lanqiushe.engine.DataService;
import cn.lanqiushe.entity.User;
import cn.lanqiushe.manager.ConstantManager;
import cn.lanqiushe.manager.StringManager;
import cn.lanqiushe.manager.TitleManager;
import cn.lanqiushe.manager.ToastManager;
import cn.lanqiushe.manager.UIManager;
import cn.lanqiushe.App;
import cn.lanqiushe.R;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;

public class SetBindPhoneActivity extends BaseActivity {
	private EditText mPhone,mPwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	setContentView(R.layout.layout_set_bind_mobile_phone);
    	
    	
    	TitleManager.showTitle(this, null, R.string.title_bindMB, R.string.title_my,
				 R.string.title_sure);
    	super.onCreate(savedInstanceState);
    }
    @Override
    protected void init() {
    	mPhone = (EditText) findViewById(R.id.bind_mb_input_phone_et);
    	mPwd = (EditText) findViewById(R.id.bind_mb_input_pwd_et);
    	super.init();
    }
    public void onClick(View v){
    	String phone = StringManager.getStringByET(mPhone);
		String pwd = StringManager.getStringByET(mPwd);
		
		if(StringManager.isEmpty(phone)){
        	ToastManager.show(this, R.string.toast_cellphone_empty);
        	return;
        }
        if(StringManager.isBadCellphone(phone)){
        	ToastManager.show(this, R.string.toast_cellphone_bad);
        	return;
        }
        if(StringManager.isEmpty(pwd)){
        	ToastManager.show(this, R.string.toast_pwd_empty);
        	return;
        }
        if(StringManager.isBadPwd(pwd)){
        	ToastManager.show(this, R.string.toast_pwd_bad);
        	return;
        }
        DataService.bindPhone(this,phone,pwd, handler);
    }
    
    private Handler handler  = new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		switch (msg.what) {
			case ConstantManager.SUCCESS_1:{
				  App app = (App) getApplication();
				  User user = app.getUser(); 
				  user.phone = (String) msg.obj;
				  
				  app.setUser(user);
				  Intent  i = new Intent(MeSetActivity.ACTION);
				  sendBroadcast(i);
				  UIManager.switcher(SetBindPhoneActivity.this, AskOpenContactActivity.class);
				  finish();
			break;
			}
			default:
				break;
			}
    	};
    };
}
