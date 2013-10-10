package com.bums.small;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.Menu;

public class MainActivity extends FragmentActivity {

	private FragmentTabHost mTabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bottom_tabs);
		// mTabHost = new FragmentTabHost(this);
		// mTabHost.setup(this, getSupportFragmentManager(),
		// R.id.menu_settings);
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

		Bundle b = new Bundle();
//		b.putString("key", "Feed");
//		mTabHost.addTab(mTabHost.newTabSpec("feed").setIndicator("feed"),
//				Fragment1.class, b);
		//
		b = new Bundle();
		b.putString("key", "Events");
		mTabHost.addTab(mTabHost.newTabSpec("events")
				.setIndicator("Events"), Fragment2.class, b);
		
		b = new Bundle();
		b.putString("key", "incmedia");
		mTabHost.addTab(mTabHost.newTabSpec("incmedia").setIndicator("incmedia"),
				INCMediaFragment.class, b);
		
		b = new Bundle();
		b.putString("key", "Account");
		mTabHost.addTab(mTabHost.newTabSpec("account").setIndicator("account"),
				Fragment3.class, b);
		// setContentView(mTabHost);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// public TabSpec setIndicator(TabSpec spec, int resid) {
	// // TODO Auto-generated method stub
	// View v = LayoutInflater.from(this).inflate(R.layout.tabs_text, null);
	// v.setBackgroundResource(resid);
	// TextView text = (TextView) v.findViewById(R.id.tab_title);
	//
	// text.setText(spec.getTag());
	// return spec.setIndicator(v);
	// }

}
