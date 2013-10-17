package com.bums.small;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.webkit.WebView;
import android.widget.Toast;

import com.bums.library.DatabaseHandler;
import com.bums.library.UserFunctions;

public class MainActivity extends FragmentActivity {
	private FragmentTabHost mTabHost;
	private HashMap<String,String> user;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		user = new HashMap<String, String>();
		user = db.getUserDetails();
		
		System.out.println(db.getRowCount());

		setContentView(R.layout.bottom_tabs);

		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

		Bundle b = new Bundle();
		b.putString("key", "Fashion");
		mTabHost.addTab(mTabHost.newTabSpec("fashion").setIndicator("fashion"),
				FashionFragment.class, b);
		
		b = new Bundle();
		b.putString("key", "Events");
		mTabHost.addTab(mTabHost.newTabSpec("events")
				.setIndicator("Events"), EventsFragment.class, b);

		b = new Bundle();
		b.putString("key", "incmedia");
		mTabHost.addTab(mTabHost.newTabSpec("incmedia").setIndicator("incmedia"),
				INCMediaFragment.class, b);

		b = new Bundle();
		b.putString("key", "Account");
		mTabHost.addTab(mTabHost.newTabSpec("account").setIndicator("account"),
				AccountFragment.class, b);
	}

	public HashMap<String,String> getUser() {
		return user;
	}

	@Override
	public void onBackPressed() {
		Fragment webview = getSupportFragmentManager().findFragmentByTag("incmedia");
		if (webview instanceof INCMediaFragment) {
			WebView w = ((INCMediaFragment)webview).getWebView();
			if (w.canGoBack())
				w.goBack();
			else
				super.onBackPressed();
		}
		super.onBackPressed();
	}
}
