package com.iastate.se.cymap.activities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.iastate.se.cymap.R;
import com.iastate.se.cymap.schedule.SchedListActivity;

/**
 * CyMap - SE 319 AddCourseActivity.java is an activity that add a course to the
 * schedule
 * 
 * @author xiangle wang
 * @date 10/06/2012 Copyright (c) 2012
 */
public class AddCourseActivity extends Activity {
	// Private Variables
	private EditText textClassName;
	private EditText textRoomNum;
	private Spinner sBuilding;
	// Check boxes
	private CheckBox boxMonday;
	private CheckBox boxTuesday;
	private CheckBox boxWednesday;
	private CheckBox boxThursday;
	private CheckBox boxFriday;
	// Buttons
	private Button sPicktime;
	private Button ePicktime;
	private Button sPickDate;
	private Button ePickDate;
	private Button saveBtn;
	static LinearLayout addCourseView;
	// file
	private final String FILENAME = "data_file";
	// Dialog IDs for calling the dialogs to show
	static final int SDATE_DIALOG_ID = 0;
	static final int EDATE_DIALOG_ID = 1;
	static final int STIME_DIALOG_ID = 2;
	static final int ETIME_DIALOG_ID = 3;
	// Schedule and course
	private Course course;
	private Context thisCtx;

