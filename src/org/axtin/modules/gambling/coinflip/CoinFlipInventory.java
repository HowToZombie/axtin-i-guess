package org.axtin.modules.gambling.coinflip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.axtin.container.facade.Container;
import org.axtin.modules.gambling.roulette.RouletteManager;
import org.axtin.user.UserData;
import org.axtin.util.ListUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class CoinFlipInventory {

	private Inventory inventory;
	private Player player1;
	private Player player2;
	
	private int dyecolor1 = 9;
	private int dyecolor2 = 10;
	
	private int panecolor1 = 5;
	private int panecolor2 = 6;

	
	private double amount;
	private boolean done;
	private int pointer = 0;
	private Map<Integer, Integer> panePositions;
	private List<ItemStack> rotatingPanes;
	private CoinFlip game;
	
	public double getAmount() {
		return amount;
	}
	
	public CoinFlipInventory(CoinFlip coinFlip) {
		this.game = coinFlip;
		this.player1 = coinFlip.getChallenger();
		this.player2 = coinFlip.getOpponent();
		this.amount = coinFlip.getAmount();
		this.panePositions = getPanePositions();
		this.rotatingPanes = getRotatingPanes();
		this.inventory = setUpInventory();
		this.done = false;
	}
	
	public void finishGame() {
		this.game.finishGame();
	}

	public Inventory setUpInventory() {
		this.inventory = Bukkit.createInventory(null, 45, color("Coin Flip"));
		
		this.inventory.setItem(4, getHead(player1));
		this.inventory.setItem(5, getDye(dyecolor1, "&cRed"));
		
		this.inventory.setItem(40, getHead(player2));
		this.inventory.setItem(41, getDye(dyecolor2, "&eYellow"));

		this.inventory.setItem(13, getColoredGlassPane(panecolor1, "-"));
		this.inventory.setItem(23, getColoredGlassPane(panecolor2, "-"));
		
		this.inventory.setItem(21, getColoredGlassPane(panecolor2, "-"));
		this.inventory.setItem(31, getColoredGlassPane(panecolor1, "-"));
		
		this.inventory.setItem(22, getDye(8, "-"));
		
		
		return inventory;
	}
	
	private String color(String arg0) {
		return ChatColor.translateAlternateColorCodes('&', arg0);
	}
	
	private Map<Integer, Integer> getPanePositions() {
		Map<Integer, Integer> map = new HashMap<>();
		map.put(0, 13);
		map.put(1, 23);
		map.put(2, 31);
		map.put(3, 21);
		return map;
	}
	
	private List<ItemStack> getRotatingPanes() {
		List<ItemStack> list = new ArrayList<>();
		list.add(getColoredGlassPane(panecolor1, "."));
		list.add(getColoredGlassPane(panecolor2, "."));
		list.add(getColoredGlassPane(panecolor1, "."));
		list.add(getColoredGlassPane(panecolor2, "."));
		return list;
	}
	
	private void changePanePositions() {
		if(pointer < rotatingPanes.size())
			pointer++;
		else
			pointer = 0;
		List<ItemStack> items = ListUtil.getFrom(rotatingPanes, pointer, 4);
		for(int i = 0; i < 4; i++) {
			int slot = panePositions.get(i);
			inventory.setItem(slot, items.get(i));
		}
	}
	
	@SuppressWarnings("deprecation")
	private ItemStack getDye(int color, String name) {
		ItemStack dye = new ItemStack(351, 1, (short) color);
		ItemMeta meta = dye.getItemMeta();
		meta.setDisplayName(color(name));
		dye.setItemMeta(meta);
		return dye;
	}
	
	@SuppressWarnings("deprecation")
	private ItemStack getColoredGlassPane(int color, String name) {
		ItemStack pane = new ItemStack(160, 1, (short) color);
		ItemMeta meta = pane.getItemMeta();
		meta.setDisplayName(color(name));
		pane.setItemMeta(meta);
		return pane;
	}
	
	private ItemStack getHead(Player player) {
		ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta playerheadmeta = (SkullMeta) head.getItemMeta();
        playerheadmeta.setOwner(player.getName());
        playerheadmeta.setDisplayName(player.getName());
        head.setItemMeta(playerheadmeta);
        return head;
	}
	
	
	
	public void open() {
		this.player1.openInventory(inventory);
		this.player2.openInventory(inventory);
		start(5, getGame(10));
	}
	
	public void start(int countdown, BukkitRunnable game) {
		new BukkitRunnable() {
			int timer = new Integer(countdown);
			
			@Override
			public void run() {
				
				timer--;
				
				if(timer <= 0) {
					game.runTaskTimerAsynchronously(Container.get(Plugin.class), 0, 10);
					this.cancel();
				}
			}
			
			
		}.runTaskTimerAsynchronously(Container.get(Plugin.class), 0, 20);
	}
	
	public BukkitRunnable getGame(int switches) {
		return new BukkitRunnable() {
			int localSwitches = new Integer(switches);
			@Override
			public void run() {
			
				changePanePositions();
				flipCoin();
				
				if(localSwitches <= 0) {
					this.cancel();
					getWinner().sendMessage("yey... you won " + amount * 2 + "$ I guess");
					UserData winnerData = Container.get(RouletteManager.class).getUser(getWinner()).getData();
					winnerData.setBalance(winnerData.getBalance() + (amount * 2));
				}
				
				localSwitches--;
				
			}
			
			
		};
	}
	
	public void flipCoin() {
		ItemStack current = inventory.getItem(22);
		if(current.getDurability() == dyecolor1)
			inventory.setItem(22, getDye(dyecolor2, player2.getName()));
		else
			inventory.setItem(22, getDye(dyecolor1, player1.getName()));
	}
	
	public Player getWinner() {
		ItemStack current = inventory.getItem(22);
		if(current.getDurability() == dyecolor1)
			return player1;
		else
			return player2;
	}
	
	public boolean isFinished() {
		return this.done; 
	}
	
	
}
