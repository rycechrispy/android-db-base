package com.bums.small;

import java.util.ArrayList;

import com.bums.library.DatabaseHandler;
import com.bums.small.AccountFragment.ViewHolder;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DetailsFragment extends Fragment {

	private TextView text;

	public DetailsFragment() {
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
		View v = LayoutInflater.from(getActivity()).inflate(R.layout.layout,
				null);
		text = (TextView) v.findViewById(R.id.text);
		if (getArguments() != null) {
			//
			try {
				String value = getArguments().getString("key");
				text.setText("Current Tab is: " + value + "\n" + ((MainActivity) getActivity()).getUser().get("username"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return v;
	}
	
	/*
	public class MyCustomAdapter extends BaseAdapter {
		private ArrayList<ArrayList<String>> mData = new ArrayList<ArrayList<String>>();
		private ArrayList<Integer> the_position = new ArrayList<Integer>();
		private LayoutInflater mInflater;
		private ArrayList<String> the_labels;

		public MyCustomAdapter() {
			mInflater = (LayoutInflater)((Context) context).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public ArrayList<ArrayList<String>> getMData() {
			return mData;
		}

		public ArrayList<Integer> getPosition() {
			return the_position;
		}

		public void addHeader(final String header) {
			the_labels = new ArrayList<String>();
			the_labels.add(header);
			mData.add(the_labels);
			the_position.add(TYPE_HEADER);
			notifyDataSetChanged();
		}


		public void addCategoryInfo(final String category, final String info) {
			the_labels = new ArrayList<String>();
			the_labels.add(category);
			the_labels.add(info);
			mData.add(the_labels);
			// save separator position
			the_position.add(TYPE_CATEGORY);
			notifyDataSetChanged();
		}

		public void addOffice(final String adder) {
			the_labels = new ArrayList<String>();
			the_labels.add(adder);
			mData.add(the_labels);
			the_position.add(TYPE_ADDOFFICE);
			notifyDataSetChanged();
		}

		public void addDepartment(final String adder) {
			the_labels = new ArrayList<String>();
			the_labels.add(adder);
			mData.add(the_labels);
			the_position.add(TYPE_DEPARTMENT);
			notifyDataSetChanged();
		}

		public int getAddOfficePosition() {
			return the_position.indexOf(TYPE_ADDOFFICE);
		}


		public void removeOffice(int position) {
			the_position.remove(position);
			office = mData.get(position).get(0);
			//deleteOfficeASync();
			DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());
			db.deleteOffice(id, office);
			mData.remove(position);
			notifyDataSetChanged();
		}

		@Override
		public int getItemViewType(int position) {
			return the_position.get(position);
		}

		@Override
		public int getViewTypeCount() {
			return TYPE_MAX_COUNT;
		}

		@Override
		public int getCount() {
			return mData.size();
		}

		@Override
		public ArrayList<String> getItem(int position) {
			return mData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			int type = getItemViewType(position);
			//System.out.println("getView " + position + " " + convertView + " type = " + type);
			if (convertView == null) {
				holder = new ViewHolder();
				
				convertView = mInflater.inflate(R.layout.event_row, null);
				holder.textView = (TextView)convertView.findViewById(R.id.acc_text);
				holder.textView2 = (TextView)convertView.findViewById(R.id.acc_text2);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder)convertView.getTag();
			}

			holder.textView.setText(mData.get(position).get(0));
			holder.textView2.setText(mData.get(position).get(1));


			return convertView;
		}

	}

	public static class ViewHolder {
		public TextView textView;
		public TextView textView2;
	}*/

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

}
