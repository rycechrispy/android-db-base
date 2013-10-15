package com.bums.small;

import java.util.ArrayList;
import java.util.TreeSet;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AccountFragment extends ListFragment {
	
	private static final int TYPE_HEADER = 0;
	private static final int TYPE_CATEGORY = 1;
	private static final int TYPE_ADDOFFICE = 2;
	private static final int TYPE_DEPARTMENT = 3;
	private static final int TYPE_D_DETAILS = 4;
	private static final int TYPE_MAX_COUNT = 5; //amount of types
	private ArrayList<Integer> the_position = new ArrayList<Integer>();

	private MyCustomAdapter mAdapter;
	private Context context; 

	public AccountFragment() {
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
		View v = inflater.inflate(R.layout.account_list, container, false);
		context = inflater.getContext();
		mAdapter = new MyCustomAdapter();
		mAdapter.addHeader("General");
		mAdapter.addCategoryInfo("Username: ", ((MainActivity) getActivity()).getUser().get("username"));
		mAdapter.addCategoryInfo("Password: ", ((MainActivity) getActivity()).getUser().get("password"));
		mAdapter.addHeader("Offices");
		mAdapter.addOffice("Add Office");
		mAdapter.addHeader("Organization and Department"); //organization = kad, choir, over - department = cfo, los, ws
		mAdapter.addDepartment("Add Organization and Department");
		//mAdapter.addDepartmentDetails("Binhi", "CFO");
        setListAdapter(mAdapter);
        
//		text = (TextView) v.findViewById(R.id.text);
//		if (getArguments() != null) {
//			//
//			try {
//				String value = getArguments().getString("key");
//				text.setText("Current Tab is: " + value + "\n" + ((MainActivity) getActivity()).getUser().get("username"));
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		return v;
	}

	public MyCustomAdapter getmAdapter() {
		return mAdapter;
	}

	public void setmAdapter(MyCustomAdapter mAdapter) {
		setListAdapter(mAdapter);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				int type = the_position.get(position);
				FragmentTransaction transaction;
				Fragment current;
				switch (type) {
					case TYPE_ADDOFFICE:
						// Create new fragment and transaction
						ChooseOffice newFragment = new ChooseOffice();
						transaction = getFragmentManager().beginTransaction();
						// Replace whatever is in the fragment_container view with this fragment,
						// and add the transaction to the back stack
						current = getFragmentManager().findFragmentByTag("account");
						((MainActivity) getActivity()).setAccountFragment(current);
						transaction.detach(current);
						transaction.replace(R.id.realtabcontent, newFragment, "office");

						// Commit the transaction
						transaction.commit();
					break;
					
					case TYPE_DEPARTMENT:
						// Create new fragment and transaction
						ChooseDepartment departFragment = new ChooseDepartment();
						transaction = getFragmentManager().beginTransaction();
						// Replace whatever is in the fragment_container view with this fragment,
						// and add the transaction to the back stack
						
						current = getFragmentManager().findFragmentByTag("account");
						((MainActivity) getActivity()).setAccountFragment(current);
						transaction.detach(current);
						//transaction.replace(((ViewGroup)(getView().getParent())).getId(), departFragment, "office");
						transaction.replace(R.id.realtabcontent, departFragment, "office");
						// Commit the transaction
						transaction.commit();
					break;
				}
			}
		});
	}

	public class MyCustomAdapter extends BaseAdapter {
		private ArrayList<ArrayList<String>> mData = new ArrayList<ArrayList<String>>();
		private LayoutInflater mInflater;
		private ArrayList<String> the_labels;
		private int officeLast;
		private int departLast;

		private TreeSet mSeparatorsSet = new TreeSet();

		public MyCustomAdapter() {
			mInflater = (LayoutInflater)((Context) context).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
			mData.add(the_labels);
			// save separator position
			the_position.add(TYPE_D_DETAILS);
			notifyDataSetChanged();
		}
		
		public void addOfficeDetails(final String office, final String is_leader) {
			the_labels = new ArrayList<String>();
			the_labels.add(office);
			the_labels.add(is_leader);
			mData.add(the_labels);
			// save separator position
			the_position.add(TYPE_ADDOFFICE);
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
			System.out.println("getView " + position + " " + convertView + " type = " + type);
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
					//holder.textView2 = (TextView)convertView.findViewById(R.id.acc_text2);
					break;
				case TYPE_DEPARTMENT:
					convertView = mInflater.inflate(R.layout.add_row, null);
					holder.textView = (TextView)convertView.findViewById(R.id.add);
					//holder.textView2 = (TextView)convertView.findViewById(R.id.acc_text2);
					break;
				case TYPE_D_DETAILS:
					convertView = mInflater.inflate(R.layout.department_row, null);
					holder.textView = (TextView)convertView.findViewById(R.id.group);
					holder.textView2 = (TextView)convertView.findViewById(R.id.department);
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

}
