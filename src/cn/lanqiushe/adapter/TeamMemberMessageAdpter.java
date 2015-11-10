package cn.lanqiushe.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cn.lanqiushe.entity.TeamMemberMessage;

import cn.lanqiushe.R;

public class TeamMemberMessageAdpter extends BaseAdapter {
	Context mContext;
	List<TeamMemberMessage> mData;
	private LayoutInflater inflater;

	public TeamMemberMessageAdpter(Context context, List<TeamMemberMessage> data) {
		inflater = LayoutInflater.from(context);
		this.mContext = context;
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
			convertView = inflater
					.inflate(R.layout.list_team_member_item, null);
			holder.img_head = (ImageView) convertView
					.findViewById(R.id.img_head);
			holder.tv_team_member_name = (TextView) convertView
					.findViewById(R.id.tv_team_member_name); 
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_team_member_name.setText(mData.get(position)
				.getTeam_member_name()); 
		return convertView;
	}

	private static class ViewHolder {
		ImageView img_head;
		TextView tv_team_member_name;
	}

}