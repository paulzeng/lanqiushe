package cn.lanqiushe.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import cn.lanqiushe.adapter.NearbyContentAdapter;
import cn.lanqiushe.engine.DataService;
import cn.lanqiushe.entity.Park;
 
import cn.lanqiushe.manager.ConstantManager;
import cn.lanqiushe.manager.LogManager;
import cn.lanqiushe.manager.ToastManager;
import cn.lanqiushe.manager.UIManager;
import cn.lanqiushe.view.BaseListView;
import cn.lanqiushe.view.pullrefresh.PullToRefreshBase;
import cn.lanqiushe.view.pullrefresh.PullToRefreshBase.OnRefreshListener;
import cn.lanqiushe.view.pullrefresh.PullToRefreshListView;
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

public class NearbyParkFragment extends BaseFragment implements
		OnItemClickListener, OnRefreshListener<BaseListView> {
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			mParkPullRefresh.onPullDownRefreshComplete();
			mParkPullRefresh.setLastUpdatedLabel(new Date().toLocaleString());
			mParkPullRefresh.onPullUpRefreshComplete();
			switch (msg.what) {
			case ConstantManager.SUCCESS_1:
				listParks = (ArrayList<Park>) msg.obj;
				if (parkAdapter == null && parkPage == 1) {
					parkAdapter = new NearbyContentAdapter(fa,
							NearbyContentAdapter.PARK);

					parkAdapter.setListPark(listParks);
					mParkListView.setAdapter(parkAdapter);
					// 第一次加载
					((MainActivity) fa).setEmptyView(mParkListView,
							nearByLvEmptyView, R.string.lv_empty_no_park);
				} else {
					parkAdapter.setListPark(listParks);
					parkAdapter.notifyDataSetChanged();
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
		LogManager.e(tag, "NearbyBallParkFragment  开始。。。。");
		return inflater
				.inflate(R.layout.layout_nearby_f_park, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	@Override
	public void init() {
		mParkPullRefresh = initPullRefresh(R.id.nearby_park_prlv, this);
		mParkListView = ((MainActivity) fa).getListView(mParkPullRefresh);
		
		parkMap = new HashMap<String, Object>();
		parkMap.put("lng", "113.87080803");
		parkMap.put("lat", "22.573333");
		onPullDownToRefresh(mParkPullRefresh);
		mParkListView.setOnItemClickListener(this);
		super.init();
	}

	 

	@Override
	public void onDestroy() {
		LogManager.e(tag, "NearbyBallParkFragment  销毁");
		super.onDestroy();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Park p = listParks.get(position);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("park", p);
		UIManager.switcher(fa, NearbyParkInfoActivity.class, map);
	}

	/**
	 * 下拉刷新
	 */
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<BaseListView> refreshView) {
		parkPage = 0;
		parkAdapter = null;
		DataService.getParkList(++parkPage, parkMap, fa, handler);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<BaseListView> refreshView) {
		DataService.getParkList(++parkPage, parkMap, fa, handler);
	}
}
