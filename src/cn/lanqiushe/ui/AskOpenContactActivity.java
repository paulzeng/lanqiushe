package cn.lanqiushe.ui;

import cn.lanqiushe.R;
import cn.lanqiushe.manager.TitleManager;
import cn.lanqiushe.manager.ToastManager;
import cn.lanqiushe.manager.UIManager;
import android.os.Bundle;
import android.view.View;

public class AskOpenContactActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.layout_ask_open_contact);
		TitleManager.showTitle(this, null, 0, 0, R.string.title_skip);
		super.onCreate(savedInstanceState);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.open_contact_bt:
			UIManager.switcher(this, MBContactsActivity.class);
		case R.id.title_next_ll: {
			finish();
			break;
		}

		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
	}
}
