package cn.lanqiushe.ui;

import java.util.HashMap;

import cn.lanqiushe.engine.DataService;
import cn.lanqiushe.manager.ConstantManager;
import cn.lanqiushe.manager.StringManager;
import cn.lanqiushe.manager.TitleManager;
import cn.lanqiushe.manager.ToastManager;
import cn.lanqiushe.manager.UIManager;

import cn.lanqiushe.R;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;

public class RInputCellphoneActivity extends BaseActivity {
	private EditText mCellphoneET;
	public static final int NEW_USER = 0;
	public static final int FORGET_PWD = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.layout_register_input_cellphone);
		TitleManager.showTitle(this, null, R.string.title_input_cellphone,
				R.string.title_back, R.string.title_next);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void findViews() {
		mCellphoneET = (EditText) findViewById(R.id.register_cellphone_et);
		super.findViews();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_next_ll:
			String cellphone = StringManager.getStringByET(mCellphoneET);
			if (StringManager.isEmpty(cellphone)) {
				ToastManager.show(this, R.string.toast_cellphone_empty);
				return;
			}
			if (StringManager.isBadCellphone(cellphone)) {
				ToastManager.show(this, R.string.toast_cellphone_bad);
				return;
			}
			//这里不管是否发送成功。直接跳转
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("cellphone", cellphone);
			map.put("type", getIntent().getIntExtra("type", -1));
			UIManager.switcher(this, RInputVerificationCodeActivity.class, map);
			break;

		}
	}
}
