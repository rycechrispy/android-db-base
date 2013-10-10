package com.bums.small;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bums.library.DatabaseHandler;
import com.bums.library.UserFunctions;

public class Login extends Activity {

	Button btnLogin;
	Button Btnregister;
	EditText inputUsername;
	EditText inputPassword;
	private TextView loginErrorMsg;
	/**
	 * Called when the activity is first created.
	 */
	private static String KEY_SUCCESS = "success";
	private static String KEY_ID = "id";
	private static String KEY_USERNAME = "username";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.login);

		inputUsername = (EditText) findViewById(R.id.username);
		inputPassword = (EditText) findViewById(R.id.password);
		Btnregister = (Button) findViewById(R.id.register);
		btnLogin = (Button) findViewById(R.id.login);

		Btnregister.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent myIntent = new Intent(view.getContext(), Register.class);
				startActivityForResult(myIntent, 0);
				//finish();
			}});

		/**
		 * Login button click event
		 * A Toast is set to alert when the Email and Password field is empty
		 **/
		btnLogin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {

				if (  ( !inputUsername.getText().toString().equals("")) && ( !inputPassword.getText().toString().equals("")) )
				{
					NetAsync(view);
				}
				else if ( ( !inputUsername.getText().toString().equals("")) )
				{
					Toast.makeText(getApplicationContext(),
							"Password field empty", Toast.LENGTH_SHORT).show();
				}
				else if ( ( !inputPassword.getText().toString().equals("")) )
				{
					Toast.makeText(getApplicationContext(),
							"Email field empty", Toast.LENGTH_SHORT).show();
				}
				else
				{
					Toast.makeText(getApplicationContext(),
							"Email and Password field are empty", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}


	/**
	 * Async Task to check whether internet connection is working.
	 **/

	private class NetCheck extends AsyncTask<String,String,Boolean>
	{
		private ProgressDialog nDialog;

		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			nDialog = new ProgressDialog(Login.this);
			nDialog.setTitle("Checking Network");
			nDialog.setMessage("Loading..");
			nDialog.setIndeterminate(false);
			nDialog.setCancelable(true);
			nDialog.show();
		}
		/**
		 * Gets current device state and checks for working internet connection by trying Google.
		 **/
		@Override
		protected Boolean doInBackground(String... args){
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
			if(th == true){
				nDialog.dismiss();
				new ProcessLogin().execute();
			}
			else{
				nDialog.dismiss();
				loginErrorMsg.setText("Error in Network Connection");
			}
		}
	}

	/**
	 * Async Task to get and send data to My Sql database through JSON respone.
	 **/
	private class ProcessLogin extends AsyncTask<String, String, JSONObject> {
		private ProgressDialog pDialog;

		String email,password;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			inputUsername = (EditText) findViewById(R.id.username);
			inputPassword = (EditText) findViewById(R.id.password);
			email = inputUsername.getText().toString();
			password = inputPassword.getText().toString();
			pDialog = new ProgressDialog(Login.this);
			pDialog.setTitle("Contacting Servers");
			pDialog.setMessage("Logging in ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... args) {
			UserFunctions userFunction = new UserFunctions();
			JSONObject json = userFunction.loginUser(email, password);
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			try {
				if (json.getString(KEY_SUCCESS) != null) {

					String res = json.getString(KEY_SUCCESS);

					if(Integer.parseInt(res) == 1){
						pDialog.setMessage("Loading User Space");
						pDialog.setTitle("Getting Data");
						DatabaseHandler db = new DatabaseHandler(getApplicationContext());
						JSONObject json_user = json.getJSONObject("user");
						/**
						 * Clear all previous data in SQlite database.
						 **/
						UserFunctions logout = new UserFunctions();
						logout.logoutUser(getApplicationContext());
						db.addUser(json_user.getString(KEY_ID),json_user.getString(KEY_USERNAME));
						/**
						 *If JSON array details are stored in SQlite it launches the User Panel.
						 **/
						Intent upanel = new Intent(getApplicationContext(), MainActivity.class);
						upanel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						pDialog.dismiss();
						startActivity(upanel);
						/**
						 * Close Login Screen
						 **/
						finish();
					} else{
						pDialog.dismiss();
						Toast.makeText(getApplicationContext(),
								"Invalid Username/Password.", Toast.LENGTH_SHORT).show();
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	public void NetAsync(View view){
		new NetCheck().execute();
	}
	
	@Override
	public void onBackPressed() {
	}
}