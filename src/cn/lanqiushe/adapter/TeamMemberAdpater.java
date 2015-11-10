package cn.lanqiushe.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.lanqiushe.R;  
import cn.lanqiushe.entity.Player;
import cn.lanqiushe.entity.TeamMember;
import cn.lanqiushe.manager.ImageManager;

public class TeamMemberAdpater extends BaseAdapter {
	private Context context;
	private ArrayList<Player> mData;
	 

	public TeamMemberAdpater(Context context, ArrayList<Player> data) {
		 
		this.context = context;
		this.mData = data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			/* 绑定相应的视图 */
			convertView = View.inflate(context,R.layout.list_team_member, null);
			holder.head = (ImageView) convertView
					.findViewById(R.id.img_head);
			holder.name = (TextView) convertView
					.findViewById(R.id.tv_name);
			holder.post = (TextView) convertView
					.findViewById(R.id.tv_role); 
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Player p  =mData.get(position);
		ImageManager.getInstance().displayImage(p.playerProtrait, holder.head, ImageManager.getUserHeadOptions());
		holder.name.setText(p.nickName);
		holder.post.setText(p.playerPost); 
		return convertView;
	}

	private static class ViewHolder {
		ImageView head;
		TextView name,post;
	}

}