package cn.lanqiushe.adapter;

import java.util.ArrayList;

import cn.lanqiushe.entity.Park;
import cn.lanqiushe.entity.Player;
import cn.lanqiushe.entity.Team;
import cn.lanqiushe.manager.ImageManager;

import cn.lanqiushe.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NearbyContentAdapter extends BaseAdapter {

	public static final int TEAM = 0;
	public static final int PLayer = 1;
	public static final int PARK = 2;

	private Context context;
	private int category;
	private ArrayList<Team> listTeam;
	private ArrayList<Player> listPlayer;
	private ArrayList<Park> listPark;

	public NearbyContentAdapter(Context context, int category) {
		this.context = context;
		this.category = category;
	}

	public void setListTeam(ArrayList<Team> listTeam) {
		
		if(this.listTeam==null){
			this.listTeam = listTeam;
		}else{
			this.listTeam.addAll(listTeam);
		}
	}

	public void setListPlayer(ArrayList<Player> listPlayer) {
		 
		if(this.listPlayer==null){
			this.listPlayer = listPlayer;
		}else{
			this.listPlayer.addAll(listPlayer);
		}
		
	}

	public void setListPark(ArrayList<Park> listPark) {
		if(this.listPark==null){
			this.listPark = listPark;
		}else{
			this.listPark.addAll(listPark);
		}
		
	}

	@Override
	public int getCount() {
		if (category == TEAM) {
			return listTeam.size();
		} else if (category == PARK) {
			return listPark.size();
		} else {
			return listPlayer.size();
		}
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
			convertView = View.inflate(context, R.layout.item_nearby, null);
			holder.head = (ImageView) convertView
					.findViewById(R.id.item_nearby_head_iv);
			holder.name = (TextView) convertView
					.findViewById(R.id.item_nearby_name_tv);
			holder.distance = (TextView) convertView
					.findViewById(R.id.item_nearby_distance_tv);

			switch (category) {
			case TEAM: {
				holder.number = (TextView) convertView
						.findViewById(R.id.item_nearby_number_tv);

				break;
			}
			case PARK:
			case PLayer: {
				holder.money = (TextView) convertView
						.findViewById(R.id.item_nearby_money_tv);
				holder.location = (TextView) convertView
						.findViewById(R.id.item_nearby_location_tv);
				break;
			}

			}

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// 通用，三个界面都有，而且表示同样的类型

		switch (category) {
		case TEAM: {
			Team t = listTeam.get(position);
			ImageManager.getInstance().displayImage(t.teamLogo, holder.head,
					ImageManager.getUserHeadOptions());
			holder.name.setText(t.teamName);
			holder.number.setText(t.playerTotal);
			holder.distance.setText(t.distance);
			holder.number.setVisibility(View.VISIBLE);
			break;

		}

		case PLayer: {
			Player p = listPlayer.get(position);
			ImageManager.getInstance().displayImage(p.playerProtrait,
					holder.head, ImageManager.getUserHeadOptions());
			holder.name.setText(p.nickName);
			holder.location.setText(p.playerPost);
			holder.distance.setText(p.distance);
			Team team = p.team;
			if (team == null) {
				holder.money.setText("暂无");
			} else {
				holder.money.setText(team.teamName + team.playerTotal);
			}

			holder.money.setVisibility(View.VISIBLE);
			holder.location.setVisibility(View.VISIBLE);

			break;
		}

		case PARK: {
			Park p = listPark.get(position);
			ImageManager.getInstance().displayImage(p.logo, holder.head,
					ImageManager.getUserHeadOptions());
			holder.name.setText(p.name);
			holder.money.setText(p.money);
			holder.distance.setText(p.distance);
			holder.location.setVisibility(View.INVISIBLE);
			holder.money.setVisibility(View.VISIBLE);

			break;
		}
		}

		return convertView;
	}

	private static class ViewHolder {
		ImageView head;
		TextView name, number, money, location, distance;
	}

}
