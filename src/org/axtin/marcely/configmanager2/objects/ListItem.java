package org.axtin.marcely.configmanager2.objects;

import java.util.ArrayList;
import java.util.List;

public class ListItem extends Config {

	public ListItem(String value, Tree parent){
		super(value, null, parent, value);
	}
	
	@Override
	public byte getType(){
		return TYPE_LISTITEM;
	}
	
	public static List<String> valuesToString1(List<ListItem> configs){
		final List<String> list = new ArrayList<String>();
		
		for(ListItem c:configs)
			list.add(c.getValue());
		
		return list;
	}
}
