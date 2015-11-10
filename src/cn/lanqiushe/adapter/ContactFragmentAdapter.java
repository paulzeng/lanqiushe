package cn.lanqiushe.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ContactFragmentAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Fragment> list;

	public ContactFragmentAdapter(FragmentManager fm,ArrayList<Fragment> list) {
		super(fm);
		 this.list = list;
	}

	@Override
	public Fragment getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public int getCount() {
		return list.size();
	}

}
