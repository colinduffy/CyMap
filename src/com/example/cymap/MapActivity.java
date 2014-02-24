package com.example.cymap;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class MapActivity extends Activity {
	private WebView webview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		webview = (WebView) findViewById(R.id.webview);
		webview.loadUrl("file:///android_asset/map.html");
		
		WebSettings settings = webview.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setBuiltInZoomControls(true);
		
		//drawPoint(445, 417, "yellow"); //Atanasoff
		//drawPoint(510, 580, "red");   //Carver
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}
	
	private void drawPoint(int x, int y, String color) {
		webview.loadUrl("javascript:drawPoint('" + x + "', '" + y + "', '"
				+ color + "')");
	}

}
