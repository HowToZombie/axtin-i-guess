package org.axtin.modules.meteorite;

import java.io.File;
import java.util.logging.Logger;

import org.axtin.container.facade.Container;
import org.axtin.marcely.configmanager2.ConfigFile;
import org.axtin.marcely.configmanager2.objects.Config;
import org.axtin.marcely.configmanager2.objects.Tree;
import org.axtin.util.ItemStackUtil;
import org.axtin.util.Util;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

/**
 * 
 * @author Marcel (MrEAlderson)
 * @date 10/6/2017
 */
public class MeteoriteConfig {
	
	private static final ConfigFile FILE = new ConfigFile(new File(Container.get(Plugin.class).getDataFolder() + "/meteorite.cm2"));
	
	public static void load(MeteoriteManager m){
		m.removeAll();
		
		if(!FILE.exists()){
			saveDefault(m);
			return;
		}
		
		FILE.load();
		
		for(Tree t:FILE.getRootTree().getTreeChilds()){
			if(t.getName().startsWith("region-")){
				final String rawID = t.getName().replaceFirst("region-", "");
				final Config cMin = t.getConfigChield("min");
				final Config cMax = t.getConfigChield("max");
				
				if(Util.isInteger(rawID) &&
				   cMin != null && cMin.getValueAsLocation() != null &&
				   cMax != null && cMax.getValueAsLocation() != null){
					m.getRegions().add(new MeteoriteRegion(Integer.valueOf(rawID), cMin.getValueAsLocation(), cMax.getValueAsLocation()));
				}else
					Logger.getLogger(MeteoriteConfig.class.getName()).warning("Failed to load region '" + t.getName() + "'");
			
			}else if(t.getName().equals("drops")){
				for(Config c:t.getChilds()){
					final String[] strs = c.getRawLine().split(",");
					
					if(strs.length == 3 &&
					  Util.isInteger(strs[0]) && Util.isInteger(strs[1]) &&
					  ItemStackUtil.toItemStack(strs[2]) != null){
						final ItemStack is = ItemStackUtil.toItemStack(strs[2]);
						is.setAmount(Integer.valueOf(strs[1]));
						
						final Item item = new Item(is, Integer.valueOf(strs[0]));
						m.getItems().add(item);
					}else
						Logger.getLogger(MeteoriteConfig.class.getName()).warning("Failed to load an item");
				}
			}
		}
		
		FILE.clear();
	}
	
	public static void save(MeteoriteManager m){
		FILE.clear();
		
		for(MeteoriteRegion mr:m.getRegions()){
			FILE.getPicker().addConfig("region-" + mr.getID() + ".min", mr.getLocMin());
			FILE.getPicker().addConfig("region-" + mr.getID() + ".max", mr.getLocMax());
		}
		
		FILE.getPicker().addEmptyLine();
		FILE.getPicker().addComment("<% chance of spawning>,<amount>,<item (e.g. wool:11)>");
		
		for(Item item:m.getItems())
			FILE.getPicker().addListItem("drops", item.getRarity() + "," + item.getItemStack().getAmount() + "," + ItemStackUtil.itemstackToString(item.getItemStack()));
		
		FILE.save();
	}
	
	public static void saveDefault(MeteoriteManager m){
		m.getItems().add(new Item(new ItemStack(Material.DIRT, 32), 100));
		
		save(m);
	}
}
