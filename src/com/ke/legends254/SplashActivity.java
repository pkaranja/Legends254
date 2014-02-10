package com.ke.legends254;

import android.app.Activity;
import android.os.Bundle;
import com.ke.legends254.util.*;

public class SplashActivity extends Activity {
	boolean isLoggedin = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		//Check if logged in
		if ( isLoggedin().execute().equal(true) )
		{
			//TODO:Proceed to fetch data from mysql async task
			//This guy is logged in
			//Pass to main intent after fetching all data
		}else{
			//TODO:Redirect to login Activity	
			//No data required
		}
	}
}
