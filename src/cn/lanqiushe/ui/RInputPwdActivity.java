 
package cn.lanqiushe.ui;

import java.util.HashMap;

import cn.lanqiushe.engine.DataService;
import cn.lanqiushe.manager.SecurityManager;
import cn.lanqiushe.manager.StringManager;
import cn.lanqiushe.manager.TitleManager;
import cn.lanqiushe.manager.ToastManager;
import cn.lanqiushe.manager.UIManager;
import cn.lanqiushe.App;
import cn.lanqiushe.R;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class RInputPwdActivity extends BaseActivity implements OnClickListener {
	private EditText mPwdET;
 
	private ImageView mLockIV;
	 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	setContentView(R.layout.layout_register_input_pwd);
		TitleManager.showTitle(this, null, R.string.title_input_pwd, R.string.title_back,
				 R.string.title_next);
    	super.onCreate(savedInstanceState);
    }
    @Override
    protected void findViews() {
    	// TODO Auto-generated method stub
    	 mPwdET  = (EditText) findViewById(R.id.register_input_pwd_et);
    	 mLockIV  = (ImageView) findViewById(R.id.register_input_pwd_lock_iv);
    	super.findViews();
    }
    @Override
    protected void setListener() {
    	((RelativeLayout) findViewById(R.id.register_input_pwd_lock_rl)).setOnClickListener(this);
    	super.setListener();
    }
    public void onClick(View v){
    	switch (v.getId()) {
    	case R.id.register_input_pwd_lock_rl:{
    		StringManager.togglePwd(mPwdET, mLockIV);
    		break;
    	}
		case R.id.title_next_ll:
			String pwd = StringManager.getStringByET(mPwdET);
			if(StringManager.isEmpty(pwd)){
				ToastManager.show(this, R.string.toast_pwd_empty);
				return;
			}
			if(StringManager.isBadPwd(pwd)){
				ToastManager.show(this, R.string.toast_pwd_bad);
				return;
			}
			//这里不需要提交到服务端，和下一个界面中的数据一起提交
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("cellphone", getIntent().getStringExtra("cellphone"));
			map.put("pwd", pwd);
			int type  =getIntent().getIntExtra("type", -1);
			if(type!=-1){
				if(type==RInputCellphoneActivity.NEW_USER){
					UIManager.switcher(this, RPerfectDataActivity.class,map);
				}else{
					App app =  (App) getApplication();
					app.exit(App.EXIT_APP);
					UIManager.switcher(this, LoginAndRegisterActivity.class);
				}
			}
			
			break;

	 
		}
    }
}
