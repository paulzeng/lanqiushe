package cn.lanqiushe.ui;

import java.util.ArrayList;
import java.util.HashMap;

import cn.lanqiushe.R;
import cn.lanqiushe.adapter.ContactsAdapter;
import cn.lanqiushe.adapter.MessageAdapter;
import cn.lanqiushe.adapter.NearbyContentAdapter;
import cn.lanqiushe.entity.ChatMsg;
import cn.lanqiushe.entity.Notify;
import cn.lanqiushe.entity.Park;
import cn.lanqiushe.entity.Player;
import cn.lanqiushe.entity.Team;
import cn.lanqiushe.view.BaseListView;
import cn.lanqiushe.view.pullrefresh.PullToRefreshBase.OnRefreshListener;
import cn.lanqiushe.view.pullrefresh.PullToRefreshListView;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ListView;

public class BaseFragment extends Fragment  {
	public static final String tag= "BaseFragment";
	public FragmentActivity fa;
	public FragmentManager fm;
	/**
	 *  下拉刷新容器view中实际存放数据的view
	 */
	public ListView mTeamListView;
	public ListView mPlayerListView;
	public ListView mParkListView;
	public ListView mNotifyListView;
	public ListView mChatListView;
	/**
	 *  下拉刷新容器view
	 */
	public PullToRefreshListView mTeamPullRefresh;
	public PullToRefreshListView mPlayerPullRefresh;
	public PullToRefreshListView mParkPullRefresh;
	public PullToRefreshListView mNotifyPullRefresh;
	public PullToRefreshListView mChatPullRefresh;
	/**
	 * 实际的数据list
	 */
	public ArrayList<Team> listTeams;
	public ArrayList<Player> listPlayers;
	public ArrayList<Park> listParks;
	public ArrayList<Notify> listNotifies;
	public ArrayList<ChatMsg> listChats;
	/**
	 * 分页加载的索引
	 */
	public int teamPage;
	public int playerPage;
	public int parkPage;
	public int notifyPage ;
	public int chatPage;
	/**
	 * 传递参数的map
	 */
	public HashMap<String, Object>  teamMap;
	public HashMap<String, Object>  playerMap;
	public HashMap<String, Object>  parkMap;
	public HashMap<String, Object>  notifyMap;
	public HashMap<String, Object>  chatMap;
	/**
	 * adapter
	 */
	public NearbyContentAdapter teamAdapter;
	public NearbyContentAdapter playerAdapter;
	public NearbyContentAdapter parkAdapter;
	/**
	 * 通讯模块adapter
	 */
	public MessageAdapter notifyAdapter;
	public MessageAdapter chatAdapter;
	
	
	public View nearByLvEmptyView;
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		fa = (FragmentActivity) activity;
		fm = getFragmentManager();
		nearByLvEmptyView = View.inflate(fa, R.layout.empty_view_nearby, null);
		super.onAttach(activity);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		findViews();
		init();
		setListener();

	}
	/**
	 * 所有的下拉刷新。不同界面的初始化
	 * @param resId
	 * @param refreshListener
	 */
	public PullToRefreshListView initPullRefresh(int resId,OnRefreshListener<BaseListView> refreshListener){
		PullToRefreshListView pullRefresh = (PullToRefreshListView) fa.findViewById(resId);
		pullRefresh.setPullLoadEnabled(false);
		pullRefresh.setScrollLoadEnabled(true);
		pullRefresh.setOnRefreshListener(refreshListener);
		return pullRefresh;
	}
	protected void findViews() {
	};

	protected void init() {
	}

	protected void setListener() {
	}

}
