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
import org.json.JSONObject;

import com.royce.stayzilla.R;

import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements OnClickListener {

	AutoCompleteTextView atvPlaces;
	DatePicker from, to;
	TextView w1, w2, go;
	String[] id = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getSupportActionBar().setTitle("STAYZILLA");

		setContentView(R.layout.activity_main);

		from = (DatePicker) findViewById(R.id.from_date);
		to = (DatePicker) findViewById(R.id.to_date);
		go = (TextView) findViewById(R.id.go);
		w1 = (TextView) findViewById(R.id.wheels_tv1);
		w2 = (TextView) findViewById(R.id.wheels_tv2);
		go.setClickable(true);
		go.setOnClickListener(this);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		atvPlaces = (AutoCompleteTextView) findViewById(R.id.atv_places);
		atvPlaces.setThreshold(1);

		atvPlaces.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(final CharSequence s, int start,
					int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				readTravelInfo(s.toString());

			}
		});
	}

	private void readTravelInfo(final String search) {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				StringBuilder builder = new StringBuilder();
				HttpClient client = new DefaultHttpClient();
				String url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input="
						+ search
						+ "&types=geocode&key=AIzaSyCUykPstomDep6-PWmMenJ2rUccTsjUGak";
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
		}, 1500);
	}

	public void decodeString(String result) {
		// TODO Auto-generated method stub
		String[] name = null;
		try {
			JSONObject a = new JSONObject(result);
			JSONArray results = a.getJSONArray("predictions");
			int l = results.length();
			if (l == 0) {
				Toast.makeText(getApplicationContext(),
						"Sorry, no places available", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			name = new String[l];
			id = new String[l];
			for (int i = 0; i < results.length(); i++) {

				JSONObject obj = results.getJSONObject(i);
				name[i] = obj.getString("description");
				id[i] = obj.getString("id");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				MainActivity.this, android.R.layout.simple_list_item_1, name);
		atvPlaces.setAdapter(adapter);
	
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.go) {
			String f = (Integer.parseInt(from.getMonth() + "") + 1) + "";
			String newf = new String();
			if (f.length() == 1)
				newf = "0" + f;
			else
				newf = f;

			String t = (Integer.parseInt(to.getMonth() + "") + 1) + "";
			String newt = new String();
			if (t.length() == 1)
				newt = "0" + t;
			else
				newt = t;

			String fm = from.getDayOfMonth() + "";
			;
			String newfm = new String();
			if (fm.length() == 1)
				newfm = "0" + fm;
			else
				newfm = fm;

			String tm = to.getDayOfMonth() + "";
			String newtm = new String();
			if (tm.length() == 1)
				newtm = "0" + tm;
			else
				newtm = tm;

			String start = newfm + "/" + newf + "/" + from.getYear();
			String end = newtm + "/" + newt + "/" + to.getYear();
			String place = atvPlaces.getText().toString().split(",")[0];
			Intent stay = new Intent(MainActivity.this, StayActivity.class);
			stay.putExtra("start", start);
			stay.putExtra("end", end);
			stay.putExtra("place", place);
			stay.putExtra("id", id[0]);
			startActivity(stay);
			overridePendingTransition(R.anim.right_in, R.anim.left_out);

		}
	}

}
