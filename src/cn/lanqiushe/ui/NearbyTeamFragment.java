package cn.lanqiushe.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import cn.lanqiushe.adapter.NearbyContentAdapter;
import cn.lanqiushe.engine.DataService;
import cn.lanqiushe.entity.Team;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class NearbyTeamFragment extends BaseFragment implements
		OnItemClickListener, OnRefreshListener<BaseListView> {
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			mTeamPullRefresh.onPullDownRefreshComplete();
			mTeamPullRefresh.setLastUpdatedLabel(new Date().toLocaleString());
			mTeamPullRefresh.onPullUpRefreshComplete();
			switch (msg.what) {
			case ConstantManager.SUCCESS_1:
				listTeams = (ArrayList<Team>) msg.obj;
				if (teamAdapter == null && teamPage == 1) {
					teamAdapter = new NearbyContentAdapter(fa,
							NearbyContentAdapter.TEAM);

					teamAdapter.setListTeam(listTeams);
					mTeamListView.setAdapter(teamAdapter);
					// 第一次加载
					((MainActivity) fa).setEmptyView(mTeamListView,
							nearByLvEmptyView, R.string.lv_empty_no_team);
				} else {
					teamAdapter.setListTeam(listTeams);
					teamAdapter.notifyDataSetChanged();
				}

				break;

			default:
				break;
			}
		};

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LogManager.e(tag, "球队界面  开始");
		return inflater
				.inflate(R.layout.layout_nearby_f_team, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void init() {
		mTeamPullRefresh = initPullRefresh(R.id.nearby_team_prlv, this);
		mTeamListView = ((MainActivity) fa).getListView(mTeamPullRefresh);
		teamMap = new HashMap<String, Object>();
		App app = (App) fa.getApplication();
		teamMap.put("userId", app.getUser().userId);
		teamMap.put("lng", "113.87080803");
		teamMap.put("lat", "22.573333");
		teamMap.put("page", 1);
		onPullDownToRefresh(mTeamPullRefresh);
		mTeamListView.setOnItemClickListener(this);
		super.init();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Team team = listTeams.get(position);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("teamLogo", team.teamLogo);
		map.put("teamName", team.teamName);
		map.put("distance", team.distance);
		map.put("teamId", team.teamId);
		UIManager.switcher(fa, NearbyTeamInfoActivity.class, map);
	}

	@Override
	public void onDestroy() {
		LogManager.e(tag, "NearbyTeamFragment  销毁");
		super.onDestroy();
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<BaseListView> refreshView) {
		teamPage = 0;
		teamAdapter = null;
		DataService.getTeamList(++teamPage, teamMap, fa, handler);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<BaseListView> refreshView) {
		// TODO Auto-generated method stub
		DataService.getTeamList(++teamPage, teamMap, fa, handler);
	}

}
