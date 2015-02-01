package com.royce.stayzilla.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.royce.stayzilla.R;
import com.royce.stayzilla.adapter.StayItemAdapter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class StayActivity extends ActionBarActivity {

	String[] name, address, price, star;
	JSONObject[] amenities, distanceFrom;
	JSONArray[] rooms;
	String[] imageLink;
	ListView list;
	public ProgressDialog pd;
	String start, end, place;
	String id;
	String phrase;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_stay);
		list = (ListView) findViewById(R.id.options);
		pd = new ProgressDialog(StayActivity.this);
		pd.setTitle("Loading");
		pd.setMessage("Please wait...");
		pd.show();
		start = new String(getIntent().getStringExtra("start"));
		end = new String(getIntent().getStringExtra("end"));
		place = new String(getIntent().getStringExtra("place"));
		id = new String(getIntent().getStringExtra("id"));
		getSupportActionBar().setTitle(place);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				readStayInfo();
				getPhrase();
			}
		}, 1000);

	}

	protected void getPhrase() {
		// TODO Auto-generated method stub
		
	}

	private void readStayInfo() {
		// TODO Auto-generated method stub

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://180.92.168.7/hotels");
		StringBuilder builder = new StringBuilder();

		try {

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("location", place));
			nameValuePairs.add(new BasicNameValuePair("checkin", start));
			nameValuePairs.add(new BasicNameValuePair("checkout", end));
			nameValuePairs
					.add(new BasicNameValuePair("property_type", "Hotels"));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);

				}
			} else {
			}
			decodeString(builder.toString());

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
	}

	private void decodeString(String string) {
		// TODO Auto-generated method stub
		try {
			JSONObject a = new JSONObject(string);
			JSONArray results = a.getJSONArray("hotels");
			int l = results.length();
			if (l == 0) {
				Toast.makeText(getApplicationContext(),
						"Sorry, no hotels available", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			int oo = results.length();
		//	if (results.length() > 10)
			//	oo = 10;
			name = new String[oo];
			star = new String[oo];
			address = new String[oo];
			price = new String[oo];
			rooms = new JSONArray[oo];
			amenities = new JSONObject[oo];
			distanceFrom = new JSONObject[oo];
			imageLink = new String[oo];

			for (int i = 0; i < oo; i++) {
				JSONObject obj = results.getJSONObject(i);
				name[i] = obj.getString("displayName");
				star[i] = obj.getString("starRating");
				address[i] = obj.getString("address");
				price[i] = obj.getString("price");
				imageLink[i] = obj.getString("imageURL");
				rooms[i] = obj.getJSONArray("rooms");
				amenities[i] = obj.getJSONObject("amenities");
				distanceFrom[i] = obj.getJSONObject("distanceFrom");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		pd.cancel();
		setUpList();
	}

	private void setUpList() {
		// TODO Auto-generated method stub
		StayItemAdapter adapter = new StayItemAdapter(getApplicationContext(),
				name, price, star);

		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				handleItemClick(position);
			}

		});
	}

	protected void handleItemClick(int position) {
		// TODO Auto-generated method stub
		Intent hotel = new Intent(StayActivity.this, HotelActivity.class);
		hotel.putExtra("rooms", rooms[position] + "");
		hotel.putExtra("distance", distanceFrom[position] + "");
		hotel.putExtra("amenities", amenities[position] + "");
		hotel.putExtra("link", imageLink[position]);
		hotel.putExtra("name", name[position]);
		hotel.putExtra("star", star[position]);
		startActivity(hotel);
		overridePendingTransition(R.anim.right_in, R.anim.left_out);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.menu_stay, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		
		case R.id.action_places:
			Intent _place = new Intent(StayActivity.this, PlacesActivity.class);
			_place.putExtra("place",place );
			startActivity(_place);
			overridePendingTransition(R.anim.right_in, R.anim.left_out);
	
			break;
		}
		return true;
	}

}
