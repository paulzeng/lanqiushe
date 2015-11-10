package cn.lanqiushe.ui;

import java.util.HashMap;

import cn.lanqiushe.App;
import cn.lanqiushe.R;
import cn.lanqiushe.adapter.TeamInfoAdapter;
import cn.lanqiushe.engine.DataService;
import cn.lanqiushe.entity.ChatMsg;
import cn.lanqiushe.entity.Player;
import cn.lanqiushe.entity.Team;
import cn.lanqiushe.entity.User;
import cn.lanqiushe.manager.ConstantManager;
import cn.lanqiushe.manager.ImageManager;
import cn.lanqiushe.manager.LogManager;
import cn.lanqiushe.manager.StringManager;
import cn.lanqiushe.manager.TitleManager;
import cn.lanqiushe.manager.ToastManager;
import cn.lanqiushe.manager.UIManager;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class NearbyPlayerInfoActivity extends BaseActivity {
	private Team team;
	private Player p;
	private ImageView head, open;
	private TextView name, sex, distance, post, teamName, playerCount;
	private Button footerLeftBt, footerRightBt;
	private LinearLayout footerRightLL;

	private Dialog loadDialog;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			UIManager.toggleDialog(loadDialog);
			switch (msg.what) {
			case ConstantManager.SUCCESS_1:
				 //��Ҫ������ػ�������Ϣ
				break;
		
			// ������ӳɹ����
			case ConstantManager.SUCCESS_2: {
				ToastManager.show(NearbyPlayerInfoActivity.this,
						"�Ѿ��ɹ����ͼ������󣬵ȴ��ظ�");
				break;
			}
			// �˳���ӳɹ����
			case ConstantManager.SUCCESS_3: {
				App app = (App) getApplication();
				User user = app.getUser();
				user.team = null;
				app.setUser(user);
				Intent i = new Intent(ConstantManager.ACTION_REFRESH_ME);
				i.putExtra(ConstantManager.INTENT_KEY_REFRESH,
						ConstantManager.REFRESH_TEAM_INFO);
				sendBroadcast(i);
				ToastManager.show(NearbyPlayerInfoActivity.this, "�Ѿ��ɹ��˳����");
				break;
			}
			case ConstantManager.SUCCESS_4: {
				ToastManager.show(NearbyPlayerInfoActivity.this, "�����ѷ�������ȴ��ظ�");
				break;
			}
			
			
			case ConstantManager.FAIL_CODE_ERROR: {
				ToastManager.show(NearbyPlayerInfoActivity.this,
						(String) msg.obj);
				break;
			}
			// ------------����˻�������������-------------------
			case ConstantManager.FAIL_SERVER_ERROR:
			case ConstantManager.FAIL_NO_NET: {
				ToastManager.show(NearbyPlayerInfoActivity.this,
						R.string.toast_un_net);
				break;
			}
			// ����post��
			case ConstantManager.LOADING_1: {
				loadDialog = UIManager
						.getLoadingDialog(NearbyPlayerInfoActivity.this);
				loadDialog.show();
				break;
			}
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.layout_nearby_player_info);
		TitleManager.showTitle(this, null, R.string.title_detailed_info,
				R.string.player, 0);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void findViews() {
		head = (ImageView) findViewById(R.id.nearby_player_info_head_iv);
		name = (TextView) findViewById(R.id.nearby_player_info_name_tv);
		sex = (TextView) findViewById(R.id.nearby_player_info_sex_tv);
		distance = (TextView) findViewById(R.id.nearby_player_info_distance_tv);

		post = (TextView) findViewById(R.id.nearby_player_info_location_tv);
		teamName = (TextView) findViewById(R.id.nearby_player_info_teamname_tv);
		playerCount = (TextView) findViewById(R.id.nearby_player_info_team_item_number_tv);

		open = (ImageView) findViewById(R.id.nearby_player_info_item_team_open_iv);
		footerLeftBt = (Button) findViewById(R.id.team_and_player_info_comm_footer_left_bt);
		footerRightBt = (Button) findViewById(R.id.team_and_player_info_comm_footer_right_bt);
		footerRightLL = (LinearLayout) findViewById(R.id.team_and_player_info_comm_footer_right_ll);
	}

	@Override
	protected void init() {
		footerLeftBt.setText(R.string.playerinfo_send_paper);
		// ----------------
		Intent i = getIntent();
		p = (Player) i.getSerializableExtra("player");
		if (p == null)
			return;
		if (!StringManager.isEmpty(i.getStringExtra("from"))) {
			
			ToastManager.show(this, "��ȡ������ϸ��Ϣ���ӿڱ�ɾ��");
			
			//HashMap<String, Object> map = new HashMap<String, Object>();
			
			//DataService.getPlayerInfo(map, this, handler);
		} else {
			ImageManager.getInstance().displayImage(p.playerProtrait, head,
					ImageManager.getUserHeadOptions());
			name.setText(p.nickName);
			sex.setVisibility(View.VISIBLE);
			sex.setText(p.sex);
			distance.setText(p.distance);
			post.setText(p.playerPost);
			team = p.team;
			// --------------
			if (team != null) {
				teamName.setText(team.teamName);
				String total = team.playerTotal;
				if (!"(3/3)".equals(total)) {
					playerCount.setText(team.playerTotal);
					footerRightLL.setVisibility(View.VISIBLE);
					footerRightBt.setText(R.string.join_team);
				}

			} else {
				open.setVisibility(View.INVISIBLE);
				teamName.setText(R.string.not_available);
				footerRightLL.setVisibility(View.VISIBLE);
				footerRightBt.setText(R.string.playerinfo_build_team);
			}

		}

		super.init();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.team_and_player_info_comm_footer_right_bt: {
			Dialog dialog = null;
			User user = ((App) getApplication()).getUser();
			final Team myTeam = user.team;
			if (myTeam == null) {// �Լ�����û�����
				if (footerRightBt.getText().equals(// �Լ�����û�����-���������
						getResources().getString(R.string.join_team))) {
					// ���������ӣ��򽫴�������г�Ա����һ��������ӵ�ϵͳ֪ͨ
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("userId",((App)getApplication()).getUser().userId);
				    map.put("teamId", myTeam.teamId);
					DataService.joinTeam(map, this, handler);
				} else {// �Լ�����û�����-----�� �������
					dialog = UIManager.getCommWarnDialog(this,
							R.string.dialog_team_no, R.string.create_team,
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									// ���������ӣ���ת�������������д��ҳ�� ����Ӵ����ɹ�����ת�ش�ҳ��

									UIManager.switcher(
											NearbyPlayerInfoActivity.this,
											CreateTeamActivity.class);
								}
							});
				}
			} else {// �Լ����������
				if (footerRightBt.getText().equals(// �Լ����������-->�������
						getResources().getString(R.string.join_team))) {
					// ��Ҫ�˳����
					dialog = UIManager.getCommWarnDialog(this,
							R.string.dialog_need_exit_team,
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									HashMap<String, Object> map = new HashMap<String, Object>();
									// userId=1&teamId=1
									map.put("userId",
											((App) getApplication()).getUser().userId);
									map.put("teamId", myTeam.teamId);
									DataService.exitTeam(map,
											NearbyPlayerInfoActivity.this,
											handler);
								}
							});

				} else {// �Լ����������--�� ��������ӡ�
					if (myTeam.players.contains(null)) {
						HashMap<String, Object> map = new HashMap<String, Object>();
						// userId=1&teamId=1
						map.put("notifierId",((App) getApplication()).getUser().userId);
						map.put("recipientId", myTeam.teamId);
					    DataService.invitePlayerBuildTeam(map, this, handler);
					 
					} else {
						// ��ӳ�Ա�Ѿ�����
						ToastManager.show(this, "��ӳ�Ա���������ܼ���");
					}

				}
			}
			if (dialog != null) {
				dialog.show();
			}
			break;
		}
		case R.id.team_and_player_info_comm_footer_left_bt: {
			HashMap<String, Object> map = new HashMap<String, Object>();
			ChatMsg chat  =new ChatMsg();
			chat.name = p.nickName;
			map.put("chat", chat);
			UIManager.switcher(this, ChatActivity.class, map);
			break;
		}

		case R.id.nearby_player_info_team_rl: {
			if (team != null) {
				HashMap<String, Object> map = new HashMap<String, Object>();
			  //  map.put("from", "from");
				map.put("teamId", team.teamId);
				UIManager.switcher(this, NearbyTeamInfoActivity.class, map);
			}
			break;
		}

		}
	}
}
