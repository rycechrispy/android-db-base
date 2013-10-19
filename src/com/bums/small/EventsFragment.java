package com.bums.small;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class EventsFragment extends Fragment {
	private FragmentTabHost mTabHost;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mTabHost = new FragmentTabHost(getActivity());
		mTabHost.setup(getActivity(), getChildFragmentManager(),
				R.id.menu_settings);
		
		Bundle b = new Bundle();
		b.putString("key", "Christian Family Organization");
		mTabHost.addTab(mTabHost.newTabSpec("cfo").setIndicator("Christian Family Organization"),
				DetailsFragment.class, b);
		
		b = new Bundle();
		b.putString("key", "Light of Salvation");
		mTabHost.addTab(mTabHost.newTabSpec("los")
				.setIndicator("Light of Salvation"), DetailsFragment.class, b);
		
		b = new Bundle();
		b.putString("key", "Worship Service");
		mTabHost.addTab(mTabHost.newTabSpec("ws")
				.setIndicator("Worship Service"), DetailsFragment.class, b);
		return mTabHost;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}
}
