package cn.lanqiushe.ui;

import cn.lanqiushe.App;
import android.app.Activity;
import android.os.Bundle;

public class BaseActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		((App) getApplication()).activitys.add(this);
		findViews();
		init();
		setListener();
	}
	protected void findViews(){}
	protected void init(){}
	protected void setListener(){}
}
