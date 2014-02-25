package com.ke.legends254;

import com.ke.legends254.util.Factory;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

public class LoginActivity extends Activity {
	Factory factory;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Hide the status bar
		if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else{
        	ActionBar actionBar = getActionBar();
        	actionBar.hide();
        }
		
		//Check connection
		if ( factory.isOnline() == false ){
			Toast.makeText(getApplicationContext(), "Connection was lost, please make sure you are connected to a data network then try again", Toast.LENGTH_LONG ).show();
			return;
		}
		setContentView(R.layout.login);	
		
	}
}