package org.axtin.modules.shulkercrates;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ShulkerInventoryHolder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6700644046311883932L;
	UUID pUUID;
	private List<ShulkerInventory> shulkers = new ArrayList<ShulkerInventory>();
	public boolean hasOpened = false;
	
	public ShulkerInventoryHolder(UUID pUUID) {
		this.pUUID = pUUID;
	}
	
	public void readyForSerialization() {
		hasOpened = false;
		for(ShulkerInventory si : shulkers) {
			si.readyForSerialization();
		}
	}
	
	public void deserializeEverything() {
		for(ShulkerInventory si : shulkers) {
			si.deserializeEverything();
		}
	}
	
	public ShulkerInventory getInv(int which) {
		while((shulkers.size() - 1) < which)
			shulkers.add(new ShulkerInventory());
		return shulkers.get(which);
	}
	
	public UUID getUUID() {
		return this.pUUID;
	}
	
}
