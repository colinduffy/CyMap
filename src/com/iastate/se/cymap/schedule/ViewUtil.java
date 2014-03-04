package com.iastate.se.cymap.schedule;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.iastate.se.cymap.R;
import com.iastate.se.cymap.activities.AddCourseActivity;
import com.iastate.se.cymap.activities.MapActivity;

/**
 * Utility method for the Views in the application
 * 
 */
public class ViewUtil {
	Context context;

	public static void setViewWidths(View view, View[] views) {
		int width = view.getWidth();
		int height = view.getHeight();
		for (int i = 0; i < views.length; i++) {
			View v = views[i];
			v.layout((i + 1) * width, 0, (i + 2) * width, height);
			printView("view[" + i + "]", v);
		}
	}

	public static void printView(String msg, View v) {
		System.out.println(msg + "=" + v);
		if (v == null) {
			System.out.println("View is null!");
			return;
		}
		System.out.print("[" + v.getLeft());
		System.out.print("," + v.getTop());
		System.out.print(", width: " + v.getWidth());
		System.out.println(", height: " + v.getHeight() + "]");
		System.out.println("MeasuredWidth: " + v.getMeasuredWidth());
		System.out.print("MeasuredHeight: " + v.getMeasuredHeight());
		System.out.print("scroll [" + v.getScrollX() + "," + v.getScrollY()
				+ "]");
	}

	public static void initListView(Context context, ListView listView,
			String prefix, int numItems, int layout) {

		/*
		 * By using SetAdapter method in ListView we can add a String Array to a
		 * List
		 */
		ArrayList<String> stringArray = new ArrayList<String>();
		stringArray.add("Monday");
		stringArray.add("Tuesday");
		stringArray.add("Wednesday");
		stringArray.add("Thursday");
		stringArray.add("Friday");
		stringArray.add("Add Class +");

		listView.setAdapter(new MyTypeFaceAdapter(context, layout, stringArray));
		listView.setBackgroundColor(Color.rgb(130, 36, 51));
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id) {
				Context context = view.getContext();
				String msg = "item[" + pos + "]: "
						+ parent.getItemAtPosition(pos);
				Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
				System.out.println(msg);

				String day = parent.getItemAtPosition(pos).toString()
						.toUpperCase();
				slideMenuOnClick(view);
				setMenuTitleOnClick(view, day);
				updateScheduleView(context, view, day);
			}

			/**
			 * Slides the menu back on the multiple day views
			 * 
			 * @param view
			 */
			private void slideMenuOnClick(View view) {
				int menuWidth = view.getMeasuredWidth();

				/* Ensure the menu is visible */
				view.setVisibility(View.VISIBLE);

				if (SchedListActivity.menuVisible) {
					/* Scroll to menuWidth to disappear */
					SchedListActivity.scrollView.smoothScrollTo(menuWidth, 0);
				} else {
					/* Scroll to 0 to reveal menu */
					int left = 0;
					SchedListActivity.scrollView.smoothScrollTo(left, 0);
				}
				SchedListActivity.menuVisible = !SchedListActivity.menuVisible;
			}

			/**
			 * Sets the menu title on the main schedule page to match whats been
			 * clicked.
			 * 
			 * @param view
			 */
			private void setMenuTitleOnClick(View view, String day) {
				TextView title = (TextView) SchedListActivity.app
						.findViewById(R.id.mainTitle);
				if (day.equals(Day.MONDAY.name())) {
					title.setText("Monday");
				} else if (day.equals(Day.TUESDAY.name())) {
					title.setText("Tuesday");
				} else if (day.equals(Day.WEDNESDAY.name())) {
					title.setText("Wednesday");
				} else if (day.equals(Day.THURSDAY.name())) {
					title.setText("Thursday");
				} else if (day.equals(Day.FRIDAY.name())) {
					title.setText("Friday");
				} else if (day.equals("ADD CLASS +")) {
					title.setText("Today");
				}
			}

