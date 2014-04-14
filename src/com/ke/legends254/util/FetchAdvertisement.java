package com.ke.legends254.util;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.Toast;

public class FetchAdvertisement extends IntentService {
	Factory factory;
	Context context;
	
	  public FetchAdvertisement() {
	    super("FetchAdvertisement");
	    context = this;
	  }
	  
	  // will be called asynchronously
	  @Override
	  protected void onHandleIntent(Intent intent) {
		  factory = new Factory(null);		  
		  if( factory.isExternalStorageWritable() == false){
        	//Cant write to external storage end there
			 Toast.makeText(getApplicationContext(), "Cant right", Toast.LENGTH_SHORT).show();
        	return;
		  }
		  
		  try
          {
			  Toast.makeText(getApplicationContext(), "Downloading Started", Toast.LENGTH_SHORT).show();
              URL url = new URL("http://www.samsung-wallpapers.com/uploads/allimg/130527/1-13052F20G8.jpg");
              HttpURLConnection connection = (HttpURLConnection) url.openConnection();
              connection.setDoInput(true);
              connection.connect();
              InputStream input = connection.getInputStream();
              Bitmap myBitmap = BitmapFactory.decodeStream(input);

              String data1 = String.valueOf(String.format(Environment.getExternalStorageDirectory().getAbsolutePath() +  "/Legends254/advertisement.jpg"));

              FileOutputStream stream = new FileOutputStream(data1);

              ByteArrayOutputStream outstream = new ByteArrayOutputStream();
              myBitmap.compress(Bitmap.CompressFormat.JPEG, 85, outstream);
              byte[] byteArray = outstream.toByteArray();

              stream.write(byteArray);
              stream.close();

              Toast.makeText(getApplicationContext(), "Downloading Completed", Toast.LENGTH_SHORT).show();
          }
          catch (Exception e)
          {
              e.printStackTrace();
          }
		} 
}