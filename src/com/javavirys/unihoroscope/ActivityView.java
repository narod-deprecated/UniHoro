package com.javavirys.unihoroscope;

import com.javavirys.lib.net.Loader;
import com.javavirys.unihoroscope.sys.GlobalVars;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityView extends Activity {

	public String title = null;
	public String description = null;
	public String fullDescription = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		GlobalVars.viewer = this;
		
		setContentView(R.layout.activity_view);
		
		Button more = (Button)findViewById(R.id.more_activity_view);
		more.setVisibility(View.GONE);
		
		Intent i = getIntent();
		if(i == null)
			finish();
		String url = i.getStringExtra("url");
		GlobalVars.level = GlobalVars.LEVEL_VALUE_LOADER;
		GlobalVars.value_Loader = new Loader(GlobalVars.listener);
		GlobalVars.listener.runLoader(GlobalVars.value_Loader, url);
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		GlobalVars.level --;
		
	}
	
}
