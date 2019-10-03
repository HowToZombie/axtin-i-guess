package org.axtin.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Alan Tavakoli
 *
 */
public class ListUtil {

	/**
	 * Returns a sublist but with the ability to get resting elements by starting from 0 again.
	 * @param source - The list you want to get elements from.
	 * @param index - Where it starts getting elements (must be in range).
	 * @param amount - How many elements it should get, if it exceeds the elements left in the list (starting from index) it gets the remaining amount from the start.
	 * @return A new list with the specified amount of elements starting from index.
	 */
	public static <T> List<T> getFrom(List<T> source, int index, int amount) {
		
		int localPointer = new Integer(index);
		int left = new Integer(amount);
		
		List<T> tempList = new ArrayList<>();
		
		while (localPointer < source.size() && left > 0) {
			tempList.add(source.get(localPointer));
			localPointer++;
			left--;
		}
		
		if(left > 0) {
			tempList.addAll(getFrom(source, 0, left));
		}
		
		return tempList;		
	}
	
	
	
	/**
	 * This method returns a sublist with the requested elements, starting from the top. If there's too few elements, it gets as many as possible. 
	 * And returns an empty list if the source list is empty.
	 * <p>
	 * Warning - Does not sort/order the list.
	 * @param list - The source list.
	 * @param amount - How many elements it should get.
	 * @return a sublist with the requested elements.
	 */
	public static <T> List<T> getTop(List<T> list, int amount) {
		if(list.size() < 1) {
			return new ArrayList<>();
		} else if(list.size() == 1) { 
			return Arrays.asList(list.get(0));
		} else {
			if(list.size() < amount) {
				return list.subList(0, list.size() - 1);
			} else {
				return list.subList(0, amount - 1);
			}
		}
	}
	
	public static <T> int getIndexAfterIterations(List<T> list, int startIndex, int iterations) {
		int localPointer = new Integer(startIndex);
		for(int i = 0; i < iterations; i++) {
			if(localPointer == list.size() - 1)
				localPointer = 0;
			else
				localPointer++;
		}
		return localPointer;
	}
	
}
