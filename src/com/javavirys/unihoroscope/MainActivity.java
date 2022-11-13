package com.javavirys.unihoroscope;

import java.util.ArrayList;
import java.util.Stack;

import com.javavirys.lib.net.Loader;
import com.javavirys.unihoroscope.sys.GlobalVars;
import com.javavirys.unihoroscope.sys.ListItem;
import com.javavirys.unihoroscope.sys.Listeners;
import com.javavirys.unihoroscope.ui.ListAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import net.frakbot.jumpingbeans.JumpingBeans;

public class MainActivity extends Activity{
	
	public Stack<String> stack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final TextView aboutView = (TextView) findViewById(R.id.aboutView);
		aboutView.setMovementMethod(LinkMovementMethod.getInstance());
		JumpingBeans.with(aboutView).makeTextJump(0, aboutView.getText().length())
		        .build();
		
		GlobalVars.main = this; 
		stack = new Stack<String>();
		GlobalVars.level = GlobalVars.LEVEL_MAIN_LOADER;
		GlobalVars.listener = new Listeners();
		
		initComponents();
		GlobalVars.main_loader = new Loader(GlobalVars.listener);
		//--------------------Get screen dpi------------------------
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
		System.out.println("DPI: " + metrics.densityDpi);
		
		GlobalVars.listener.runLoader(GlobalVars.main_loader,
				"http://projects.srcblog.ru/unihoroscope/horoscope.php?dpi=" + metrics.densityDpi);
	}
	
	@Override
	public void onBackPressed() {
		
		if(GlobalVars.level-- <= GlobalVars.LEVEL_MAIN_LOADER)
		{
			super.onBackPressed();
			return;
		}
		
		GlobalVars.adapter_list.clear();
		
		String url = stack.pop();
		
		switch(GlobalVars.level)
		{
		case GlobalVars.LEVEL_MAIN_LOADER:
			((View) findViewById(R.id.aboutView)).setVisibility(View.VISIBLE);
			((View) GlobalVars.main.findViewById(R.id.list_grid_view)).setVisibility(View.GONE);
			
			if(GlobalVars.listener.oldJumpingText != null)
				GlobalVars.listener.oldJumpingText.stopJumping();
			
			if(GlobalVars.listener.oldMenuImagePtr != null)
			{
				GlobalVars.listener.oldMenuImagePtr.setScaleX(1.0f);
				GlobalVars.listener.oldMenuImagePtr.setScaleY(1.0f);
				GlobalVars.listener.oldMenuImagePtr = null;
				
			}
			break;
		case GlobalVars.LEVEL_TYPE_LOADER:
			GlobalVars.type_Loader = new Loader(GlobalVars.listener);
			GlobalVars.listener.runLoader(GlobalVars.type_Loader,url);
			break;
		}
		
	}
	
	public void initComponents()
	{
		GridView grid = (GridView)findViewById(R.id.list_grid_view);
		grid.setOnItemClickListener(GlobalVars.listener);
		GlobalVars.arrayList_items = new ArrayList<ListItem>();
		GlobalVars.adapter_list = new ListAdapter(this,GlobalVars.arrayList_items);
		grid.setAdapter(GlobalVars.adapter_list);
	}
	
	
	
}
