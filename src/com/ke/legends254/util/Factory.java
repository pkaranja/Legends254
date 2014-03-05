package com.ke.legends254.util;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

public class Factory{
	ConnectivityManager cm;
	
	public boolean isOnline(Context context){
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		 
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
		
		return isConnected;
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
	
	public void initImageLoader(Context context){
		 // Create global configuration and initialize ImageLoader with this configuration
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
	        .memoryCacheExtraOptions(1200, 1000) // default = device screen dimensions
	        .discCacheExtraOptions(1200, 1000, CompressFormat.JPEG, 75, null)
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