package com.ke.legends254.util;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class AdvertisementReceiver extends BroadcastReceiver {
	Factory factory;
	Context c;
    @Override
    public void onReceive(Context context, Intent intent) {
    	factory = new Factory(null);
    	c = context;
    	Intent serv = new Intent(context, FetchAdvertisement.class);
    	
    	//Check connection
		if ( factory.isOnline(context) == false ){
			return;
		}
		
		if ( factory.isDownloading(context) == true ){
			context.stopService(serv); 
		}
		
		
		factory.initImageLoader(context);
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.loadImage("http://www.samsung-wallpapers.com/uploads/allimg/130527/1-13052F20G8.jpg", new SimpleImageLoadingListener() {
		    @Override
		    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
		    	String filepath = c.getExternalFilesDir(Environment.DIRECTORY_PICTURES) +  "/Legends254/advertisement.jpg";
	              FileOutputStream stream;
				try {
					stream = new FileOutputStream(filepath);
					 ByteArrayOutputStream outstream = new ByteArrayOutputStream();
		              loadedImage.compress(Bitmap.CompressFormat.JPEG, 85, outstream);
		              byte[] byteArray = outstream.toByteArray();

		              stream.write(byteArray);
		              stream.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		});
 	}
}