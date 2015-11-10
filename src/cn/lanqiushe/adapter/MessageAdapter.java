package cn.lanqiushe.adapter;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.lanqiushe.engine.DataService;
import cn.lanqiushe.entity.ChatMsg;
import cn.lanqiushe.entity.Notify;
import cn.lanqiushe.manager.ConstantManager;
import cn.lanqiushe.manager.ImageManager;
import cn.lanqiushe.manager.StringManager;
import cn.lanqiushe.manager.ToastManager;
import cn.lanqiushe.ui.MessageFragment;
import cn.lanqiushe.R;

public class MessageAdapter extends BaseAdapter {
	 public static final int CATEGORY_NOTIFY = 0;
	    public static final int CATEGORY_CHAT = 1;
	private Context context;
	private ArrayList<Notify> notifyList;
	private ArrayList<ChatMsg> chatList;
	private Handler handler;
	private int category;
	private Resources rs;
	public MessageAdapter(Context context, int category ) {
		this.context = context;
		this.category = category;
		rs = context.getResources();
	}
	
	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public ArrayList<Notify> getNotifyList() {
		return notifyList;
	}
	public ArrayList<ChatMsg> getChatList() {
		return chatList;
	}
	public void setNotifyList(ArrayList<Notify> notifyList) {
		if(this.notifyList==null){
			this.notifyList = notifyList;
		}else{
			this.notifyList.addAll(notifyList);
		}
		
	}
	public void setChatList(ArrayList<ChatMsg> chatList) {
		if(this.chatList==null){
			this.chatList = chatList;
		}else{
			this.chatList.addAll(chatList);
		}
		 
	}
	@Override
	public int getCount() {
		 if(category==  CATEGORY_CHAT){
			 return chatList.size();
		 }else{
			 return notifyList.size();
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
	public View getView(final int position, View convertView, final ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			/* 绑定相应的视图 */
			convertView = View.inflate(context,R.layout.item_message, null);
			holder.head = (ImageView) convertView
					.findViewById(R.id.item_notify_head_iv);
			holder.title = (TextView) convertView
					.findViewById(R.id.item_notify_title_tv);
			holder.content = (TextView) convertView
					.findViewById(R.id.item_notify_content_tv);
			holder.status = (TextView) convertView
					.findViewById(R.id.item_notify_status_tv);
			holder.date = (TextView) convertView
					.findViewById(R.id.item_notify_date_tv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		switch (category) {
		case  CATEGORY_NOTIFY:{
			final Notify notify = notifyList.get(position);
			holder.title.setText(notify.notifyType);
			holder.content.setText(notify.notifySimpleContent);
			holder.status.setVisibility(View.VISIBLE);
			if(rs.getString(R.string.agreed).equals(notify.notifyStatus)){
				holder.status.setBackgroundDrawable(null);
                holder.status.setTextColor(rs.getColor(R.color.gray));
			}else{
				// android:textColor="@color/blue_cb"
				//  android:background="@drawable/bg_blue"
				
				holder.status.setBackgroundResource(R.drawable.bg_blue);
                holder.status.setTextColor(rs.getColor(R.color.blue_cb));
			}
			holder.status.setText(notify.notifyStatus);
			holder.date.setText(notify.notifyTime);
			ImageManager.getInstance().displayImage(notify.notifierHead, holder.head, ImageManager.getUserHeadOptions());
			holder.status.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//只有按钮为同意的时候才可以点击
					    if(rs.getString(R.string.agree).equals(holder.status.getText().toString())){
					    	HashMap<String, Object> map = new HashMap<String, Object>();
							map.put("position", position);
							map.put("notifyId", notify.notifyId);
							map.put("handleType", ConstantManager.NOTIFY_TREATEDTYPE_STATUS);
						    DataService.handleNotify(map, context, handler);
					    }
						
				}
			});
			
			break;
		}
			

		case  CATEGORY_CHAT:
			ChatMsg chat = chatList.get(position);
			ImageManager.getInstance().displayImage(chat.head, holder.head, ImageManager.getUserHeadOptions());
			holder.title.setText(chat.name);
			holder.content.setText(chat.content);
			holder.date.setText(chat.time);
			break;
		}
		
//		final View view = convertView;
//        final int p = position;
//        final int one = holder.status.getId();
//		holder.status.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               // callback.onClick(view, parent, p,one);
//            }
//        });
		return convertView;
	}

	private static class ViewHolder {
		ImageView head;
		TextView title, content,status,date;
	}

}
