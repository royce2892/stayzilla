package com.royce.stayzilla.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import com.royce.stayzilla.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PlacesAdapter extends BaseAdapter {
	private Context mContext;
	private final String[] name;
	private final String[] rating;
	private final String[] type;
	private final String[] addr;
	private final String[] image;

	public PlacesAdapter(Context c, String[] Name, String[] Rating,
			String Type[], String[] Addr, String[] Image) {
		mContext = c;
		this.name = Name;
		this.rating = Rating;
		this.type = Type;
		this.addr = Addr;
		this.image = Image;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		View list;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			list = inflater.inflate(R.layout.row_city_item, parent, false);

		} else {
			list = (View) convertView;
		}
		TextView _name = (TextView) list.findViewById(R.id.city_name);
		TextView _entry = (TextView) list.findViewById(R.id.city_addr);
		TextView _rating = (TextView) list.findViewById(R.id.city_rate);
		TextView _type = (TextView) list.findViewById(R.id.city_types);
		final ImageView _image = (ImageView) list.findViewById(R.id.city_image);
		_image.setImageBitmap(getBitmapFromURL(image[position]));

		_name.setText(name[position]);
		_entry.setText(addr[position]);
		_type.setText(type[position]);
		_rating.setText(rating[position]);
		return list;
	}

	public Bitmap getBitmapFromURL(String src) {
		try {
			java.net.URL url = new java.net.URL(src);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			if (myBitmap != null)
				return myBitmap;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}