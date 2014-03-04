package com.iastate.se.cymap.schedule;

/**
 * CyMap - SE 319
 * @Author John Shelley
 * Copyright (c) 2012
 * CyMap Schedule implementation
 * -Uses slide out tab to choose the day.
 * -Default view depends on the day of the week
 *  
 */
import java.io.File;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.iastate.se.cymap.R;
import com.iastate.se.cymap.activities.Schedule;
import com.iastate.se.cymap.schedule.MyHorizontalScrollView.SizeCallback;

/**
 * Main activity to determine what the users schedule is for the day.
 */
public class SchedListActivity extends Activity {
	static MyHorizontalScrollView scrollView;
	static View menu;
	static ListView dayListView;
	static ListView classListView;
	static View app;
	ImageView buttonSlide;
	static boolean menuVisible = false;
	Handler handler = new Handler();
	int buttonWidth;
	String selectedText = "";
	public static Schedule schedule = new Schedule();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Typeface tf = Typeface.createFromAsset(getAssets(),
				"fonts/Roboto-Thin.ttf");
		parseScheduleFile();
		loadObjects(tf);
	}

	/**
	 * 
	 */
	private void parseScheduleFile() {
		String FILENAME = "data_file";
		System.out.println(getApplicationContext().getFilesDir());
		File file = new File(getApplicationContext().getFilesDir(), FILENAME);
		if (schedule.getCourselist().isEmpty()) {
			schedule.parseCourse(file);
		}
	}

	/**
	 * Loads all the views and initializes certain settings.
	 * 
	 * @param tf
	 *            Roboto type face
	 */
	private void loadObjects(Typeface tf) {
		LayoutInflater inflater = LayoutInflater.from(this);
		scrollView = (MyHorizontalScrollView) inflater.inflate(
				R.layout.schedule_menu_scroll, null);
		setContentView(scrollView);

		/* The pop out view */
		menu = inflater.inflate(R.layout.schedule_menu, null);
		/* Cyclone Cardinal Shade */
		menu.setBackgroundColor(Color.rgb(130, 36, 51));

		/* The actual schedule shown */
		app = inflater.inflate(R.layout.schedule_menu_app, null);
		/* Cyclone Gold Highlight */
		app.setBackgroundColor(Color.rgb(250, 218, 99));

		/* Set menu title font detail */
		TextView menuTitle = (TextView) app.findViewById(R.id.mainTitle);
		createFontDetail(tf, menuTitle);

		/* Creates the header for the app */
		ViewGroup tabBar = (ViewGroup) app.findViewById(R.id.tabBar);
		/* Cyclone Cardinal Shade */
		tabBar.setBackgroundColor(Color.rgb(130, 36, 51));

		/* Creates the menu list */
		dayListView = (ListView) menu.findViewById(R.id.list);
		ViewUtil.initListView(this, dayListView, "Item ", 5,
				android.R.layout.simple_list_item_1);
		/* Helps sets the fonts and size of menu header */
		TextView headerType = (TextView) menu.findViewById(R.id.menu_title);
		createFontDetail(tf, headerType);

		/* Creates the students schedule */
		classListView = (ListView) app.findViewById(R.id.classList);
		ViewUtil.initClassView(this, classListView, "Menu ", 5,
				android.R.layout.simple_list_item_1, Day.EMPTY);
		registerForContextMenu(classListView);
		/* Allows the image to slide the menu in and out */
		buttonSlide = (ImageView) tabBar.findViewById(R.id.buttonSlide);
		buttonSlide.setOnClickListener(new ClickListenerForScrolling(
				scrollView, menu));

		final View[] children = new View[] { menu, app };

		/* Scroll to app (view[i]) when layout is finished */
		int scrollToViewIndex = 1;
		scrollView.initView(children, scrollToViewIndex, new SizeCallbackMenu(
				buttonSlide));
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View view,
			ContextMenuInfo menuInfo) {
		if (view.getId() == R.id.classList) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			System.out.println(info.position);
			selectedText = ((TextView) info.targetView).getText().toString();
			int index = selectedText.indexOf("\n");
			if (index > 0) {
				super.onCreateContextMenu(menu, view, menuInfo);
				selectedText = selectedText.substring(0, index);
				menu.setHeaderTitle("Edit:");
				menu.add("Delete");
			}
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getTitle() == "Delete") {
			ViewUtil.delete(selectedText);
		}
		return true;
	}

	/**
	 * Helps create the side menu
	 * 
	 * @param tf
	 *            Typeface
	 * @param view
	 *            View to be changed
	 */
	static private void createFontDetail(Typeface tf, TextView view) {
		view.setTypeface(tf);
		view.setTextSize(24);
		view.setTextColor(Color.WHITE);
	}

	/**
	 * Helper for scrolling
	 * 
	 * @author johnshelley
	 * 
	 */
	static class ClickListenerForScrolling implements OnClickListener {

		HorizontalScrollView scrollView;
		View menu;

		public ClickListenerForScrolling(HorizontalScrollView scrollView,
				View menu) {
			super();
			this.scrollView = scrollView;
			this.menu = menu;
		}

		public void onClick(View v) {
			Context context = menu.getContext();
			String msg = "Slide " + new Date();
			Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
			System.out.println(msg);

			int menuWidth = menu.getMeasuredWidth();

			/* Ensure the menu is visible */
			menu.setVisibility(View.VISIBLE);

			if (!menuVisible) {
				/* Scroll to 0 to reveal menu */
				int left = 0;
				scrollView.smoothScrollTo(left, 0);
			} else {
				/* Scroll to menuWidth to disappear */
				scrollView.smoothScrollTo(menuWidth, 0);
			}
			menuVisible = !menuVisible;
		}
	}

	static class SizeCallbackMenu implements SizeCallback {
		View buttonSlide;

		public SizeCallbackMenu(View buttonSlide) {
			super();
			this.buttonSlide = buttonSlide;
		}

		public void onGlobalLayout() {
			System.out
					.println("ButtonWidth: " + buttonSlide.getMeasuredWidth());
		}

		public void getViewSize(int idx, int w, int h, int[] dims) {
			dims[0] = w;
			dims[1] = h;
			final int menuIndex = 0;
			if (idx == menuIndex) {
				dims[0] = w - buttonSlide.getMeasuredWidth();
			}
		}

	}
}
