package com.ke.legends254;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONException;
import org.json.JSONObject;

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

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.ke.legends254.util.Factory;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFacebookUtils.Permissions;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

public class UserloginActivity extends Activity {
	String username, password;
	EditText usernameText, passwordText;
	Factory factory;
	Context context;
	Button login,fblogin,cancel;
	ProgressDialog progressDialog;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		factory = new Factory(this);
		
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
		
		//Initialize Parse
		Parse.initialize(this, "makinbymGIvXisIUXyjYqKsFqB3xTGRlohnRKpDq", "GueDSIGPHPudH7Oun7MkifVaOFxpaRorLDLQ64gF");
		ParseFacebookUtils.initialize("599440406802394");
		
		//You are already logged in
		ParseUser currentUser = ParseUser.getCurrentUser();
	    if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
	    	Intent i = new Intent(UserloginActivity.this, MainActivity.class);
	    	startActivity(i);
	    	overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	    }   
		
		// Fetch Facebook user info if the session is active
	    Session session = ParseFacebookUtils.getSession();
	    if (session != null && session.isOpened()) {
	        makeMeRequest();
	    }
	    
	   
		//Cancel and go back
		cancel = (Button) findViewById(R.id.cancel);
		cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent i = new Intent(UserloginActivity.this, SplashActivity.class);
            	startActivity(i);
            	overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
		
		//This is for facebook registrations
		fblogin = (Button) findViewById(R.id.facebook);
		
		//On Click, validate input data then proceed to register the new user
        fblogin.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		UserloginActivity.this.progressDialog = ProgressDialog.show(
        	       UserloginActivity.this, "", "Logging in...", true);
        		         	    
        	    factory.getKeyHash(UserloginActivity.this);
        	    
        	    ParseFacebookUtils.logIn(Arrays.asList("email", Permissions.Friends.ABOUT_ME), UserloginActivity.this, new LogInCallback() {
        	        @Override
        	        public void done(ParseUser user, ParseException err) {
        	        	if (user == null) {
        	                Toast.makeText(getApplicationContext(), "Login failed, please try again", Toast.LENGTH_LONG).show();
        	            } else if (user.isNew()) {
        	            	Intent i = new Intent(UserloginActivity.this, MainActivity.class);
        	            	startActivity(i);
        	            	overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        	            	UserloginActivity.this.progressDialog.dismiss();
        	            } else {
           	            	Intent i = new Intent(UserloginActivity.this, MainActivity.class);
        	            	startActivity(i);
        	            	overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        	            	UserloginActivity.this.progressDialog.dismiss();
        	            }
        	        }
        	    });
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
        		clearErrors();
        		
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
        		
        		factory.dialogueLoader(UserloginActivity.this, "Please wait", "We are logging you in...");
        		login(username, password);
            }
        });
	}
	
	private void login(String username, String password) {
		// TODO Auto-generated method stub
		ParseUser.logInInBackground(username, password, new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException e) {
				if(e == null)
					loginSuccessful();
				else
					loginUnSuccessful();
			}
		});

	}

	protected void loginSuccessful() {
		factory.dialogueLoader(this, "Loading data", "Please be patient...");
		Intent i = new Intent(UserloginActivity.this, MainActivity.class);
    	startActivity(i);
    	overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}
	
	protected void loginUnSuccessful() {
		factory.dialogueLoader(this, "Login Error", "Username and password not valid.");
	}

	private void clearErrors(){
		usernameText.setError(null);
		passwordText.setError(null);
	}
	
	 private void makeMeRequest() {
	        Request request = Request.newMeRequest(ParseFacebookUtils.getSession(),
	                new Request.GraphUserCallback() {
	                    @Override
	                    public void onCompleted(GraphUser user, Response response) {
	                        if (user != null) { 
	                            // Create a JSON object to hold the profile info
	                            JSONObject userProfile = new JSONObject();
	                            try {                   
	                                // Populate the JSON object 
	                                userProfile.put("facebookId", user.getId());
	                                userProfile.put("name", user.getName());
	                                if (user.getLocation().getProperty("name") != null) {
	                                    userProfile.put("location", (String) user
	                                            .getLocation().getProperty("name"));    
	                                }                           
	                                if (user.getProperty("gender") != null) {
	                                    userProfile.put("gender",       
	                                            (String) user.getProperty("gender"));   
	                                }                           
	                                if (user.getBirthday() != null) {
	                                    userProfile.put("birthday",     
	                                            user.getBirthday());                    
	                                }                           
	                                if (user.getProperty("relationship_status") != null) {
	                                    userProfile                     
	                                        .put("relationship_status",                 
	                                            (String) user                                           
	                                                .getProperty("relationship_status"));                               
	                                }                           
	                                // Now add the data to the UI elements
	                               /* ParseObject userData = new ParseObject("userdetails");
	                                userData.add("userid", ParseUser.getCurrentUser());
	                                userData.add("facebookid", user.getId());
	                                userData.add("altemail", user.getProperty("email").toString());
	                                userData.add("gender", user.getProperty("gender").toString());
	                                userData.add("fullname", user.getName());
                                	userData.add("dob", user.getBirthday().toString());

	                                ParseUser currentuser = ParseUser.getCurrentUser();
	                                ParseRelation<ParseObject> relation = currentuser.getRelation("userdetails");
	                                relation.add(userData);
	                                userData.saveEventually();
	                                */
	                            } catch (JSONException e) {
	                                Log.e("Facebook login","Error parsing returned user data.");
	                            }
	        
	                        } else if (response.getError() != null) {
	                            // handle error
	                        	Log.e("Facebook login Error","Error parsing returned user data."+response.getError());
	                        	
	                        }                  
	                    }               
	                });
	        request.executeAsync();
	     
	    }
		
}