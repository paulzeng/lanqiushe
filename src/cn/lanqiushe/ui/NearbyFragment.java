package cn.lanqiushe.ui;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import cn.lanqiushe.R;
import cn.lanqiushe.adapter.NearbyFragmentAdapter;
import cn.lanqiushe.manager.ConstantManager;
import cn.lanqiushe.manager.LogManager;

public class NearbyFragment extends BaseFragment implements
		OnPageChangeListener, OnClickListener {
	private ViewPager mvp;
	private ArrayList<TextView> tvList;
	private ArrayList<RadioButton> rbList;
	private ChangeTopTabReceiver receiver;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LogManager.e("xx", "附近界面创建");
		return inflater.inflate(R.layout.layout_nearby, container, false);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		receiver = new ChangeTopTabReceiver();
		IntentFilter filter = new IntentFilter(
				ConstantManager.ACTION_CHANGE_TOP_TAB_NEARBY);
		fa.registerReceiver(receiver, filter);

		super.onActivityCreated(savedInstanceState);
	}

	class ChangeTopTabReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			int topTab = intent.getIntExtra(ConstantManager.TOP_TAB_INDEX, -1);
			if (topTab != -1) {
				mvp.setCurrentItem(topTab);
				onPageSelected(topTab);
			}

		}

	}

	protected void findViews() {
		mvp = (ViewPager) fa.findViewById(R.id.nearby_vp);
		mvp.setOffscreenPageLimit(2);

	}

	protected void init() {
		// 存放头部导航的tv 文本控件
		tvList = new ArrayList<TextView>();
		// 存放头部导航中的 line控件
		rbList = new ArrayList<RadioButton>();
		TextView leftTV = (TextView) fa.findViewById(R.id.nearby_top_nav_left_tv);
		TextView middleTV = (TextView) fa.findViewById(R.id.nearby_top_nav_middle_tv);
		TextView rightTV = (TextView) fa.findViewById(R.id.nearby_top_nav_right_tv);
		RadioButton leftLine = (RadioButton) fa
				.findViewById(R.id.nearby_top_nav_left_line);
		RadioButton middleLine = (RadioButton) fa
				.findViewById(R.id.nearby_top_nav_middle_line);
		RadioButton rightLine = (RadioButton) fa
				.findViewById(R.id.nearby_top_nav_right_line);
		tvList.add(leftTV);
		tvList.add(middleTV);
		tvList.add(rightTV);
		rbList.add(leftLine);
		rbList.add(middleLine);
		rbList.add(rightLine);
		leftTV.setOnClickListener(this);
		middleTV.setOnClickListener(this);
		rightTV.setOnClickListener(this);
		leftTV.setText(R.string.team);
		middleTV.setText(R.string.player);
		rightTV.setText(R.string.park);

		// 初始进入的
		((MainActivity)fa).changeNavText(tvList,0);

		// 初始化球队，球友，球场三个面板
		ArrayList<Fragment> list = new ArrayList<Fragment>();
		list.add(new NearbyTeamFragment());
		list.add(new NearbyPlayerFragment());
		list.add(new NearbyParkFragment());
		NearbyFragmentAdapter adapter = new NearbyFragmentAdapter(fm, list);
		mvp.setAdapter(adapter);

	}

	protected void setListener() {
		mvp.setOnPageChangeListener(this);
	}

	/**
	 * 通讯模块，通知，聊天，球队没有数据的时候提示方法
	 */
	public void setEmptyView(ListView lv, View emptyView, int res) {
		ViewGroup parentView = (ViewGroup) lv.getParent();
		parentView.addView(emptyView);
		TextView warn = (TextView) emptyView
				.findViewById(R.id.empty_view_nearby_tv);
		warn.setText(res);
		lv.setEmptyView(emptyView);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
         
		((MainActivity)fa).changeNavText(tvList,arg0);//改变文字
		 rbList.get(arg0).setChecked(true);//改变底部tab
	}


	@Override
	public void onClick(View v) {
		int index = -1;
		switch (v.getId()) {
		case R.id.nearby_top_nav_left_tv:
			index = 0;
			break;

		case R.id.nearby_top_nav_middle_tv:
			index = 1;
			break;
		case R.id.nearby_top_nav_right_tv:
			index = 2;
			break;
		}
		if (index != -1) {
			mvp.setCurrentItem(index);
		}
	}

	@Override
	public void onDestroy() {
		LogManager.e("xx", "附近界面销毁");
		fa.unregisterReceiver(receiver);
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
