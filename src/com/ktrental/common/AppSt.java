package com.ktrental.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.ktrental.util.kog;

public class AppSt {

	public static final String KDH_SEND_RESULT = "queryBaseKDH";
	public static final String KDH_SEND_COUNT_ZERO = "0";
	public static final String KDH_SEND_COUNT = "COUNT";
	
	public static final String KDH_SEND_MSG = "MESSAGE";
	
	public static final int EMPTY = -1;
	
	public static final String ETC_Y = "Y";
	
	
	
	
	
	
	public static void map_log(HashMap<String, String> map)
	{
		Iterator<String> iterator = map.keySet().iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			kog.e("KDH", "map_log key = "+key);
			kog.e("KDH", "map_log value = "+map.get(key));
		}	
	}
	
	public static void map_log2(ArrayList<HashMap<String, String>> map)
	{
		for (int i = 0; i < map.size(); i++) {
			Iterator<String> iterator = map.get(i).keySet().iterator();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				kog.e("KDH", "map_log key = "+key);
				kog.e("KDH", "map_log value = "+map.get(i).get(key));
			}
		}
			
	}
	
			
}
