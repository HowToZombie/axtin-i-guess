

package org.axtin.modules.kitsystem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.axtin.container.facade.Container;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.ShulkerBox;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public final class Kit {

    String name;
    List<Item> items;
    long cooldown;
    Map<String, Date> cooldowns;
    List<String> commands;
    SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
	int required;
    

    
    
    public Kit(String name, int required, int cooldown) {
        this.name = name;
        this.required = required;
        this.cooldown = cooldown;
        this.items = new ArrayList<>();
        this.cooldowns = new HashMap<>();
        this.commands = new ArrayList<>();
    }
    
    public boolean isOnCooldown(Player player) {
        if(!cooldowns.containsKey(player.getName()))
            return false;
        Date d1 = cooldowns.get(player.getName());
        Date d2 = new Date();
        long seconds = getDateDiff(d1, d2, TimeUnit.SECONDS);
        return seconds <= cooldown;
    }
    
	private long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return diffInMillies / 1000;
    }
    
    public void setCooldown(Player player) {
    	Date date = new Date();
        cooldowns.put(player.getName(), date);
    }
    
    public long getSecondsLeft(Player player) {
        if(cooldowns.containsKey(player.getName())) {
            Date d1 = cooldowns.get(player.getName());
            Date d2 = new Date();
            long seconds = getDateDiff(d1, d2, TimeUnit.SECONDS);
            return cooldown - seconds;
        }
        return 0;
    }
    
    public void saveCooldowns(YamlConfiguration config) {
        config.createSection("Kits." + name + ".Data", mapToSerializableMap(cooldowns));
    }
    
    public void loadCooldowns(YamlConfiguration config) {
        if(config.getConfigurationSection("Kits." + name + ".Data") != null) {
            Map<String, Object> map = config.getConfigurationSection("Kits." + name + ".Data").getValues(false);
            this.cooldowns = serializableMapToMap(map);
        }
    }
    
    public Map<String, Object> mapToSerializableMap(Map<String, Date> map) {
        Map<String, Object> newMap = new HashMap<>();
        map.entrySet().forEach((entry) -> {
            newMap.put(entry.getKey(), format.format(entry.getValue()));
        });
        return newMap;
    }
    
    public Map<String, Date> serializableMapToMap(Map<String, Object> map){
    
        Map<String, Date> newMap = new HashMap<>();
        map.entrySet().forEach((entry) -> {
            Date date;
            try {
                date = format.parse((String) entry.getValue());
                newMap.put(entry.getKey(), date);
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        });
        return newMap;
    }   
    
    public Kit(String path, YamlConfiguration config) {
        
        this.name = config.getString(path + ".Name");
        this.required = config.getInt(path + ".RequiredPrisonRank");
        this.cooldown = config.getLong(path + ".Cooldown");
        this.items = new ArrayList<>();
        if(config.getStringList(path + ".Commands") != null) {
        	this.commands = config.getStringList(path + ".Commands");
        }
        if(config.getConfigurationSection(path + ".Items") != null) {
            config.getConfigurationSection(path + ".Items").getKeys(false).stream().map((pathPart) -> path + ".Items." + pathPart).forEachOrdered((itemPath) -> {
                this.items.add(new Item(itemPath, config));
            });
        }
        cooldowns = new HashMap<>();
        this.loadCooldowns(Container.get(KitHandler.class).dataConfig);
    }
    
    public static List<Kit> getKits(YamlConfiguration config)
    {
        List<Kit> list = new ArrayList<>();
        if(config.getConfigurationSection("Kits") != null) {
            config.getConfigurationSection("Kits").getKeys(false).stream().map((path) -> "Kits." + path).forEachOrdered((path2) -> {
                list.add(new Kit(path2, config));
            });
                
        }
        
        
        return list;
    }
    public void save(YamlConfiguration config) {
        String path = "Kits." + name + ".";
        config.set(path + "RequiredPrisonRank", required);
        config.set(path + "Cooldown", cooldown);
        config.set(path + "Name", name);
        int i = 0;
        items.forEach((item) -> {
                String itemPath = path + "Items." + "Item_" + i;
                item.save(itemPath, config);
        });
    }

    String getName() {
        return this.name;
    }

    public int getRequiredRank() {
        return this.required;
    }
    
    public boolean spawn(Block block, Player recipent) {
        if(block.getType() != Material.AIR)
            return false;
        block.setType(shulkerTypes().get(new Random().nextInt(shulkerTypes().size())));
        ShulkerBox sb = (ShulkerBox) block.getState();
        items.forEach((item) -> {
            sb.getInventory().addItem(item.toItemStack());
        });
        return true;
    }

    private List<Material> shulkerTypes() {
		List<Material> list = new ArrayList<>();
		
		for(Material mat : Material.values()) {
			if(mat.toString().contains("SHULKER_BOX"))
				list.add(mat);
		}
		
		return list;
	}

    boolean canUse(Player player) {
    	return !this.isOnCooldown(player);
    }
    
    public String getTimeLeft(Player player) {
    	String res;
            long seconds = this.getSecondsLeft(player);
            int day = (int)TimeUnit.SECONDS.toDays(seconds);        
            long hours = TimeUnit.SECONDS.toHours(seconds) - (day *24);
            long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds)* 60);
            long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) *60);
            res = String.format("%dD: %dH: %dM: %dS", day, hours, minute, second);
        
        return res;
    } 
    
    public int getSpaceRequired() {
        return this.items.size();
    }
    
    public boolean hasEnoughSpace(PlayerInventory inv) {
        int initialFreeSlots = 0;
        boolean hasEnoughSpace = true;
        for(ItemStack is : inv.getStorageContents())
        	if(is == null)
        		initialFreeSlots++;
        for(Item item : items) {
        	boolean canBeAdded = false;
        	if(initialFreeSlots - 1 >= 0) {
        		//Can be added without problem but will use a slot
        		canBeAdded = true;
        		initialFreeSlots--;
        		continue;
        	} else {
        		//must be added to existing itemstack as the freeslots are full (small chance it'll find anything suitable)
        		for(ItemStack itemstack : inv.getStorageContents()){
        			if(itemstack != null) {
        				if(item.toItemStack().isSimilar(itemstack)) {
        					if(itemstack.getAmount() >= itemstack.getMaxStackSize()) {
        						//found similar but is already full
        						continue;
        					} else {
        						if(itemstack.getAmount() + item.toItemStack().getAmount() > itemstack.getMaxStackSize()) {
        							//found a similar itemstack but can't add because it will be too much
        							continue;
        						} else {
        							//can be added
        							canBeAdded = true;
        							break;
        						}
        					}
        				}
        			}
        		}
        		hasEnoughSpace = canBeAdded;
        	}
        }
        return hasEnoughSpace;
    }

	public void give(Player player) {
		items.forEach((item) -> player.getInventory().addItem(item.toItemStack()));
	}

    
    
}
