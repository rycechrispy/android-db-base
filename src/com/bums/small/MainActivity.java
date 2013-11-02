package com.bums.small;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.webkit.WebView;

import com.bums.library.DatabaseHandler;

public class MainActivity extends FragmentActivity {
	private FragmentTabHost mTabHost;
	private HashMap<String,String> user;
	private int the_tab;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		user = new HashMap<String, String>();
		user = db.getUserDetails();

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
				.setIndicator("events"), EventFragment.class, b);

		b = new Bundle();
		b.putString("key", "incmedia");
		mTabHost.addTab(mTabHost.newTabSpec("incmedia").setIndicator("incmedia"),
				WebsiteFragment.class, b);

		b = new Bundle();
		b.putString("key", "Account");
		mTabHost.addTab(mTabHost.newTabSpec("account").setIndicator("account"),
				AccountFragment.class, b);
	}

	public HashMap<String,String> getUser() {
		return user;
	}
	
	public int getTab() {
		return the_tab;
	}

	public void setTab(int the_tab) {
		this.the_tab = the_tab;
	}

	@Override
	public void onBackPressed() {
		Fragment webview = getSupportFragmentManager().findFragmentByTag("incmedia");
		if (webview instanceof WebsiteFragment) {
			WebView w = ((WebsiteFragment)webview).getWebView();
			if (w.canGoBack())
				w.goBack();
			else
				super.onBackPressed();
		}
		super.onBackPressed();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) { //updating and deleting
			if(resultCode == MainActivity.RESULT_OK){ 
				AccountFragment d = null;
				d = ((AccountFragment) getSupportFragmentManager().findFragmentByTag("account"));
				AccountEvent ae = ((AccountEvent) d.getChildFragmentManager().findFragmentByTag("accountevents"));
				String updateOrDelete = data.getStringExtra("update_or_delete");
				ae.setPosition(data.getIntExtra("position", 0));
				if (updateOrDelete.equals("delete")) {
					ae.setTitle(data.getStringExtra("title"));
					ae.deleteEventSync();
				} else {
					ae.setEventData(
							new EventData(data.getStringExtra("title"), data.getStringExtra("location"), 
									data.getStringExtra("description"), data.getStringExtra("date_from"), 
									data.getStringExtra("date_to"), data.getStringExtra("time_from"), 
									data.getStringExtra("time_to"), data.getStringExtra("organization")));
					ae.setPrevTitle(data.getStringExtra("prevTitle"));
					ae.updateEventSync();
				}
			} 
			if (resultCode == MainActivity.RESULT_CANCELED) {    

			} 
		} else if (requestCode == 2) { //adding events
			if(resultCode == MainActivity.RESULT_OK){ 
				EventFragment e = null;
				e = ((EventFragment) getSupportFragmentManager().findFragmentByTag("events"));
				EventList d = null;
				if (the_tab == 0) 
					d = ((EventList) e.getChildFragmentManager().findFragmentByTag("cfo"));
				else if (the_tab == 1) 
					d = ((EventList) e.getChildFragmentManager().findFragmentByTag("los"));
				else if (the_tab == 2) 
					d = ((EventList) e.getChildFragmentManager().findFragmentByTag("ws"));
				
				d.setEventData(
						new EventData(data.getStringExtra("title"), data.getStringExtra("location"), 
								data.getStringExtra("description"), data.getStringExtra("date_from"), 
								data.getStringExtra("date_to"), data.getStringExtra("time_from"), 
								data.getStringExtra("time_to"), data.getStringExtra("organization")));
				d.addEventSync();
			} 
			if (resultCode == MainActivity.RESULT_CANCELED) {    
			}
		} 
		else if (requestCode == 3) { //adding office or department
			if(resultCode == MainActivity.RESULT_OK){    
				AccountFragment d = null;
				d = ((AccountFragment) getSupportFragmentManager().findFragmentByTag("account"));
				AccountList e = (AccountList) d.getChildFragmentManager().findFragmentByTag("accountlist");
				e.setChoose(data);
			} 
			if (resultCode == MainActivity.RESULT_CANCELED) {    
			}
		}
	}
}
