package cn.lanqiushe.adapter;

import java.util.ArrayList;

import cn.lanqiushe.R;
import cn.lanqiushe.entity.Player;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TeamInfoAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Player> list;
    public TeamInfoAdapter(Context context,ArrayList<Player> list){
    	this.context = context;
    	this.list = list;
    }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
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
			convertView = View.inflate(context, R.layout.item_team_info, null);
			holder.post = (TextView) convertView.findViewById(R.id.item_team_info_post_tv);
			holder.isnull = (TextView) convertView.findViewById(R.id.item_team_info_null_tv);
			holder.name = (TextView) convertView.findViewById(R.id.item_team_info_name_tv);
			holder.iv = (ImageView) convertView.findViewById(R.id.nearby_player_info_item_location_open_iv);
			convertView.setTag(holder);
		}else{
			holder =  (ViewHolder) convertView.getTag();
		}
		
		Player p = list.get(position);
		if(p!=null){
			holder.post.setText(p.playerPost);
			holder.name.setText(p.nickName);
		}else{
			holder.isnull.setText("ÔÝÈ±");
			holder.iv.setVisibility(View.GONE);
		}
		
		
		
		return convertView;
	}
	private static class ViewHolder{
		TextView post,isnull,name;
		ImageView iv;
	}

}
