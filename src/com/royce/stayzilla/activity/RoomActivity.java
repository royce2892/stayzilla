package com.royce.stayzilla.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.royce.stayzilla.R;
import com.royce.stayzilla.adapter.RoomItemAdapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class RoomActivity extends ActionBarActivity {

	String r;
	JSONArray roo;
	String[] rName, rRate, rBed, rICH, rRoomLeft, rOcc;
	ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getSupportActionBar().setTitle("ROOMS");
		setContentView(R.layout.activity_stay);
		listView = (ListView) findViewById(R.id.options);
		r = new String(getIntent().getStringExtra("room"));
		try {
			roo = new JSONArray(r);
			decode(roo);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void decode(JSONArray roo2) {
		// TODO Auto-generated method stub
		int l = roo2.length();
		if (l == 0) {
			Toast.makeText(getApplicationContext(),
					"Sorry, no rooms available", Toast.LENGTH_SHORT).show();
			return;
		}
		rName = new String[l];
		rBed = new String[l];
		rICH = new String[l];
		rRate = new String[l];
		rRoomLeft = new String[l];
		rOcc = new String[l];

		for (int i = 0; i < l; i++) {
			JSONObject obj;
			try {
				obj = roo2.getJSONObject(i);
				Log.i("fail", "0" + l);

				rName[i] = new String(obj.getString("rtype"));
				rBed[i] = new String(obj.getBoolean("isExtraBedType") + "");
				rICH[i] = new String(obj.getBoolean("isICHRoom") + "");
				Log.i("fail", "1" + l);
				rRate[i] = new String(obj.getString("rdiscountPriceWithTax"));
				rRoomLeft[i] = new String(obj.getString("totalnoofrooms"));
				rOcc[i] = new String(obj.getString("roccupants"));
				Log.i("fail", "2" + l);
				setUpList();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void setUpList() {
		// TODO Auto-generated method stub
		RoomItemAdapter adapter = new RoomItemAdapter(getApplicationContext(),
				rName, rRate);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				handleClick(position);
			}
		});
	}

	protected void handleClick(int position) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(RoomActivity.this);

		String outdetails[] = { "Room Rate - " + rRate[position],
				"Total Rooms - " + rRoomLeft[position],
				"Extra Bed - " + rBed[position],
				"ICH Room - " + rICH[position],
				"Occupants - " + rOcc[position], };

		builder.setTitle(rName[position]).setItems(outdetails,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// The 'which' argument contains the index
						// position
						// of the selected item
					}
				});
		builder.setPositiveButton("Book", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Booked", 500).show();
			}
		});
		builder.setNegativeButton("Cancel", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});
		builder.create().show();
	}
}
