package cn.lanqiushe.adapter;

import cn.lanqiushe.R;
import cn.lanqiushe.manager.LogManager;
import cn.lanqiushe.manager.ToastManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactPhoneAdapter extends CursorAdapter {
	private ContentResolver resolver;
	public ContactPhoneAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
	 
		resolver = context.getContentResolver();
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return View.inflate(context, R.layout.item_contacts, null);
	}

	@Override
	public void bindView(View view, final Context context, Cursor cursor) {

		String name = cursor.getString(cursor
				.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
		String id = cursor.getString(cursor
				.getColumnIndex(ContactsContract.Contacts._ID));

//		 Cursor c =resolver.query(
//		 ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//		 null,
//		 ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "="
//		 + id, null, null);
//		 while (c.moveToNext()) {
//		 String phone = c
//		 .getString(c
//		 .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//		
//		 }
//		 }
		  ViewHolder holder = null;
		if (holder == null) {
			holder = new ViewHolder();
		}
		if (holder.head == null) {
			 
		}
		if (holder.name == null) {
			holder.name = (TextView) view
					.findViewById(R.id.item_contacts_name_tv);
		}
		if (holder.status == null) {
			holder.status = (TextView) view
					.findViewById(R.id.item_contacts_status_tv);
		}
		holder.name.setText(name);
		final int status = R.string.status_yaoqing;
		holder.status.setText(status);
		final TextView tv = holder.status;
		holder.status.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (status == R.string.status_yaoqing) {
					tv.setText(R.string.status_yiyaoqing);
					 ToastManager.show(context, "已经邀请   post一条数据 等待回复");

				} else if (status == R.string.status_yiyaoqing) {
					 
				} else if (status == R.string.status_tianjia) {

				} else {// 否则就是已经添加，不做任何

				}

			}
		});
	}

	private static class ViewHolder {
		ImageView head;
		TextView name, status;
	}

}
