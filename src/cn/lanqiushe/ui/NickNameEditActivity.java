package cn.lanqiushe.ui;

import java.util.HashMap;

import cn.lanqiushe.App;
import cn.lanqiushe.R;
import cn.lanqiushe.engine.DataService;
import cn.lanqiushe.entity.User;
import cn.lanqiushe.manager.ConstantManager;
import cn.lanqiushe.manager.MediaManager;
import cn.lanqiushe.manager.StringManager;
import cn.lanqiushe.manager.TitleManager;
import cn.lanqiushe.manager.ToastManager;
import cn.lanqiushe.manager.UIManager;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;

public class NickNameEditActivity extends BaseActivity {
	private EditText mInputNickName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.layout_nickname);
		TitleManager.showTitle(this, null, R.string.title_alert_nickname,
				R.string.title_back, R.string.title_finish);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void findViews() {
		mInputNickName = (EditText) findViewById(R.id.nickname_input_et);
		super.findViews();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.title_next_ll:
			String nickName = StringManager.getStringByET(mInputNickName);
			if (StringManager.isEmpty(nickName)) {
				ToastManager.show(this, R.string.toast_nickname_empty);
				return;
			}
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("nickname", nickName);
			UIManager.resultActivity(NickNameEditActivity.this,MyInfoActivity.ALERT_NAME_REQUESTCODE, map);
			break;
		}
	}
}
