package com.iastate.se.cymap.schedule;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MyTypeFaceAdapter extends ArrayAdapter<String> {
	private ArrayList<String> days;
	private final Context context;

	public MyTypeFaceAdapter(Context context, int layout,
			ArrayList<String> objects) {
		super(context, layout, objects);
		this.context = context;
		this.days = objects;

	}

	@Override
	public int getCount() {
		return days.size();
	}

	@Override
	public String getItem(int position) {
		return days.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		String day = getItem(position);

		TextView tv = new TextView(context);
		tv.setText(day.toString());

		Typeface tf = Typeface.createFromAsset(context.getAssets(),
				"fonts/Roboto-Thin.ttf");
		tv.setTypeface(tf);
		tv.setTextSize(56);
		tv.setTextColor(Color.WHITE);
		return tv;
	}

}
