package com.royce.stayzilla.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.royce.stayzilla.R;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.GestureDetector;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.TextView;

public class ReviewActivity extends ActionBarActivity {

	TextView name, text, count;
	String rev;
	String[] n, t;
	int l, curr;
	GestureDetector gestureDetector;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getSupportActionBar().setTitle("REVIEWS");
		gestureDetector = new GestureDetector(simpleOnGestureListener);
		setContentView(R.layout.activity_review);
		rev = new String(getIntent().getStringExtra("rev"));
		name = (TextView) findViewById(R.id.rev_name);
		count = (TextView) findViewById(R.id.rev_number);
		text = (TextView) findViewById(R.id.rev_text);
		text.setMovementMethod(new ScrollingMovementMethod());
		if(!rev.contentEquals(""))
			decodeString(rev);
		else
			norev();
	}

	private void decodeString(String rev2) {
		// TODO Auto-generated method stub
		try {
			JSONArray rev = new JSONArray(rev2);
			l = rev.length();
			if(l==0)
			{
				norev();
				return;
			}
			n = new String[l];
			t = new String[l];
			for (int i = 0; i < l; i++) {
				JSONObject _r = rev.getJSONObject(i);
				n[i] = new String(_r.getString("author_name"));
				t[i] = new String(_r.getString("text"));

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setUpView(0);
	}

	private void norev() {
		// TODO Auto-generated method stub
		name.setVisibility(View.GONE);
		count.setVisibility(View.GONE);
		text.setVisibility(View.GONE);
		findViewById(R.id.rev_0).setVisibility(View.VISIBLE);
	}

	private void setUpView(int k) {
		// TODO Auto-generated method stub
		int a = k+1;
		count.setText(" < " + a + "/" + l + " >");
		name.setText(n[k].toUpperCase());
		text.setText(t[k]);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return gestureDetector.onTouchEvent(event);
	}

	SimpleOnGestureListener simpleOnGestureListener = new SimpleOnGestureListener() {

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			int swipe = 0;
			float sensitvity = 50;

			// TODO Auto-generated method stub
			if ((e1.getX() - e2.getX()) > sensitvity) {
				swipe = 0;
			} else if ((e2.getX() - e1.getX()) > sensitvity) {
				swipe = 1;
			}
			if ((e1.getY() - e2.getY()) > sensitvity) {
				swipe = 2;
			} else if ((e2.getY() - e1.getY()) > sensitvity) {
				swipe = 3;
			}

			setSwipe(swipe);

			return super.onFling(e1, e2, velocityX, velocityY);
		}
	};

	protected void setSwipe(int swipe) {
		// TODO Auto-generated method stub
		if (curr != l-1 && swipe == 0)
			setUpView(++curr);
		if (curr != 0 && swipe == 1)
			setUpView(--curr);

	}

}
