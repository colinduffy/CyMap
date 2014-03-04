package com.iastate.se.cymap.activities;

import com.iastate.se.cymap.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SplashScreen extends Activity {
	protected boolean active = true;
	protected int splashTime = 5000; // time to display the splash screen in ms
	private Button login;
	Context thisCtx;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		thisCtx = this;
		login = (Button) findViewById(R.id.btnLogin);
		
		login.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(thisCtx, MainActivity.class);
				startActivity(i);
			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			active = false;
		}
		return true;
	}
}
