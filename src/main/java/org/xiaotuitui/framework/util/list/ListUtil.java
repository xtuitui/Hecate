package org.xiaotuitui.framework.util.list;

import java.util.ArrayList;
import java.util.List;

public class ListUtil {
	
	public static List<Integer> convertStringArrayToIntegerList(String[] array) {
		List<Integer> list = new ArrayList<Integer>();
		for(String element:array){
			list.add(Integer.valueOf(element));
		}
		return list;
	}

}