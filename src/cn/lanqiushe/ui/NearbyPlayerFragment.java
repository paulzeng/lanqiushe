package cn.lanqiushe.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import cn.lanqiushe.adapter.NearbyContentAdapter;
import cn.lanqiushe.engine.DataService;
import cn.lanqiushe.entity.Player;
 
import cn.lanqiushe.manager.ConstantManager;
import cn.lanqiushe.manager.LogManager;
import cn.lanqiushe.manager.ToastManager;
import cn.lanqiushe.manager.UIManager;
import cn.lanqiushe.view.BaseListView;
import cn.lanqiushe.view.pullrefresh.PullToRefreshBase;
import cn.lanqiushe.view.pullrefresh.PullToRefreshBase.OnRefreshListener;
import cn.lanqiushe.App;
import cn.lanqiushe.R;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class NearbyPlayerFragment extends BaseFragment implements OnItemClickListener, OnRefreshListener<BaseListView> {
 
	 private   Handler handler = new Handler(){
	    	public void handleMessage(android.os.Message msg) {
	    		mPlayerPullRefresh.onPullDownRefreshComplete();
	    		mPlayerPullRefresh.setLastUpdatedLabel(new Date().toLocaleString());
	    		mPlayerPullRefresh.onPullUpRefreshComplete();
	    		switch (msg.what) {
	    	
				case ConstantManager.SUCCESS_1:
					 listPlayers = (ArrayList<Player>) msg.obj;
					 if (playerAdapter == null && playerPage == 1) {
						 playerAdapter =new NearbyContentAdapter(fa, NearbyContentAdapter.PLayer);

						 playerAdapter.setListPlayer(listPlayers);
							mPlayerListView.setAdapter(playerAdapter);
							// 第一次加载
							 ((MainActivity) fa).setEmptyView(mPlayerListView, nearByLvEmptyView, R.string.lv_empty_no_player);
						} else {
							playerAdapter.setListPlayer(listPlayers);
							playerAdapter.notifyDataSetChanged();
						}
					 
					 
					 
					 
					 
					
				  break;
				  
				  
				  
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
		LogManager.e(tag, "NearbyBallFriendFragment  开始");
		return inflater.inflate(R.layout.layout_nearby_f_player, container,
				false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
	}
 
	@Override
	public void init() {
		mPlayerPullRefresh = initPullRefresh(R.id.nearby_player_prlv, this);
		mPlayerListView = ((MainActivity) fa).getListView(mPlayerPullRefresh);
		
		
		
		
		
		playerMap  = new HashMap<String, Object>();
		//lng=113.87080803&lat=22.573333
		playerMap.put("lng", "113.87080803");
		playerMap.put("lat", "22.573333");
		
		onPullDownToRefresh(mPlayerPullRefresh);
		mPlayerListView.setOnItemClickListener(this);
		
		
		super.init();
	}
	 
    @Override
	public void onDestroy() {
		LogManager.e(tag, "NearbyBallFriendFragment  销毁");
		super.onDestroy();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		 Player p  = listPlayers.get(position);
			
	     HashMap<String, Object> map =new  HashMap<String, Object>();
	     map.put("player", p);
	    
		 UIManager.switcher(fa, NearbyPlayerInfoActivity.class,map);
		
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<BaseListView> refreshView) {
		playerPage = 0;
		playerAdapter = null;
		 
		 DataService.getPlayerList(++playerPage,playerMap, fa, handler);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<BaseListView> refreshView) {
		 
		 DataService.getPlayerList(++playerPage,playerMap, fa, handler);
	}
}
