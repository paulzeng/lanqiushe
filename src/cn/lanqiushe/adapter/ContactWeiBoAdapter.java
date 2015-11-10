package cn.lanqiushe.adapter;

import java.util.ArrayList;

import cn.lanqiushe.R;
import cn.lanqiushe.entity.Contact;
import cn.lanqiushe.manager.ImageManager;
import cn.lanqiushe.manager.LogManager;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactWeiBoAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Contact> list;
	 
 

	public ContactWeiBoAdapter(Context context, ArrayList<Contact> list) {
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
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.item_contacts, null);
			holder.name = (TextView) convertView
					.findViewById(R.id.item_contacts_name_tv);
		 
			holder.status = (TextView) convertView
			.findViewById(R.id.item_contacts_status_tv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
        Contact c = list.get(position);
        
        
        
        holder.name.setText(c.name);
      //  holder.status.setText(c.status);
//      holder.status.setBackgroundDrawable(null);
//      holder.status.setTextColor(Color.rgb(146,148,151));
	    return convertView;
	}

	private static class ViewHolder {
		TextView name, status;
		ImageView head;
	}

}
