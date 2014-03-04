package com.iastate.se.cymap.activities;

import java.util.LinkedList;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.iastate.se.cymap.R;

/**
 * This class is used for controlling the map view
 * 
 * @author Colin Duffy
 * 
 */
public class MapActivity extends Activity {
	// Physical ojects
	private WebView webview;
	private TextView textClass;
	private Button btnPrev;
	private Button btnNext;
	private Spinner spinBuilding;

	// Helper variables
	private int spinCount; // Number of times the building selection has been
							// changed
	private boolean spinnerOpen; // True if user opens the spinner

	// private LinkedList<Class_> schedule;
	private int current; // 0 - numClasses, for the current selected class
	private int numClasses = 4; // Number of classes on said day, preset for
								// presentation

	// Pointers for string arrays in strings.xml
	private String[] points; // Contains points of buildings

	private LinkedList<String> classes; // Contains names of classes on said day
	private LinkedList<Integer> buildings; // Contains numbers referring to the
											// order of the building

	// Constants
	private final String CURRENT_COLOR = "red";
	private final String OTHER_COLOR = "yellow";

	// Used in item listeners
	private Context thisCtx;

	private String curBuilding;

	@Override
	/**
	 * 
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_layout);

		thisCtx = this;
		loadObjects();

		// Get objects passed from other activities
		Bundle b = this.getIntent().getExtras();
		curBuilding = "";
		if (b != null) {
			curBuilding = b.getString("BUILDING");
		}

		spinCount = 0;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	/**
	 * Initializes and loads all objects used in this activities. Starts by
	 * initializing lists and variables Then points arrays to the corresponding
	 * array in strings.xml Adds listeners to the buttons and spinner Finally
	 * loads the webview with the html and calls the initial javascript
	 */
	private void loadObjects() {
		classes = new LinkedList<String>();
		buildings = new LinkedList<Integer>();

		createSchedule();

		// Point to xml objects
		webview = (WebView) findViewById(R.id.webview);
		textClass = (TextView) findViewById(R.id.textClass);
		btnPrev = (Button) findViewById(R.id.btnPrev);
		btnNext = (Button) findViewById(R.id.btnNext);
		spinBuilding = (Spinner) findViewById(R.id.spinner1);

		spinnerOpen = false;

		points = getResources().getStringArray(R.array.buildings_pos_array);

		// Add listeners

		// When you click the class name you will move to the schedule
		textClass.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
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

		// Will switch to the previous class
		btnPrev.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				current--;
				// Were we on the first class
				if (current < 0) {
					// Go to the last class
					current = numClasses - 1;
				}

				String n = classes.get(current);
				int bNum = buildings.get(current);
				textClass.setText(n);
				drawCurrent();
				spinnerOpen = false;
				spinBuilding.setSelection(bNum);
			}
		});

		// Will switch to the next class
		btnNext.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				current++;
				// Were we on the last class
				if (current == numClasses) {

					current = 0;
				}

				// Class_ t = schedule.get(1);
				String n = classes.get(current);
				int bNum = buildings.get(current);
				textClass.setText(n);
				drawCurrent();
				spinnerOpen = false;
				spinBuilding.setSelection(bNum);
			}
		});


		spinnerOpen = false;

		// Draws point at newly selected building
		spinBuilding.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				if (spinCount > 0 && spinnerOpen) {
					webview.loadUrl("file:///android_asset/map.html");

					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					textClass.setText("");
					int p = spinBuilding.getSelectedItemPosition();
					drawPoint(p, CURRENT_COLOR);
				}
				String s = "" + spinCount;
				// Toast.makeText(getApplicationContext(), s,
				// Toast.LENGTH_SHORT).show();
				spinCount++;
				spinnerOpen = true;

			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});

		// Set up webview
		WebSettings settings = webview.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setBuiltInZoomControls(true);

		current = 0;

		// Set current building
		spinBuilding.setSelection(buildings.get(current));
		webview.loadUrl("file:///android_asset/map.html");
		
		// Draw current points
		drawCurrent();
	}

	/**
	 * For now the schedule is preset
	 * 
	 * Simply loads lists with the schedule info
	 */
	private void createSchedule() {
		classes.add("Com S 319");
		classes.add("Math 307");
		classes.add("Com S 229");
		classes.add("Cpr E 288");

		buildings.add(5); // Coover
		buildings.add(1); // Carver
		buildings.add(2); // Gillman
		buildings.add(6); // Hoover

	}

	/**
	 * Draws a point on the map
	 * 
	 * @param pos
	 *            - number to point to the position
	 * @param color
	 *            - color to draw the point
	 */
	private void drawPoint(int pos, String color) {
		String point = points[pos];
		String[] p = point.split(",");

		int p1 = Integer.parseInt(p[0]);
		int p2 = Integer.parseInt(p[1]);
		webview.loadUrl("javascript:drawPoint('" + p1 + "', '" + p2 + "', '"
				+ color + "')");
	}

	/**
	 * Updates the map
	 * 
	 * Calls drawPoint for each of the schedule items
	 */
	private void drawCurrent() {
		webview.loadUrl("file:///android_asset/map.html");

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String color = OTHER_COLOR;
		for (int i = 0; i < buildings.size(); ++i) {
			// Is this the current item?
			if (i == current) {
				color = CURRENT_COLOR;
			} else
				color = OTHER_COLOR;

			drawPoint(buildings.get(i), color);
		}
	}

	/*
	 * private void drawOne(){
	 * webview.loadUrl("file:///android_asset/map.html");
	 * 
	 * try { Thread.sleep(300); } catch (InterruptedException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); }
	 * 
	 * int p1 = 100; int p2 = 100;
	 * 
	 * webview.loadUrl("javascript:drawPoint('" + p1 + "', '" + p2 + "', '" +
	 * CURRENT_COLOR + "')"); }
	 * 
	 * private void drawUpdate(int cur) {
	 * webview.loadUrl("file:///android_asset/map.html");
	 * 
	 * try { Thread.sleep(100); } catch (InterruptedException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); }
	 * 
	 * for (int i = 0; i < 4; ++i) { int p1 = 100; int p2 = 100 + 10 * i; if (i
	 * == cur) { webview.loadUrl("javascript:drawPoint('" + p1 + "', '" + p2 +
	 * "', '" + CURRENT_COLOR + "')"); //
	 * Toast.makeText(getApplicationContext(), "Current", //
	 * Toast.LENGTH_SHORT).show(); } else {
	 * webview.loadUrl("javascript:drawPoint('" + p1 + "', '" + p2 + "', '" +
	 * OTHER_COLOR + "')"); // Toast.makeText(getApplicationContext(), "Other",
	 * // Toast.LENGTH_SHORT).show(); } } }
	 */
}
