package com.ke.legends254.util;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ke.legends254.R;
import com.newrelic.agent.android.NewRelic;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class Factory{
	ConnectivityManager cm;
	
	 public Factory(Activity activity){
		super();
			if( activity != null ){
				//Initialize application monitor
				NewRelic.withApplicationToken("AA5f55a3c1bbea146f2fa4f808323078164a372288").start(activity.getApplication());
				
				//Initialize Parse
				Parse.initialize(activity, "makinbymGIvXisIUXyjYqKsFqB3xTGRlohnRKpDq", "GueDSIGPHPudH7Oun7MkifVaOFxpaRorLDLQ64gF");
			}
		}
	
	public boolean isOnline(Context context){
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		 
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
		
		return isConnected;
	}
	
	public boolean isLoggedin(){
		ParseUser currentUser = ParseUser.getCurrentUser();
    	if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
    		return true;
    	}else{
    		return false;
    	}
	}
	
	public void hideActionbar(Activity activity){
		if (Build.VERSION.SDK_INT < 16) {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else{
        	ActionBar actionBar = activity.getActionBar();
        	actionBar.hide();
        }
	}
	
	public boolean isDownloading(Context context) {
	    ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (FetchAdvertisement.class.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
	
	public boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
	public boolean directoryExists( Context context, String albumName) {
		File file = new File(context.getExternalFilesDir(
	            Environment.DIRECTORY_PICTURES), albumName);
		if (file.isDirectory()) {
			if (!file.exists()) {
			    return false;
			}
	       return true;
	    }
		
	    return false;
	}
	
	public File getAlbumStorageDir( Context context, String albumName) {
	    // Get the directory for the app's private pictures directory. 
	    File file = new File(context.getExternalFilesDir(
	            Environment.DIRECTORY_PICTURES), albumName);
	    if (!file.mkdirs()) {
	        Log.e("Error creating directory", "Directory not created");
	    }
	    return file;
	}
	
	public void LoggedinUIHelper(Context context){
		
	}
	
	public String getKeyHash(Activity activity){
    	String keyhash = "";
    	try {
            PackageInfo info = activity.getPackageManager().getPackageInfo(
                    "com.ke.legends254", 
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                keyhash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (NameNotFoundException e) {
        	Log.e( "NameNotFoundException on package info", e+"Error" );
        } catch (NoSuchAlgorithmException e) {
        	Log.e("NoSuchAlgorithmException on package info",  e+"Error" );
        }
    	
    	return keyhash;
    }
	
	public void dialogueLoader(Context context, String title, String message){
		Dialog dialog = new Dialog(context, R.style.cust_dialog);
		dialog.setContentView(R.layout.dialogue);
		
		ProgressBar spinner = new android.widget.ProgressBar(
                context,
                null,
                android.R.attr.progressBarStyle);

spinner.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);


		dialog.setTitle(title); 
		dialog.setCancelable(false);
		TextView text = (TextView) dialog.findViewById(R.id.message);
		text.setText(message);
		dialog.show();
	}
	
	public void initImageLoader(Context context){
		 // Create global configuration and initialize ImageLoader with this configuration
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
	        .memoryCacheExtraOptions(1200, 1000) // default = device screen dimensions
	        .discCacheExtraOptions(1200, 1000, CompressFormat.JPEG, 85, null)
	        .threadPoolSize(3) // default
	        .threadPriority(Thread.NORM_PRIORITY - 1) // default
	        .tasksProcessingOrder(QueueProcessingType.FIFO) // default
	        .denyCacheImageMultipleSizesInMemory()
	        .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
	        .memoryCacheSize(2 * 1024 * 1024)
	        .memoryCacheSizePercentage(13) // default
	        .discCacheSize(50 * 1024 * 1024)
	        .discCacheFileCount(100)
	        .discCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
	        .imageDownloader(new BaseImageDownloader(context)) // default
	        .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
	        .writeDebugLogs()
            .build();
        ImageLoader.getInstance().init(config);
	}
	
	public void animate(final ImageView imageView, final ArrayList<Bitmap> images, final int imageIndex, final boolean forever) {
	    int fadeInDuration = 1000; // Configure time values here
	    int timeBetween = 5000;
	    int fadeOutDuration = 1500;

	    imageView.setVisibility(View.INVISIBLE);    //Visible or invisible by default - this will apply when the animation ends
	   // imageView.setImageResource(images[imageIndex]);
	    imageView.setImageBitmap(images.get(imageIndex));

	    Animation fadeIn = new AlphaAnimation(0, 1);
	    fadeIn.setInterpolator(new DecelerateInterpolator()); // add this
	    fadeIn.setDuration(fadeInDuration);

	    Animation fadeOut = new AlphaAnimation(1, 0);
	    fadeOut.setInterpolator(new AccelerateInterpolator()); // and this
	    fadeOut.setStartOffset(fadeInDuration + timeBetween);
	    fadeOut.setDuration(fadeOutDuration);

	    AnimationSet animation = new AnimationSet(false); // change to false
	    animation.addAnimation(fadeIn);
	    animation.addAnimation(fadeOut);
	    animation.setRepeatCount(1);
	    imageView.setAnimation(animation);

	    animation.setAnimationListener(new AnimationListener() {
	        public void onAnimationEnd(Animation animation) {
	            if (images.size() - 1 > imageIndex) {
	                animate(imageView, images, imageIndex + 1,forever); //Calls itself until it gets to the end of the array
	            }
	            else {
	                if (forever == true){
	                animate(imageView, images, 0,forever);  //Calls itself to start the animation all over again in a loop if forever = true
	                }
	            }
	        }
	        public void onAnimationRepeat(Animation animation) {
	            // TODO Auto-generated method stub
	        }
	        public void onAnimationStart(Animation animation) {
	            // TODO Auto-generated method stub
	        }
	    });
	}
	
}