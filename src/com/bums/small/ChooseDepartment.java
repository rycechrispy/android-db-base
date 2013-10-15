package com.bums.small;

import java.util.HashMap;

import com.bums.small.AccountFragment.MyCustomAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


public class ChooseDepartment extends Fragment {

	String[] departments;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = LayoutInflater.from(getActivity()).inflate(R.layout.office,
				null);

		return v;
	}

	/** add radio buttons to the group */
	private void populateList(){
		departments = new String[10];
		// get reference to radio group in layout
		RadioGroup radiogroup = (RadioGroup) getActivity().findViewById(R.id.radiogroup);
		// layout params to use when adding each radio button
		LinearLayout.LayoutParams layoutParams = new RadioGroup.LayoutParams(
				RadioGroup.LayoutParams.WRAP_CONTENT,
				RadioGroup.LayoutParams.WRAP_CONTENT);
		String[] cfo = {"Buklod", "Kadiwa", "Binhi"};
		String[] ws = {"Overseer", "Choir", "SCAN-I", "Secretary", "Finance", "Leadership"};
		String[] los = {"Missionizer"};
		for (int i = 0; i < cfo.length; i++){
			RadioButton newRadioButton = new RadioButton(getActivity());
			newRadioButton.setText(cfo[i]);
			newRadioButton.setId(i);
			radiogroup.addView(newRadioButton, layoutParams);
			departments[i] = cfo[i];
		}

		for (int i = 0; i < ws.length; i++){
			RadioButton newRadioButton = new RadioButton(getActivity());
			newRadioButton.setText(ws[i]);
			newRadioButton.setId(i + cfo.length);
			radiogroup.addView(newRadioButton, layoutParams);
			departments[i + cfo.length] = ws[i];
		}

		for (int i = 0; i < los.length; i++){
			RadioButton newRadioButton = new RadioButton(getActivity());
			newRadioButton.setText(los[i]);
			newRadioButton.setId(i + cfo.length + ws.length);
			radiogroup.addView(newRadioButton, layoutParams);
			departments[i + cfo.length + ws.length] = los[i];
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		populateList();

		final Button back = (Button) getActivity().findViewById(R.id.btn1);
		back.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
				Fragment old_frag = getActivity().getSupportFragmentManager().findFragmentByTag("office");
				transaction.detach(old_frag);

				transaction.attach(((MainActivity) getActivity()).getAccountFragment());
				transaction.commit();
			}
		});

		final Button finish = (Button) getActivity().findViewById(R.id.btn2);
		finish.setText("Finish");
		finish.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				RadioGroup radiogroup = (RadioGroup) getActivity().findViewById(R.id.radiogroup);
				System.out.println("radio btn: " + radiogroup.getCheckedRadioButtonId());
				
				
//				AccountFragment a = (AccountFragment) getFragmentManager().findFragmentByTag("account");
//				a.getmAdapter().addDepartmentDetails(departments[radiogroup.getCheckedRadioButtonId()], "CFO");
//				a.setmAdapter(a.getmAdapter());
//				a.getmAdapter().notifyDataSetChanged();
//
//				((AccountFragment) ((MainActivity) getActivity()).getAccountFragment()).getmAdapter()
//				.addDepartmentDetails(departments[radiogroup.getCheckedRadioButtonId()], "CFO");
//				
//				((AccountFragment) ((MainActivity) getActivity()).getAccountFragment()).getmAdapter().notifyDataSetChanged();

				//((AccountFragment) ((MainActivity) getActivity()).getAccountFragment()).setmAdapter(((AccountFragment) ((MainActivity) getActivity()).getAccountFragment()).getmAdapter());
				//((MainActivity) getActivity()).setAccountFragment(af);
				
//				AccountFragment af = new AccountFragment();
//				MyCustomAdapter a = ((AccountFragment) ((MainActivity) getActivity()).getAccountFragment()).getmAdapter();
//				af.setmAdapter(a);
//				af.getmAdapter().addDepartmentDetails(departments[radiogroup.getCheckedRadioButtonId()], "CFO");
//				af.getmAdapter().notifyDataSetChanged();
//				((MainActivity) getActivity()).setAccountFragment(af);

//				AccountFragment af = ((AccountFragment) ((MainActivity) getActivity()).getAccountFragment());
				AccountFragment af = (AccountFragment) getFragmentManager().findFragmentByTag("account");
				MyCustomAdapter a = af.getmAdapter();
				a.addDepartmentDetails(departments[radiogroup.getCheckedRadioButtonId()], "CFO");
				a.notifyDataSetChanged();
				af.setmAdapter(a);
				
				((MainActivity) getActivity()).setAccountFragment(af);

				FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
				Fragment old_frag = getActivity().getSupportFragmentManager().findFragmentByTag("office");
				transaction.detach(old_frag);
				//transaction.replace(R.id.realtabcontent, new AccountFragment());
				//attaching the original af because its state changed after it was detached
				transaction.attach(af);
				transaction.commit();


			}
		});


	}
}
