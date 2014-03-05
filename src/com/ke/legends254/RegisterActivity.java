package com.ke.legends254;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ke.legends254.util.Factory;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFacebookUtils.Permissions;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class RegisterActivity extends Activity {
	String name, username, password, confirm, email;
	EditText nameText, usernameText, passwordText, confirmText, emailText;
	Button register, facebook;
	Factory factory;
	Context context;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		factory = new Factory();

		//Instantiate Context
		context = this.getApplicationContext();
		
		//Hide actionbar
		factory.hideActionbar(this);
		//Load View
		setContentView(R.layout.register);
		
		//Check connection
		if ( factory.isOnline(context) == false ){
			Toast.makeText(getApplicationContext(), "Connection was lost, please make sure you are connected to a data network then try again", Toast.LENGTH_LONG ).show();
			return;
		}
		
		Parse.initialize(this, "makinbymGIvXisIUXyjYqKsFqB3xTGRlohnRKpDq", "GueDSIGPHPudH7Oun7MkifVaOFxpaRorLDLQ64gF");
		ParseFacebookUtils.initialize("599440406802394");
		
		//Cancel and go back
		final Button cancel = (Button) findViewById(R.id.cancel);
		cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent i = new Intent(RegisterActivity.this, SplashActivity.class);
            	startActivity(i);
            	overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
		
		//Facebook register
		facebook = (Button) findViewById(R.id.facebook);
		facebook.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
        		ParseFacebookUtils.logIn(Arrays.asList("email", Permissions.Friends.ABOUT_ME),RegisterActivity.this, new LogInCallback() {
        			@Override
	  				  public void done(ParseUser user, ParseException err) {
	  				    if (user == null) {
	  				      Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
	  				    } else if (user.isNew()) {
	  				      Log.d("MyApp", "User signed up and logged in through Facebook!");
	  				    } else {
	  				      Log.d("MyApp", "User logged in through Facebook!");
	  				    }
	  				  }
				});
        	}
        });
		
		//This is for normal registrations
		register = (Button) findViewById(R.id.signup);
		
		//On Click, validate input data then proceed to register the new user
        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	nameText = (EditText) findViewById(R.id.name);
        		name = nameText.getText().toString();
        		
        		usernameText = (EditText) findViewById(R.id.uname);
        		username = usernameText.getText().toString();
        		
        		passwordText = (EditText) findViewById(R.id.pwd);
        		password = passwordText.getText().toString();
        		
        		confirmText = (EditText) findViewById(R.id.confirm);
        		confirm = confirmText.getText().toString();
        		
        		emailText = (EditText) findViewById(R.id.email);
        		email = emailText.getText().toString();
        		
        		//Confirm that the name is not empty
        		if( name == null || name.isEmpty()   ){
        			Toast.makeText(getApplicationContext(), "Name cannot be empty, please try again",  Toast.LENGTH_LONG).show();
        			Log.e("Signup", "No name supplied.");
        			return;
        		}
        		
        		//Confirm that username is not empty
        		if( username == null || username.isEmpty() ){
        			Toast.makeText(getApplicationContext(), "Username cannot be empty, please try again",  Toast.LENGTH_LONG).show();
        			Log.e("Signup", "No username supplied.");
        			return;
        		}
        		
        		//Confirm that email is not empty
        		if( email == null || email.isEmpty() ){
        			Toast.makeText(getApplicationContext(), "Email cannot be empty, please try again",  Toast.LENGTH_LONG).show();
        			Log.e("Signup", "No email supplied.");
        			return;
        		}
        		
        		//Confirm that password is not empty
        		if( password == null || password.isEmpty() ){
        			Toast.makeText(getApplicationContext(), "Password cannot be empty, please try again",  Toast.LENGTH_LONG).show();
        			Log.e("Signup", "No password supplied.");
        			return;
        		}
        		
        		//Double check the email address if its real
        		if( isEmailValid(email) == false ){
        			Toast.makeText(getApplicationContext(), "Email does not seem to be valid, please try again",  Toast.LENGTH_LONG).show();
        			Log.e("Signup", "Email used does not appear to be real.");
        			return;
        		}
        		
        		//Confirm that the passwords match
        		if( !password.equals( confirm ) ){
        			Toast.makeText(getApplicationContext(), "Passwords do not seem to match, please try again",  Toast.LENGTH_LONG).show();
        			Log.e("Signup", "Password does not match.");
        			return;
        		}
        		
        		final ProgressDialog ringProgressDialog = ProgressDialog.show(RegisterActivity.this, "Please wait ...",	"Signing up...", true);
        		ringProgressDialog.setCancelable(false);
        		new Thread(new Runnable() {
        			@Override
        			public void run() {
        				try {
        					//PARSE NICENESS
        	        		//We have passed all checks now we can add our user to the cloud
        		        		ParseUser user = new ParseUser();
        		        		user.setUsername(username);
        		        		user.setPassword(password);
        		        		user.setEmail(email);
        		        		  
        		        		// other fields can be set just like with ParseObject
        		        		user.put("fullname", name);
        		        		  
        		        		user.signUpInBackground(new SignUpCallback() {
        		        		  public void done(ParseException e) {
        		        		    if (e == null) {
        		        		      // Hooray! Let them use the app now.
        		        		    	Log.d("Signup", "Success, we are now registered");
        		        		    } else {
        		        		      // Sign up didn't succeed. Look at the ParseException
        		        		      // to figure out what went wrong
        		        		      Log.e("Signup", e.toString());
        		        		    }
        		        		  }
        		        		});
        				} catch (Exception e) {
        					Log.e("Sign up", "Error generating dialogue."+e.toString());
        				}
        				ringProgressDialog.dismiss();
        			}
        		}).start();
            }
        });
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
	  ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
	}
	
	public static boolean isEmailValid(String email) {
	    boolean isValid = false;

	    String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
	    CharSequence inputStr = email;

	    Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(inputStr);
	    if (matcher.matches()) {
	        isValid = true;
	    }
	    return isValid;
	}
}