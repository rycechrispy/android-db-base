package com.bums.small;

import com.bums.library.DatabaseHandler;

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
import android.widget.Toast;


public class AccountFragment extends Fragment {
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

		Bundle b = new Bundle();
		b.putString("key", "accountlist");
		mTabHost.addTab(mTabHost.newTabSpec("accountlist").setIndicator("Details"),
				AccountList.class, b);

		b = new Bundle();
		b.putString("key", "accountevents");
		mTabHost.addTab(mTabHost.newTabSpec("accountevents")
				.setIndicator("My Events"), AccountEvent.class, b);
		return mTabHost;
	}

//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (requestCode == 1) {
//			if(resultCode == MainActivity.RESULT_OK){ 
//				AccountEvent d = null;
//				d = ((AccountEvent) getChildFragmentManager().findFragmentByTag("accountevents"));
//				String updateOrDelete = data.getStringExtra("update_or_delete");
//				d.setPosition(data.getIntExtra("position", 0));
//				if (updateOrDelete.equals("delete")) {
//					d.setTitle(data.getStringExtra("title"));
//					d.deleteEventSync();
//				} else {
//					d.setEventData(
//							new EventData(data.getStringExtra("title"), data.getStringExtra("location"), 
//									data.getStringExtra("description"), data.getStringExtra("date_from"), 
//									data.getStringExtra("date_to"), data.getStringExtra("time_from"), 
//									data.getStringExtra("time_to"), data.getStringExtra("organization")));
//					d.setPrevTitle(data.getStringExtra("prevTitle"));
//					d.updateEventSync();
//				}
//			} 
//			if (resultCode == MainActivity.RESULT_CANCELED) {    
//				//Write your code if there's no result
//			}
//		}
//	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}
}
