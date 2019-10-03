package org.axtin.modules.gambling.coinflip;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.axtin.util.gui.paging.PageItem;
import org.axtin.util.gui.paging.Tuple;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class GlobalCoinFlipRequest {

	private UUID user;
	private double amount;
	private int expires;
	private Date created;
	private static String format = "Time left: %02dH %02dM";
	
	public GlobalCoinFlipRequest(UUID user, double amount) {
		this.user = user;
		this.amount = amount;
		this.expires = 1800;
		this.created = new Date();
	}
	
	public Tuple<Long, Long> getHoursAndMinutesLeft() {
		Date now = new Date();
		long seconds = (now.getTime()-created.getTime())/1000;
		long secondsUntilExpiry = expires - seconds;
		
		Long hours = secondsUntilExpiry / 3600;
		Long minutes = (secondsUntilExpiry % 3600) / 60;		
		return new Tuple<>(hours, minutes);
	}
	
	public ItemStack getHead() {
		Player player = Bukkit.getPlayer(user);
		ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta playerheadmeta = (SkullMeta) head.getItemMeta();
        playerheadmeta.setOwner(player.getName());
        playerheadmeta.setDisplayName(player.getName());
        playerheadmeta.setLore(Arrays.asList(String.valueOf(amount), "Time left: "));
        head.setItemMeta(playerheadmeta);
        return head;
	}
	
	public Tuple<UUID, Double> getInfo() {
		return new Tuple<UUID, Double>(user, amount);
	}
	
	private void accept() {
		
	}
	
	public PageItem getPageItem() {
		return new PageItem(getHead()) {
			
			@Override
			public void update() {
				ItemMeta im = this.getItemMeta();
				
				List<String> lore = im.getLore();
				Tuple<Long, Long> hoursAndMinutes = getHoursAndMinutesLeft();
				lore.set(1, String.format(format, hoursAndMinutes._x, hoursAndMinutes._y));
				
				im.setLore(lore);
				
				this.setItemMeta(im);
				
			}
			
			@Override
			public void onClick(Player player, Inventory inv, ItemStack item) {
				accept();
			}
		};
	}
	
}
