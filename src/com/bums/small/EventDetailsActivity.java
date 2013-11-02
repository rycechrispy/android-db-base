package com.bums.small;

import java.util.Calendar;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract.Events;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class EventDetailsActivity extends FragmentActivity {

	private View v;
	private TextView title;
	private TextView organization;
	private TextView department;
	private TextView location;
	private TextView date;
	private TextView time;
	private TextView description;
	private EventData event;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_details);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		title = (TextView) findViewById(R.id.event_name);
		organization = (TextView) findViewById(R.id.organization);
		department = (TextView) findViewById(R.id.department);
		location = (TextView) findViewById(R.id.location);
		date = (TextView) findViewById(R.id.date);
		time = (TextView) findViewById(R.id.time);
		description = (TextView) findViewById(R.id.description);

		Intent i = getIntent();
		event = (EventData)i.getSerializableExtra("the_event");

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

		final Button calendar = (Button) findViewById(R.id.btn1);
		calendar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Calendar cal = Calendar.getInstance();              
				Intent intent = new Intent(Intent.ACTION_EDIT);
				intent.setType("vnd.android.cursor.item/event");
				intent.putExtra("beginTime", event.getBeginTime());
				intent.putExtra("endTime", event.getEndTime());
				intent.putExtra("title", event.getOrganization() + "- " + event.getTitle());
				intent.putExtra(Events.EVENT_LOCATION, event.getLocation());
				intent.putExtra(Events.DESCRIPTION, event.getDescription());
				startActivity(intent);
			}
		});

		final Button reminder = (Button) findViewById(R.id.btn2);
		reminder.setOnClickListener(new View.OnClickListener() {
			private Calendar mcurrentTime;

			public void onClick(View v) {
				mcurrentTime = Calendar.getInstance();

				DatePickerDialog datePicker = new DatePickerDialog(EventDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear,
							int dayOfMonth) {
						mcurrentTime.set(Calendar.YEAR, year);
						mcurrentTime.set(Calendar.MONTH, monthOfYear);
						mcurrentTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
					}
				}, mcurrentTime.get(Calendar.YEAR), mcurrentTime.get(Calendar.MONTH), mcurrentTime.get(Calendar.DAY_OF_MONTH));
				
				//now add the time
				int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
				int minute = mcurrentTime.get(Calendar.MINUTE);
				TimePickerDialog mTimePicker = new TimePickerDialog(EventDetailsActivity.this, new TimePickerDialog.OnTimeSetListener() {
					@Override
					public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
						mcurrentTime.set(Calendar.HOUR_OF_DAY, selectedHour);
						mcurrentTime.set(Calendar.MINUTE, selectedMinute);

						//create alarm notification
						createAlarm(mcurrentTime);
						Toast.makeText(getApplicationContext(),
								"Sucessfully added reminder", Toast.LENGTH_SHORT).show();
					}
				}, hour, minute, false);
				
				mTimePicker.setTitle("Select Time");
				mTimePicker.show();
				
				datePicker.show();
				datePicker.setTitle("Select Date");
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
	
	public void createAlarm(Calendar calendar) {
		AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		
		Intent intent = new Intent(this, Reciever.class);
		intent.putExtra("the_event", event);
		PendingIntent pIntent = PendingIntent.getBroadcast(this, EventAlarmService.UNIQUE_VALUE, intent, 0);
		am.set(AlarmManager.RTC, calendar.getTimeInMillis(), pIntent);
		EventAlarmService.UNIQUE_VALUE++;
	}

}
