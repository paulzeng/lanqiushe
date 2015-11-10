package cn.lanqiushe.adapter;

import cn.lanqiushe.R;
import cn.lanqiushe.entity.Park;
import cn.lanqiushe.entity.Team;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ParkInfoAdapter extends BaseAdapter {
	private String[] left;
    private String[] right = new String[3];
    private Context context;
    public ParkInfoAdapter(Context context,Park park){
    	this.context = context;
    	this.right[0] =  park.name;
    	this.right[1] = park.distance;
    	this.right[2] = park.money;
    	this.left  = context.getResources().getStringArray(R.array.myparkinfo);
    }
    
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return left.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView==null){
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.item_park_info, null);
			holder.left = (TextView) convertView.findViewById(R.id.item_park_info_left_tv);
			holder.right = (TextView) convertView.findViewById(R.id.item_park_info_right_tv);
			holder.iv = (ImageView) convertView.findViewById(R.id.item_park_info_right_distance_iv);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.left.setText(left[position]);
		holder.right.setText(right[position]);
		if(position==1){
			holder.iv.setVisibility(View.VISIBLE);
		}
		return convertView;
	}
	private static class ViewHolder{
		TextView left,right;
		
		ImageView iv;
	}
	
	

}
