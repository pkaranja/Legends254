package com.ke.legends254;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ke.legends254.util.Factory;
import com.newrelic.agent.android.NewRelic;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.parse.Parse;
import com.parse.PushService;

public class SplashActivity extends Activity {
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
		setContentView(R.layout.splash);
		
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
        
		//Check connection
		if ( factory.isOnline(context) == false ){
			Toast.makeText(getApplicationContext(), "Connection was lost, please make sure you are connected to a data network then try again", Toast.LENGTH_LONG ).show();
			return;
		}
		
		//Initialize application monitor
		NewRelic.withApplicationToken("AA5f55a3c1bbea146f2fa4f808323078164a372288").start(this.getApplication());
		
		//Initialize Parse
		Parse.initialize(this, "makinbymGIvXisIUXyjYqKsFqB3xTGRlohnRKpDq", "GueDSIGPHPudH7Oun7MkifVaOFxpaRorLDLQ64gF");
		PushService.setDefaultPushCallback(this, SplashActivity.class);
		
        //fetch image from backend
		factory.initImageLoader(this);
		ImageLoader imageLoader = ImageLoader.getInstance();
		
		// Load image, decode it to Bitmap and return Bitmap to callback
		String imageUri1 = "http://www.gstatic.com/webp/gallery/4.webp";
		String imageUri2 = "http://www.gstatic.com/webp/gallery/3.webp";
		String imageUri3 = "http://www.gstatic.com/webp/gallery/5.webp";
		
		final ArrayList<Bitmap> bitmapArray = new ArrayList<Bitmap>();
		
		 final ImageView demoImage = (ImageView) findViewById(R.id.animate);
		 
		imageLoader.loadImage(imageUri1, new SimpleImageLoadingListener() {
		    @Override
		    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage1) {
		    	//done
		    	bitmapArray.add(loadedImage1);
		    }
		});
		
		imageLoader.loadImage(imageUri2, new SimpleImageLoadingListener() {
		    @Override
		    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage2) {
		    	//done
		    	bitmapArray.add(loadedImage2);
		    }
		});
		imageLoader.loadImage(imageUri3, new SimpleImageLoadingListener() {
		    @Override
		    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage3) {
		    	//done
		    	bitmapArray.add(loadedImage3);
		    	factory.animate(demoImage, bitmapArray, 0, true);
		    }
		});
	}
}