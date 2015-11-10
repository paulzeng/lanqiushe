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
import android.widget.ImageView;
import android.widget.TextView;
import cn.lanqiushe.App;
import cn.lanqiushe.R;
import cn.lanqiushe.engine.DataService;
import cn.lanqiushe.entity.User;
import cn.lanqiushe.manager.ConstantManager;
import cn.lanqiushe.manager.ImageManager;
import cn.lanqiushe.manager.MediaManager;
import cn.lanqiushe.manager.TitleManager;
import cn.lanqiushe.manager.ToastManager;
import cn.lanqiushe.manager.UIManager;

import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

public class MyInfoActivity extends BaseActivity {
	public static final int ALERT_NAME_REQUESTCODE = 2;
	private TextView mName, mSex, mPost;
	private ImageView mHead;
	private boolean isChangeHead = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.layout_myinfo);
		TitleManager.showTitle(this, null, R.string.title_myinfo,
				R.string.title_back, R.string.title_finish);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void findViews() {
		mName = (TextView) findViewById(R.id.myinfo_name_tv);
		mHead = (ImageView) findViewById(R.id.myinfo_head_iv);
		mSex = (TextView) findViewById(R.id.myinfo_sex_tv);
		mPost = (TextView) findViewById(R.id.myinfo_location_tv);
		super.findViews();
	}

	@Override
	protected void init() {
		User user = ((App) getApplication()).getUser();
		ImageManager.getInstance().displayImage(user.portrait, mHead,
				ImageManager.getUserHeadOptions());
		mName.setText(user.nickName);
		mSex.setText(user.gender);
		mPost.setText(user.post);
		super.init();
	}

	private Dialog dialog;

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_next_ll: {
			// 判断是否有改变。如果没有改变。就不要提交到服务端了
			User user = ((App) getApplication()).getUser();
			if (!isChangeHead
					&& mPost.getText().toString().trim().equals(user.post)
					&& mName.getText().toString().trim().equals(user.nickName)) {
				// 没有改变
				finish();
				return;
			}
			HashMap<String, Object> map = new HashMap<String, Object>();
			App app  =(App) getApplication();
			map.put("userId", app.getUser().userId);
			map.put("nickName", app.getUser().userId);
			map.put("post",  mPost.getText().toString().trim());
			map.put("sex",mSex.getText().toString().trim());
			DataService.alertUserInfo(
					app.getUser().userId,
					mName.getText().toString().trim(),
					mSex.getText().toString().trim(),
					mPost.getText().toString().trim(),
					  MyInfoActivity.this, handler);
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
		case R.id.myinfo_head_iv: {
			View view = View.inflate(this, R.layout.layout_actionsheet_up_head,
					null);
			dialog = UIManager.getActionSheet(this, view);
			dialog.show();
			break;
		}
		case R.id.myinfo_name_rl: {
			UIManager.switcherFor(this, NickNameEditActivity.class,
					ALERT_NAME_REQUESTCODE);
			break;
		}
		case R.id.myinfo_sex_rl: {
			 
			break;
		}
		case R.id.myinfo_location_rl: {
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
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			if (requestCode == ALERT_NAME_REQUESTCODE) {
				// 昵称修改完毕后，的返回
				mName.setText(data.getStringExtra("nickname"));
			} else {
				// 拍照
				MediaManager.onActivityResult(this, handler, requestCode,
						resultCode, data);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private Dialog loadDialog;
	private final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			UIManager.toggleDialog(loadDialog);
			switch (msg.what) {
			case ConstantManager.SUCCESS_1: {
				Intent i = new Intent(ConstantManager.ACTION_REFRESH_ME);
				i.putExtra(ConstantManager.INTENT_KEY_REFRESH,
						ConstantManager.REFRESH_HEAD);
				sendBroadcast(i);
				finish();
				break;
			}
			case ConstantManager.FAIL_SERVER_ERROR:
			case ConstantManager.FAIL_NO_NET: {
				ToastManager.show(MyInfoActivity.this, R.string.toast_un_net);
				break;
			}
			case ConstantManager.LOADING_1: {
				loadDialog = UIManager.getLoadingDialog(MyInfoActivity.this);
				loadDialog.show();
				break;
			}

			case ConstantManager.SHOW_PHOTO: {
				Bitmap bm = (Bitmap) msg.obj;
				if (bm == null) {

				} else {
					isChangeHead = true;
					new RoundedBitmapDisplayer(100).display(bm, (ImageAware) mHead, null);
				}
				
				if(bm!=null&&!bm.isRecycled()){
					bm.recycle();
				}
				break;
			}
		 
			}

		}
	};
}
