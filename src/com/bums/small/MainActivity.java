package com.bums.small;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.webkit.WebView;
import android.widget.TabHost.OnTabChangeListener;

import com.bums.library.DatabaseHandler;

public class MainActivity extends FragmentActivity {

	private FragmentTabHost mTabHost;
	private HashMap<String,String> user;
	private ArrayList<String> office;
	private ArrayList<String> department;
	private AccountFragment af;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		user = new HashMap<String, String>();
		user = db.getUserDetails();

		setContentView(R.layout.bottom_tabs);

		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
//		mTabHost.setOnTabChangedListener(new OnTabChangeListener(){
//			@Override
//			public void onTabChanged(String tabId) {
//				Fragment old_frag = getSupportFragmentManager().findFragmentByTag("office");
//			    if (!tabId.equals("account") && old_frag != null) {
//			    	FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//
//			    	transaction.detach(old_frag);
//			    	transaction.attach(new AccountFragment()); //if done this way probably have to set the lastest adapter to it
//			    	transaction.commit();
//			    }
//			}});

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
	
	public AccountFragment getAccountFragment() {
		return af;
	}
	
	public void setAccountFragment(AccountFragment current) {
		this.af = current;
	}

	public ArrayList<String> getOffice() {
		return office;
	}

	public void setOffice(ArrayList<String> office) {
		this.office = office;
	}

	public ArrayList<String> getDepartment() {
		return department;
	}

	public void setDepartment(ArrayList<String> department) {
		this.department = department;
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
