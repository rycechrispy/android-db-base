package com.bums.small;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bums.library.DatabaseHandler;
import com.bums.library.UserFunctions;

public class FashionFragment extends ListFragment {
	/**
	 * Called when the activity is first created.
	 */
	private static String KEY_SUCCESS = "success";
	private static String KEY_ID = "id";

	private static String KEY_DATA = "data";
	private static String KEY_IMAGES = "images";
	private static String KEY_STANDARD = "standard_resolution";
	private static String KEY_CAPTION = "caption";
	private static String KEY_TEXT = "text";
	private static String KEY_USER = "user";
	private static String KEY_USERNAME = "username";
	private static String KEY_NAME = "full_name";
	private static String KEY_URL = "url";

	private ProgressBar bar;

	// An array of all of our comments
	private JSONArray iData = null;
	// manages all of our comments in a list.
	private ArrayList<HashMap<String, String>> iDataList;
	private View v;

	private ArrayList<String> images;
	private boolean flag = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = super.onCreateView(inflater, container, savedInstanceState);

		//		bar = (ProgressBar) v.findViewById(R.id.progressBar);
		//		bar.setVisibility(View.VISIBLE);
		if (flag) {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(inflater.getContext(), android.R.layout.simple_list_item_1, images);
			setListAdapter(adapter);
		} else {
			images = new ArrayList<String>();
			flag = true;
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(inflater.getContext(), android.R.layout.simple_list_item_1,images);
			setListAdapter(adapter);
		}

		NetAsync(v);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
	   super.onActivityCreated(savedInstanceState);
	   if (flag) {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, images);
			setListAdapter(adapter);
		}
	}

	/**
	 * Async Task to check whether internet connection is working.
	 **/
	private class NetCheck extends AsyncTask<String,String,Boolean>
	{
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			//			bar = (ProgressBar) v.findViewById(R.id.progressBar);
			//			bar.setVisibility(View.VISIBLE);
		}
		/**
		 * Gets current device state and checks for working internet connection by trying Google.
		 **/
		@Override
		protected Boolean doInBackground(String... args){
			ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getActiveNetworkInfo();
			if (netInfo != null && netInfo.isConnected()) {
				try {
					URL url = new URL("http://www.google.com");
					HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
					urlc.setConnectTimeout(3000);
					urlc.connect();
					if (urlc.getResponseCode() == 200) {
						return true;
					}
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean th){
			if(th == true){
				new LoadFashion().execute();
			}
			else{
				bar = (ProgressBar) v.findViewById(R.id.progressBar);
				bar.setVisibility(View.INVISIBLE);
				Toast.makeText(getActivity().getApplicationContext(),
						"Network Error Connection", Toast.LENGTH_SHORT).show();
			}
		}
	}

	public void updateJSONdata(JSONObject json) {
		iDataList = new ArrayList<HashMap<String, String>>();
		try {
			iData = json.getJSONArray(KEY_DATA);

			for (int i = 0; i < iData.length(); i++) {
				JSONObject data = iData.getJSONObject(i);

				JSONObject images = data.getJSONObject(KEY_IMAGES);
				JSONObject standard = images.getJSONObject(KEY_STANDARD);
				String url = standard.getString(KEY_URL);

				JSONObject caption = data.getJSONObject(KEY_CAPTION);
				String text = caption.getString(KEY_TEXT);

				JSONObject user = data.getJSONObject(KEY_USER);
				String username = user.getString(KEY_USERNAME);

				// creating new HashMap
				HashMap<String, String> map = new HashMap<String, String>();

				map.put(KEY_URL, url);
				map.put(KEY_USERNAME, username);
				map.put(KEY_TEXT, text);

				// adding HashList to ArrayList
				iDataList.add(map);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> getURLs(ArrayList<HashMap<String, String>> data) {
		for (int i = 0; i < data.size(); i++) {
			images.add(iDataList.get(i).get(KEY_URL));
		}
		return images;
	}

	/**
	 * Async Task to get and send data to My Sql database through JSON respone.
	 **/
	private class LoadFashion extends AsyncTask<String, String, JSONObject> {
		String url, username, text;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			//			bar = (ProgressBar) v.findViewById(R.id.progressBar);
			//			bar.setVisibility(View.VISIBLE);
		}

		@Override
		protected JSONObject doInBackground(String... args) {
			UserFunctions userFunction = new UserFunctions();
			JSONObject json = userFunction.getFashionJSON();

			updateJSONdata(json);

			return json;
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			try {
					//					bar = (ProgressBar) v.findViewById(R.id.progressBar);
					//					bar.setVisibility(View.INVISIBLE);

						images = getURLs(iDataList);
						ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, images);
						setListAdapter(adapter);
				
			} catch (Exception e) {
				Toast.makeText(getActivity().getApplicationContext(),
						"JSON FAILED", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
		}
	}
	public void NetAsync(View view){
		new NetCheck().execute();
	}
}