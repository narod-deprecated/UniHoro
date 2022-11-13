package com.javavirys.unihoroscope.sys;

import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.javavirys.lib.net.Loader;
import com.javavirys.lib.net.Loader.LoaderCallback;
import com.javavirys.lib.net.Loader.LoaderException;
import com.javavirys.unihoroscope.ActivityView;
import com.javavirys.unihoroscope.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;
import net.frakbot.jumpingbeans.JumpingBeans;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Данный класс содержит все обработчики в приложения
 * @version 1.0
 * @author Виталий Сычёв
 *
 */

public class Listeners implements LoaderCallback,OnItemClickListener,OnClickListener{

	ProgressDialog progress;
	
	boolean errorFlag;
	
	public View oldMenuImagePtr = null;
	public JumpingBeans oldJumpingText = null;
	
	@Override
	public void onPreExecute(Loader sender) {
		// TODO Auto-generated method stub
		Context c = null;
		if(sender == GlobalVars.value_Loader)
			c = GlobalVars.viewer;
		else
			c = GlobalVars.main;
		showProgressDialog(c);
		errorFlag = false;
	}

	@Override
	public void onSuccessfulExecute(Loader sender, StringBuffer[] result) {
		// TODO Auto-generated method stub
		hideProgressDialog();
		
		if(errorFlag || result == null || result[0] == null)
		{
			Toast.makeText(GlobalVars.main, "Error loader!!!", Toast.LENGTH_LONG).show();
			return;
		}
		
			//System.out.println(result[0].toString());
		    try {
				String xml = new String(result[0].toString().getBytes("ISO-8859-1"),"UTF-8");
				xmlParsing(xml);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Listeners.onSuccessfulExecute().exception: " + e.toString());
				e.printStackTrace();
			}
	}

	@Override
	public void onErrorFoundExecute(Loader sender, int index, LoaderException ex) {
		// TODO Auto-generated method stub
		errorFlag = true;
	}

	@Override
	public void onProgressUpdate(Loader sender, int progress) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		ListItem item = GlobalVars.adapter_list.getItem(position);
		
