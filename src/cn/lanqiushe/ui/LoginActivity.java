package cn.lanqiushe.ui;

 
import java.util.HashMap;

import cn.lanqiushe.engine.DataService;
 
import cn.lanqiushe.entity.User;
import cn.lanqiushe.manager.ConstantManager;
import cn.lanqiushe.manager.ImageManager;
 
import cn.lanqiushe.manager.StringManager;
import cn.lanqiushe.manager.TitleManager;
import cn.lanqiushe.manager.ToastManager;
import cn.lanqiushe.manager.UIManager;
import cn.lanqiushe.App;
import cn.lanqiushe.R;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LoginActivity extends BaseActivity implements OnClickListener {
	private TextView mChangeUserTV, mUserNameTV, mNewUser, mForgetPwd;
	private ImageView mHeadIV, mLockIV;
	private EditText mCellPhoneET, mPwdET;
	private Button mLoginBT;
	private RelativeLayout mTitleRootRL;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.layout_login);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void findViews() {
		mTitleRootRL = (RelativeLayout) findViewById(R.id.title_root_rl);
		mChangeUserTV = (TextView) findViewById(R.id.login_change_user_tv);
		mUserNameTV = (TextView) findViewById(R.id.login_username_tv);
		mHeadIV = (ImageView) findViewById(R.id.login_head_iv);
		mCellPhoneET = (EditText) findViewById(R.id.login_cellphone_et);
		mPwdET = (EditText) findViewById(R.id.login_pwd_et);
		mLoginBT = (Button) findViewById(R.id.login_login_bt);
		mNewUser = (TextView) findViewById(R.id.login_newuser_tv);
		mForgetPwd = (TextView) findViewById(R.id.login_forget_pwd_tv);
		mLockIV = (ImageView) findViewById(R.id.login_lock_iv);
		super.findViews();
	}

	@Override
	protected void init() {
		// 判断是否登录过
		User user = ((App) getApplication()).getUser();
		if (!user.isShowFootprint()) {
			// 为empty
			mCellPhoneET.setVisibility(View.VISIBLE);
			TitleManager.showTitle(this, null, 0, R.string.title_back,
					R.string.title_finish);
		} else {
			mChangeUserTV.setVisibility(View.VISIBLE);
			mHeadIV.setVisibility(View.VISIBLE);
			ImageManager.getInstance().displayImage(user.portrait, mHeadIV, ImageManager.getUserHeadOptions());
			mUserNameTV.setVisibility(View.VISIBLE);
			mUserNameTV.setText(user.nickName
					+ getResources().getString(R.string.welcome));
			mCellPhoneET.setText(user.phone);
			mTitleRootRL.setVisibility(View.GONE);
		}
		super.init();
	}

	@Override
	protected void setListener() {
		mChangeUserTV.setOnClickListener(this);
		mHeadIV.setOnClickListener(this);
		mUserNameTV.setOnClickListener(this);
		mLoginBT.setOnClickListener(this);
		mNewUser.setOnClickListener(this);
		mForgetPwd.setOnClickListener(this);
		super.setListener();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_login_bt:
		case R.id.title_next_ll: {
			// 用户主界面
			String cellphone = StringManager.getStringByET(mCellPhoneET);
			String pwd = StringManager.getStringByET(mPwdET);
			if (mCellPhoneET.isShown()) {// 有用户登录记录的情况下是不用展示电话号码的框框的
											// .只有电话号码框展示的时候才做号码验证
				if (StringManager.isEmpty(cellphone)) {
					ToastManager.show(this, R.string.toast_cellphone_empty);
					return;
				}
				if (StringManager.isBadCellphone(cellphone)) {
					ToastManager.show(this, R.string.toast_cellphone_bad);
					return;
				}
			}
			if (StringManager.isEmpty(pwd)) {
				ToastManager.show(this, R.string.toast_pwd_empty);
				return;
			}
			if (StringManager.isBadPwd(pwd)) {
				ToastManager.show(this, R.string.toast_pwd_bad);
				return;
			}
			// 前面验证通过。用户登录的数据就该保存

			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("mob", cellphone);
			map.put("pwd", pwd);
			map.put("type", ConstantManager.LOGIN_TYPE_MY);
			//经纬度为获取登录的时候的经纬度
			map.put("lng", "112.12");
			map.put("lat", "123.22");

			DataService.login(map, this, handler);
			break;
		}
		case R.id.login_change_user_tv: {
			TitleManager.showTitle(this, null, 0, R.string.title_back,
					R.string.title_finish);
			mTitleRootRL.setVisibility(View.VISIBLE);
			mChangeUserTV.setVisibility(View.GONE);
			mHeadIV.setVisibility(View.GONE);
			mUserNameTV.setVisibility(View.GONE);
			mCellPhoneET.setVisibility(View.VISIBLE);
			break;
		}
		case R.id.login_lock_rl: {
			StringManager.togglePwd(mPwdET, mLockIV);
			break;
		}
		case R.id.login_newuser_tv: {
			HashMap<String, Object> map  =new HashMap<String, Object>();
			map.put("type", RInputCellphoneActivity.NEW_USER);
			UIManager.switcher(this, RInputCellphoneActivity.class,map);
			break;
		}
		case R.id.login_forget_pwd_tv: {
			ToastManager.show(this, "暂时无接口。。。。");
//			HashMap<String, Object> map  =new HashMap<String, Object>();
//			map.put("type", RInputCellphoneActivity.FORGET_PWD);
//			UIManager.switcher(this, RInputCellphoneActivity.class,map);
			break;
		}

		}
	}

	private Dialog loadDialog;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			UIManager.toggleDialog(loadDialog);
			switch (msg.what) {
			case ConstantManager.SUCCESS_1:
				// 登录成功会得到登录用户的信息！！
				UIManager.switcher(LoginActivity.this, MainActivity.class);
				break;
			case ConstantManager.FAIL_CODE_ERROR:{
				ToastManager.show(LoginActivity.this,(String)msg.obj);
				break;
			}
			case ConstantManager.FAIL_SERVER_ERROR:
			case ConstantManager.FAIL_NO_NET: {
				ToastManager.show(LoginActivity.this, R.string.toast_un_net);
				break;
			}
			case ConstantManager.LOADING_1: {
				loadDialog = UIManager.getLoadingDialog(LoginActivity.this,R.string.dialog_loading_login);
				loadDialog.show();
				break;
			}
			}
		};
	};

}
