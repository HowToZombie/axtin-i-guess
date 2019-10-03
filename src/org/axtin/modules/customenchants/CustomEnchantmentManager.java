package org.axtin.modules.customenchants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import org.axtin.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * 
 * @author Marcel (MrEAlderson)
 * @date 11/11/2017
 */
public class CustomEnchantmentManager implements Listener {
	
	public final int CHANCE_MAX = 25;
	public final int CHANCE_MIN = 1;
	
	private HashMap<Player, PlayerEnchantmentUseData> datas = new HashMap<Player, PlayerEnchantmentUseData>();
	
	public CustomEnchantmentManager(){
		for(Player player:Bukkit.getOnlinePlayers())
			datas.put(player, new PlayerEnchantmentUseData(player));
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		datas.put(event.getPlayer(), new PlayerEnchantmentUseData(event.getPlayer()));
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		final PlayerEnchantmentUseData data = datas.get(event.getPlayer());
		data.stopBossBar();
		
		datas.remove(event.getPlayer());
	}
	
	@EventHandler
	public void onEnchantItem(EnchantItemEvent event){
		final Player player = event.getEnchanter();
		final EnchantableSlot slot = EnchantableSlot.getByMaterial(event.getItem().getType());
		if(slot == null) return;
		final CustomEnchantmentType[] types = CustomEnchantmentType.getTypes(slot);
		if(types.length == 0) return;
		final double chance = ((event.getExpLevelCost()/30D*(CHANCE_MAX-CHANCE_MIN))+CHANCE_MIN)/types.length;
		
		for(CustomEnchantmentType type:types){
			if(Util.RAND.nextInt(100) <= chance){
				player.playSound(player.getLocation(), Sound.ENTITY_PARROT_IMITATE_WITCH, 1f, 1f);
				
				addEnchantment(event.getItem(), type);
			}
		}
	}
	
	@EventHandler
	public void onPlayerItemHeld(PlayerItemHeldEvent event){
		final Player player = event.getPlayer();
		final ItemStack oldItem = player.getInventory().getItem(event.getPreviousSlot());
		final ItemStack newItem = player.getInventory().getItem(event.getNewSlot());
		final PlayerEnchantmentUseData data = datas.get(player);
		
		data.stopBossBar();
		
		if(oldItem != null){
			final CustomEnchantmentType[] enchants = getEnchantments(oldItem);
			
			// event
			for(CustomEnchantmentType type:enchants)
				type.obj.onPutAway(player, oldItem);
		}
		
		if(newItem != null){
			final CustomEnchantmentType[] enchants = getEnchantments(newItem);
			
			// boss bar
			if(enchants.length >= 1){
				double highestValue = -1;
				CustomEnchantmentType type = null;
				
				// get type that takes the most time
				for(CustomEnchantmentType t:enchants){
					final double timeLeft = data.getTimeLeft(t);
					
					if(timeLeft > highestValue){
						highestValue = timeLeft;
						type = t;
					}
				}
				
				// update bar
				if(highestValue > 0)
					data.playBossBar(type);
			}
			
			// event
			for(CustomEnchantmentType type:enchants)
				type.obj.onTake(player, newItem);
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event){
		final Player player = event.getPlayer();
		final ItemStack is = event.getItem();
		final PlayerEnchantmentUseData data = datas.get(player);
		
		if(is != null && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)){
			final CustomEnchantmentType[] enchants = getEnchantments(is);
			
			// check if can use
			for(CustomEnchantmentType type:enchants){
				if(data.getTimeLeft(type) > 0)
					return;
			}
			
			// use
			for(CustomEnchantmentType type:enchants){
				type.obj.onUse(player, is);
				
				data.setLastTimeUsed(type, System.currentTimeMillis());
			}
			
			// get type with longest waiting time
			int highestValue = -1;
			CustomEnchantmentType highestValueType = null;
			
			for(CustomEnchantmentType type:enchants){
				if(type.cooldownTime > highestValue){
					highestValue = type.cooldownTime;
					highestValueType = type;
				}
			}
			
			// update boss bar
			if(highestValue > 0)
				data.playBossBar(highestValueType);
		}
	}
	
	public boolean addEnchantment(ItemStack is, CustomEnchantmentType type){
		if(hasEnchantment(is, type) || !type.isMaterial(is.getType()))
			return false;
		
		final ItemMeta im = is.getItemMeta();
		final List<String> lore = im.getLore() != null ? im.getLore() : new ArrayList<String>();
		
		lore.add(0, ChatColor.GOLD + type.name);
		im.setLore(lore);
		is.setItemMeta(im);
		
		return true;
	}
	
	public int removeEnchantments(ItemStack is){
		return removeEnchantment(is, null);
	}
	
	public int removeEnchantment(ItemStack is, @Nullable CustomEnchantmentType type){
		if(is.getItemMeta().getLore() == null) return 0;
		
		final ItemMeta im = is.getItemMeta();
		final List<String> lore = im.getLore();
		int removed = 0;
		
		for(String str:new ArrayList<String>(lore)){
			final CustomEnchantmentType t = CustomEnchantmentType.getByName(Util.removeChatColor(str));
			
			if(t != null && (type == null || t == type)){
				lore.remove(str);
				removed++;
			}
		}
		
		im.setLore(lore);
		is.setItemMeta(im);
		
		return removed;
	}
	
	public boolean hasEnchantment(ItemStack is, CustomEnchantmentType type){
		final CustomEnchantmentType[] types = getEnchantments(is);
		
		for(CustomEnchantmentType t:types){
			if(type == t)
				return true;
		}
		
		return false;
	}
	
	public CustomEnchantmentType[] getEnchantments(ItemStack is){
		if(is.getItemMeta().getLore() == null) return new CustomEnchantmentType[0];
		
		final List<CustomEnchantmentType> types = new ArrayList<CustomEnchantmentType>();
		
		for(String lore:is.getItemMeta().getLore()){
			final CustomEnchantmentType type = CustomEnchantmentType.getByName(Util.removeChatColor(lore));
			
			if(type != null)
				types.add(type);
		}
		
		return types.toArray(new CustomEnchantmentType[types.size()]);
	}
}