package com.javavirys.unihoroscope.sys;

import java.io.InputStream;
import java.net.URL;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class Utils {

	public Utils() {
		// TODO Auto-generated constructor stub
	}
	
	public static void setImageToImageView(final Activity activity,
			final ImageView imgView,
			final String url)
	{
		new Thread(new Runnable(){public void run(){
	        try {
	        	 InputStream is = (InputStream) new URL(url).openStream();
	             final Drawable d = Drawable.createFromStream(is, "src name");
	             
	             activity.runOnUiThread(new Runnable(){
	            	 public void run(){
	            		 imgView.setImageDrawable(d);
	            	 }
	             });
	             
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Error: " + e.toString());
			}
	        }}).start();
	}
	
}
