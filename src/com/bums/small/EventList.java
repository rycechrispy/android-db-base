package com.bums.small;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bums.library.UserFunctions;

public class EventList extends ListFragment {
	private Context context;
	private EventAdapter eAdapter;
	private EventData eventData;
	private int the_tab;
	
	private ArrayList<EventData> eventDataList;
	private ProgressBar bar;
	private View v;
	
	private static final int CFO = 0;
	private static final int LOS = 1;
	private static final int WS = 2;
	
	public EventData getEventData() {
		return eventData;
	}

	public void setEventData(EventData eventData) {
		this.eventData = eventData;
	}

	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR = "error";

	public EventList() {
		// TODO Auto-generated constructor stub

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		//if there are no events, load the view without lists
		//View v = LayoutInflater.from(getActivity()).inflate(R.layout.no_events,
		//		null);
		v = LayoutInflater.from(getActivity()).inflate(R.layout.event_list,
				null);

		bar = (ProgressBar) v.findViewById(R.id.progressBar);
		bar.setVisibility(View.VISIBLE);
		
		context = inflater.getContext();
		eAdapter = new EventAdapter();
		setListAdapter(eAdapter);

		if (getArguments() != null) {
			try {
				String value = getArguments().getString("key");
				if (value.equals("Christian Family Organization")) {
					the_tab = CFO;
					//((EventsFragment) getParentFragment()).setTab(CFO);
					((MainActivity) getActivity()).setTab(CFO);
					new NetCheck().execute();
					//load cfo
				} else if(value.equals("Light of Salvation")) {
					the_tab = LOS;
					//((EventsFragment) getParentFragment()).setTab(LOS);
					((MainActivity) getActivity()).setTab(LOS);
					new NetCheck().execute();
				} else if (value.equals("Worship Service")) {
					the_tab = WS;
					((MainActivity) getActivity()).setTab(WS);
					//((EventsFragment) getParentFragment()).setTab(WS);
					new NetCheck().execute();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return v;
	}
	
	public void getEventInformation(JSONObject json) {
		eventDataList = new ArrayList<EventData>();
		try {
			if (json.getString(KEY_SUCCESS) != null) {
				String res = json.getString(KEY_SUCCESS);
				String red = json.getString(KEY_ERROR);

				if(Integer.parseInt(res) == 1) {
					JSONArray eventJson = json.getJSONArray("events");
					for (int i = 0; i < eventJson.length(); i++) {
						JSONObject data = eventJson.getJSONObject(i);
						String title = data.getString("title");
						String location = data.getString("location");
						String description = data.getString("description");
						String dateFrom = data.getString("dateFrom");
						String dateTo = data.getString("dateTo");
						String timeFrom = data.getString("timeFrom");
						String timeTo = data.getString("timeTo");
						String organization = data.getString("organization");

						eventDataList.add(new EventData(title, location, 
								description, dateFrom, 
								dateTo, timeFrom, 
								timeTo, organization));
					}
				} else if (Integer.parseInt(red) == 4){

				}
			} 
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private class GetEvents extends AsyncTask<String, String, JSONObject> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		@Override
		protected JSONObject doInBackground(String... args) {
			UserFunctions userFunction = new UserFunctions();
			JSONObject json = null;
			if (the_tab == CFO) {
				json = userFunction.getEvents("Christian Family Organization");	
			} else if (the_tab == LOS) {
				json = userFunction.getEvents("Light of Salvation");	
			} else if (the_tab == WS) {
				json = userFunction.getEvents("Worship Service");	
			}
			getEventInformation(json);
			return json;
		}
		@Override
		protected void onPostExecute(JSONObject json) {
			bar = (ProgressBar) v.findViewById(R.id.progressBar);
			bar.setVisibility(View.INVISIBLE);
			
			try {
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);
					String red = json.getString(KEY_ERROR);

					if(Integer.parseInt(res) == 1) {
						for (int i = 0; i < eventDataList.size(); i++) {
							EventData event = eventDataList.get(i);
							eAdapter.addEvent(event);
						}
						Collections.sort(eventDataList);

						Toast.makeText(getActivity().getApplicationContext(),
								"Successfully retrieved events", Toast.LENGTH_SHORT).show();

					} else if (Integer.parseInt(red) == 4){
						Toast.makeText(getActivity().getApplicationContext(),
								"There are no events", Toast.LENGTH_SHORT).show();
					} 
				} else {
					Toast.makeText(getActivity().getApplicationContext(),
							"Error occurred retrieving events", Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}}
	
	private class AddEventSync extends AsyncTask<String, String, JSONObject> {
		String id;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			id = ((MainActivity) getActivity()).getUser().get("id");
		}
		@Override
		protected JSONObject doInBackground(String... args) {
			UserFunctions userFunction = new UserFunctions();
			JSONObject json = userFunction.addEvent(id, eventData);
			return json;
		}
		@Override
		protected void onPostExecute(JSONObject json) {
			try {
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);
					String red = json.getString(KEY_ERROR);

					if(Integer.parseInt(res) == 1) {
						if (eventData.getDepartment().equals("Christian Family Organization") 
								|| eventData.getDepartment().equals("All Officers") 
								&& the_tab == CFO)
							eAdapter.addEvent(eventData);
						else if (eventData.getDepartment().equals("Light of Salvation")
								|| eventData.getDepartment().equals("All Officers") 
								&& the_tab == LOS)
							eAdapter.addEvent(eventData);
						else if (eventData.getDepartment().equals("Worship Service") 
								|| eventData.getDepartment().equals("All Officers") 
								&& the_tab == WS)
							eAdapter.addEvent(eventData);
						
						eventDataList.add(eventData);
						Collections.sort(eventDataList);
						
						Toast.makeText(getActivity().getApplicationContext(),
								"Successfully added an event", Toast.LENGTH_SHORT).show();

					} else if (Integer.parseInt(red) == 2){
						Toast.makeText(getActivity().getApplicationContext(),
								"You are already in that organization", Toast.LENGTH_SHORT).show();
					} else if (Integer.parseInt(red) == 3){
						Toast.makeText(getActivity().getApplicationContext(),
								"JSON error", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getActivity().getApplicationContext(),
							"Error occurred adding department", Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}}

	public class EventAdapter extends BaseAdapter {
		private ArrayList<EventData> eData = new ArrayList<EventData>();
		private LayoutInflater mInflater;

		public EventAdapter() {
			mInflater = (LayoutInflater)((Context) context).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public ArrayList<EventData> getEData() {
			return eData;
		}

		public void addEvent(EventData data) {
			eData.add(data); //can get the organization by calling getOrganization
			Collections.sort(eData);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return eData.size();
		}

		@Override
		public EventData getItem(int position) {
			return eData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.event_row, null);
				holder.organization = (ImageView)convertView.findViewById(R.id.list_image);
				holder.title = (TextView)convertView.findViewById(R.id.title);
				holder.location = (TextView)convertView.findViewById(R.id.location);
				holder.date = (TextView)convertView.findViewById(R.id.date);
				holder.time = (TextView)convertView.findViewById(R.id.time);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder)convertView.getTag();
			}

			holder.organization.setImageResource(eData.get(position).getImage());
			holder.title.setText(eData.get(position).getTitle());
			holder.location.setText(eData.get(position).getLocation());
			holder.date.setText(eData.get(position).getRegularDateFrom());
			holder.time.setText(eData.get(position).getRegularTimeFrom());

			return convertView;
		}

	}
	
	public void addEventSync() {
		new AddEventSync().execute();
	}

	public static class ViewHolder {
		public ImageView organization;
		public TextView title;
		public TextView location;
		public TextView date;
		public TextView time;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				
				Intent intent = new Intent(getActivity(), EventDetailsActivity.class);
				intent.putExtra("the_event", eventDataList.get(position));
				startActivity(intent);
				
//				EventDetailsFragment edFragment = new EventDetailsFragment();
//				FragmentTransaction transaction = getFragmentManager().beginTransaction();
//				// Replace whatever is in the fragment_container view with this fragment,
//				// and add the transaction to the back stack
//				
//				Fragment current = getFragmentManager().findFragmentByTag("cfo");
//				//((MainActivity) getActivity()).setAccountFragment(current);
//				transaction.detach(current);
//				//transaction.replace(((ViewGroup)(getView().getParent())).getId(), departFragment, "office");
//				transaction.replace(R.id.realtabcontent, edFragment, "details");
//				// Commit the transaction
//				transaction.commit();

			}
		});
	}
	
	/**
	 * Async Task to check whether internet connection is working.
	 **/
	private class NetCheck extends AsyncTask<String,String,Boolean>
	{
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			bar = (ProgressBar) v.findViewById(R.id.progressBar);
			bar.setVisibility(View.VISIBLE);
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
				new GetEvents().execute();
			}
			else{
				Toast.makeText(getActivity().getApplicationContext(),
						"Network Error Connection", Toast.LENGTH_SHORT).show();
				
				bar = (ProgressBar) v.findViewById(R.id.progressBar);
				bar.setVisibility(View.INVISIBLE);
			}
		}
	}

}
