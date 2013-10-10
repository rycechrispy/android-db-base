package com.bums.small;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bums.library.DatabaseHandler;
import com.bums.library.UserFunctions;

public class Main extends Activity {
	Button btnLogout;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		btnLogout = (Button) findViewById(R.id.logout);

		DatabaseHandler db = new DatabaseHandler(getApplicationContext());

		/**
		 * Hashmap to load data from the Sqlite database
		 **/
		HashMap<String,String> user = new HashMap<String, String>();
		user = db.getUserDetails();



		/**
		 *Logout from the User Panel which clears the data in Sqlite database
		 **/
		btnLogout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				UserFunctions logout = new UserFunctions();
				logout.logoutUser(getApplicationContext());
				Intent login = new Intent(getApplicationContext(), Login.class);
				login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(login);
				finish();
			}
		});
		/**
		 * Sets user first name and last name in text view.
		 **/
		final TextView login = (TextView) findViewById(R.id.textwelcome);
		login.setText("Welcome  "+user.get("username"));
	}}
