package cn.lanqiushe.adapter;

import java.util.ArrayList;

import cn.lanqiushe.R;
import cn.lanqiushe.manager.ImageManager;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
public class ParkInfoPhotosAdapter extends PagerAdapter {
	private ArrayList<View> list; 
	private String[] urls;
	public ParkInfoPhotosAdapter(ArrayList<View> list,String[] urls){
		this.list =list;
		this.urls = urls;
	}
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(list.get(position));

	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = list.get(position);
		ViewHolder holder=null;
		if(holder==null){
			holder  = new ViewHolder();
			holder.iv = (ImageView) view.findViewById(R.id.item_nearby_park_info_iv);
		}
		ImageManager.getInstance().displayImage(urls[position], holder.iv,ImageManager.getNorOptions());
		container.addView(holder.iv);

		return list.get(position);
	} 
	private static class ViewHolder{
		ImageView iv;
	}
}
