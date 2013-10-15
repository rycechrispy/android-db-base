package com.bums.small;

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


public class ChooseOffice extends Fragment {

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

		// get reference to radio group in layout
		RadioGroup radiogroup = (RadioGroup) getActivity().findViewById(R.id.radiogroup);
		// layout params to use when adding each radio button
		LinearLayout.LayoutParams layoutParams = new RadioGroup.LayoutParams(
				RadioGroup.LayoutParams.WRAP_CONTENT,
				RadioGroup.LayoutParams.WRAP_CONTENT);
		
		String[] offices = {"Minister", "Deacon", "Deaconess", "Overseer", "Choirmember", "Organist", "Secretary Officer", 
				"Finance Officer", "SCAN-I", "LOS Officer", "Buklod Officer", "Kadiwa Officer", "Binhi Officer", 
				"CWS Watcher", "CWS Teacher", "CWS Chairperson", "CWS Choirmember"};
		for (int i = 0; i < offices.length; i++){
			RadioButton newRadioButton = new RadioButton(getActivity());
			newRadioButton.setText(offices[i]);
			newRadioButton.setId(i);
			radiogroup.addView(newRadioButton, layoutParams);
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
            	
            }
        });
	}
}
