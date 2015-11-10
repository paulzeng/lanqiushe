package cn.lanqiushe.adapter;

import java.util.ArrayList;

import cn.lanqiushe.R;
import cn.lanqiushe.entity.Contact;
import cn.lanqiushe.manager.ImageManager;
import cn.lanqiushe.manager.ToastManager;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactsAdapter extends BaseAdapter {
	private ArrayList<Contact> list;
	private Context context;
    private Resources rs;
	public ContactsAdapter( Context context) {
		 
		this.context = context;
        rs = context.getResources();
	}

	public ArrayList<Contact> getList() {
		
		return list;
	}

	public void setList(ArrayList<Contact> list) {
		if(this.list==null){
			this.list= list;
		} else{
			this.list.addAll(list);
		}
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
		final int status = c.status;
		//   android:background="@drawable/bg_blue"
       // android:textColor="@color/blue_cb"
		switch (status) {
		case R.string.status_tianjia:
		case R.string.status_yaoqing:
			holder.status.setBackgroundDrawable(rs.getDrawable(R.drawable.bg_blue));
			holder.status.setTextColor(rs.getColor(R.color.blue_cb));
			 break;
		case R.string.status_yitianjia:
		case R.string.status_yiyaoqing:
			holder.status.setBackgroundDrawable(null);
			holder.status.setTextColor(rs.getColor(R.color.gray));
			break;
		}
 	
		holder.status.setText(status);
		 
		
		return convertView;
	}

	private static class ViewHolder {
		TextView name, status;
	}
}
