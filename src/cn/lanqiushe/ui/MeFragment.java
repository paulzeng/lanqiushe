package cn.lanqiushe.ui;

import java.util.ArrayList;
import java.util.HashMap;

import cn.lanqiushe.App;
import cn.lanqiushe.R;
import cn.lanqiushe.adapter.MeTeamMemberAdapter;
import cn.lanqiushe.engine.DataService;
import cn.lanqiushe.entity.Player;
import cn.lanqiushe.entity.Team;
import cn.lanqiushe.entity.User;
import cn.lanqiushe.manager.ConstantManager;
import cn.lanqiushe.manager.ImageManager;
import cn.lanqiushe.manager.StringManager;
import cn.lanqiushe.manager.ThreadPool;
import cn.lanqiushe.manager.TitleManager;
import cn.lanqiushe.manager.ToastManager;
import cn.lanqiushe.manager.UIManager;
import cn.lanqiushe.view.ScrollListView;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MeFragment extends BaseFragment {
	private ImageView mAdd;

	private Dialog noTeamDialog;
	private Dialog lackPlayerDialog;
	private User user;
	// -----head--------
	private ImageView mHeadHead;
	private TextView mHeadName, mHeadPost;

	// ----lack full top-----
	private ImageView mTeamLogo;
	private TextView mTeamName;

	private ScrollListView mSlv;
	private MeTeamMemberAdapter adapter;

	private View lvHeadEr;
	private View lvFootEr;
	private Button footerRedBt;

	private RefreshUserInfoReceiver receiver;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.layout_me, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
	}

	public void findViews() {
		mHeadHead = (ImageView) fa.findViewById(R.id.me_head_iv);
		mHeadName = (TextView) fa.findViewById(R.id.me_head_name_tv);
		mHeadPost = (TextView) fa.findViewById(R.id.me_head_post_tv);
		mSlv = (ScrollListView) fa.findViewById(R.id.common_scroll_listview);

	}

	public void init() {
		// 登录成功，获取登录用户信息
		App app = ((App) fa.getApplication());

		user = app.getUser();

		// init head信息
		ImageManager.getInstance().displayImage(user.portrait, mHeadHead,
				ImageManager.getUserHeadOptions());
		mHeadName.setText(user.nickName);
		mHeadPost.setText(user.post);

		View emptyView = View.inflate(fa, R.layout.layout_me_status_noteam,
				null);
		TextView tv = (TextView) emptyView.findViewById(R.id.me_build_team_bt);
		tv.setText(R.string.build_team);
		ViewGroup parentView = (ViewGroup) mSlv.getParent();
		parentView.addView(emptyView);
		mSlv.setEmptyView(emptyView);

		addTeamInfo();
		receiver = new RefreshUserInfoReceiver();
		IntentFilter filter = new IntentFilter(
				ConstantManager.ACTION_REFRESH_ME);
		fa.registerReceiver(receiver, filter);
	}

	public void addTeamInfo() {
		// 有球队情况： 1、球队未满， 2.球队满了
		// 每次都重新获取，以便及时的刷新
		user = ((App) fa.getApplication()).getUser();
		Team team = user.team;
		if (team != null) {
			// 说明球队了，
			ArrayList<Player> players = team.players; // 有球队了一定就有球员
			int headerViewCount = mSlv.getHeaderViewsCount();
			int footerViewCount = mSlv.getFooterViewsCount();
			if (headerViewCount == 0) {
				initHeader();
			}
			if (footerViewCount == 0) {
				lvFootEr = View.inflate(fa, R.layout.layout_me_lv_footer, null);
				footerRedBt = ((Button) lvFootEr
						.findViewById(R.id.footer_red_bt));
			}
			if (players.contains(null)) {
				((ImageView) lvHeadEr.findViewById(R.id.me_add_iv))
						.setVisibility(View.VISIBLE);
				// 球友没有满，bt显示邀请球友 (默认的)
			} else {
				// 球友没有满，bt显示退出
				footerRedBt.setText(R.string.exit_team);
			}

			if (headerViewCount == 0) {
				mSlv.addHeaderView(lvHeadEr);
			}
			if (footerViewCount == 0) {
				mSlv.addFooterView(lvFootEr);
			}
			adapter = new MeTeamMemberAdapter(fa, players);
			mSlv.setAdapter(adapter);
		} else {
			mSlv.setAdapter(null);

			// 没有球队的时候填充的view

		}
	}

	private void initHeader() {
		lvHeadEr = View.inflate(fa, R.layout.layout_me_lv_header, null);
		mTeamLogo = (ImageView) lvHeadEr.findViewById(R.id.me_teamlogo_iv);
		mTeamName = (TextView) lvHeadEr.findViewById(R.id.me_teamname_tv);
		Team team = user.team;
		ImageManager.getInstance().displayImage(team.teamLogo, mTeamLogo,
				ImageManager.getUserHeadOptions());
		mTeamName.setText(team.teamName);

	}

	public void onClick(View v) {
		switch (v.getId()) {
		// --------------------------------------
		case R.id.action_sheet_create_team_bt: {// actionsheet 创建球队
			UIManager.switcher(fa, CreateTeamActivity.class);
			noTeamDialog.dismiss();
			break;
		}
		case R.id.action_sheet_near_team_bt: {// actionsheet 附近球队

			// 跳转到附近的球队后应该有，点击加入的按钮。。。。。。。
			Intent i = new Intent(ConstantManager.ACTION_CHANGE_BOTTOM_TAB);
			i.putExtra(ConstantManager.BOTTOM_TAB_INDEX, 1);
			i.putExtra(ConstantManager.TOP_TAB_INDEX, 1);
			fa.sendBroadcast(i);
			// 附近---> 球队

			noTeamDialog.dismiss();
			break;
		}
		// 没有球队的时候 ， 组建球队
		case R.id.me_build_team_bt: {
			View view = View.inflate(fa,
					R.layout.layout_actionsheet_build_team, null);
			noTeamDialog = UIManager.getActionSheet(fa, view);
			noTeamDialog.show();

			break;
		}

		// -------------------------
		case R.id.action_sheet_phone_contact_bt: {
			UIManager.switcher(fa, MBContactsActivity.class);
			lackPlayerDialog.dismiss();
			break;
		}
		case R.id.action_sheet_near_player_bt: {
			// 附近---> 球友
			Intent i = new Intent(ConstantManager.ACTION_CHANGE_BOTTOM_TAB);
			i.putExtra(ConstantManager.BOTTOM_TAB_INDEX, 1);
			i.putExtra(ConstantManager.TOP_TAB_INDEX, 1);
			fa.sendBroadcast(i);
			lackPlayerDialog.dismiss();
			break;
		}
		case R.id.action_sheet_my_player_bt: {
			Intent i = new Intent(ConstantManager.ACTION_CHANGE_BOTTOM_TAB);
			i.putExtra(ConstantManager.BOTTOM_TAB_INDEX, 2);
			i.putExtra(ConstantManager.TOP_TAB_INDEX, 2);
			fa.sendBroadcast(i);
			lackPlayerDialog.dismiss();
			break;
		}
		// 当球队不足的时候
		case R.id.me_add_iv:
		case R.id.footer_red_bt: {
			if (getResources().getString(R.string.exit_team).equals(// 如果是退出球队
					footerRedBt.getText())) {
				// 退出球队
				Dialog dialog = UIManager.getCommWarnDialog(fa,
						R.string.dialog_exit_team, new OnClickListener() {

							@Override
							public void onClick(View v) {
								HashMap<String, Object> map = new HashMap<String, Object>();
								User user = ((App) fa.getApplication())
										.getUser();
								map.put("userId", user.userId);
								map.put("teamId", user.team.teamId);
								DataService.exitTeam(map, fa, handler);

							}
						});
				dialog.show();
				return;
			}

			View view = View.inflate(fa,
					R.layout.layout_actionsheet_invite_friend, null);
			lackPlayerDialog = UIManager.getActionSheet(fa, view);
			lackPlayerDialog.show();
			break;
		}

		// ----------- head-----------
		case R.id.me_root_rl:
			UIManager.switcher(fa, MyInfoActivity.class);
			break;

		}

	}

	class RefreshUserInfoReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			int code = intent.getIntExtra(ConstantManager.INTENT_KEY_REFRESH,
					-1);
			switch (code) {
			case ConstantManager.REFRESH_HEAD: {
				App app = (App) fa.getApplication();
				User user = app.getUser();
				ImageManager.getInstance().displayImage(user.portrait,
						mHeadHead, ImageManager.getUserHeadOptions());
				mHeadName.setText(user.nickName);
				mHeadPost.setText(user.post);
				break;
			}
			case ConstantManager.REFRESH_TEAM_INFO: {
				addTeamInfo();
				break;
			}
			case ConstantManager.REFRESH_ALL: {

			}
			}

		}

	}

	private Dialog loadDialog;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			UIManager.toggleDialog(loadDialog);
			switch (msg.what) {
			case ConstantManager.SUCCESS_1:

				App app = ((App) fa.getApplication());
				User user = app.getUser();
				user.team = null;
				app.setUser(user);
				addTeamInfo();

				break;
			case ConstantManager.FAIL_CODE_ERROR: {
				ToastManager.show(fa, (String) msg.obj);
				break;
			}
			case ConstantManager.FAIL_SERVER_ERROR:
			case ConstantManager.FAIL_NO_NET: {
				ToastManager.show(fa, R.string.toast_un_net);
				break;
			}
			case ConstantManager.LOADING_1: {
				loadDialog = UIManager.getLoadingDialog(fa,
						R.string.dialog_loading_login);
				loadDialog.show();
				break;
			}
			}
		};
	};

	@Override
	public void onDestroy() {
		fa.unregisterReceiver(receiver);
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