	private DatePickerDialog.OnDateSetListener sDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			course.setsYear(year);
			course.setsMonth(monthOfYear + 1);
			course.setsDay(dayOfMonth);
			if (course.compareDate() == -1) {
				course.seteYear(course.getsYear());
				course.seteMonth(course.getsMonth());
				course.seteDay(course.getsDay());
			}
			updateDate();
		}
	};

	private DatePickerDialog.OnDateSetListener eDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			course.seteYear(year);
			course.seteMonth(monthOfYear + 1);
			course.seteDay(dayOfMonth);
			updateDate();
		}
	};

	private TimePickerDialog.OnTimeSetListener sTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker arg0, int arg1, int arg2) {
			if (arg1 > 11) {
				course.setsHour(arg1 - 12);
				course.setsMin(arg2);
				course.setsAMPM("PM");
			} else {
				course.setsAMPM("AM");
				course.setsHour(arg1);
				course.setsMin(arg2);
			}
			if (course.compareTime() == -1) {
				course.seteHour(course.getsHour());
				course.seteMin(course.getsMin());
				course.seteAMPM(course.getsAMPM());
			}
			updateTime();
		}
	};

	private TimePickerDialog.OnTimeSetListener eTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker arg0, int arg1, int arg2) {
			if (arg1 > 11) {
				course.seteHour(arg1 - 12);
				course.seteMin(arg2);
				course.seteAMPM("PM");
			} else {
				course.seteAMPM("AM");
				course.seteHour(arg1);
				course.seteMin(arg2);
			}
			updateTime();
		}
	};

	/**
	 * onCreate
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflator = LayoutInflater.from(this);
		thisCtx = this;
		/* inflates the view */
		addCourseView = (LinearLayout) inflator.inflate(R.layout.add_course,
				null);
		/* Sets the views details */
		setViewDetails();
		setContentView(addCourseView);
		loadObjects();
		updateTime();
		updateDate();
		addListenerOnBtton();
	}

	/**
	 * loads all the objects on the add course page
	 */
	private void loadObjects() {
		textClassName = (EditText) findViewById(R.id.textClassName);
		textRoomNum = (EditText) findViewById(R.id.textRoomNo);
		boxMonday = (CheckBox) findViewById(R.id.cBoxMon);
		boxTuesday = (CheckBox) findViewById(R.id.cBoxTue);
		boxWednesday = (CheckBox) findViewById(R.id.cBoxWed);
		boxThursday = (CheckBox) findViewById(R.id.cBoxTh);
		boxFriday = (CheckBox) findViewById(R.id.cBoxFri);
		sBuilding = (Spinner) findViewById(R.id.spinBulding);
		sPicktime = (Button) findViewById(R.id.starttimePicker);
		ePicktime = (Button) findViewById(R.id.endtimePicker);
		sPickDate = (Button) findViewById(R.id.startdatePicker);
		ePickDate = (Button) findViewById(R.id.enddatePicker);
		saveBtn = (Button) findViewById(R.id.saveBtn);
		course = new Course();
	}

	private void setViewDetails() {
		addCourseView.setBackgroundColor(Color.rgb(0, 0, 0));
	}

	/**
	 * formats a one digit number
	 * 
	 * @param num
	 * @return
	 */
	public String format(int num) {
		if (num < 10) {
			return "0" + num;
		}
		return "" + num;
	}

	/**
	 * updates time on the TimePicker buttons
	 */
	private void updateTime() {
		sPicktime.setText(course.getsHour() + ":" + format(course.getsMin())
				+ '\t' + course.getsAMPM());
		ePicktime.setText(course.geteHour() + ":" + format(course.geteMin())
				+ '\t' + course.geteAMPM());
		if(course.getsHour() == 0){
			sPicktime.setText("12:" + format(course.getsMin())
					+ '\t' + course.getsAMPM());
		}
		
		if(course.geteHour() == 0){
			ePicktime.setText("12:" + format(course.geteMin())
					+ '\t' + course.geteAMPM());
		}
	}

	/**
	 * updates the text on the DatePicker buttons
	 */
	public void updateDate() {
		sPickDate.setText(course.getsMonth() + "/" + format(course.getsDay())
				+ "/" + course.getsYear());
		ePickDate.setText(course.geteMonth() + "/" + format(course.geteDay())
				+ "/" + course.geteYear());
	}

	/**
	 * create a new DatePicker dialog
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case SDATE_DIALOG_ID:
			return new DatePickerDialog(this, sDateSetListener,
					course.getsYear(), course.getsMonth(), course.getsDay());
		case EDATE_DIALOG_ID:
			return new DatePickerDialog(this, eDateSetListener,
					course.geteYear(), course.geteMonth(), course.geteDay());
		case STIME_DIALOG_ID:
			return new TimePickerDialog(this, sTimeSetListener,
					course.getsHour(), course.getsMin(), false);
		case ETIME_DIALOG_ID:
			return new TimePickerDialog(this, eTimeSetListener,
					course.getsHour(), course.getsMin(), false);
		}
		return null;
	}

	/**
	 * adds click listener on all the buttons
	 */
	public void addListenerOnBtton() {
		sPickDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(SDATE_DIALOG_ID);
			}
		});
		ePickDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(EDATE_DIALOG_ID);
			}
		});
		sPicktime.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				showDialog(STIME_DIALOG_ID);
			}
		});
		ePicktime.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				showDialog(ETIME_DIALOG_ID);
			}
		});

		saveBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (!isValid()) {
					return;
				}
				course.setCourseName(textClassName.getText().toString());
				course.setRoomNum(textRoomNum.getText().toString());
				course.setBuildingName(sBuilding.getSelectedItem().toString());
				String saveString = "";
				if (boxMonday.isChecked()) {
					saveString += "Monday";
				}
				if (boxTuesday.isChecked()) {
					saveString += "Tuesday";
				}
				if (boxWednesday.isChecked()) {
					saveString += "Wednesday";
				}
				if (boxThursday.isChecked()) {
					saveString += "Thursday";
				}
				if (boxFriday.isChecked()) {
					saveString += "Friday";
				}
				course.setDay(saveString);

				File file = new File(getApplicationContext().getFilesDir(),
						FILENAME);
				SchedListActivity.schedule.getCourselist().add(course);
				System.out.println("size: "
						+ SchedListActivity.schedule.getCourselist().size());
				for (int i = 0; i < SchedListActivity.schedule.getCourselist()
						.size(); i++) {
					PrintWriter out = null;
					try {
						out = new PrintWriter(file);
						out.println(SchedListActivity.schedule.getCourselist()
								.get(i).toString());
						System.out.println("writing: "
								+ SchedListActivity.schedule.getCourselist()
										.get(i).toString());
						out.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
				clearObjects();
				finish();
			}
		});
	}

	/**
	 * clears all objects on the page
	 */
	public void clearObjects() {
		System.out.println("object cleared!");
		textClassName.setText("");
		textRoomNum.setText("");
		boxMonday.setChecked(false);
		boxTuesday.setChecked(false);
		boxWednesday.setChecked(false);
		boxThursday.setChecked(false);
		boxFriday.setChecked(false);
	}

	/**
	 * checks if all fields are valid
	 */
	public boolean isValid() {

		if (textClassName.getText().length() < 1) {
			alert("Please enter a name!");
			return false;
		} else if (textRoomNum.getText().length() < 1) {
			alert("Please enter a class room!");
			return false;
		} else if (!boxMonday.isChecked() && !boxTuesday.isChecked()
				&& !boxThursday.isChecked() && !boxWednesday.isChecked()
				&& !boxFriday.isChecked()) {
			alert("Please check at least one day!");
			return false;
		} else if (course.compareDate() == -1) {
			alert("Start date must be before end date!");
			return false;
		} else if (course.compareTime() == -1) {
			alert("Start Time must be before end date!");
			return false;
		}

		return true;
	}

	/**
	 * displays an alert on the screen
	 * 
	 * @param str
	 *            the string to be toasted
	 */
	public void alert(String str) {
		Toast t = Toast.makeText(getApplicationContext(), str,
				Toast.LENGTH_SHORT);
		t.setGravity(Gravity.CENTER, 0, 0);
		t.show();
	}
}
