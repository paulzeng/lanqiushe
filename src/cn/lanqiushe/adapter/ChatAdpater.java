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
 
import cn.lanqiushe.entity.ChatMsg;
import cn.lanqiushe.manager.ImageManager;
import cn.lanqiushe.R;

public class ChatAdpater extends BaseAdapter {

	private ArrayList<ChatMsg> list;
	private Context context;

	public ChatAdpater(Context context) {
		this.context = context;
	}

	public ArrayList<ChatMsg> getList() {
		return list;
	}

	public void setList(ArrayList<ChatMsg> list) {
		this.list = list;
	}

	// 获取ListView的项个数
	public int getCount() {
		return list.size();
	}

	// 获取项
	public Object getItem(int position) {
		return list.get(position);
	}

	// 获取项的ID
	public long getItemId(int position) {
		return position;
	}

	// 获取View
	public View getView(int position, View convertView, ViewGroup parent) {

		

		ViewHolder holder = null;
		if (convertView == null) {
			 holder = new ViewHolder();
			 convertView = View.inflate(context, R.layout.item_chatting, null);
             holder.myhead =  (ImageView) convertView.findViewById(R.id.item_chating_myhead_iv);
			 holder.mycontent = (TextView) convertView.findViewById(R.id.item_chating_mycontent_tv);
			 holder.yourhead =  (ImageView) convertView.findViewById(R.id.item_chating_yourhead_iv);
			 holder.yourcontent = (TextView) convertView.findViewById(R.id.item_chating_yourcontent_tv);
			 convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ChatMsg e = list.get(position);
		if(e.isFromOther){//是对方发来的
			holder.myhead.setVisibility(View.GONE);
			holder.mycontent.setVisibility(View.GONE);
			holder.yourhead.setVisibility(View.VISIBLE);
			holder.yourcontent.setVisibility(View.VISIBLE);
			
			
			
			holder.yourcontent.setText(e.content);
			holder.yourcontent.setBackgroundResource(R.drawable.chat_to_bg_normal);
			ImageManager.getInstance().displayImage(e.head, holder.yourhead, ImageManager.getUserHeadOptions());
		}else{ //是自己发的
			holder.yourhead.setVisibility(View.GONE);
			holder.yourcontent.setVisibility(View.GONE);
			holder.myhead.setVisibility(View.VISIBLE);
			holder.mycontent.setVisibility(View.VISIBLE);
			
			
			holder.mycontent.setText(e.content);
			holder.mycontent.setBackgroundResource(R.drawable.chat_from_bg_normal);
			ImageManager.getInstance().displayImage(e.head, holder.myhead, ImageManager.getUserHeadOptions());
		}
		
	 

		return convertView;
	}

	// 通过ViewHolder显示项的内容
	private static class ViewHolder {
		ImageView myhead,yourhead;
		TextView mycontent,yourcontent;
	}

}