		switch(GlobalVars.level)
		{
		case GlobalVars.LEVEL_TYPE_LOADER:
			String s = GlobalVars.type_Loader.paths.get(0);
			GlobalVars.main.stack.push(s);
			System.out.println("push stack: "+s);
			GlobalVars.level = GlobalVars.LEVEL_PERIOD_LOADER;
			try {
				GlobalVars.adapter_list.clear();
				for(int i = 0; i < item.list.size(); i++){
					GlobalVars.arrayList_items.add(item.list.get(i));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case GlobalVars.LEVEL_PERIOD_LOADER:
			GlobalVars.level = GlobalVars.LEVEL_VALUE_LOADER;
			Intent i = new Intent(GlobalVars.main,ActivityView.class);
			i.putExtra("url", item.url);
			GlobalVars.main.startActivity(i);
			break;
		case GlobalVars.LEVEL_VALUE_LOADER:
			break;
		}		
	}
	
	public void xmlParsing(final String xml) throws Exception
	{
		XmlPullParserFactory factory = null;
		
			factory = XmlPullParserFactory.newInstance();
			
	        XmlPullParser xpp = factory.newPullParser();
	        
	        xpp.setInput(new StringReader(xml));
	        
	        if(GlobalVars.level != GlobalVars.LEVEL_VALUE_LOADER)
	        	GlobalVars.adapter_list.clear();
	        
	        boolean foundTag = false;
	        boolean foundMoreTag = false;
	        String tagText = null;
	        ListItem item = null;
	        int eventType = xpp.getEventType();
	        while (eventType != XmlPullParser.END_DOCUMENT) {
	        	 if(eventType == XmlPullParser.START_DOCUMENT) {
	                 System.out.println("Start document");
	             } else if(eventType == XmlPullParser.START_TAG) {
	                 //System.out.println("Start tag "+xpp.getName());
	                 
	                 if(xpp.getName().equalsIgnoreCase("zodiac") && GlobalVars.level == GlobalVars.LEVEL_MAIN_LOADER)
	                 {
		                 String title = xpp.getAttributeValue(null, "name");
		                 String url = xpp.getAttributeValue(null, "path");
		                 String img_url = xpp.getAttributeValue(null,"img");
		                 if(title != null && url != null)
		                 {
		                	 addMenuItem(title, url,img_url);
		                 }
	                 }else if(xpp.getName().equalsIgnoreCase("type") && GlobalVars.level == GlobalVars.LEVEL_TYPE_LOADER)
	                 {
	                	 foundTag = true;
	                	 String title = xpp.getAttributeValue(null, "name");
	                	 
	                	 if(title != null)
	                	 {
	                		 item = new ListItem();
		                	 item.title = title;
		                	 item.list = new ArrayList<ListItem>();
	                	 }
	                	 
	                 }else if(xpp.getName().equalsIgnoreCase("period") && 
	                		 GlobalVars.level == GlobalVars.LEVEL_TYPE_LOADER && 
	                		 foundTag)
	                 {
	                	 ListItem subItem = new ListItem();
	                	 subItem.title = xpp.getAttributeValue(null, "name");
	                	 subItem.url = xpp.getAttributeValue(null, "path");
	                	 item.list.add(subItem);
	                 }else if(xpp.getName().equalsIgnoreCase("zodiac") && GlobalVars.level == GlobalVars.LEVEL_VALUE_LOADER)
	                 {
		                String title = xpp.getAttributeValue(null, "name");
		                String img_url = xpp.getAttributeValue(null, "img");
		                if(title != null)
		                {
		                	((TextView)GlobalVars.viewer.findViewById(R.id.title_activity_view)).
		                	setText(title);
		                }
		                if(img_url != null)
		                {
		                	ImageView imgView = 
		                			(ImageView)GlobalVars.viewer.findViewById(R.id.image_activity_view);
		                	Utils.setImageToImageView(GlobalVars.main, imgView, img_url);
		                }
		                
	                 }else if(xpp.getName().equalsIgnoreCase("value") && 
	                		 GlobalVars.level == GlobalVars.LEVEL_VALUE_LOADER)
	                 {
	                	 foundTag = true;
	                	 
	                 }else if(xpp.getName().equalsIgnoreCase("moreinfo") && 
	                		 GlobalVars.level == GlobalVars.LEVEL_VALUE_LOADER)
	                 {
	                	 foundMoreTag = true;
	                	 
	                 }
	                 
	             } else if(eventType == XmlPullParser.END_TAG) {
	                 //System.out.println("End tag "+xpp.getName());
	                 
	                 if(xpp.getName().equalsIgnoreCase("type") && 
	                		 GlobalVars.level == GlobalVars.LEVEL_TYPE_LOADER && 
	                		 foundTag)
	                 {
	                	 foundTag = false;
	                	 if(item != null)
	                		 GlobalVars.arrayList_items.add(item);
	                 }else if(xpp.getName().equalsIgnoreCase("value") && 
	                		 GlobalVars.level == GlobalVars.LEVEL_VALUE_LOADER &&
	                		 foundTag)
	                 {
	                	 foundTag = false;
	                	 
	                	 if(tagText != null)
	                	 {
	                		 GlobalVars.viewer.description = tagText;
	                		 TextView descView = (TextView)GlobalVars.viewer.findViewById(R.id.text_activity_view);
	                		 descView.setText(GlobalVars.viewer.description);
	                		 tagText = null;
	                	 }
	                	 
	                 }else if(xpp.getName().equalsIgnoreCase("moreinfo") && 
	                		 GlobalVars.level == GlobalVars.LEVEL_VALUE_LOADER)
	                 {
	                	 foundMoreTag = false;
	                	 if(tagText != null)
	                	 {
	                		 GlobalVars.viewer.fullDescription = tagText;
	                		 final Button more = (Button)GlobalVars.viewer.findViewById(R.id.more_activity_view);
	                		 
	                		 more.setOnClickListener(new OnClickListener(){
								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated method stub
									TextView descView = (TextView)GlobalVars.viewer.
											findViewById(R.id.text_activity_view);
									if(descView.getText().equals(GlobalVars.viewer.description))
									{
										more.setText(GlobalVars.viewer.getString(R.string.title_more_button_activity_view));
										descView.setText(GlobalVars.viewer.fullDescription);
									}else
									{
										more.setText(GlobalVars.viewer.getString(R.string.title_push_more_button_activity_view));
										descView.setText(GlobalVars.viewer.description);
									}
										
								}
	                			 
	                		 });
	                		 more.setVisibility(View.VISIBLE);
	                		 tagText = null;
	                	 }
	                	 
	                 }
	                 
	             } else if(eventType == XmlPullParser.TEXT) {
	                 //System.out.println("XmlPullParser.TEXT "+xpp.getText());
	                 if(foundTag && GlobalVars.level == GlobalVars.LEVEL_VALUE_LOADER)
	                 {
	                	 tagText = xpp.getText();
	                 }else if(foundMoreTag && GlobalVars.level == GlobalVars.LEVEL_VALUE_LOADER)
	                 {
	                	 tagText = xpp.getText();
	                 }
	             }
	             eventType = xpp.next();
	        }
	        
	        GlobalVars.adapter_list.notifyDataSetChanged();
	        
	}
	
