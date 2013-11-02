package com.bums.small;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.bums.library.UserFunctions;


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
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.signout:
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage("Are you sure you want to sign out?")
			.setPositiveButton("No", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.dismiss();
				}
			})
			.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					UserFunctions logout = new UserFunctions();
					logout.logoutUser(getActivity().getApplicationContext());
					Intent login = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
					login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(login);
					getActivity().finish();
				}
			});
			builder.create().show();
			return true;
		default:
			break;
		}

		return false;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.account, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}
}
