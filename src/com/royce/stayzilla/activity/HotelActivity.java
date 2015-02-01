package com.royce.stayzilla.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.royce.stayzilla.R;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class HotelActivity extends ActionBarActivity {

	String r, d, l, a;
	HotelActivity ac;
	JSONArray rooms;
	JSONObject amen, dist;
	String link, name;
	ImageView image;
	ProgressDialog pd;
	TextView mRoom, mAmen, mDist,mShield;
	Boolean[] amenBool = new Boolean[16];
	String air,rail,bus;
	String aird,star;
	String raild,busd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hotel);
		image = (ImageView) findViewById(R.id.hotel_image);
		ac = this;
		mRoom = (TextView) findViewById(R.id.rooms);
		mAmen = (TextView) findViewById(R.id.amen);
		mDist = (TextView) findViewById(R.id.dist);
		mShield = (TextView) findViewById(R.id.shield);
		hideAll();
		pd = new ProgressDialog(HotelActivity.this);
		pd.setTitle("Loading");
		pd.setMessage("Please wait...");
		pd.show();
		r = new String(getIntent().getStringExtra("rooms"));
		d = new String(getIntent().getStringExtra("distance"));
		l = new String(getIntent().getStringExtra("link"));
		a = new String(getIntent().getStringExtra("amenities"));
		star = new String(getIntent().getStringExtra("star"));
		name = new String(getIntent().getStringExtra("name"));
		getSupportActionBar().setTitle(name);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// rooms = new JSONArray(r);
				try {
					getBitmapFromURL(l);
					dist = new JSONObject(d);
					amen = new JSONObject(a);
					decode();
					Log.i("fail","000");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}, 1000);

	}

	private void hideAll() {
		// TODO Auto-generated method stub
		image.setVisibility(View.GONE);
		mRoom.setVisibility(View.GONE);
		mAmen.setVisibility(View.GONE);
		mDist.setVisibility(View.GONE);
		mShield.setVisibility(View.GONE);
	}

	private void showAll() {
		// TODO Auto-generated method stub
		image.setVisibility(View.VISIBLE);
		mRoom.setVisibility(View.VISIBLE);
		mAmen.setVisibility(View.VISIBLE);
		mDist.setVisibility(View.VISIBLE);
		Drawable rr;
		if(star.contentEquals("0")||star.contentEquals("1")||star.contentEquals("00"))
			rr= getResources().getDrawable(R.drawable.red);
		else if(star.contentEquals("2"))
			rr= getResources().getDrawable(R.drawable.yellow);
		else
			rr= getResources().getDrawable(R.drawable.green);
		mShield.setBackground(rr);
		mShield.setText(star.replace("00", "0"));

		mShield.setVisibility(View.VISIBLE);
		mAmen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				amenClick();
			}
		});
		mDist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				distClick();
			}
		});
		mRoom.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent room = new Intent(HotelActivity.this,RoomActivity.class);
				room.putExtra("room",r);
				startActivity(room);
				overridePendingTransition(R.anim.right_in, R.anim.left_out);

			}
		});
	}

	protected void distClick() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(ac);
		
		String outdetails[] = { air +"  "+  aird + "km",rail +"  "+  raild + "km",bus +"  "+  busd + "km"
				};

		builder.setTitle("Hotel Details").setItems(outdetails,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// The 'which' argument contains the index
						// position
						// of the selected item
					}
				});
		builder.create().show();
	}
	

	private void amenClick() {
		AlertDialog.Builder builder = new AlertDialog.Builder(ac);
		String[] v = new String[16];
		for (int kk = 0; kk < 16; kk++) {
			if (amenBool[kk])
				v[kk] = new String("Yes");
			else
				v[kk] = new String("No");
		}
		String outdetails[] = { "Restaraunt - " + v[0], "Bar - " + v[1],"Coffee - " + v[2],
				"Biz - " + v[3], "Swim - " + v[4],"Internet - " + v[5],
				"Credit Card - " + v[6], "Laundry - " + v[7],"PickupDrop - " + v[8],
				"Club - " + v[9], "Newspaper - " + v[10],"Elevator - " + v[11],
				"Pure Veg. - " + v[12], "Parking - " + v[13],"Anytime Checkin - " + v[14],
				"Anytime Checkout - " + v[15]
				};

		builder.setTitle("Hotel Details").setItems(outdetails,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// The 'which' argument contains the index
						// position
						// of the selected item
					}
				});
		builder.create().show();
	}

	private void decode() {
		// TODO Auto-generated method stub
		int i = 0;
		try {

			amenBool[i++] = amen.getBoolean("restaurant");
			amenBool[i++] = amen.getBoolean("bar");
			amenBool[i++] = amen.getBoolean("coffee");
			amenBool[i++] = amen.getBoolean("biz");
			amenBool[i++] = amen.getBoolean("swim");
			amenBool[i++] = amen.getBoolean("internet");
			amenBool[i++] = amen.getBoolean("creditCard");
			amenBool[i++] = amen.getBoolean("laundry");
			amenBool[i++] = amen.getBoolean("pickupAndDrop");
			amenBool[i++] = amen.getBoolean("healthClub");
			amenBool[i++] = amen.getBoolean("freeNewspaper");
			amenBool[i++] = amen.getBoolean("elevator");
			amenBool[i++] = amen.getBoolean("pureVeg");
			amenBool[i++] = amen.getBoolean("parking");
			amenBool[i++] = amen.getBoolean("twenty4HourCheckIn");
			amenBool[i] = amen.getBoolean("twenty4HourCheckOut");
			
			air = dist.getString("airportName");
			aird =dist.getString("airport");
			rail = dist.getString("railwayStnName");
			raild =dist.getString("railway");
			bus = dist.getString("busStandName");
			busd =dist.getString("busStand");

			Log.i("fail", "4");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getBitmapFromURL(String src) {
		try {
			java.net.URL url = new java.net.URL(src);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			if (myBitmap != null)
				setImage(myBitmap);
			pd.cancel();
			showAll();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void setImage(Bitmap myBitmap) {
		image.setImageBitmap(myBitmap);
	}
}
