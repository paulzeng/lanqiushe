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
import cn.lanqiushe.adapter.ContactFragmentAdapter;
import cn.lanqiushe.manager.ConstantManager;
import cn.lanqiushe.manager.LogManager;

public class MessageFragment extends BaseFragment implements
		OnPageChangeListener, OnClickListener {
	private ViewPager mvp;
	private ArrayList<TextView> tvList;
	private ArrayList<RadioButton> rbList;
	private ContactChangeTopTabReceiver receiver;
   
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LogManager.e("xx", "通信创建");
		return inflater.inflate(R.layout.layout_contacts, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		receiver = new ContactChangeTopTabReceiver();
		IntentFilter filter = new IntentFilter(
				ConstantManager.ACTION_CHANGE_TOP_TAB_CONTACT);
		fa.registerReceiver(receiver, filter);

		super.onActivityCreated(savedInstanceState);
	}

	class ContactChangeTopTabReceiver extends BroadcastReceiver {

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
		mvp = (ViewPager) fa.findViewById(R.id.contact_vp);
		mvp.setOffscreenPageLimit(2);

	}

	protected void init() {
		// 存放头部导航的tv 文本控件
		tvList = new ArrayList<TextView>();
		// 存放头部导航中的 line控件
		rbList = new ArrayList<RadioButton>();
		TextView leftTV = (TextView) fa.findViewById(R.id.contacts_top_nav_left_tv);
		TextView middleTV = (TextView) fa.findViewById(R.id.contacts_top_nav_middle_tv);
		TextView rightTV = (TextView) fa.findViewById(R.id.contacts_top_nav_right_tv);
		RadioButton leftLine = (RadioButton) fa
				.findViewById(R.id.contacts_top_nav_left_line);
		RadioButton middleLine = (RadioButton) fa
				.findViewById(R.id.contacts_top_nav_middle_line);
		RadioButton rightLine = (RadioButton) fa
				.findViewById(R.id.contacts_top_nav_right_line);
		tvList.add(leftTV);
		tvList.add(middleTV);
		tvList.add(rightTV);
		rbList.add(leftLine);
		rbList.add(middleLine);
		rbList.add(rightLine);
		leftTV.setOnClickListener(this);
		middleTV.setOnClickListener(this);
		rightTV.setOnClickListener(this);
		leftTV.setText(R.string.txt_notify);
		middleTV.setText(R.string.txt_chat);
		rightTV.setText(R.string.txt_team);

		// 初始进入的
		((MainActivity)fa).changeNavText(tvList,0);
		ArrayList<Fragment> list = new ArrayList<Fragment>();
		list.add(new MessageNotifyFragment());
		list.add(new MessageChatFragment());
		list.add(new MessageMyTeamFragment());
 		ContactFragmentAdapter adapter = new ContactFragmentAdapter(fm, list);
 		mvp.setAdapter(adapter);

	}

	protected void setListener() {
		mvp.setOnPageChangeListener(this);

	}
 

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {

		((MainActivity)fa).changeNavText(tvList,arg0);
		rbList.get(arg0).setChecked(true);
	}

 

	@Override
	public void onClick(View v) {
		int index = -1;
		switch (v.getId()) {
		case R.id.contacts_top_nav_left_tv:
			index = 0;
			break;

		case R.id.contacts_top_nav_middle_tv:
			index = 1;
			break;
		case R.id.contacts_top_nav_right_tv:
			index = 2;
			break;
		}
		if (index != -1) {
			mvp.setCurrentItem(index);
		}

	}

	@Override
	public void onDestroy() {
		LogManager.e("xx", "通信界面销毁");
		fa.unregisterReceiver(receiver);
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
