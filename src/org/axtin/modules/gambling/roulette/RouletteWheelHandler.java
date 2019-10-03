package org.axtin.modules.gambling.roulette;

import java.util.List;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class RouletteWheelHandler {

	private int[][] indexArray = new int[9][1];
	private List<ItemStack> items;
	private RouletteManager manager;
	private Inventory roulette;
	
	public RouletteWheelHandler(RouletteManager manager) {
		this.manager = manager;
		this.roulette = this.manager.rouletteInventory;
		this.items = this.manager.rouletteItems;
		this.nextPointer(true);
	}
	
	public void nextPointer(boolean firstTime) {
		
		if(firstTime) {
			
			for(int i = 0; i < 9; i++) {
				indexArray[i][0] = i;
			}
			
		} else {
		
			for(int i = 0; i < 9; i++) {
				indexArray[i][0] = getNextPointerOnItem(indexArray[i][0]);
			}
		
		}
	}

	public int getNextPointerOnItem(int current) {
		int realIndex = items.size() - 1;
		if(current + 1 > realIndex)
			return 0;
		else
			return current + 1;
	}
	
	public void setItems() {
		int i2 = 0;
		
		for(int i = 9; i < 18; i++) {
			
			int item = indexArray[i2][0];
			ItemStack is = items.get(item);
			roulette.setItem(i, is);
			i2++;
			
		}
		
	}
	
	public boolean predictAddition(BidColor color, int spins) {
		int item = indexArray[4][0];
		
		for(int i = 0; i < spins; i++) {
			item = getNextPointerOnItem(item);
		}
		
		ItemStack selected = items.get(item);
		
		return BidColor.getByItemStack(selected) == color;
	}
	
	
	
	public int correctSpinsAddition(int spins, BidColor color) {
		while (!predictAddition(color, spins)) {
			spins++;
		}
		return spins;
	}
	
	public int correctSpinsSubtraction(int spins, BidColor color) {
		while (!predictAddition(color, spins)) {
			spins--;
		}
		return spins;
	}
	
	public int correctSpins(int spins, BidColor color) {
		
		int addition = correctSpinsAddition(spins, color);
		int subtraction = correctSpinsSubtraction(spins, color);
		
		return closest1(spins, addition, subtraction);
		
	}
	
	private int closest1(int find, int... values) {
	    int closest = values[0];
	    for(int i: values)
	       if(Math.abs(closest - find) > Math.abs(i - find))
	           closest = i;
	    return closest;
	}
	
	
}
