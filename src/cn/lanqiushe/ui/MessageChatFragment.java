package cn.lanqiushe.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import cn.lanqiushe.App;
import cn.lanqiushe.R;
import cn.lanqiushe.adapter.MessageAdapter;
import cn.lanqiushe.adapter.NearbyContentAdapter;
import cn.lanqiushe.engine.DataService;
import cn.lanqiushe.entity.ChatMsg;
import cn.lanqiushe.entity.Park;
import cn.lanqiushe.manager.ConstantManager;
import cn.lanqiushe.manager.LogManager;
import cn.lanqiushe.manager.ToastManager;
import cn.lanqiushe.manager.UIManager;
import cn.lanqiushe.view.BaseListView;
import cn.lanqiushe.view.pullrefresh.PullToRefreshBase;
import cn.lanqiushe.view.pullrefresh.PullToRefreshBase.OnRefreshListener;

public class MessageChatFragment extends BaseFragment implements
		OnRefreshListener<BaseListView>, OnItemClickListener,
		OnItemLongClickListener {
	private Dialog delChatLoading;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			mChatPullRefresh.onPullDownRefreshComplete();
			mChatPullRefresh.setLastUpdatedLabel(new Date().toLocaleString());
			mChatPullRefresh.onPullUpRefreshComplete();
			UIManager.toggleDialog(delChatLoading);
			switch (msg.what) {
			case ConstantManager.LOADING_1: {

				break;
			}
			case ConstantManager.SUCCESS_1: {
				listChats = (ArrayList<ChatMsg>) msg.obj;
				if (chatAdapter == null && chatPage == 1) {
					chatAdapter = new MessageAdapter(fa,
							MessageAdapter.CATEGORY_CHAT);
					chatAdapter.setChatList(listChats);
					mChatListView.setAdapter(chatAdapter);
					// 第一次加载
					// ((MainActivity) fa).setEmptyView(mParkListView,
					// nearByLvEmptyView, R.string.lv_empty_no_park);
				} else {
					chatAdapter.setChatList(listChats);
					chatAdapter.notifyDataSetChanged();
				}
				break;
			}

			// ---del,chat--
			case ConstantManager.LOADING_2: {
				delChatLoading = UIManager.getLoadingDialog(fa);
				delChatLoading.show();
				break;
			}
			case ConstantManager.SUCCESS_2: {
				int position = (Integer) msg.obj;
				chatAdapter.getChatList().remove(position);
				chatAdapter.notifyDataSetChanged();
				break;
			}

			case ConstantManager.FAIL_CODE_ERROR: {
				ToastManager.show(fa, (String) msg.obj);
				break;
			}
			// ----comm
			case ConstantManager.FAIL_SERVER_ERROR:
			case ConstantManager.FAIL_NO_NET: {
				ToastManager.show(fa, R.string.toast_un_net);
				break;
			}

			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LogManager.e(tag, "NotifyMessageFragment  开始");
		return inflater.inflate(R.layout.contacts_chat, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void init() {
		mChatPullRefresh = initPullRefresh(R.id.message_chat_prlv, this);
		mChatListView = ((MainActivity) fa).getListView(mChatPullRefresh);

		chatMap = new HashMap<String, Object>();
		App app = (App) fa.getApplication();
		chatMap.put("userId", app.getUser().userId);
		//chatMap.put("userId", 1);
		onPullDownToRefresh(mChatPullRefresh);
		mChatListView.setOnItemClickListener(this);
		mChatListView.setOnItemLongClickListener(this);

		super.init();
	}

	@Override
	public void onDestroy() {
		LogManager.e(tag, "NearbyBallFriendFragment  销毁");
		super.onDestroy();
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<BaseListView> refreshView) {
		chatPage = 0;
		chatAdapter = null;
		DataService.getChatList(++chatPage, chatMap, fa, handler);

	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<BaseListView> refreshView) {
		DataService.getChatList(++chatPage, chatMap, fa, handler);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		ChatMsg chat = chatAdapter.getChatList().get(position);
		map.put("chat", chat);
		UIManager.switcher(fa, ChatActivity.class,map);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("position", position);
		DataService.delSingleChatInfo(map, fa, handler);
		return false;
	}

}
