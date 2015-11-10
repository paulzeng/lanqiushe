package cn.lanqiushe.ui;

import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import cn.lanqiushe.engine.DataService;
import cn.lanqiushe.manager.ConstantManager;
import cn.lanqiushe.manager.LogManager;
import cn.lanqiushe.manager.StringManager;
import cn.lanqiushe.manager.TitleManager;
import cn.lanqiushe.manager.ToastManager;
import cn.lanqiushe.manager.UIManager;

import cn.lanqiushe.R;

public class RInputVerificationCodeActivity extends BaseActivity implements
		OnClickListener {
	private EditText mVerificationCode;
	private Button mReGet;
	private Timer timer;
	private TimerTask timerTask;
	private String cellphone;
	private HashMap<String,Object> map;
	private static final int UPDATE_TIME = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.layout_register_input_verification_code);
		TitleManager.showTitle(this, null,
				R.string.title_input_verification_code, R.string.title_back,
				R.string.title_next);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void init(){
		cellphone = getIntent().getStringExtra("cellphone");
		// 给该手机发送验证码
		map = new HashMap<String, Object>();
		map.put("mob", cellphone);
	    DataService.sendVerificationCode(map,this,handler);
	    
	    
	    
		mVerificationCode = (EditText) findViewById(R.id.verification_code_tv);
		mReGet = (Button) findViewById(R.id.verification_code_time_tv);
		timer = new Timer();
		timerTask = getTimerTask();
		timer.scheduleAtFixedRate(timerTask, 1000, 1000);
		super.init();
	}

	@Override
	protected void setListener() {
		mReGet.setOnClickListener(this);
		super.setListener();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_next_ll: {
			String code = StringManager.getStringByET(mVerificationCode);
			if (StringManager.isEmpty(code)) {
				ToastManager.show(this, R.string.toast_verificationcode_empty);
				return;
			}
			if (StringManager.isBadVerificationCode(code)) {
				ToastManager.show(this, R.string.toast_verificationcode_error);
				return;
			}
			HashMap<String, Object> m = new HashMap<String, Object>();
			m.put("mob", cellphone);
			m.put("captcha", code);
            DataService.verification(m, this, handler);
			break;
		}

		case R.id.verification_code_time_tv: {
			mReGet.setEnabled(false);
			mReGet.setClickable(false);
			mReGet.setBackgroundResource(R.drawable.bg_get_verification_enabled);
			mReGet.setTextColor(Color.rgb(255, 255, 255));
			mReGet.setText(String.valueOf(60));
			timerTask = getTimerTask();
			timer.scheduleAtFixedRate(timerTask, 1000, 1000);
			
            DataService.sendVerificationCode(map, this, handler);
			break;
		}
		}
	}

	private Dialog loadDialog;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
		   UIManager.toggleDialog(loadDialog);
			switch (msg.what) {
			case ConstantManager.SUCCESS_1:{
				HashMap<String, Object> map  = new HashMap<String, Object>(); 
				map.put("cellphone", cellphone);
				map.put("type", getIntent().getIntExtra("type", -1));
				UIManager.switcher(RInputVerificationCodeActivity.this, RInputPwdActivity.class,map);
				break;
			}
			case ConstantManager.FAIL_CODE_ERROR:{
				ToastManager.show(RInputVerificationCodeActivity.this,(String) msg.obj );
				break;
			}
			case ConstantManager.FAIL_SERVER_ERROR: 
			case ConstantManager.FAIL_NO_NET:{
				ToastManager.show(RInputVerificationCodeActivity.this, R.string.toast_un_net);
				break;
			}
			case ConstantManager.LOADING_1:{
				loadDialog = UIManager.getLoadingDialog(RInputVerificationCodeActivity.this);
				loadDialog.show();
				break;
			}
			
			//------------
			case UPDATE_TIME:

				int number = Integer
						.valueOf(mReGet.getText().toString().trim());
				if (number == 1) {
					// 显示其他
					mReGet.setBackgroundResource(R.drawable.selector_get_verification);
					// mReGet.setTextColor(Color.rgb(234, 130, 50));
					try {
						Resources resource = (Resources) getBaseContext()
								.getResources();
						ColorStateList csl = (ColorStateList) resource
								.getColorStateList(R.drawable.selector_text_orange);
						mReGet.setTextColor(csl);
					} catch (Exception e) {
						e.printStackTrace();
					}

					mReGet.setText(R.string.again_get_verification_code);
					mReGet.setEnabled(true);
					mReGet.setClickable(true);
					timerTask.cancel();
					timerTask = null;
					return;
				}
				mReGet.setText(String.valueOf(--number));
				break;

			}
		};
	};

	public StateListDrawable newSelector(Context context, int idNormal,
			int idPressed) {
		StateListDrawable bg = new StateListDrawable();
		Drawable normal = idNormal == -1 ? null : context.getResources()
				.getDrawable(idNormal);
		Drawable pressed = idPressed == -1 ? null : context.getResources()
				.getDrawable(idPressed);
		bg.addState(new int[] { android.R.attr.state_pressed, }, pressed); // 当多种状态要呈现同一种效果的时候，可以在数组中加入不同的状态
		// 比如：bg.addState(new int[] {
		// android.R.attr.state_pressed,android.R.attr.state_checked}, pressed);
		bg.addState(new int[] {}, normal);
		return bg;
	}
	private TimerTask getTimerTask() {
		return new TimerTask() {

			@Override
			public void run() {
				handler.sendEmptyMessage(UPDATE_TIME);
			}
		};
	}
}
