package com.bums.small;

import java.util.ArrayList;

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
import android.widget.TextView;
import android.widget.Toast;

import com.bums.library.DatabaseHandler;
import com.bums.library.UserFunctions;

public class AccountList extends ListFragment {

	private static final int TYPE_HEADER = 0;
	private static final int TYPE_CATEGORY = 1;
	private static final int TYPE_ADDOFFICE = 2;
	private static final int TYPE_DEPARTMENT = 3;
	private static final int TYPE_D_DETAILS = 4;
	private static final int TYPE_O_DETAILS = 5;
	private static final int TYPE_MAX_COUNT = 6; //amount of types

	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR = "error";

	private MyCustomAdapter mAdapter;
	private Context context;
	private String group;
	private String department; 
	private String choose;
	private String office;
	private String isLeader;
	private boolean isLeaderBool;
	private ArrayList<OfficeData> officeDataList;
	private JSONArray officeJson;
	private ArrayList<DepartmentData> departmentDataList;
	private JSONArray departmentJson;
	private String id;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = LayoutInflater.from(getActivity()).inflate(R.layout.account_list,
				null);
		
		id = ((MainActivity) getActivity()).getUser().get("id");
		context = inflater.getContext();
		mAdapter = new MyCustomAdapter();
		mAdapter.addHeader("General");
		mAdapter.addCategoryInfo("Username: ", ((MainActivity) getActivity()).getUser().get("username"));
		mAdapter.addCategoryInfo("Password: ", ((MainActivity) getActivity()).getUser().get("password"));
		mAdapter.addHeader("Offices");
		mAdapter.addOffice("Add Office");
		mAdapter.addHeader("Organization and Department"); //organization = kad, choir, over - department = cfo, los, ws
		mAdapter.addDepartment("Add Organization and Department");
		
		DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());
		officeDataList = db.getOffices(id);
		for (int i = 0; i < officeDataList.size(); i++) {
			String officeType = officeDataList.get(i).getOfficeType();
			String isLeader = officeDataList.get(i).getIsLeader();
			mAdapter.addOfficeDetails(officeType, isLeader);
		}
		
		departmentDataList = db.getDepartment(id);
		for (int i = 0; i < departmentDataList.size(); i++) {
			String organization = departmentDataList.get(i).getOrganization();
			String department = departmentDataList.get(i).getDepartment();
			mAdapter.addDepartmentDetails(organization, department);
		}
		
		//new GetOffices().execute();
		//new GetDepartment().execute();
		setListAdapter(mAdapter);
		return v;
	}

	public MyCustomAdapter getmAdapter() {
		return mAdapter;
	}

	public void setmAdapter(MyCustomAdapter mAdapter) {
		setListAdapter(mAdapter);
	}
	
	public void setChoose(Intent data) {
		choose = data.getStringExtra("choose");
		if (choose.equals("choose_department")) {
			group = data.getStringExtra("group");
			department = data.getStringExtra("department"); 
			DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());
			if (db.addDepartment(id, group, department)) {
				mAdapter.addDepartmentDetails(group, department);
			} else {
				Toast.makeText(getActivity().getApplicationContext(),
						"You are already that officer", Toast.LENGTH_SHORT).show();
			}
			//storeDepartmentASync();
			//if (((MainActivity) getActivity()).isDepartmentUnique())
			//mAdapter.addDepartmentDetails(group, department);
		} else if (choose.equals("choose_office")) {
			office = data.getStringExtra("office");
			isLeaderBool = data.getBooleanExtra("isLeader", false);
			isLeader = "Officer";
			if (isLeaderBool) {
				isLeader = "Leadership";
			}
			DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());
			if (db.addOffice(id, office, isLeader)) {
				mAdapter.addOfficeDetails(office, isLeader);
			} else {
				Toast.makeText(getActivity().getApplicationContext(),
						"You are already that officer", Toast.LENGTH_SHORT).show();
			}
			//storeOfficeASync();
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				int type = mAdapter.getPosition().get(position);
				switch (type) {
				case TYPE_ADDOFFICE:
					Intent intent2 = new Intent(getActivity(), AddOfficeActivity.class);
					getActivity().startActivityForResult(intent2, 3);
					break;
				case TYPE_DEPARTMENT:
					Intent intent = new Intent(getActivity(), AddDepartmentActivity.class);
					startActivityForResult(intent, 3);
					break;
				case TYPE_D_DETAILS: 
					mAdapter.removeDepartment(position);
					break;
				case TYPE_O_DETAILS: 
					mAdapter.removeOffice(position);
					break;
				}

			}
		});
	}

	public void getOfficeInformation(JSONObject json) {
		officeDataList = new ArrayList<OfficeData>();
		try {
			if (json.getString(KEY_SUCCESS) != null) {
				String res = json.getString(KEY_SUCCESS);
				String red = json.getString(KEY_ERROR);

				if(Integer.parseInt(res) == 1) {
					officeJson = json.getJSONArray("offices");
					for (int i = 0; i < officeJson.length(); i++) {
						JSONObject data = officeJson.getJSONObject(i);
						String officeType = data.getString("officeType");
						String isLeader = data.getString("isLeader");

						officeDataList.add(new OfficeData(officeType, isLeader));
					}
				} else if (Integer.parseInt(red) == 4){

				}
			} 
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void getDepartmentInformation(JSONObject json) {
		departmentDataList = new ArrayList<DepartmentData>();
		try {
			if (json.getString(KEY_SUCCESS) != null) {
				String res = json.getString(KEY_SUCCESS);
				String red = json.getString(KEY_ERROR);

				if(Integer.parseInt(res) == 1) {
					departmentJson = json.getJSONArray("organization");
					for (int i = 0; i < departmentJson.length(); i++) {
						JSONObject data = departmentJson.getJSONObject(i);
						String organization = data.getString("organization");
						String department = data.getString("department");

						departmentDataList.add(new DepartmentData(organization, department));
					}
				} else if (Integer.parseInt(red) == 4){

				}
			} 
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}


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

		public void addDepartmentDetails(final String group, final String department) {
			the_labels = new ArrayList<String>();
			the_labels.add(group);
			the_labels.add(department);
			mData.add(getAddDepartmentPosition(), the_labels);
			// save separator position
			the_position.add(getAddDepartmentPosition(), TYPE_D_DETAILS);
			notifyDataSetChanged();
		}

		public void addOfficeDetails(final String office, final String is_leader) {
			the_labels = new ArrayList<String>();
			the_labels.add(office);
			the_labels.add(is_leader);
			mData.add(getAddOfficePosition(), the_labels);
			// save separator position
			the_position.add(getAddOfficePosition(), TYPE_O_DETAILS);
			notifyDataSetChanged();
		}

		public int getAddOfficePosition() {
			return the_position.indexOf(TYPE_ADDOFFICE);
		}

		public int getAddDepartmentPosition() {
			return the_position.indexOf(TYPE_DEPARTMENT);
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

		public void removeDepartment(int position) {
			the_position.remove(position);
			group = mData.get(position).get(0); //gets the group
			//deleteDepartmentASync();
			DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());
			db.deleteDepartment(id, group);
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
		public boolean isEnabled(int position) {
			int type = getItemViewType(position);
			boolean enabled = false;
			switch (type) {
			case TYPE_HEADER:
				enabled = false;
				break;
			case TYPE_ADDOFFICE:
				enabled = true;
				break;
			case TYPE_DEPARTMENT:
				enabled = true;
				break;
			case TYPE_D_DETAILS:
				enabled = true;
				break;
			case TYPE_O_DETAILS:
				enabled = true;
				break;
			case TYPE_CATEGORY:
				enabled = false;
				break;
			}
			return enabled;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			int type = getItemViewType(position);
			//System.out.println("getView " + position + " " + convertView + " type = " + type);
			if (convertView == null) {
				holder = new ViewHolder();
				switch (type) {
				case TYPE_HEADER:
					convertView = mInflater.inflate(R.layout.header_row, null);
					holder.textView = (TextView)convertView.findViewById(R.id.acc_header);
					break;
				case TYPE_ADDOFFICE:
					convertView = mInflater.inflate(R.layout.add_row, null);
					holder.textView = (TextView)convertView.findViewById(R.id.add);
					break;
				case TYPE_DEPARTMENT:
					convertView = mInflater.inflate(R.layout.add_row, null);
					holder.textView = (TextView)convertView.findViewById(R.id.add);
					break;
				case TYPE_D_DETAILS:
					convertView = mInflater.inflate(R.layout.department_row, null);
					holder.textView = (TextView)convertView.findViewById(R.id.group);
					holder.textView2 = (TextView)convertView.findViewById(R.id.department);
					break;
				case TYPE_O_DETAILS:
					convertView = mInflater.inflate(R.layout.office_row, null);
					holder.textView = (TextView)convertView.findViewById(R.id.office);
					holder.textView2 = (TextView)convertView.findViewById(R.id.is_leader);
					break;
				case TYPE_CATEGORY:
					convertView = mInflater.inflate(R.layout.category_row, null);
					holder.textView = (TextView)convertView.findViewById(R.id.acc_text);
					holder.textView2 = (TextView)convertView.findViewById(R.id.acc_text2);
					break;
				}
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder)convertView.getTag();
			}
			switch (type) {
			case TYPE_HEADER:
				holder.textView.setText(mData.get(position).get(0));
				break;
			case TYPE_ADDOFFICE:
				holder.textView.setText(mData.get(position).get(0));
				break;
			case TYPE_DEPARTMENT:
				holder.textView.setText(mData.get(position).get(0));
				break;
			case TYPE_D_DETAILS:
				holder.textView.setText(mData.get(position).get(0));
				holder.textView2.setText(mData.get(position).get(1));
				break;
			case TYPE_O_DETAILS:
				holder.textView.setText(mData.get(position).get(0));
				holder.textView2.setText(mData.get(position).get(1));
				break;
			case TYPE_CATEGORY:
				holder.textView.setText(mData.get(position).get(0));
				holder.textView2.setText(mData.get(position).get(1));
				break;
			}

			return convertView;
		}

	}

	public static class ViewHolder {
		public TextView textView;
		public TextView textView2;
	}



	private class GetOffices extends AsyncTask<String, String, JSONObject> {
		String id;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			id = ((MainActivity) getActivity()).getUser().get("id");
		}
		@Override
		protected JSONObject doInBackground(String... args) {
			UserFunctions userFunction = new UserFunctions();
			JSONObject json = userFunction.getOffices(id);
			getOfficeInformation(json);
			return json;
		}
		@Override
		protected void onPostExecute(JSONObject json) {
			try {
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);
					String red = json.getString(KEY_ERROR);
					if(Integer.parseInt(res) == 1) {
						for (int i = 0; i < officeDataList.size(); i++) {
							String officeType = officeDataList.get(i).getOfficeType();
							String isLeader = officeDataList.get(i).getIsLeader();
							mAdapter.addOfficeDetails(officeType, isLeader);
						}

						Toast.makeText(getActivity().getApplicationContext(),
								"Successfully retrieved offices", Toast.LENGTH_SHORT).show();

					} else if (Integer.parseInt(red) == 4){
						Toast.makeText(getActivity().getApplicationContext(),
								"You have no offices", Toast.LENGTH_SHORT).show();
					} 
				} else {
					Toast.makeText(getActivity().getApplicationContext(),
							"Error occurred retrieving office", Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}}

	private class GetDepartment extends AsyncTask<String, String, JSONObject> {
		String id;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			id = ((MainActivity) getActivity()).getUser().get("id");
		}
		@Override
		protected JSONObject doInBackground(String... args) {
			UserFunctions userFunction = new UserFunctions();
			JSONObject json = userFunction.getDepartment(id);
			getDepartmentInformation(json);
			return json;
		}
		@Override
		protected void onPostExecute(JSONObject json) {
			try {
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);
					String red = json.getString(KEY_ERROR);

					if(Integer.parseInt(res) == 1) {
						for (int i = 0; i < departmentDataList.size(); i++) {
							String organization = departmentDataList.get(i).getOrganization();
							String department = departmentDataList.get(i).getDepartment();
							mAdapter.addDepartmentDetails(organization, department);
						}

						Toast.makeText(getActivity().getApplicationContext(),
								"Successfully retrieved organizations", Toast.LENGTH_SHORT).show();

					} else if (Integer.parseInt(red) == 4){
						Toast.makeText(getActivity().getApplicationContext(),
								"You have no organization", Toast.LENGTH_SHORT).show();
					} 
				} else {
					Toast.makeText(getActivity().getApplicationContext(),
							"Error occurred retrieving office", Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}}
	

	private class StoreOffice extends AsyncTask<String, String, JSONObject> {
		String id;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			id = ((MainActivity) getActivity()).getUser().get("id");
		}
		@Override
		protected JSONObject doInBackground(String... args) {
			UserFunctions userFunction = new UserFunctions();
			JSONObject json = userFunction.storeOffice(id, office, isLeader);
			return json;
		}
		@Override
		protected void onPostExecute(JSONObject json) {
			try {
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);
					String red = json.getString(KEY_ERROR);

					if(Integer.parseInt(res) == 1) {
						//DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());
						//db.addOffice(id, office, isLeader);
						mAdapter.addOfficeDetails(office, isLeader);

						Toast.makeText(getActivity().getApplicationContext(),
								"Successfully added office", Toast.LENGTH_SHORT).show();

					} else if (Integer.parseInt(red) == 2){
						Toast.makeText(getActivity().getApplicationContext(),
								"You are already that officer", Toast.LENGTH_SHORT).show();
					} else if (Integer.parseInt(red) == 3){
						Toast.makeText(getActivity().getApplicationContext(),
								"JSON error", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getActivity().getApplicationContext(),
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
			id = ((MainActivity) getActivity()).getUser().get("id");
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
						mAdapter.addDepartmentDetails(group, department);

						Toast.makeText(getActivity().getApplicationContext(),
								"Successfully added organization/department", Toast.LENGTH_SHORT).show();

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

	private class DeleteOffice extends AsyncTask<String, String, JSONObject> {
		String id;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			id = ((MainActivity) getActivity()).getUser().get("id");
		}
		@Override
		protected JSONObject doInBackground(String... args) {
			UserFunctions userFunction = new UserFunctions();
			JSONObject json = userFunction.deleteOffice(id, office);
			return json;
		}
		@Override
		protected void onPostExecute(JSONObject json) {

			try {
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);

					if(Integer.parseInt(res) == 1) {

						Toast.makeText(getActivity().getApplicationContext(),
								"Successfully removed office", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getActivity().getApplicationContext(),
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
			id = ((MainActivity) getActivity()).getUser().get("id");
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
						Toast.makeText(getActivity().getApplicationContext(),
								"Successfully removed organization", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getActivity().getApplicationContext(),
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