	public void addMenuItem(final String title,final String url,final String img_url)
	{
		LinearLayout layout = (LinearLayout) 
   			 GlobalVars.main.findViewById(R.id.layout_signs_scroll_view);
		
   	 	final ViewGroup nullParent = null;
		final View view = GlobalVars.main.getLayoutInflater().inflate(R.layout.element_signs_scroll_view, 
		    		nullParent);
		final TextView titleView = (TextView)view.findViewById(R.id.text_layout_element_signs_scroll_view);
		final ImageView imgView = (ImageView)view.findViewById(R.id.image_layout_element_signs_scroll_view);
		if(img_url != null)
		{
			Utils.setImageToImageView(GlobalVars.main, imgView, img_url);
		}
		
		
		titleView.setText(title);
		
		
		imgView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				/*
				 * Делаем эфект выбора элемена
				 */
				final View vImg = v;
				
				Animation animation = AnimationUtils.loadAnimation(GlobalVars.main, R.anim.anim_menu_scale);
				animation.setAnimationListener(new AnimationListener(){

					@Override
					public void onAnimationEnd(Animation animation) {
						// TODO Auto-generated method stub
						if(oldMenuImagePtr != null)
						{
							oldMenuImagePtr.setScaleX(1.0f);
							oldMenuImagePtr.setScaleY(1.0f);
						}
						vImg.setScaleX(0.75f);
						vImg.setScaleY(0.75f);
						oldMenuImagePtr = vImg;
						if(oldJumpingText != null)
							oldJumpingText.stopJumping();
						JumpingBeans j = JumpingBeans.with(titleView).makeTextJump(0, titleView.getText().length())
				        .build();
						oldJumpingText = j;
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub
						
					}
					
				});
				
				view.startAnimation(animation);
				
				((View) GlobalVars.main.findViewById(R.id.aboutView))
				.setVisibility(View.GONE);
				((View) GlobalVars.main.findViewById(R.id.list_grid_view))
				.setVisibility(View.VISIBLE);
				
				//System.out.println("!!!GlobalVars.main_loader: " + GlobalVars.main_loader);
				GlobalVars.main.stack.push(GlobalVars.main_loader.paths.get(0));
				GlobalVars.level = GlobalVars.LEVEL_TYPE_LOADER;
				GlobalVars.type_Loader = new Loader(GlobalVars.listener);
				runLoader(GlobalVars.type_Loader,url);
			}
			
		});
		
   	 	layout.addView(view);
	}
	
	public void showProgressDialog(Context c)
	{
		progress = new ProgressDialog(c);
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setTitle(c.getString(R.string.title_progress_dialog));
		
		progress.setMessage(c.getString(R.string.text_progress_dialog));
		progress.setIndeterminate(true);
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);
		progress.show();
	}
	
	public void hideProgressDialog()
	{
		if(progress != null)
		{
			progress.dismiss();
			progress = null;
			System.gc();
		}
	}
	
	
	public void runLoader(Loader loader,final String url)
	{
		try {
			loader.execute(new URL(url));
			//Toast.makeText(GlobalVars.main, "url: " + url, Toast.LENGTH_SHORT).show();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}
	
}
