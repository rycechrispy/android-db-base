package com.bums.small;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
public class ChooseOffice extends FragmentActivity {

	private String[] offices = {"Minister", "Deacon", "Deaconess", "Overseer", "Choirmember", "Organist", "Secretary Officer", 
			"Finance Officer", "SCAN-I", "LOS Officer", "Buklod Officer", "Kadiwa Officer", "Binhi Officer", 
			"CWS Watcher", "CWS Teacher", "CWS Chairperson", "CWS Choirmember"};


	private boolean isLeader;

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
				final RadioGroup radiogroup = (RadioGroup) findViewById(R.id.radiogroup);

				AlertDialog.Builder builder = new AlertDialog.Builder(ChooseOffice.this);
				builder.setMessage("Are you a part of the leadership in this office?")
				.setPositiveButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						isLeader = false;
						dialog.dismiss();
						Intent returnIntent = new Intent();
						returnIntent.putExtra("office", offices[radiogroup.getCheckedRadioButtonId()]);
						returnIntent.putExtra("isLeader", isLeader);
						returnIntent.putExtra("choose", "choose_office");
						setResult(RESULT_OK,returnIntent);     
						finish();
					}
				})
				.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						isLeader = true;
						dialog.dismiss();
						Intent returnIntent = new Intent();
						returnIntent.putExtra("office", offices[radiogroup.getCheckedRadioButtonId()]);
						returnIntent.putExtra("isLeader", isLeader);
						returnIntent.putExtra("choose", "choose_office");
						setResult(RESULT_OK,returnIntent);     
						finish();
					}
				});
				builder.create().show();
				// Create the AlertDialog object and return it


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

	/** add radio buttons to the group */
	private void populateList(){

		// get reference to radio group in layout
		RadioGroup radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
		// layout params to use when adding each radio button
		LinearLayout.LayoutParams layoutParams = new RadioGroup.LayoutParams(
				RadioGroup.LayoutParams.WRAP_CONTENT,
				RadioGroup.LayoutParams.WRAP_CONTENT);

		for (int i = 0; i < offices.length; i++){
			RadioButton newRadioButton = new RadioButton(this);
			newRadioButton.setText(offices[i]);
			newRadioButton.setId(i);
			radiogroup.addView(newRadioButton, layoutParams);
		}
	}
}