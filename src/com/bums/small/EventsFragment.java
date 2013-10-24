package com.bums.small;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


public class EventsFragment extends Fragment {
	private FragmentTabHost mTabHost;
	private int the_tab;
	
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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 2) {
			if(resultCode == MainActivity.RESULT_OK){ 
				DetailsFragment d = null;
				if (the_tab == 0) 
					d = ((DetailsFragment) getChildFragmentManager().findFragmentByTag("cfo"));
				else if (the_tab == 1) 
					d = ((DetailsFragment) getChildFragmentManager().findFragmentByTag("los"));
				else if (the_tab == 2) 
					d = ((DetailsFragment) getChildFragmentManager().findFragmentByTag("ws"));
				
				d.setEventData(
						new EventData(data.getStringExtra("title"), data.getStringExtra("location"), 
								data.getStringExtra("description"), data.getStringExtra("date_from"), 
								data.getStringExtra("date_to"), data.getStringExtra("time_from"), 
								data.getStringExtra("time_to"), data.getStringExtra("organization")));
				d.addEventSync();
			} 
			if (resultCode == MainActivity.RESULT_CANCELED) {    
				//Write your code if there's no result
			}
		}
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.event, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add:
			//			Random r = new Random();
			//			int a = r.nextInt(11);
			//			EventData ed = new EventData("General Cleaning", "Kent Chapel", "10/20/13", "10:00am", a);
			//			eAdapter.addEvent(ed);
			//			Intent calIntent = new Intent(Intent.ACTION_INSERT);
			//			calIntent.setType("vnd.android.cursor.item/event");
			//			calIntent.putExtra(Events.ORGANIZER, "Kadiwa");
			//			startActivity(calIntent);

			Intent intent = new Intent(getActivity(), AddEvent.class);
			//startActivityForResult(intent, 2);
			startActivityForResult(intent, 2);
			return true;
		default:
			break;
		}

		return false;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	public int getTab() {
		return the_tab;
	}

	public void setTab(int the_tab) {
		this.the_tab = the_tab;
	}
}
