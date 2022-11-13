package com.javavirys.unihoroscope.ui;

import java.util.List;

import com.javavirys.unihoroscope.R;
import com.javavirys.unihoroscope.sys.GlobalVars;
import com.javavirys.unihoroscope.sys.ListItem;
import com.javavirys.unihoroscope.sys.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapter extends ArrayAdapter<ListItem> {

	List<ListItem> values;
	
	public ListAdapter(Context context, List<ListItem> objects) {
		super(context, android.R.layout.simple_list_item_1, objects);
		// TODO Auto-generated constructor stub
		values = objects;
	}
	
	
	@Override	
    public View getView(int position, View convertView, ViewGroup parent) {
		View rowView;
		
		LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        rowView = inflater.inflate(R.layout.main_list, parent, false);

        TextView textView = (TextView) rowView.findViewById(R.id.title_item_list);
        final ImageView imgView = (ImageView) rowView.findViewById(R.id.image_item_list);
        
        final ListItem item = (ListItem)values.get(position);
        if(item.title != null)
        	textView.setText(item.title);
        
        if(item.img_url != null)
        {	
        	Utils.setImageToImageView(GlobalVars.main, imgView, item.img_url);
        }
        
        return rowView;
	}

	// возвращает содержимое выделенного элемента списка
	public ListItem getItem(int position) {
		return values.get(position);
	}
			
	public int getSize()
	{
		return values.size();
	}
			
	public void setItem(ListItem item, int index) {
		values.set(index, item);
	}
	
			
}
