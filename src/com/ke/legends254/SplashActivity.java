package com.ke.legends254;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.parse.Parse;
import com.parse.PushService;

import com.newrelic.agent.android.NewRelic;

public class SplashActivity extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Initialize application monitor
		NewRelic.withApplicationToken("AA5f55a3c1bbea146f2fa4f808323078164a372288").start(this.getApplication());
		
		//Hide the status bar
		if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else{
        	ActionBar actionBar = getActionBar();
        	actionBar.hide();
        }
		
		setContentView(R.layout.splash);
    	
		//Initialize Parse
		Parse.initialize(this, "makinbymGIvXisIUXyjYqKsFqB3xTGRlohnRKpDq", "GueDSIGPHPudH7Oun7MkifVaOFxpaRorLDLQ64gF");
		PushService.setDefaultPushCallback(this, SplashActivity.class);
		
		final Button signup = (Button) findViewById(R.id.signup);
		final Button signin = (Button) findViewById(R.id.signin);
		
		//Open and transition to the register activity
        signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent i = new Intent(SplashActivity.this, RegisterActivity.class);
            	startActivity(i);
            	overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        
        //Open and transition to the login activity
        signin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent i = new Intent(SplashActivity.this, LoginActivity.class);
            	startActivity(i);
            	overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });	
	}
}