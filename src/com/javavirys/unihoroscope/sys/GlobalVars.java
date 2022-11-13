package com.javavirys.unihoroscope.sys;

import java.util.ArrayList;

import com.javavirys.lib.net.Loader;
import com.javavirys.unihoroscope.ActivityView;
import com.javavirys.unihoroscope.MainActivity;
import com.javavirys.unihoroscope.ui.ListAdapter;

public class GlobalVars {

	public static final int LEVEL_MAIN_LOADER = 1001;
	public static final int LEVEL_TYPE_LOADER = 1002;
	public static final int LEVEL_PERIOD_LOADER = 1003;
	public static final int LEVEL_VALUE_LOADER = 1004;
	
	public static MainActivity main;
	public static ActivityView viewer;
	public static Listeners listener;
	
	public static Loader main_loader;
	public static Loader type_Loader;
	public static Loader value_Loader;
	
	public static ArrayList<ListItem> arrayList_items;
	public static ListAdapter adapter_list;
	public static int level;
}
