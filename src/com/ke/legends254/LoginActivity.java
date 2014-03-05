package com.ke.legends254;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ke.legends254.util.Factory;

public class LoginActivity extends Activity {
	String username, password;
	EditText usernameText, passwordText;
	Factory factory;
	Context context;
	Button login;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		factory = new Factory();
		
		//Instantiate Context
		context = this.getApplicationContext();
		
		//Hide actionbar
		factory.hideActionbar(this);
		
		setContentView(R.layout.login);	
		
		//Check connection
		if ( factory.isOnline(context) == false ){
			Toast.makeText(getApplicationContext(), "Connection was lost, please make sure you are connected to a data network then try again", Toast.LENGTH_LONG ).show();
			return;
		}
		
		//Cancel and go back
		final Button cancel = (Button) findViewById(R.id.cancel);
		cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent i = new Intent(LoginActivity.this, SplashActivity.class);
            	startActivity(i);
            	overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
		
		//This is for normal registrations
		login = (Button) findViewById(R.id.login);
		
		//On Click, validate input data then proceed to register the new user
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	usernameText = (EditText) findViewById(R.id.uname);
        		username = usernameText.getText().toString();
        		
        		passwordText = (EditText) findViewById(R.id.pwd);
        		password = passwordText.getText().toString();
        		
        		//Confirm that username is not empty
        		if( username == null || username.isEmpty() ){
        			Toast.makeText(getApplicationContext(), "Username cannot be empty, please try again",  Toast.LENGTH_LONG).show();
        			Log.e("Login", "No username supplied.");
        			return;
        		}
        		
        		//Confirm that password is not empty
        		if( password == null || password.isEmpty() ){
        			Toast.makeText(getApplicationContext(), "Password cannot be empty, please try again",  Toast.LENGTH_LONG).show();
        			Log.e("Login", "No password supplied.");
        			return;
        		}
            }
        });
				
		
	}
}