package cn.lanqiushe.ui;

import java.util.ArrayList;

import java.util.HashMap;

import android.app.Dialog;
import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import cn.lanqiushe.App;
import cn.lanqiushe.R;
import cn.lanqiushe.adapter.ChatAdpater;
import cn.lanqiushe.engine.DataService;
import cn.lanqiushe.entity.ChatMsg;
import cn.lanqiushe.entity.ChatMsg;
import cn.lanqiushe.manager.ConstantManager;
import cn.lanqiushe.manager.StringManager;
import cn.lanqiushe.manager.TitleManager;
import cn.lanqiushe.manager.ToastManager;
import cn.lanqiushe.manager.UIManager;

public class ChatActivity extends BaseActivity {

	private EditText mSendContent;
	// 聊天内容的适配器
	private ChatAdpater adapter;
	private ListView mListView;

	private Dialog loadDialog;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			UIManager.toggleDialog(loadDialog);
			switch (msg.what) {
			// ---获取列表
			case ConstantManager.LOADING_1: {
				loadDialog = UIManager.getLoadingDialog(ChatActivity.this);
				loadDialog.show();
				break;
			}
			case ConstantManager.SUCCESS_1: {
				ArrayList<ChatMsg> data = (ArrayList<ChatMsg>) msg.obj;
				adapter = new ChatAdpater(ChatActivity.this);
				adapter.setList(data);
				mListView.setAdapter(adapter);
				mListView.setSelection(adapter.getCount() - 1);
				break;
			}
			case ConstantManager.LOADING_2: {

				break;
			}
			case ConstantManager.SUCCESS_2: {

				break;
			}

			case ConstantManager.FAIL_CODE_ERROR: {
				ToastManager.show(ChatActivity.this, (String) msg.obj);
				break;
			}
			// ----comm---
			case ConstantManager.FAIL_SERVER_ERROR:
			case ConstantManager.FAIL_NO_NET: {
				ToastManager.show(ChatActivity.this, R.string.toast_un_net);
				break;
			}
			}
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.layout_chat);
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void findViews() {
		mSendContent = (EditText) findViewById(R.id.chat_msg_content_et);
		mListView = (ListView) findViewById(R.id.listview);
		super.findViews();
	}

	@Override
	protected void init() {
		ChatMsg chat = (ChatMsg) getIntent().getSerializableExtra("chat");
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (chat.type == ChatMsg.CHAT_SINGLE) {// 如果是单聊
			TitleManager.showTitle(this,
					new int[] { TitleManager.PLAYER_INFO }, "与" + chat.name
							+ "聊天", R.string.title_back, 0);

			// userId=1&golfersId=2&count=100
			map.put("userId", 1);
			map.put("golfersId", 2);
			map.put("count", 100);

		} else {// 如果群聊
			TitleManager.showTitle(this, new int[] { TitleManager.GROUP },
					chat.name, R.string.title_back, 0);
			map.put("groupId", 1);
			map.put("userId", 2);
			map.put("count", 100);

		}

		DataService.getChatDetailInfo(map, this, handler);

		super.init();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.chat_send_msg_bt: {
			String msg = StringManager.getStringByET(mSendContent);
			if (StringManager.isEmpty(msg)) {
				return;
			}
			HashMap<String, Object> map = new HashMap<String, Object>();
			// senderId=1&receiverId=2&content=你多大啊？
			map.put("sendId", 1);// 发送者id
			map.put("receiverId", 2);// 接受者id
			map.put("content", msg);
			DataService.addSingleChatInfo(map, this, handler);
			mSendContent.setText("");

			// 只要已发送。 就更新界面。发送一条信息就是组件一条聊天实体
			ChatMsg cm = new ChatMsg();
			App app = (App) getApplication();
			cm.head = app.getUser().portrait;
			cm.content = msg;
			ArrayList<ChatMsg> d = adapter.getList();
			d.add(d.size(), cm);
			adapter.notifyDataSetChanged();
			mListView.setSelection(adapter.getCount() - 1);

			break;
		}

		// title 右边的跳转
		case R.id.title_group_iv: {

			UIManager.switcher(this, GroupChatPlayerActivity.class);
			break;
		}

		case R.id.title_player_info_iv: {
			UIManager.switcher(this, NearbyPlayerInfoActivity.class);
			break;
		}
		}
	}

}