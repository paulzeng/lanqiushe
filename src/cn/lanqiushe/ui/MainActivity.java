package cn.lanqiushe.ui;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import cn.lanqiushe.App;
import cn.lanqiushe.R;
import cn.lanqiushe.adapter.MainTabPager;
import cn.lanqiushe.entity.User;
import cn.lanqiushe.manager.ConstantManager;
import cn.lanqiushe.manager.LogManager;
import cn.lanqiushe.manager.TitleManager;
import cn.lanqiushe.manager.ToastManager;
import cn.lanqiushe.manager.UIManager;
import cn.lanqiushe.view.BaseListView;
import cn.lanqiushe.view.ViewPagerUnTouch;
import cn.lanqiushe.view.pullrefresh.PullToRefreshListView;

public class MainActivity extends FragmentActivity implements
		OnCheckedChangeListener, OnPageChangeListener {
	private Fragment mMe;
	private Fragment mNearby;
	private Fragment mContacts;
	private ViewPager mViewPager;
	// 底部tab导航
	private RadioGroup mTabRG;

	private ChangeTabAndItemReceiver receiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		((App) getApplication()).activitys.add(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main);
		ArrayList<Fragment> list = new ArrayList<Fragment>();
		mMe = new MeFragment();
		mNearby = new NearbyFragment();
		mContacts = new MessageFragment();
		list.add(mMe);
		list.add(mNearby);
		list.add(mContacts);

		MainTabPager myAdapter = new MainTabPager(getSupportFragmentManager(),
				list);
		mViewPager = (ViewPagerUnTouch) findViewById(R.id.main_vp);
		mViewPager.setAdapter(myAdapter);
		mViewPager.setOffscreenPageLimit(2);
		mViewPager.setOnPageChangeListener(this);
		mTabRG = (((RadioGroup) findViewById(R.id.main_tab_group)));
		mTabRG.setOnCheckedChangeListener(this);

		TitleManager.showTitle(this, new int[] { TitleManager.SET },
				R.string.app_name);

		// ----注册改变条目和tab的广播--------------
		receiver = new ChangeTabAndItemReceiver();
		IntentFilter filter = new IntentFilter(
				ConstantManager.ACTION_CHANGE_BOTTOM_TAB);
		registerReceiver(receiver, filter);
	}

	class ChangeTabAndItemReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			int bottomTab = intent.getIntExtra(
					ConstantManager.BOTTOM_TAB_INDEX, -1);
			int topTab = intent.getIntExtra(ConstantManager.TOP_TAB_INDEX, -1);
			if (bottomTab != -1) {
				mViewPager.setCurrentItem(bottomTab);
				if (topTab != -1) {// 如果上面的导航有改变
					Intent i = null;
					if (topTab == 1) {
						i = new Intent(
								ConstantManager.ACTION_CHANGE_TOP_TAB_NEARBY);
					} else {
						i = new Intent(
								ConstantManager.ACTION_CHANGE_TOP_TAB_CONTACT);
					}

					i.putExtra(ConstantManager.TOP_TAB_INDEX, topTab);
					sendBroadcast(i);
				}
			}
		}

	}

	public void onClick(View v) {
		((MeFragment) mMe).onClick(v);
	}

	/**
	 * 获得所有下拉刷新listview
	 */
	public ListView getListView(PullToRefreshListView pv) {
		BaseListView mlv = pv.getRefreshableView();
		mlv.setDivider(new ColorDrawable(getResources().getColor(
				R.color.divider)));
		mlv.setDividerHeight(1);
		mlv.setCacheColorHint(Color.rgb(0, 0, 0));
		mlv.setSelector(R.drawable.selector_lv_item);

		return mlv;
	}

	/**
	 * 附近模块，球队，球友，球场没有数据的时候提示方法
	 */

	public void setEmptyView(ListView lv, View emptyView, int res) {
		if(lv.getEmptyView()!=null){
			ViewGroup parentView = (ViewGroup) lv.getParent();
			parentView.addView(emptyView);
			TextView warn = (TextView) emptyView
					.findViewById(R.id.empty_view_nearby_tv);
			warn.setText(res);
			lv.setEmptyView(emptyView);
		}
		
		
	}

	/**
	 * 底部tab改变事件
	 */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		int index = 0;
		switch (checkedId) {
		case R.id.main_tab_me:
			TitleManager.showTitle(this, new int[] { TitleManager.SET },
					R.string.app_name);
			index = 0;
			break;
		case R.id.main_tab_near:
			TitleManager.showTitle(this, null, R.string.title_nearby);
			index = 1;
			break;
		case R.id.main_tab_message:
			TitleManager.showTitle(this, null, R.string.title_contacts);
			index = 2;
			break;

		}
		mViewPager.setCurrentItem(index);
	}

	/**
	 * vp 切换事件
	 */

	@Override
	public void onPageSelected(int arg0) {
		switch (arg0) {
		case 0:
			TitleManager.showTitle(this, new int[] { TitleManager.SET },
					R.string.app_name);

			break;
		case 1:
			TitleManager.showTitle(this, null, R.string.title_nearby);

			break;
		case 2:
			TitleManager.showTitle(this, null, R.string.title_contacts);
			break;

		}
		RadioButton rb = (RadioButton) mTabRG.getChildAt(arg0);
		rb.setChecked(true);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	/**
	 * 当从 主界面跳转到Activity界面后回来做数据处理的时候。利用该方法
	 */

	// @Override
	// public void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// // if (requestCode == Activity.RESULT_OK) {
	// switch (resultCode) {
	// case ConstantManager.RESULT_CODE: {
	// // 设置当前用户信息
	// // 这时候，球队里面一定只有一个人，就是自己 （这里返回的是创建球队成功后后）
	// ((MeFragment) mMe).addTeamInfo();
	//
	// break;
	// }
	//
	// }
	// // }
	// super.onActivityResult(requestCode, resultCode, data);
	// }

	/**
	 * 改变导航字体
	 * 
	 * @param index
	 */

	public void changeNavText(ArrayList<TextView> tvList, int index) {
		// 改变导航字体
		for (int i = 0, len = tvList.size(); i < len; i++) {
			TextView tv = tvList.get(i);
			//
			tv.setTextColor(Color.rgb(146, 148, 151));
			if (i == index) {// 232,129,59
				tv.setTextColor(Color.rgb(232, 129, 59));
			}

		}

	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(receiver);
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	/**
	 * 退出app
	 */
	private long firstTime;

	@Override
	public void onBackPressed() {
		if (System.currentTimeMillis() - firstTime < 4000) {
			((App) getApplication()).exit(App.EXIT_APP);
		} else {
			firstTime = System.currentTimeMillis();
			ToastManager.show(this, R.string.toast_press_again_backrun);
		}
	}

}
