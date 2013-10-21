package com.bums.small;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailsFragment extends ListFragment {

	private TextView text;
	private Context context;
	private EventAdapter eAdapter;

	public DetailsFragment() {
		// TODO Auto-generated constructor stub

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		//if there are no events, load the view without lists
		//View v = LayoutInflater.from(getActivity()).inflate(R.layout.no_events,
		//		null);
		View v = LayoutInflater.from(getActivity()).inflate(R.layout.event_list,
				null);

		context = inflater.getContext();
		eAdapter = new EventAdapter();
		setListAdapter(eAdapter);
		return v;
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
			Random r = new Random();
			int a = r.nextInt(11);
			EventData ed = new EventData("General Cleaning", "Kent Chapel", "10/20/13", "10:00am", a);
			eAdapter.addEvent(ed);
			return true;
		default:
			break;
		}

		return false;
	}

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
			notifyDataSetChanged();
		}

		public void removeOffice(int position) {

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
			holder.date.setText(eData.get(position).getDate());
			holder.time.setText(eData.get(position).getTime());

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
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

}
