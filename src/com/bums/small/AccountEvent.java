package com.bums.small;

import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
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

public class AccountEvent extends ListFragment {
	private Context context;
	private EventAdapter eAdapter;
	private EventData eventData;
	private String title;
	private String prevTitle;
	private int position;
	
	private ArrayList<EventData> eventDataList;
	private ProgressBar bar;
	private View v;
	
	public EventData getEventData() {
		return eventData;
	}

	public void setEventData(EventData eventData) {
		this.eventData = eventData;
	}
	
	

	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR = "error";
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

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

		new GetEvents().execute();

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
	
	public void deleteEventSync() {
		new DeleteEvent().execute();
	}
	
	public void updateEventSync() {
		new UpdateEvent().execute();
	}
	
	private class UpdateEvent extends AsyncTask<String, String, JSONObject> {
		private String id;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			id = ((MainActivity) getActivity()).getUser().get("id");
		}
		@Override
		protected JSONObject doInBackground(String... args) {
			UserFunctions userFunction = new UserFunctions();
			JSONObject json = userFunction.updateEvent(id, eventData, prevTitle);	
			return json;
		}
		@Override
		protected void onPostExecute(JSONObject json) {
			try {
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);
					String red = json.getString(KEY_ERROR);

					if(Integer.parseInt(res) == 1) {
						eAdapter.updateEvent(getPosition(), eventData);
						eventDataList.set(getPosition(), eventData);
						Collections.sort(eventDataList);

						Toast.makeText(getActivity().getApplicationContext(),
								"Successfully updated event", Toast.LENGTH_SHORT).show();

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
	
	private class DeleteEvent extends AsyncTask<String, String, JSONObject> {
		private String id;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			id = ((MainActivity) getActivity()).getUser().get("id");
		}
		@Override
		protected JSONObject doInBackground(String... args) {
			UserFunctions userFunction = new UserFunctions();
			JSONObject json = userFunction.deleteEvent(id, title);	
			return json;
		}
		@Override
		protected void onPostExecute(JSONObject json) {
			try {
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);
					String red = json.getString(KEY_ERROR);

					if(Integer.parseInt(res) == 1) {
						eAdapter.deleteEvent(getPosition());
						eventDataList.remove(getPosition());
						Collections.sort(eventDataList);

						Toast.makeText(getActivity().getApplicationContext(),
								"Successfully removed event", Toast.LENGTH_SHORT).show();

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
	
	private class GetEvents extends AsyncTask<String, String, JSONObject> {
		private String id;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			id = ((MainActivity) getActivity()).getUser().get("id");
		}
		@Override
		protected JSONObject doInBackground(String... args) {
			UserFunctions userFunction = new UserFunctions();
			JSONObject json = userFunction.getUserEvents(id);	
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

		public void deleteEvent(int position) {
			eData.remove(position);
			Collections.sort(eData);
			notifyDataSetChanged();
		}
		
		public void updateEvent(int position, EventData data) {
			eData.set(position, data);
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

				Intent intent = new Intent(getActivity(), EditEventActivity.class);
				intent.putExtra("the_event", eventDataList.get(position));
				intent.putExtra("position", position);
				getActivity().startActivityForResult(intent, 1);

			}
		});
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getPrevTitle() {
		return prevTitle;
	}

	public void setPrevTitle(String prevTitle) {
		this.prevTitle = prevTitle;
	}

}
