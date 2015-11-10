package cn.lanqiushe.ui;

import java.util.HashMap;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.lanqiushe.R;
import cn.lanqiushe.engine.DataService;
import cn.lanqiushe.entity.User;
import cn.lanqiushe.manager.ConstantManager;
import cn.lanqiushe.manager.ImageManager;
import cn.lanqiushe.manager.LogManager;
import cn.lanqiushe.manager.MediaManager;
import cn.lanqiushe.manager.StringManager;
import cn.lanqiushe.manager.TitleManager;
import cn.lanqiushe.manager.ToastManager;
import cn.lanqiushe.manager.UIManager;

import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

public class RPerfectDataActivity extends BaseActivity {
	private boolean isSetHead;
	private ImageView mHeadIV;
	private EditText mNickName;
	private TextView mPost, mSex;
	private Dialog dialog;
	private Dialog loadDialog;
	private   String nickName;
	private final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			UIManager.toggleDialog(loadDialog);
			switch (msg.what) {
			case ConstantManager.SUCCESS_1: {
				String userId = (String) msg.obj;
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("userId", userId);
				LogManager.e("xx", "我发送了userid"+userId);
				UIManager.switcher(RPerfectDataActivity.this,
						RShareActivity.class, map);
				break;
			}
			case ConstantManager.FAIL_SERVER_ERROR:
			case ConstantManager.FAIL_NO_NET: {
				ToastManager.show(RPerfectDataActivity.this,
						R.string.toast_un_net);
				break;
			}
			case ConstantManager.LOADING_1: {
				loadDialog = UIManager
						.getLoadingDialog(RPerfectDataActivity.this);
				loadDialog.show();
				break;
			}
			
			
			case ConstantManager.SHOW_PHOTO: {
				Bitmap bm = (Bitmap) msg.obj;
				if (bm == null) {
				} else {
					new RoundedBitmapDisplayer(100).display(bm, (ImageAware) mHeadIV, null);
					isSetHead = true;
				}
				if (bm != null && !bm.isRecycled()) {
					bm.recycle();
				}
				break;
			}
			}
		}
	};

	protected void init() {
		mHeadIV = (ImageView) findViewById(R.id.perfect_data_head);
		mNickName = (EditText) findViewById(R.id.perfect_nickname_et);
		mPost = (TextView) findViewById(R.id.perfect_data_post_tv);
		mSex = (TextView) findViewById(R.id.perfect_data_sex_tv);
		// 如果是通过授权登录方式过来的，那么就会有user信息，这个信息并非最终的用户信息，所以不会保留在本地

		User user = (User) getIntent().getSerializableExtra("user");
		if (user != null) {
			mNickName.setText(user.nickName);
			ImageManager.getInstance().displayImage(user.portrait, mHeadIV,
					ImageManager.getUserHeadOptions());
			isSetHead = true;
			mSex.setText(user.gender);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.layout_register_perfect_data);
		TitleManager.showTitle(this, null, R.string.perfect_data,
				R.string.title_back, R.string.title_next);

		super.onCreate(savedInstanceState);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_next_ll: {
		    nickName = StringManager.getStringByET(mNickName);
			if (StringManager.isEmpty(nickName)) {
				ToastManager.show(this, R.string.toast_nickname_empty);
				return;
			}
			if (!isSetHead) {
				ToastManager.show(this, R.string.toast_head_empty);
				return;
			}

			
			// //要传递的数据，
			HashMap<String, Object> map = new HashMap<String, Object>();
			// map.put("openid", "qq 第三方登录的唯一编号");//如果是第三方登录，加入唯一授权的唯一编码
			// map.put("uid", "weibo 第三方登录的唯一编号");
			Intent i = getIntent();
			DataService
					.sendRegisterUserInfo(
							i.getStringExtra("cellphone"),
							i.getStringExtra("pwd"),
							nickName,
							DataService.unParsePost(mPost.getText().toString().trim()),
							DataService.unParseGender(mSex.getText().toString().trim()),
							10.10,
							22.22,
							MediaManager.cropPhotoFile,
							handler);

			break;

		}
		case R.id.action_sheet_photo_camera_bt: {
			MediaManager.getPhotoFromCamera(this);
			dialog.dismiss();
			break;
		}
		case R.id.action_sheet_photo_album_bt: {
			MediaManager.getPhotoFromAlbum(this);
			dialog.dismiss();
			break;
		}
		case R.id.perfect_data_head: {

			View view = View.inflate(this, R.layout.layout_actionsheet_up_head,
					null);
			dialog = UIManager.getActionSheet(this, view);
			dialog.show();

			break;
		}
		case R.id.perfect_data_post_rl: {
		    AlertDialog.Builder builder=  new AlertDialog.Builder(this);
		    //1=控球后卫 2=得分后卫 3=小前锋 4=大前锋 5=中锋
		   final String[] objects = {"控球后卫","得分后卫","小前锋","大前锋 ","中锋"};
		    ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,
		    		android.R.layout.simple_list_item_1, objects);
		    		
		    builder.setAdapter(adapter, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					 mPost.setText(objects[which]);
					
				}
			});
		    builder.create().show();
			break;
		}
		case R.id.perfect_data_sex_rl: {
			  AlertDialog.Builder builder=  new AlertDialog.Builder(this);
			   final String[] objects = {"男","女"};
			    ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,
			    		android.R.layout.simple_list_item_1, objects);
			    		
			    builder.setAdapter(adapter, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						 mSex.setText(objects[which]);
						
					}
				});
			    builder.create().show();
			break;
		}

		}
	}

	@Override
	protected void onActivityResult(final int requestCode, int resultCode,
			final Intent data) {
		MediaManager.onActivityResult(this, handler, requestCode, resultCode,
				data);
	}
}
