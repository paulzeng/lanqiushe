package cn.lanqiushe.ui;

import cn.lanqiushe.App;
import cn.lanqiushe.R;
import cn.lanqiushe.entity.User;
import cn.lanqiushe.manager.StringManager;
import cn.lanqiushe.manager.UIManager;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {
	private static final int FIRSTLOGIN = 0;
	private static final int UNFIRSTLOGIN = 1;
	/**
	 * splash界面展示的时间   毫秒为单位
	 */
	private static final int DELAYMILLIS = 10;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case FIRSTLOGIN:
				UIManager.switcher(SplashActivity.this,LoginAndRegisterActivity.class);
				break;
			case UNFIRSTLOGIN: {
				UIManager.switcher(SplashActivity.this, MainActivity.class);
				break;
			}
			}
			finish();
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.layout_splash);
		super.onCreate(savedInstanceState);
		App app = (App) getApplication();
		 if (app.getUser().isEntryLogin()) {
			 handler.sendEmptyMessageDelayed(FIRSTLOGIN, DELAYMILLIS);
		 } else {
			handler.sendEmptyMessageDelayed(UNFIRSTLOGIN, DELAYMILLIS);
		 }

	}
}
