package com.royce.stayzilla.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.royce.stayzilla.R;
import com.royce.stayzilla.adapter.PlacesAdapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class PlacesActivity extends ActionBarActivity {

	String place;
	ListView list;
	String[] name = null;
	String[] rating = null;
	String[] types = null;
	String[] link = null;
	String[] addr = null;
	String[] lat = null;
	String[] lon = null;
	String[] ref = null;
	String[] call = null;
	String[] plus, web, reviews;
	ProgressDialog pd;
	PlacesActivity ac;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		place = new String(getIntent().getStringExtra("place"));
		setContentView(R.layout.activity_stay);
		pd = new ProgressDialog(PlacesActivity.this);
		pd.setTitle("Loading");
		pd.setMessage("Please wait...");
		pd.show();
		list = (ListView) findViewById(R.id.options);
		ac = this;
		getSupportActionBar().setTitle(place);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				readPlaceInfo(place);
			}
		}, 100);
	}

	private void readPlaceInfo(final String search) {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				StringBuilder builder = new StringBuilder();
				HttpClient client = new DefaultHttpClient();
				String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=attractions+in+"
						+ search
						+ "&key=AIzaSyCwIJNpiLThD8JRk9N5G-opFQ_-YWY7cag";
				HttpGet httpGet = new HttpGet(url);
				try {
					HttpResponse response = client.execute(httpGet);
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
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				decodeString(builder.toString());
			}
		}, 150);
	}

	protected void decodeString(String string) {
		// TODO Auto-generated method stub

		try {
			JSONObject a = new JSONObject(string);
			JSONArray results = a.getJSONArray("results");
			int l = 7;
			if (l == 0) {
				Toast.makeText(getApplicationContext(),
						"Sorry, no places available", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			name = new String[l];
			rating = new String[l];
			types = new String[l];
			link = new String[l];
			addr = new String[l];
			lat = new String[l];
			lon = new String[l];
			ref = new String[l];
			call = new String[l];
			plus = new String[l];
			web = new String[l];
			reviews = new String[l];
			for (int i = 0; i < l; i++) {

				JSONObject obj = results.getJSONObject(i);
				name[i] = new String(obj.getString("name"));
				rating[i] = new String("Rating : " + obj.getString("rating"));
				types[i] = new String(obj.getString("types").replace("\"", "")
						.replace("[", "").replace("]", "").replace("_", ""));
				link[i] = new String(obj.getString("icon"));
				addr[i] = new String(obj.getString("formatted_address"));
				ref[i] = new String(obj.getString("reference"));
				getNumber(i);
				JSONObject geo = obj.getJSONObject("geometry");
				JSONObject loc = geo.getJSONObject("location");
				lat[i] = new String(loc.getString("lat"));
				lon[i] = new String(loc.getString("lng"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		setUpList();
	}

	private void getNumber(final int i) {
		// TODO Auto-generated method stub
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				StringBuilder builder = new StringBuilder();
				HttpClient client = new DefaultHttpClient();
				String url = "https://maps.googleapis.com/maps/api/place/details/json?reference="
						+ ref[i]
						+ "&key=AIzaSyCwIJNpiLThD8JRk9N5G-opFQ_-YWY7cag";
				HttpGet httpGet = new HttpGet(url);
				try {
					HttpResponse response = client.execute(httpGet);
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
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					JSONObject a = new JSONObject(builder.toString());
					JSONObject r = a.getJSONObject("result");
					call[i] = new String(r
							.getString("international_phone_number"));
					plus[i] = new String(r.getString("url"));
					web[i] = new String(r.getString("website"));
					JSONArray rrr = r.getJSONArray("reviews");
					reviews[i] = new String(rrr.toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, 100);
		pd.cancel();

	}

	private void setUpList() {
		// TODO Auto-generated method stub
		PlacesAdapter adapter = new PlacesAdapter(getApplicationContext(),
				name, rating, types, addr, link);
		list.setAdapter(adapter);

		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				handleClick(position);

			}

		});
	}

	protected void handleClick(final int position) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(ac);

		String outdetails[] = { "View on Map", "Call", "Google Plus",
				"Website", "Reviews" };

		builder.setTitle(name[position]).setItems(outdetails,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							Uri uri = Uri.parse("geo:0,0?q=" + lat[position]
									+ "," + lon[position]);
							Intent intent = new Intent(Intent.ACTION_VIEW, uri);
							startActivity(intent);
						} else if (which == 1) {
							Intent intent = new Intent(Intent.ACTION_CALL, Uri
									.parse("tel:" + call[position]));
							startActivity(intent);

						} else if (which == 2) {
							Intent intent = new Intent(Intent.ACTION_VIEW, Uri
									.parse(plus[position]));
							startActivity(intent);
						} else if (which == 3) {
							Intent intent = new Intent(Intent.ACTION_VIEW, Uri
									.parse(web[position]));
							startActivity(intent);
						} else {
							Intent intent = new Intent(PlacesActivity.this,
									ReviewActivity.class);
							if(reviews[position]==null)
								reviews[position]="";
							intent.putExtra("rev", reviews[position]);
							startActivity(intent);

							overridePendingTransition(R.anim.right_in,
									R.anim.left_out);
						}
					}
				});
		builder.create().show();
	}
}
