package com.bums.small;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TimePicker;
import android.widget.Toast;

public class EditEvent extends FragmentActivity implements OnItemSelectedListener {
	private Calendar myCalendar;

	private EditText title;
	private EditText location;
	private EditText description;
	private EditText dateFrom;
	private EditText dateTo;
	private EditText timeFrom;
	private EditText timeTo;
	private String organization;
	private String dateFromSQL;
	private String dateToSQL;
	private String timeFromSQL;
	private String timeToSQL;
	private String prevTitle;
	private int isDateFrom;

	private EventData event;

	private int position;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_edit);

		Spinner spinner = (Spinner) findViewById(R.id.dropdown);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				R.array.organization_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);

		title = (EditText) findViewById(R.id.event_name);
		location = (EditText) findViewById(R.id.event_location);
		description = (EditText) findViewById(R.id.event_description);
		dateFrom = (EditText) findViewById(R.id.date_from);
		dateTo = (EditText) findViewById(R.id.date_to);
		timeFrom = (EditText) findViewById(R.id.time_from);
		timeTo = (EditText) findViewById(R.id.time_to);
		
		//get the event that is clicked
		Intent i = getIntent();
		event = (EventData)i.getSerializableExtra("the_event");
		position = i.getIntExtra("position", 0);
		
		//setting the textfields to have the correct information
		title.setText(event.getTitle());
		setSpinner(spinner, event.getOrganization());
		location.setText(event.getLocation());
		dateFrom.setText(event.getRegularDateFrom());
		dateTo.setText(event.getRegularDateTo());
		timeFrom.setText(event.getRegularTimeFrom());
		timeTo.setText(event.getRegularTimeTo());
		description.setText(event.getDescription());
		
		//setting the previous fields that are empty without clicking
		dateFromSQL = event.getDateFrom();
		dateToSQL = event.getDateTo();
		timeFromSQL = event.getTimeFrom();
		timeToSQL = event.getTimeTo();
		organization = event.getOrganization();
		prevTitle = event.getTitle();

		timeFrom.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Calendar mcurrentTime = Calendar.getInstance();
				int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
				int minute = mcurrentTime.get(Calendar.MINUTE);
				TimePickerDialog mTimePicker;
				mTimePicker = new TimePickerDialog(EditEvent.this, new TimePickerDialog.OnTimeSetListener() {
					@Override
					public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
						if (selectedHour < 10) {
							if (selectedMinute < 10) {
								timeFromSQL = "0" + selectedHour + ":0" + selectedMinute + ":00";
							} else {
								timeFromSQL = "0" + selectedHour + ":" + selectedMinute + ":00";
							}
						} else {
							if (selectedMinute < 10) {
								timeFromSQL = selectedHour + ":0" + selectedMinute + ":00";
							} else {
								timeFromSQL = selectedHour + ":" + selectedMinute + ":00";
							}
						}
						
						String time = "am";
						if (selectedHour == 0) {
							selectedHour = 12;
							time = "am";
						} else if (selectedHour == 12) {
							time = "pm";
						} else if (selectedHour > 12) {
							selectedHour -= 12;
							time = "pm";
						}
						if (selectedMinute < 10) {
							timeFrom.setText( selectedHour + ":0" + selectedMinute + time);
						} else {
							timeFrom.setText( selectedHour + ":" + selectedMinute + time);
						}
					} 
				}, hour, minute, false);
				mTimePicker.setTitle("Select Time");
				mTimePicker.show();
			}
		});

		timeTo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Calendar mcurrentTime = Calendar.getInstance();
				int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
				int minute = mcurrentTime.get(Calendar.MINUTE);
				TimePickerDialog mTimePicker;
				mTimePicker = new TimePickerDialog(EditEvent.this, new TimePickerDialog.OnTimeSetListener() {
					@Override
					public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
						if (selectedHour < 10) {
							if (selectedMinute < 10) {
								timeToSQL = "0" + selectedHour + ":0" + selectedMinute + ":00";
							} else {
								timeToSQL = "0" + selectedHour + ":" + selectedMinute + ":00";
							}
						} else {
							if (selectedMinute < 10) {
								timeToSQL = selectedHour + ":0" + selectedMinute + ":00";
							} else {
								timeToSQL = selectedHour + ":" + selectedMinute + ":00";
							}
						}
						
						String time = "am";
						if (selectedHour == 0) {
							selectedHour = 12;
							time = "am";
						} else if (selectedHour == 12) {
							time = "pm";
						} else if (selectedHour > 12) {
							selectedHour -= 12;
							time = "pm";
						}
						if (selectedMinute < 10) {
							timeTo.setText( selectedHour + ":0" + selectedMinute + time);
						} else {
							timeTo.setText( selectedHour + ":" + selectedMinute + time);
						}
						
					}
				}, hour, minute, false);
				mTimePicker.setTitle("Select Time");
				mTimePicker.show();
			}
		});

		myCalendar = Calendar.getInstance();
		final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				myCalendar.set(Calendar.YEAR, year);
				myCalendar.set(Calendar.MONTH, monthOfYear);
				myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				if (isDateFrom == 0) {
					dateFromSQL = updateLabelSQL(dateFromSQL);
					updateLabel(dateFrom);
				} else {
					dateToSQL = updateLabelSQL(dateToSQL);
					updateLabel(dateTo);
				}
			}
		};
		dateFrom.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new DatePickerDialog(EditEvent.this, date, myCalendar
						.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
						myCalendar.get(Calendar.DAY_OF_MONTH)).show();
				isDateFrom = 0;
			}
		});

		dateTo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new DatePickerDialog(EditEvent.this, date, myCalendar
						.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
						myCalendar.get(Calendar.DAY_OF_MONTH)).show();
				isDateFrom = 1;
			}
		});
		
		final Button remove = (Button) findViewById(R.id.btn3);
		remove.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent returnIntent = new Intent();
				returnIntent.putExtra("title", event.getTitle());
				returnIntent.putExtra("update_or_delete", "delete");
				returnIntent.putExtra("position", position);
				setResult(RESULT_OK, returnIntent);
				finish();
			}
		});

		final Button back = (Button) findViewById(R.id.btn1);
		back.setText("Cancel");
		back.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

		final Button finish = (Button) findViewById(R.id.btn2);
		finish.setText("Update");
		finish.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (title.getText().toString().equals("")) {
					Toast.makeText(getApplicationContext(),
							"Please enter an event title", Toast.LENGTH_SHORT).show();
				} else if (location.getText().toString().equals("")) {
					Toast.makeText(getApplicationContext(),
							"Please enter a location", Toast.LENGTH_SHORT).show();
				} else if (dateFrom.getText().toString().equals("")) {
					Toast.makeText(getApplicationContext(),
							"Please enter the first field for the date", Toast.LENGTH_SHORT).show();
				} else if (timeFrom.getText().toString().equals("")) {
					Toast.makeText(getApplicationContext(),
							"Please enter the first field for the time", Toast.LENGTH_SHORT).show();
				} else {
					
					if (timeTo.getText().toString().equals("")) {
						timeToSQL = timeFromSQL;
					}
					if (dateTo.getText().toString().equals("")) {
						dateToSQL = dateFromSQL;
					}
					
					Intent returnIntent = new Intent();
					returnIntent.putExtra("position", position);
					returnIntent.putExtra("title", title.getText().toString());
					returnIntent.putExtra("location", location.getText().toString());
					returnIntent.putExtra("description", description.getText().toString());
					returnIntent.putExtra("date_from", dateFromSQL);
					returnIntent.putExtra("date_to", dateToSQL);
					returnIntent.putExtra("time_from", timeFromSQL);
					returnIntent.putExtra("time_to", timeToSQL);
					returnIntent.putExtra("organization", organization);
					returnIntent.putExtra("prevTitle", prevTitle);
					returnIntent.putExtra("update_or_delete", "update");
					setResult(RESULT_OK, returnIntent);
					finish();
				}
			}
		});
	}
	
	private void setSpinner(Spinner spnr, String value) {
	    SpinnerAdapter adapter = (SpinnerAdapter) spnr.getAdapter();
	    for (int position = 0; position < adapter.getCount(); position++) {
	        if(adapter.getItem(position).equals(value)) {
	            spnr.setSelection(position);
	            return;
	        }
	    }
	}
	
	private String updateLabelSQL(String date) {
		String myFormat = "yyyy-MM-dd"; //In which you need put here
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
		return sdf.format(myCalendar.getTime());
	}

	private void updateLabel(EditText date) {
		String myFormat = "MM-dd-yy"; //In which you need put here
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
		date.setText(sdf.format(myCalendar.getTime()));
	}

	public void onItemSelected(AdapterView<?> parent, View view, 
			int pos, long id) {
		organization = parent.getItemAtPosition(pos).toString();
	}

	public void onNothingSelected(AdapterView<?> parent) {
		// Another interface callback
	}
}