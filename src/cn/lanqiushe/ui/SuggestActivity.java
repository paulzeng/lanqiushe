package cn.lanqiushe.ui;

import cn.lanqiushe.R;
import cn.lanqiushe.manager.TitleManager;
import android.os.Bundle;
import android.view.View;

public class SuggestActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.layout_suggest);
		TitleManager.showTitle(this, null, R.string.title_suggest,
				R.string.title_back, R.string.title_submit);

		super.onCreate(savedInstanceState);
	}
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.title_next_ll:
			
			break;

		 
		}
	}
}
