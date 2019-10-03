package org.axtin.modules.shulkercrates;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.axtin.container.facade.Container;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ShulkerDataHandler {

	 public void save(ShulkerInventoryHolder shulkerInv) {
		 try {
			 shulkerInv.readyForSerialization();
			 File file = new File(Container.get(Plugin.class).getDataFolder(), shulkerInv.getUUID().toString() + ".dat");
			 if(!file.exists())
				    file.createNewFile();
		      FileOutputStream fout = new FileOutputStream(file);
		      ObjectOutputStream oos = new ObjectOutputStream(fout);
		      oos.writeObject(shulkerInv);
		      oos.close();
		      }
		   catch (Exception e) { e.printStackTrace(); }
	 }
	 
	 public ShulkerInventoryHolder get(Player player) {
		 ShulkerInventoryHolder inv = null;
		 File file = new File(Container.get(Plugin.class).getDataFolder(), player.getUniqueId().toString() + ".dat");

		 if(file.exists()) {
		 try {
			    FileInputStream fin = new FileInputStream(file);
			    ObjectInputStream ois = new ObjectInputStream(fin);
			    inv = (ShulkerInventoryHolder) ois.readObject();
			    ois.close();
			    }
			   catch (Exception e) { return null; }
		 inv.deserializeEverything();
		 return inv;
		 }
		 return null;
	 }
	
	
	
	
}
