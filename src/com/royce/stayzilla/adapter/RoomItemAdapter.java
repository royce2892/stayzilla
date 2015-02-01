package com.royce.stayzilla.adapter;

import com.royce.stayzilla.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RoomItemAdapter extends BaseAdapter {
	private Context mContext;
	private String[] name, price;

	public RoomItemAdapter(Context c, String[] _name, String[] _price) {
		mContext = c;
		this.name = _name;
		this.price = _price;
	}

	public RoomItemAdapter(Context c) {
		mContext = c;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return name.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View list;
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			list = new View(mContext);
			list = inflater.inflate(R.layout.room_row_item, null);
			TextView title = (TextView) list.findViewById(R.id.room_name);
			TextView summary = (TextView) list.findViewById(R.id.room_rate);

			title.setText(name[position].trim());
			summary.setText(price[position]);

		} else {
			list = (View) convertView;
		}
		return list;
	}
}