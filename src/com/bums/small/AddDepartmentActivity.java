package com.bums.small;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class AddDepartmentActivity extends FragmentActivity {

	String[] departments;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.office);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		populateList();

		final Button back = (Button) findViewById(R.id.btn1);
		back.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

		final Button finish = (Button) findViewById(R.id.btn2);
		finish.setText("Finish");
		finish.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				RadioGroup radiogroup = (RadioGroup) findViewById(R.id.radiogroup);

				Intent returnIntent = new Intent();
				returnIntent.putExtra("group", departments[radiogroup.getCheckedRadioButtonId()]);
				returnIntent.putExtra("department", getDepartment(departments[radiogroup.getCheckedRadioButtonId()]));
				returnIntent.putExtra("choose", "choose_department");
				setResult(RESULT_OK,returnIntent);     
				finish();
			}
		});

	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
        	finish();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	
	private String getDepartment(String group) {
		String theGroup = "";
		if (group.equals("Buklod") || group.equals("Kadiwa") || group.equals("Binhi")) 
			theGroup = "Christian Family Organization"; 
		else if (group.equals("Overseer") || group.equals("Choir") || group.equals("Finance") 
				|| group.equals("SCAN-I") || group.equals("Secretary") || group.equals("Leadership")) 
			theGroup = "Worship Service";
		else if (group.equals("Missionizer"))
			theGroup = "Light of Salvation";
		
		return theGroup;
	}

	/** add radio buttons to the group */
	private void populateList(){
		departments = new String[10];
		// get reference to radio group in layout
		RadioGroup radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
		// layout params to use when adding each radio button
		LinearLayout.LayoutParams layoutParams = new RadioGroup.LayoutParams(
				RadioGroup.LayoutParams.WRAP_CONTENT,
				RadioGroup.LayoutParams.WRAP_CONTENT);
		String[] cfo = {"Buklod", "Kadiwa", "Binhi"};
		String[] ws = {"Overseer", "Choir", "SCAN-I", "Secretary", "Finance", "Leadership"};
		String[] los = {"Missionizer"};
		for (int i = 0; i < cfo.length; i++){
			RadioButton newRadioButton = new RadioButton(this);
			newRadioButton.setText(cfo[i]);
			newRadioButton.setId(i);
			radiogroup.addView(newRadioButton, layoutParams);
			departments[i] = cfo[i];
		}

		for (int i = 0; i < ws.length; i++){
			RadioButton newRadioButton = new RadioButton(this);
			newRadioButton.setText(ws[i]);
			newRadioButton.setId(i + cfo.length);
			radiogroup.addView(newRadioButton, layoutParams);
			departments[i + cfo.length] = ws[i];
		}

		for (int i = 0; i < los.length; i++){
			RadioButton newRadioButton = new RadioButton(this);
			newRadioButton.setText(los[i]);
			newRadioButton.setId(i + cfo.length + ws.length);
			radiogroup.addView(newRadioButton, layoutParams);
			departments[i + cfo.length + ws.length] = los[i];
		}
	}
}