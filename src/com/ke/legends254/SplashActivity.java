package com.ke.legends254;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ke.legends254.util.Factory;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class SplashActivity extends Activity {
	Factory factory;
	Context context;
	Boolean loggedin;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Instantiate main helper class
		factory = new Factory(this);
		
		//Instantiate Context
		context = this.getApplicationContext();
		
		//Create images directory
		if ( factory.directoryExists(context, "Legends254") == false){
			factory.getAlbumStorageDir( this, "Legends254");
		}
		
		//Hide actionbar
		factory.hideActionbar(this);
		
		//Load View
		setContentView(R.layout.splash);
		
		//check if logged in
		loggedin=factory.isLoggedin();
	    
		//Hide buttons if logged in
    	if(loggedin==true){
    		factory.LoggedinUIHelper(this);
    	}
		
		//Show advertisement from file
		Bitmap bmp = BitmapFactory.decodeFile(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) +  "/Legends254/advertisement.jpg");
		final ImageView splashImage = (ImageView) findViewById(R.id.animate);
		splashImage.setImageBitmap(bmp);
		
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
            	Intent i = new Intent(SplashActivity.this, UserloginActivity.class);
            	startActivity(i);
            	overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });	
        
		//Check connection
		if ( factory.isOnline(context) == false ){
			Toast.makeText(getApplicationContext(), "Connection was lost, please make sure you are connected to a data network then try again", Toast.LENGTH_LONG ).show();
			return;
		}
		
		//After we have shown our advertisement $$ lets check if you are logged in
		if(loggedin==true){
	    	new CountDownTimer(30000, 1000) {
	    	     public void onFinish() {
	    	    	 Intent i = new Intent(SplashActivity.this, MainActivity.class);
	    		    	startActivity(i);
	    		    	overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	    	     }

				@Override
				public void onTick(long millisUntilFinished) {
					// TODO Auto-generated method stub
					
				}
	    	  }.start();
	    	
	    }
	    
        //Instantiate image loader
		factory.initImageLoader(this);
		
		//Create image loader instance
		ImageLoader imageLoader = ImageLoader.getInstance();
		
		//Get advertisement image URI to use on the slider
		File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "/Legends254/advertisement.jpg");
		
		//Put all image paths in strings ready to put to the slider
		String imageUri1 = "http://www.gstatic.com/webp/gallery/4.webp";
		String imageUri3 = Uri.fromFile(file).toString();
		
		//Create array of the bitmaps to use on the slider
		final ArrayList<Bitmap> bitmapArray = new ArrayList<Bitmap>();
			//First image
			imageLoader.loadImage(imageUri1, new SimpleImageLoadingListener() {
			    @Override
			    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage1) {
			    	//add to array when done loading async from given path
			    	bitmapArray.add(loadedImage1);
			    }
			});
			
			//Third image, loads from the sd card advertisement
			imageLoader.loadImage(imageUri3, new SimpleImageLoadingListener() {
			    @Override
			    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage3) {
			    	//add to array when done loading async from given path
			    	bitmapArray.add(loadedImage3);
			    	factory.animate(splashImage, bitmapArray, 0, true);
			    }
			});
		
	}
}