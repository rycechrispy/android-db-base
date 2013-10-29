package com.bums.small;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EventDetails extends FragmentActivity {
	
	private View v;
	private TextView title;
	private TextView organization;
	private TextView department;
	private TextView location;
	private TextView date;
	private TextView time;
	private TextView description;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_details);
		
		title = (TextView) findViewById(R.id.event_name);
		organization = (TextView) findViewById(R.id.organization);
		department = (TextView) findViewById(R.id.department);
		location = (TextView) findViewById(R.id.location);
		date = (TextView) findViewById(R.id.date);
		time = (TextView) findViewById(R.id.time);
		description = (TextView) findViewById(R.id.description);
		
		Intent i = getIntent();
		EventData event = (EventData)i.getSerializableExtra("the_event");
		
		title.setText(event.getTitle());
		organization.setText(event.getOrganization());
		department.setText(event.getDepartment());
		location.setText(event.getLocation());
		if (event.getDateFrom().equals(event.getDateTo())) 
			date.setText(event.getRegularDateFrom());
		else 
			date.setText(event.getRegularDateFrom() + " to " + event.getRegularDateTo());
		
		if (event.getTimeFrom().equals(event.getTimeTo())) 
			time.setText(event.getRegularTimeFrom());
		else 
			time.setText(event.getRegularTimeFrom() + " to " + event.getRegularTimeTo());
		description.setText(event.getDescription());
		
		final Button back = (Button) findViewById(R.id.btn1);
		back.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}

}