			/**
			 * Helps update the list view on the main schedule page.
			 * 
			 * @param view
			 */
			private void updateScheduleView(Context context, View view,
					String day) {
				if (day.equals(Day.MONDAY.name())) {
					ViewUtil.initClassView(context,
							SchedListActivity.classListView, "Menu", 5,
							android.R.layout.simple_list_item_1, Day.MONDAY);
				} else if (day.equals(Day.TUESDAY.name())) {
					ViewUtil.initClassView(context,
							SchedListActivity.classListView, "Menu", 5,
							android.R.layout.simple_list_item_1, Day.TUESDAY);
				} else if (day.equals(Day.WEDNESDAY.name())) {
					ViewUtil.initClassView(context,
							SchedListActivity.classListView, "Menu", 5,
							android.R.layout.simple_list_item_1, Day.WEDNESDAY);
				} else if (day.equals(Day.THURSDAY.name())) {
					ViewUtil.initClassView(context,
							SchedListActivity.classListView, "Menu", 5,
							android.R.layout.simple_list_item_1, Day.THURSDAY);
				} else if (day.equals(Day.FRIDAY.name())) {
					ViewUtil.initClassView(context,
							SchedListActivity.classListView, "Menu", 5,
							android.R.layout.simple_list_item_1, Day.FRIDAY);
				} else if (day.equals("ADD CLASS +")) {
					Intent i = new Intent(context, AddCourseActivity.class);
					context.startActivity(i);
				} else {

				}
			}

		});
	}

	public static void initClassView(Context context, ListView listView,
			String prefix, int numItems, int layout, Day day) {
		/*
		 * By using SetAdapter method in ListView we can add a String Array to a
		 * List
		 */
		ArrayList<String> stringArray = null;

		Calendar calendar = Calendar.getInstance();
		int calDay = 0;
		if (day == Day.EMPTY) {
			calDay = calendar.get(Calendar.DAY_OF_WEEK);
			stringArray = determineToday(context, stringArray, calDay);
		} else {
			stringArray = determineToday(context, stringArray, day.getById(day));
		}

		listView.setAdapter(new MyTypeFaceAdapter(context, layout, stringArray));
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long is) {
				Context context = view.getContext();
				String building = parent.getItemAtPosition(pos).toString();
				viewMapFromClass(context, view, building);
			}

			/**
			 * Helps view the map from clicking on a class.
			 * 
			 * @param view
			 */
			private void viewMapFromClass(Context context, View view,
					String building) {
				boolean foundBuilding = false;
				final String[] buildingList = context.getResources()
						.getStringArray(R.array.buildings_array);
				for (int i = 0; i < buildingList.length; i++) {
					if (building.contains(buildingList[i])) {
						showMapView(context, buildingList[i].toString());
						foundBuilding = true;
					}
				}
				if (!foundBuilding) {
					String msg = "Currently unsupported building location.";
					Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
				}
			}

			/**
			 * @param context
			 * @param classRoom
			 */
			private void showMapView(Context context, String classRoom) {
				Intent i = new Intent();
				Bundle b = new Bundle();
				b.putString("BUILDING", classRoom);
				i.putExtras(b);
				i.setClass(context, MapActivity.class);
				context.startActivity(i);
			}

		});
	}

	/**
	 * @param stringArray
	 * @param day
	 * @return array of today's classes
	 */
	private static ArrayList<String> determineToday(Context context,
			ArrayList<String> stringArray, int day) {

		switch (day) {
		case 1:
			stringArray = new ArrayList<String>();
			stringArray.add("Today is Sunday!");
			break;
		case 2:
			stringArray = createScheduleFromFile(stringArray, "Monday");
			break;
		case 3:
			stringArray = createScheduleFromFile(stringArray, "Tuesday");
			break;
		case 4:
			stringArray = createScheduleFromFile(stringArray, "Wednesday");
			break;
		case 5:
			stringArray = createScheduleFromFile(stringArray, "Thursday");
			break;
		case 6:
			stringArray = createScheduleFromFile(stringArray, "Friday");
			break;
		case 7:
			stringArray = new ArrayList<String>();
			stringArray.add("Today is Saturday!");
			break;
		default:
			break;
		}
		return stringArray;
	}

	/**
	 * @param stringArray
	 * @param schedule
	 * @return
	 */
	private static ArrayList<String> createScheduleFromFile(
			ArrayList<String> stringArray, String day) {
		stringArray = new ArrayList<String>();
		for (int i = 0; i < SchedListActivity.schedule.getCourselist().size(); i++) {
			if (SchedListActivity.schedule.getCourselist().get(i).getDay()
					.contains(day)) {
				stringArray.add(SchedListActivity.schedule.getCourselist()
						.get(i).getCourseName()
						+ "\n"
						+ SchedListActivity.schedule.getCourselist().get(i)
								.getBuildingName()
						+ " "
						+ SchedListActivity.schedule.getCourselist().get(i)
								.getRoomNum());
			}
		}
		return stringArray;
	}

	public static void delete(String selectedToDelete) {
		SchedListActivity.schedule.removeCourse(selectedToDelete);

	}
}
