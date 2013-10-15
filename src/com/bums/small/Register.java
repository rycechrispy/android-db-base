package com.bums.small;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bums.library.DatabaseHandler;
import com.bums.library.UserFunctions;

public class Register extends Activity {

	/**
	 *  JSON Response node names.
	 **/
	private static String KEY_SUCCESS = "success";
	private static String KEY_ID = "id";
	private static String KEY_USERNAME = "username";
	private static String KEY_PASSWORD = "password";
	private static String KEY_ERROR = "error";

	/**
	 * Defining layout items.
	 **/
	EditText inputUsername;
	EditText inputPassword;
	Button btnRegister;
	
	private ProgressDialog nDialog;
	private AlertDialog.Builder builder;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		/**
		 * Defining all layout items
		 **/
		inputUsername = (EditText) findViewById(R.id.username);
		inputPassword = (EditText) findViewById(R.id.password);
		btnRegister = (Button) findViewById(R.id.register);

		/**
		 * Button which Switches back to the login screen on clicked
		 **/

		Button login = (Button) findViewById(R.id.bktologin);
		login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent myIntent = new Intent(view.getContext(), Login.class);
				startActivityForResult(myIntent, 0);
				finish();
			}

		});

		/**
		 * Register Button click event.
		 * A Toast is set to alert when the fields are empty.
		 * Another toast is set to alert Username must be 5 characters.
		 **/

		btnRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if ((!inputUsername.getText().toString().equals("")) && (!inputPassword.getText().toString().equals("")))
				{
					NetAsync(view);
				}
				else {
					Toast.makeText(getApplicationContext(),
							"One or more fields are empty", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	public void createAlertMessage(final String title, String message) {
		builder.setTitle(title);
		builder.setMessage(message)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
				if (title.equals("Success")) {
					finish();
				}
			}
		});
		builder.create().show();
	}
	
	
	/**
	 * Async Task to check whether internet connection is working
	 **/
	private class NetCheck extends AsyncTask<String,String,Boolean> {

		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			nDialog = new ProgressDialog(Register.this);
			builder = new AlertDialog.Builder(Register.this);
			nDialog.setMessage("Loading..");
			nDialog.setTitle("Checking Network");
			nDialog.setIndeterminate(false);
			nDialog.setCancelable(true);
			nDialog.show();
		}

		@Override
		protected Boolean doInBackground(String... args){
			/**
			 * Gets current device state and checks for working internet connection by trying Google.
			 **/
			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getActiveNetworkInfo();
			if (netInfo != null && netInfo.isConnected()) {
				try {
					URL url = new URL("http://www.google.com");
					HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
					urlc.setConnectTimeout(3000);
					urlc.connect();
					if (urlc.getResponseCode() == 200) {
						return true;
					}
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return false;
		}
		
		@Override
		protected void onPostExecute(Boolean th){
			if(th == true) {
				nDialog.dismiss();
				new ProcessRegister().execute();
			} else{
				nDialog.dismiss();
				Toast.makeText(getApplicationContext(),
						"Network Error Connection", Toast.LENGTH_SHORT).show();
				createAlertMessage("Network Error Connection", "There was no internet connection, please try again.");
			}
		}
	}
	
	private class ProcessRegister extends AsyncTask<String, String, JSONObject> {
		String username, password;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			inputUsername = (EditText) findViewById(R.id.username);
			inputPassword = (EditText) findViewById(R.id.password);
			username = inputUsername.getText().toString();
			password = inputPassword.getText().toString();
			nDialog = new ProgressDialog(Register.this);
			builder = new AlertDialog.Builder(Register.this);
			nDialog.setTitle("Contacting Servers");
			nDialog.setMessage("Registering...");
			nDialog.setIndeterminate(false);
			nDialog.setCancelable(true);
			nDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... args) {
			UserFunctions userFunction = new UserFunctions();
			JSONObject json = userFunction.registerUser(username, password);
			return json;
		}
		
		@Override
		protected void onPostExecute(JSONObject json) {
			/**
			 * Checks for success message.
			 **/
			try {
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);
					String red = json.getString(KEY_ERROR);

					if(Integer.parseInt(res) == 1) {
						nDialog.setTitle("Getting Data");
						nDialog.setMessage("Loading Info");
						DatabaseHandler db = new DatabaseHandler(getApplicationContext());
						JSONObject json_user = json.getJSONObject("user");

						/**
						 * Removes all the previous data in the SQlite database
						 **/

						UserFunctions logout = new UserFunctions();
						logout.logoutUser(getApplicationContext());
						db.addUser(json_user.getString(KEY_ID),json_user.getString(KEY_USERNAME), json_user.getString(KEY_PASSWORD));
						
						nDialog.dismiss();
						
						createAlertMessage("Success", "You have successfully registered.");
						
						Toast.makeText(getApplicationContext(),
								"Successfully Registered.", Toast.LENGTH_SHORT).show();
						//close the register activity
						
					} else if (Integer.parseInt(red) == 2){
						nDialog.dismiss();
						Toast.makeText(getApplicationContext(),
								"User already exists!", Toast.LENGTH_SHORT).show();
					} else if (Integer.parseInt(red) == 3){
						nDialog.dismiss();
						Toast.makeText(getApplicationContext(),
								"Invalid username!", Toast.LENGTH_SHORT).show();
					}
				} else {
					nDialog.dismiss();
					Toast.makeText(getApplicationContext(),
							"Error occurred in registration", Toast.LENGTH_SHORT).show();
					createAlertMessage("Application Error", "There was an error that occurred in the registration process.");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}}
	
	public void NetAsync(View view){
		new NetCheck().execute();
	}
}