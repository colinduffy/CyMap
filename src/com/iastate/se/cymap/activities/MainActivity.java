package com.iastate.se.cymap.activities;

import com.iastate.se.cymap.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Note: Do not set the content view of each activity from this page. The
 * content view must be set at the activities themselves.
 * 
 * @author Colin Duffy
 * 
 */
public class MainActivity extends Activity {
	private Button btnMap;
	private Button btnViewSched;
	private Button btnAddApt;

	private Context thisCtx;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		thisCtx = this;
		setContentView(R.layout.activity_main);
		loadObjects();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	/**
	 * Loads all the buttons for the multiple views (Possibly deprecating this
	 * view, and initialize on Schedule view)
	 */
	public void loadObjects() {
		btnMap = (Button) findViewById(R.id.btnMap);
		btnViewSched = (Button) findViewById(R.id.btnViewSched);
		btnAddApt = (Button) findViewById(R.id.btnAddApt);

		/**
		 * Sets the on click listener for the Map class
		 */
		btnMap.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				Intent i = new Intent(thisCtx, MapActivity.class);
				startActivity(i);
			}
		});

		/**
		 * Sets the on click listener for the Schedule class
		 */
		btnViewSched.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {

				final String packageName = "com.iastate.se.cymap.schedule";

				try {
					Class<?> c = Class.forName(packageName + "."
							+ "SchedListActivity");
					Intent i = new Intent(thisCtx, c);
					startActivity(i);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		});

		/**
		 * Sets the on click listener for the Add Appointment class
		 */
		btnAddApt.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				Intent i = new Intent(thisCtx, AddCourseActivity.class);
				startActivity(i);
			}
		});
	}
}
