package com.bums.small;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class AccountFragment extends DetailsFragment {

	private FragmentTabHost mTabHost;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mTabHost = new FragmentTabHost(getActivity());
		mTabHost.setup(getActivity(), getChildFragmentManager(),
				R.id.menu_settings);

//		Bundle b = new Bundle();
//		b.putString("key", "Details");
//		mTabHost.addTab(mTabHost.newTabSpec("details").setIndicator("Details"),
//				Fragment1.class, b);
//		//
//		b = new Bundle();
//		b.putString("key", "Edit");
//		mTabHost.addTab(mTabHost.newTabSpec("edit")
//				.setIndicator("Edit"), Fragment1.class, b);
		
		Bundle b = new Bundle();
		b.putString("key", "Details");
		mTabHost.addTab(mTabHost.newTabSpec("details").setIndicator("Details"),
				DetailsFragment.class, b);
		//
		b = new Bundle();
		b.putString("key", "Edit");
		mTabHost.addTab(mTabHost.newTabSpec("edit")
				.setIndicator("Edit"), DetailsFragment.class, b);
		return mTabHost;
	}
	
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    inflater.inflate(R.menu.account, menu);
	    super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.action_overflow:
	        // Do Fragment menu item stuff here
	        return true;
	    default:
	        break;
	    }

	    return false;
	}
}
