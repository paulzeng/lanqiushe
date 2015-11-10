package cn.lanqiushe.adapter;

import java.util.ArrayList;

import cn.lanqiushe.entity.Player;
import cn.lanqiushe.manager.ImageManager;
import cn.lanqiushe.view.ScrollListView;

import cn.lanqiushe.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MeTeamMemberAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Player> list;
	 

	public MeTeamMemberAdapter(Context context, ArrayList<Player> list) {
		this.context = context;
		this.list = list;
	 
	}

	public void addPlayers(ArrayList<Player> list) {
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
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.item_me_team_member,
					null);
			holder.head = (ImageView) convertView
					.findViewById(R.id.item_me_team_memeber_head_iv);
			holder.name = (TextView) convertView
					.findViewById(R.id.item_me_team_memeber_name_tv);
			holder.location = (TextView) convertView
					.findViewById(R.id.item_me_team_memeber_location_tv);
			holder.noTeamMember = (TextView) convertView
					.findViewById(R.id.item_me_team_memeber_empty_tv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Player tm  = list.get(position);
		 
		if (tm == null) {
			holder.noTeamMember.setVisibility(View.VISIBLE);
			holder.noTeamMember.setText(R.string.not_available2);
		} else {
			holder.noTeamMember.setVisibility(View.GONE);
			holder.head.setVisibility(View.VISIBLE);
			ImageManager.getInstance().displayImage(tm.playerProtrait, holder.head, ImageManager.getUserHeadOptions());
			holder.name.setVisibility(View.VISIBLE);
			holder.name.setText(tm.nickName);
			holder.location.setVisibility(View.VISIBLE);
			holder.location.setText(tm.playerPost);
		}

		return convertView;
	}

	private static class ViewHolder {
		ImageView head;
		TextView name, location, noTeamMember;

	}

}
