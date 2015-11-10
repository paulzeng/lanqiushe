package cn.lanqiushe.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

import cn.lanqiushe.App;
import cn.lanqiushe.R;
import cn.lanqiushe.engine.DataService;
import cn.lanqiushe.entity.Player;
import cn.lanqiushe.entity.Team;
import cn.lanqiushe.entity.User;
import cn.lanqiushe.manager.ConstantManager;
import cn.lanqiushe.manager.LogManager;
import cn.lanqiushe.manager.MediaManager;
import cn.lanqiushe.manager.SecurityManager;
import cn.lanqiushe.manager.StringManager;
import cn.lanqiushe.manager.ThreadPool;
import cn.lanqiushe.manager.TitleManager;
import cn.lanqiushe.manager.ToastManager;
import cn.lanqiushe.manager.UIManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class CreateTeamActivity extends BaseActivity {
	private EditText mTeamName;
	private Dialog dialog;
	 
	private ImageView mHeadIV;
	private String teamName;
	private Dialog loadDialog;
	private final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			UIManager.toggleDialog(loadDialog);
			switch (msg.what) {
			 
			case ConstantManager.LOADING_1: {
				loadDialog = UIManager
						.getLoadingDialog(CreateTeamActivity.this);
				loadDialog.show();
				break;
			}
			case ConstantManager.SUCCESS_1: {
				HashMap<String, Object> map = new HashMap<String, Object>();
				App app = (App) getApplication();
				map.put("userId", app.getUser().userId);
				map.put("loginType", 1);
				DataService.getUserInfo(map, CreateTeamActivity.this, handler);
				break;
			}
			case ConstantManager.SUCCESS_2: {
				Intent i = new Intent(ConstantManager.ACTION_REFRESH_ME);
				i.putExtra(ConstantManager.INTENT_KEY_REFRESH,
						ConstantManager.REFRESH_TEAM_INFO);
				sendBroadcast(i);
				finish();
				break;
			}
			case ConstantManager.FAIL_CODE_ERROR:
			  {
				ToastManager.show(CreateTeamActivity.this, (String) msg.obj);
				break;
			}
			case ConstantManager.FAIL_SERVER_ERROR:
			case ConstantManager.FAIL_NO_NET: {
				ToastManager.show(CreateTeamActivity.this,
						R.string.toast_un_net);
				break;
			}
			case ConstantManager.SHOW_PHOTO: {
				Bitmap bm = (Bitmap) msg.obj;
				if (bm == null) {
				} else {
					new RoundedBitmapDisplayer(100).display(bm, (ImageAware) mHeadIV, null);
				}
                if(bm!=null&&!bm.isRecycled()){
                	 bm.recycle();
                }
				break;
			}
			 
      
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.layout_createteam);
		TitleManager.showTitle(this, null, R.string.title_teaminfo_add,
				R.string.title_back, R.string.title_finish);

		super.onCreate(savedInstanceState);
	}

	@Override
	protected void findViews() {
		mHeadIV = (ImageView) findViewById(R.id.create_team_head_iv);
		mTeamName = (EditText) findViewById(R.id.createam_teamname_et);
		super.findViews();
	}

	public void onClick(View v) {
		switch (v.getId()) {
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
		case R.id.create_team_head_iv: {
			View view = View.inflate(this, R.layout.layout_actionsheet_up_head,
					null);
			dialog = UIManager.getActionSheet(this, view);
			dialog.show();
			break;
		}
		//------------------------------------------------
		case R.id.title_next_ll:
			teamName = StringManager.getStringByET(mTeamName);
			if (StringManager.isEmpty(teamName)) {
				ToastManager.show(this, R.string.please_input_teamname);
				return;
			}
 
			//如果头像为你null代表头像没有改变
			App app = (App) getApplication();
			DataService.createTeam(app.getUser().userId, MediaManager.cropPhotoFile,
					teamName,
					CreateTeamActivity.this, handler);
			break;

		}
	}

	@Override
	protected void onActivityResult(final int requestCode, int resultCode,
			final Intent data) {
		MediaManager.onActivityResult(this, handler, requestCode, resultCode,
				data);
	}
}
