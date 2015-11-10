package cn.lanqiushe.ui;

import java.util.ArrayList;
import java.util.HashMap;

import cn.lanqiushe.App;
import cn.lanqiushe.R;
import cn.lanqiushe.adapter.TeamInfoAdapter;
import cn.lanqiushe.engine.DataService;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class NearbyTeamInfoActivity extends BaseActivity implements
		OnItemClickListener {
	private ArrayList<Player> list;
	private Button bt;
	private Dialog actionSheetDialog;
    private ListView lv;
	private Dialog loadDialog;
	private ImageView head;
	private TextView name;
	private TextView distance;
	private String teamId;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			UIManager.toggleDialog(loadDialog);
			switch (msg.what) {
			//����ý����ʱ���ȡ��ӵ������Ϣ�ӿ�
			case ConstantManager.SUCCESS_1:{
				Team team = (Team) msg.obj;
				ImageManager.getInstance().displayImage(team.teamLogo,head, ImageManager.getUserHeadOptions());
				name.setText(team.teamName);
				distance.setText(team.distance);
 				list = team.players;
				lv.setAdapter(new TeamInfoAdapter(NearbyTeamInfoActivity.this, list));
				if (list.contains(null)) {
					//�����Ա�������ͻ���ʾ�������
					bt.setText(R.string.join_team);
				} else {
					//������Ƿ���Լռ����
					bt.setText(R.string.pk);
				}
				lv.setOnItemClickListener(NearbyTeamInfoActivity.this);
				break;
			}
			//����������
			case ConstantManager.SUCCESS_2:{
				ToastManager.show(NearbyTeamInfoActivity.this, "���������ѷ�������ȴ��ظ�");
				break;
			}	
			//�˳���ӵĽӿ�
			case ConstantManager.SUCCESS_3: {
				App app = (App) getApplication();
				User user = app.getUser();
				user.team = null;;
				app.setUser(user);
				Intent i = new Intent(ConstantManager.ACTION_REFRESH_ME);
				i.putExtra(ConstantManager.INTENT_KEY_REFRESH, ConstantManager.REFRESH_TEAM_INFO);
				sendBroadcast(i);
				ToastManager.show(NearbyTeamInfoActivity.this, "�Ѿ��ɹ��˳����");
				break;
			}
			//����Լս�Ľӿڷ���
			case ConstantManager.SUCCESS_4:{
				ToastManager.show(NearbyTeamInfoActivity.this, "Լս��Ϣ�ѷ�������ȴ��ظ�");
				break;
			}
			
			
			case ConstantManager.FAIL_CODE_ERROR: {
				ToastManager.show(NearbyTeamInfoActivity.this,
						(String) msg.obj);
				break;
			}
			case ConstantManager.FAIL_SERVER_ERROR:
			case ConstantManager.FAIL_NO_NET: {
				ToastManager.show(NearbyTeamInfoActivity.this, R.string.toast_un_net);
				break;
			}
			case ConstantManager.LOADING_1: {
				loadDialog = UIManager.getLoadingDialog(NearbyTeamInfoActivity.this);
				loadDialog.show();
				break;
			}
			}
		};
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.layout_nearby_team_info);
		TitleManager.showTitle(this, null, R.string.title_detailed_info,
				R.string.team, 0);

		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void init() {
		  lv = (ListView) findViewById(R.id.team_info_lv);
		  head = (ImageView) findViewById(R.id.nearby_player_info_head_iv);
		  name = (TextView) findViewById(R.id.nearby_player_info_name_tv);
		  distance = (TextView) findViewById(R.id.nearby_player_info_distance_tv);
		  bt = (Button) findViewById(R.id.team_and_player_info_comm_footer_left_bt);

		
//		//------------------------------
         
		
		
		
		Intent i = getIntent();
//		if(StringManager.isEmpty(i.getStringExtra("from"))){//
//			HashMap<String, Object> map = new HashMap<String, Object>();
//			map.put("teamId", i.getStringExtra("teamId"));
//			DataService.getTeamInfo(map, this, handler);
//		}else{
//			Team team  = new Team();
//			team.teamLogo = i.getStringExtra("teamLogo");
//			team.teamName = i.getStringExtra("teamName");
//			team.distance = i.getStringExtra("distance");
//			team.teamId = i.getStringExtra("teamId");
//			ImageManager.getInstance().displayImage(team.teamLogo,head, ImageManager.getHeadOptions());
//			name.setText(team.teamName);
//			distance.setText(team.distance);
			
//		}
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		teamId = i.getStringExtra("teamId");
		map.put("teamId", teamId);
		DataService.getTeamInfo(map, this, handler);
		
		super.init();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.team_and_player_info_comm_footer_left_bt:
			App app = (App) getApplication();
			User user = app.getUser();
			final Team team = user.team;
			Dialog dialog = null;
			// ��ǰ��ʾ����������㣬��ʾ���Ǽ�������ı�
			if (bt.getText().equals(
					getResources().getString(R.string.join_team))) {
				if (team == null) {
					//�û����������ʱ����ϵͳ��������Ӷ�Աһ��������Ϣ��������post һ����Ϣ������
				     HashMap<String, Object> map = new HashMap<String, Object>();
				     map.put("userId", user.userId);
				     map.put("teamId", teamId);
				     DataService.joinTeam(map,this, handler);//������ӵ���Ϣ�����Ժ󡣱���ȴ��ظ�
				} else {
					dialog = UIManager.getCommWarnDialog(this,
							R.string.dialog_need_exit_team,
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									    HashMap<String, Object> map = new HashMap<String, Object>();
										map.put("userId", ((App)getApplication()).getUser().userId);
										map.put("teamId", team.teamId);
									    DataService.exitTeam(map,NearbyTeamInfoActivity.this, handler);
								}
							});
				}
			} else {//����Լռ
				if (team == null) {
					//�û�����û��ӡ���
					dialog = UIManager.getCommWarnDialog(this,
							R.string.dialog_team_no, R.string.build_team,
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									View view = View
											.inflate(
													NearbyTeamInfoActivity.this,
													R.layout.layout_actionsheet_build_team,
													null);
									actionSheetDialog = UIManager.getActionSheet(
											NearbyTeamInfoActivity.this, view);
									actionSheetDialog.show();

								}
							});

				} else if (team.players.equals(null)) {// ��ǰ�û��ģ�
					// ��Ӳ���,ȥ����
					dialog = UIManager.getCommWarnDialog(this,
							R.string.dialog_team_lack, R.string.dialog_invite,
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									
									ToastManager.show(
											NearbyTeamInfoActivity.this,
											"��ת�������");
								}
							});

				} else {
					
					    HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("userId", ((App)getApplication()).getUser().userId);
						map.put("teamId",  teamId);
                        DataService.sendPKInfo(map, this, handler);
					
					
					 
				}

			}
			if (dialog != null) {
				dialog.show();
			}
			break;
			
		//
		case R.id.action_sheet_create_team_bt: {// actionsheet �������
			ToastManager.show(this, "�������ɽ���");
			UIManager.switcher(this, CreateTeamActivity.class);
			actionSheetDialog.cancel();
			break;
		}
		case R.id.action_sheet_near_team_bt: {// actionsheet �������
			// ����---> ���
			// ��ת����������Ӻ�Ӧ���У��������İ�ť��������������
			ToastManager.show(this, "��ת����������Ӻ�Ӧ���У��������İ�ť���������������߼�ò�Ʋ�ͨ");
			// UIManager.switcherFor(this, NearbyActivity.class,
			// BUILD_REQUEST_CODE);
			actionSheetDialog.cancel();
			break;
		}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Player p = list.get(position);
		if (p != null) {
			HashMap<String, Object> map  =new HashMap<String, Object>();
			map.put("from", "from");
			map.put("player", p);
			UIManager.switcher(this, NearbyPlayerInfoActivity.class,map);
		}

	}
}
