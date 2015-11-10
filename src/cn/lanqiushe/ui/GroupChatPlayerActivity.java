package cn.lanqiushe.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.lanqiushe.R;
import cn.lanqiushe.adapter.TeamMemberAdpater;
import cn.lanqiushe.engine.DataService;
import cn.lanqiushe.entity.Player;
import cn.lanqiushe.manager.ConstantManager;
import cn.lanqiushe.manager.TitleManager;
import cn.lanqiushe.manager.ToastManager;
import cn.lanqiushe.manager.UIManager;

public class GroupChatPlayerActivity extends BaseActivity implements OnItemClickListener {
	private Dialog loadDialog;
	private ArrayList<Player> data ;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			UIManager.toggleDialog(loadDialog);
			switch (msg.what) {
			// ---获取列表
			case ConstantManager.LOADING_1: {
				loadDialog = UIManager
						.getLoadingDialog(GroupChatPlayerActivity.this);
				loadDialog.show();
				break;
			}
			case ConstantManager.SUCCESS_1: {
				  data = (ArrayList<Player>) msg.obj;
				TeamMemberAdpater mAdapter = new TeamMemberAdpater(GroupChatPlayerActivity.this, data);
				ListView mListView = (ListView) findViewById(R.id.listview);
				mListView.setAdapter(mAdapter);
				mListView.setOnItemClickListener(GroupChatPlayerActivity.this);
				break;
			}

			case ConstantManager.FAIL_CODE_ERROR: {
				ToastManager.show(GroupChatPlayerActivity.this,
						(String) msg.obj);
				break;
			}
			// ----comm---
			case ConstantManager.FAIL_SERVER_ERROR:
			case ConstantManager.FAIL_NO_NET: {
				ToastManager.show(GroupChatPlayerActivity.this,
						R.string.toast_un_net);
				break;
			}
			}
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.layout_group_chat_player_list);
		super.onCreate(savedInstanceState);
		TitleManager.showTitle(this, null, R.string.title_team_member,
				R.string.title_back, 0);

	}

	@Override
	protected void init() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("groupId", 1);
		map.put("userId", 1);
		map.put("count", 1);
		DataService.getCurrGroupChatUserList(map, this, handler);

		super.init();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		UIManager.switcher(this, ChatActivity.class);
		 
		
		
	}

}
