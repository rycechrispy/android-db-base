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
	
	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR = "error";

	private FragmentTabHost mTabHost;
	private HashMap<String,String> user;
	
	private String officeType;
	private String isLeader;
	
	private boolean isOfficeUnique;
	private boolean isDepartmentUnique;
	private String department;
	private String group;

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
	
	public void setLeadership(String leadership) {
		isLeader = leadership;
	}
	
	public void setGroup(String g) {
		group = g;
	}
	
	public void setDepartment(String d) {
		department = d;
	}
	
	public void setOffice(String office) {
		officeType = office;
	}
	
	public boolean isOfficeUnique() {
		return isOfficeUnique;
	}
	
	public boolean isDepartmentUnique() {
		return isDepartmentUnique;
	}
	
	private class StoreOffice extends AsyncTask<String, String, JSONObject> {
		String id;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			id = getUser().get("id");
		}
		@Override
		protected JSONObject doInBackground(String... args) {
			UserFunctions userFunction = new UserFunctions();
			JSONObject json = userFunction.storeOffice(id, officeType, isLeader);
			return json;
		}
		@Override
		protected void onPostExecute(JSONObject json) {
			try {
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);
					String red = json.getString(KEY_ERROR);

					if(Integer.parseInt(res) == 1) {
						isOfficeUnique = true;
	
						Toast.makeText(getApplicationContext(),
								"Successfully added office", Toast.LENGTH_SHORT).show();
						
					} else if (Integer.parseInt(red) == 2){
						isOfficeUnique = false;
						Toast.makeText(getApplicationContext(),
								"You are already that officer", Toast.LENGTH_SHORT).show();
					} else if (Integer.parseInt(red) == 3){
						isOfficeUnique = false;
						Toast.makeText(getApplicationContext(),
								"JSON error", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getApplicationContext(),
							"Error occurred adding office", Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}}
	
	private class StoreDepartment extends AsyncTask<String, String, JSONObject> {
		String id;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			id = getUser().get("id");
		}
		@Override
		protected JSONObject doInBackground(String... args) {
			UserFunctions userFunction = new UserFunctions();
			JSONObject json = userFunction.storeDepartment(id, department, group);
			return json;
		}
		@Override
		protected void onPostExecute(JSONObject json) {
			try {
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);
					String red = json.getString(KEY_ERROR);

					if(Integer.parseInt(res) == 1) {
						isDepartmentUnique = true;
	
						Toast.makeText(getApplicationContext(),
								"Successfully added organization/department", Toast.LENGTH_SHORT).show();
						
					} else if (Integer.parseInt(red) == 2){
						isDepartmentUnique = false;
						Toast.makeText(getApplicationContext(),
								"You are already in that organization", Toast.LENGTH_SHORT).show();
					} else if (Integer.parseInt(red) == 3){
						isDepartmentUnique = false;
						Toast.makeText(getApplicationContext(),
								"JSON error", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getApplicationContext(),
							"Error occurred adding department", Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}}
	
	private class DeleteOffice extends AsyncTask<String, String, JSONObject> {
		String id;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			id = getUser().get("id");
		}
		@Override
		protected JSONObject doInBackground(String... args) {
			UserFunctions userFunction = new UserFunctions();
			JSONObject json = userFunction.deleteOffice(id, officeType);
			return json;
		}
		@Override
		protected void onPostExecute(JSONObject json) {

			try {
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);

					if(Integer.parseInt(res) == 1) {
						
						Toast.makeText(getApplicationContext(),
								"Successfully removed office", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getApplicationContext(),
							"Error occurred removing office", Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}}
	
	private class DeleteDepartment extends AsyncTask<String, String, JSONObject> {
		String id;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			id = getUser().get("id");
		}
		@Override
		protected JSONObject doInBackground(String... args) {
			UserFunctions userFunction = new UserFunctions();
			JSONObject json = userFunction.deleteDepartment(id, group);
			return json;
		}
		@Override
		protected void onPostExecute(JSONObject json) {
			try {
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);
					if(Integer.parseInt(res) == 1) {	
						Toast.makeText(getApplicationContext(),
								"Successfully removed organization", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getApplicationContext(),
							"Error occurred removing organization", Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}}
	
	public void storeOfficeASync(){
		new StoreOffice().execute();
	}
	
	public void storeDepartmentASync(){
		new StoreDepartment().execute();
	}
	
	public void deleteOfficeASync(){
		new DeleteOffice().execute();
	}
	
	public void deleteDepartmentASync(){
		new DeleteDepartment().execute();
	}
}
