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
	// �ײ�tab����
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

		// ----ע��ı���Ŀ��tab�Ĺ㲥--------------
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
				if (topTab != -1) {// �������ĵ����иı�
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
	 * �����������ˢ��listview
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
	 * ����ģ�飬��ӣ����ѣ���û�����ݵ�ʱ����ʾ����
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
	 * �ײ�tab�ı��¼�
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
	 * vp �л��¼�
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
	 * ���� ��������ת��Activity�������������ݴ����ʱ�����ø÷���
	 */

	// @Override
	// public void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// // if (requestCode == Activity.RESULT_OK) {
	// switch (resultCode) {
	// case ConstantManager.RESULT_CODE: {
	// // ���õ�ǰ�û���Ϣ
	// // ��ʱ���������һ��ֻ��һ���ˣ������Լ� �����ﷵ�ص��Ǵ�����ӳɹ����
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
	 * �ı䵼������
	 * 
	 * @param index
	 */

	public void changeNavText(ArrayList<TextView> tvList, int index) {
		// �ı䵼������
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
	 * �˳�app
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
