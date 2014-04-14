package com.ke.legends254;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.ke.legends254.util.Factory;

public class MainActivity extends Activity {
	Factory factory;
	Context context;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		factory = new Factory(this);
		
		//Instantiate Context
		context = this.getApplicationContext();
		
		//Hide actionbar
		factory.hideActionbar(this);
		
		setContentView(R.layout.main);	
		
		//Check connection
		if ( factory.isOnline(context) == false ){
			Toast.makeText(getApplicationContext(), "Connection was lost, please make sure you are connected to a data network then try again", Toast.LENGTH_LONG ).show();
			return;
		}
	}
}