package org.axtin.modules.gambling.roulette;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.axtin.container.facade.Container;
import org.axtin.user.User;
import org.axtin.user.UserData;
import org.axtin.user.UserRepository;
import org.axtin.util.ListUtil;
import org.axtin.util.RandomCollection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class RouletteManager {

	public Inventory rouletteInventory;
	public boolean running = false;
	public List<Bid> redBids;
	public List<Bid> blackBids;
	public List<Bid> greenBids;
	public double greenMultiplier = 14.0D;
	public double redMultiplier = 2.0D;
	public double blackMultiplier = 2.0D;
	public double greenChance = 0.33;
	public double redChance = 0.33;
	public double blackChance = 0.33;
	public List<ItemStack> rouletteItems;
	private int pointer = 0;
	private int totalspins;
	private BukkitRunnable runner;
	private Runnable game;
	private int taskId;
	int countdown = 15;
	boolean pauseCountdown = false;
	RandomCollection<BidColor> colorChances;
	private BidColor chosen;
	private RouletteWheelHandler wheelHandler;
	
	private int maxSpins, minSpins;
	
	public RouletteManager() {
		rouletteInventory = Bukkit.createInventory(null, 54, "Roulette");
		setUpInventory(rouletteInventory);
		this.maxSpins = 40;
		this.minSpins = 30;
		totalspins = randomInt(this.minSpins, this.maxSpins);
		redBids = new ArrayList<>();
		blackBids = new ArrayList<>();
		greenBids = new ArrayList<>();;
		rouletteItems = initializeRouletteItems();
		this.wheelHandler = new RouletteWheelHandler(this);
		this.colorChances = new RandomCollection<>();
		setUpColorChances(this.colorChances);
		this.chosen = this.colorChances.next();
		this.runner = getRunner();
		this.runner.runTaskTimerAsynchronously(Container.get(Plugin.class), 0, 20);
		this.running = true;
		this.game = getGame();
		this.totalspins = wheelHandler.correctSpinsAddition(totalspins, chosen);
	}
	
	public void setUpColorChances(RandomCollection<BidColor> colorChances) {
		colorChances.add(blackChance, BidColor.BLACK);
		colorChances.add(redChance, BidColor.RED);
		colorChances.add(greenChance, BidColor.GREEN);
	}
	
	public void getNewChosen() {
		this.chosen = this.colorChances.next();
	}
	
	public void setUpInventory(Inventory inv) {
		for(int i = 0; i < 9; i++) {
			inv.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
		}
		for(int i = 18; i < 27; i++) {
			inv.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
		}
		inv.setItem(4, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5));
		inv.setItem(22, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5));
		inv.setItem(28, getNote(BidColor.RED));
		inv.setItem(31, getNote(BidColor.GREEN));
		inv.setItem(34, getNote(BidColor.BLACK));
		inv.setItem(53, getClockTimer());
	}
	
	private ItemStack getClockTimer() {
		ItemStack is = new ItemStack(Material.WATCH);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName("&cCountdown");
		is.setItemMeta(im);
		return is;
	}
	
	private ItemStack getNote(BidColor color) {
		ItemStack is = new ItemStack(Material.PAPER);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(color("&6-" + color.getName(false) + " &6top bids-"));
		is.setItemMeta(im);
		return is;
	}
	
	private String color(String arg0) {
		return ChatColor.translateAlternateColorCodes('&', arg0);
	}
	
	private void updateClockTimer() {
		ItemStack is = rouletteInventory.getItem(53);
		if(is == null) {
			rouletteInventory.setItem(53, getClockTimer());
			is = rouletteInventory.getItem(53);
		}
		if(countdown > 0)
			is.setAmount(countdown);
	}
	
	public boolean addBid(Player player, double amount, BidColor color) {
		Bid bid = new Bid(player, amount);
		switch(color) {
		case BLACK:
			blackBids.add(bid);
			break;
		case GREEN:
			greenBids.add(bid);
			break;
		case RED:
			redBids.add(bid);
			break;
		default:
			break;
		}
		return true;
	}
	
	public ItemStack getRouletteItem(BidColor color) {
		ItemStack is = new ItemStack(color.getMaterial());
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(color.getName(true));
		is.setItemMeta(im);
		return is;
	}
	
	public List<ItemStack> initializeRouletteItems() {
		List<ItemStack> list = new ArrayList<>();
		for(int i = 0; i < 12; i++) {
			if(i % 2 == 0)
				list.add(BidColor.BLACK.toItem());
			else
				list.add(BidColor.RED.toItem());
		}
		list.add(BidColor.GREEN.toItem());
		return list;
	}
	
	public void setItems(List<ItemStack> items) {
		Iterator<ItemStack> iterator = items.iterator();
		for(int i = 9; i < 18; i++) {
			rouletteInventory.setItem(i, iterator.next());
		}
	}
	
	public BukkitRunnable getRunner() {
		return new BukkitRunnable() {

			
			
			@Override
			public void run() {
				if(pauseCountdown == false) {
					countdown--;
					updateClockTimer();
					setTopBidsOnlore(rouletteInventory.getItem(28), BidColor.RED);
					setTopBidsOnlore(rouletteInventory.getItem(31), BidColor.GREEN);
					setTopBidsOnlore(rouletteInventory.getItem(34), BidColor.BLACK);
					//Bukkit.broadcastMessage(String.valueOf(countdown));
					if(countdown <= 0) {
						game = getGame();
						taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Container.get(Plugin.class), game, 1, 1);
						pauseCountdown = true;
					}
				}
				
			}
			
			
			
		};
	}
	
	public Runnable getGame() {
		
		return new Runnable() {
			
			int spin = 0;
			int spins = new Integer(totalspins);
			int ticks = 0;
			int stage = 0;
			int ticksBetween = 1;
			int lastTick = 1;
			
			@Override
			public void run() {
				this.ticks++;
				

				stage = getStage(spin, spins);
				ticksBetween = getTicksBetween(stage);
				
				//Bukkit.broadcastMessage(" Ticks: " + ticks + " TicksBetween: " + ticksBetween + " Last tick: " + lastTick + " Stage: " + stage);
				if(ticks - lastTick > ticksBetween) {
					//List<ItemStack> list = ListUtil.getFrom(rouletteItems, pointer, 9);
					//Bukkit.broadcastMessage(String.valueOf(spin));
//					Bukkit.broadcastMessage(String.valueOf(spins));

					for(HumanEntity p : rouletteInventory.getViewers())
						((Player) p).playSound(p.getLocation(), Sound.BLOCK_CHEST_LOCKED, 1f, 2f);
					//UNTESTED function
					wheelHandler.setItems();
					wheelHandler.nextPointer(false);
					//
					if(pointer < rouletteItems.size())
						pointer++;
					else
						pointer = 0;
					spin++;
					lastTick = ticks;
				}
				
				if(spin > spins) {
					ItemStack chosenItemStack = rouletteInventory.getItem(13);
					endSpin(chosenItemStack);
					//Bukkit.broadcastMessage("Landed on index: " + rouletteItems.indexOf(rouletteInventory.getItem(13)));
					Bukkit.getScheduler().cancelTask(taskId);
					
					
				}
				
				
			}
		};
		
	}
	
	public void endSpin(ItemStack at) {
		BidColor color = BidColor.getByItemStack(at);
		Bukkit.broadcastMessage("The color which it landed on is " + color.getName(true));
		playWinAnimation(color);
		if(color == BidColor.BLACK)
			payOut(this.blackBids, color);
		else if(color == BidColor.RED)
			payOut(this.redBids, color);
		else if(color == BidColor.GREEN)
			payOut(this.greenBids, color);
		clearBids();
	}
	
	public void playWinAnimation(BidColor color) {
		
		
		BukkitRunnable anim = new BukkitRunnable() {

			int i = 0;
			@Override
			public void run() {
				
				int slot1 = i;
				int slot2 = getAnimationSlot(slot1);
				
				rouletteInventory.setItem(slot1, getGlassPane(getColor(color)));
				rouletteInventory.setItem(slot2, getGlassPane(getColor(color)));
				
				if(slot1 > 0) {
					
					int replace = i - 1;
					int replace2 = slot2 - 1;
					
					if(replace == 4 && replace2 == 22) {
						rouletteInventory.setItem(replace, getGlassPane(getColor(BidColor.GREEN)));
						rouletteInventory.setItem(replace2, getGlassPane(getColor(BidColor.GREEN)));
					} else {
						rouletteInventory.setItem(replace, getGlassPane(getColor(BidColor.BLACK)));
						rouletteInventory.setItem(replace2, getGlassPane(getColor(BidColor.BLACK)));
					}
					
				}
				
				i++;
				
				if(i > 8) {
					resetCountdown();
					rouletteInventory.setItem(8, getGlassPane(getColor(BidColor.BLACK)));
					rouletteInventory.setItem(getAnimationSlot(8), getGlassPane(getColor(BidColor.BLACK)));
					this.cancel();
				}
				
				
			}
			
		};
		
		anim.runTaskTimerAsynchronously(Container.get(Plugin.class), 1, 2);
	}
	
	public ItemStack getGlassPane(short color) {
		return new ItemStack(Material.STAINED_GLASS_PANE, 1, color);
	}
	
	public short getColor(BidColor color) {
		switch(color) {
		case BLACK:
			return (short) 15;
		case GREEN:
			return (short) 5;
		case RED:
			return (short) 14;
		default:
			return (short) 15;
		
		}
	}
	
	public int getAnimationSlot(int i) {
		return i + 18;
	}
	
	public void clearBids() {
		this.redBids.clear();
		this.blackBids.clear();
		this.greenBids.clear();
	}
	
	public void payOut(List<Bid> bids, BidColor color) {
		double multiplier = getMultiplier(color);
		
		for(Bid bid : bids) {
			User target = getUser(bid.getBidder());
			UserData data = target.getData();
			double payOut = bid.getAmount() * multiplier;
			data.setBalance(data.getBalance() + payOut);
			bid.getBidder().sendMessage("You won " + multiplier + "x your bid, which is " + payOut + "$");
		}
	}
	
	public double getMultiplier(BidColor color) {
		switch(color) {
		case BLACK:
			return blackMultiplier;
		case GREEN:
			return greenMultiplier;
		case RED:
			return redMultiplier;
		default:
			return 1.0;
		}
	}
	
	public User getUser(Player player) {
		UserRepository repo = Container.get(UserRepository.class);
		if(player.isOnline()) {
			return repo.getUser(player.getUniqueId());
		} else {
			return repo.offsetGet(player.getUniqueId());
		}
	}
	
	public void resetCountdown() {
		this.countdown = 15;
		int randomSpins = randomInt(minSpins, maxSpins);
		Bukkit.broadcastMessage("The initially chosen spin amount was: " + randomSpins + ". Adjusting...");
		getNewChosen();
		this.totalspins = wheelHandler.correctSpins(randomSpins, chosen);
		Bukkit.broadcastMessage("Total spins next round: " + totalspins + " Color chosen is: " + chosen.toString()); 
		pauseCountdown = false;
		updateClockTimer();
	}
	
	private int randomInt(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max + 1); 
	}
	
	private int getStage(int gone, int total) {
		int percentage = (int) (gone * 100.0f / total);
		int stage = 0;
		if(percentage < 20)
			stage = 0;
		else if(percentage > 20 && percentage < 30)
			stage = 1;
		else if(percentage > 30 && percentage < 40)
			stage = 2;
		else if(percentage > 40 && percentage < 50)
			stage = 3;
		else if(percentage > 50 && percentage < 60)
			stage = 4;
		else if(percentage > 60 && percentage < 70)
			stage = 5;
		else if(percentage > 70 && percentage < 80)
			stage = 6;
		else if(percentage > 80 && percentage < 90)
			stage = 7;
		else if(percentage > 90 && percentage < 95)
			stage = 8;
		else if(percentage > 95 && percentage <= 100)
			stage = 9;
		else if(percentage > 100) {
			int closest = getClosest(chosen, rouletteItems.indexOf(rouletteInventory.getItem(13)));
			if(closest > 10 && closest < 20) {
			} else if(closest > 6 && closest < 10) {
				stage = 11;
			} else if(closest > 3 && closest < 6) {
				stage = 12;
			} else {
				stage = 13;
			}

		}
		//Bukkit.broadcastMessage("Percentage: " + percentage + " Stage result: " + stage);

		return stage;
	}
	
	private int getTicksBetween(int stage) {
		//int modulo = 2;
		//Bukkit.broadcastMessage("TIME & PERCENTAGE: " + System.currentTimeMillis() + percentage);
		int modulo = stage + 1;
		/*if(stage == 0)
			return 1;
		else if(stage == 1)
 			return 2;
 		else if(stage == 2) 
 			return 3;
 		else if(stage == 3)
 			return 4;
 		else if(stage == 4) 
			return 5;
 		else if(stage == 5)
			return 6;
 		else if(stage == 6)
			return 7;
 		else if(stage == 7)
			return 8;
 		else if(stage == 8)
			return 9;
 		else if(stage == 9)
			return 10;
 		else if(stage == 10)
 			return 11;*/
		return modulo;
	}

	public void open(Player sender) {
		sender.openInventory(rouletteInventory);
		
	}
	
	public void stopRoulette() {
		if(running) {
			if(pauseCountdown) {
				//this.game.cancel();
			} else {
				this.runner.cancel();
			}
			running = false;
		}
	}
	
	public void startRoulette() {
		if(pauseCountdown && running == false) {
			pauseCountdown = false;
			this.runner.runTaskTimerAsynchronously(Container.get(Plugin.class), 0, 20);
			running = true;
		}
	}
	
	public void addBid(Bid bid, BidColor color) {
		if(pauseCountdown)
			return;
		UserData data = getUser(bid.getBidder()).getData();
		data.setBalance(data.getBalance() - bid.getAmount());
		switch(color) {
		case BLACK:
			this.blackBids.add(bid);
			Collections.sort(this.blackBids);
			return;
		case GREEN:
			this.greenBids.add(bid);
			Collections.sort(this.greenBids);
			return;
		case RED:
			this.redBids.add(bid);
			Collections.sort(this.redBids);
			return;
		default:
			return;
		}
	}
	
	public boolean running() {
		return running;
	}
	
	public ItemStack setTopBidsOnlore(ItemStack is, BidColor color) {
		ItemMeta im = is.getItemMeta();
		im.setLore(getTopBids(5, color));
		is.setItemMeta(im);
		return is;
	}
	
	public List<String> getTopBids(int amount, BidColor color) {
		List<String> list = new ArrayList<>();
		List<Bid> bids;
		String colorString;
		switch(color) {
		case BLACK:
			bids = ListUtil.getTop(this.blackBids, 5);
			colorString = "&7";
			break;
		case GREEN:
			bids = ListUtil.getTop(this.greenBids, 5);
			colorString = "&a";
			break;
		case RED:
			bids = ListUtil.getTop(this.redBids, 5);
			colorString = "&c";
			break;
		default:
			bids = new ArrayList<>();
			colorString = "&9";
			break;
		}
		if(!bids.isEmpty()) {
			int i = 1;
			for(Bid bid : bids) {
				
				list.add(ChatColor.translateAlternateColorCodes('&', 
						colorString +
						i + 
						". " + 
						bid.getBidder().getName() + 
						": " + 
						bid.getAmount())
						);
				
				i++;
			}
		}
		return list;
	}
	
	
	
	
	public boolean canBid(Player player, double amount) {
		if(bidsContain(player))
			return false;
		if(pauseCountdown)
			return false;
		if(getUser(player).getData().getBalance() < amount)
			return false;
		return true;
	}
	
	public boolean bidsContain(Player player) {
		if(bidListContains(redBids, player) || bidListContains(greenBids, player) || bidListContains(blackBids, player))
			return true;
		return false;
	}
	
	public boolean bidListContains(List<Bid> list, Player player) {
		for(Bid bid : list) {
			if(bid.getBidder().equals(player))
				return true;
		}
		return false;
	}
	
	public int getSpins(BidColor color, int baseSpins) {
		//int index = rouletteItems.indexOf(rouletteInventory.getItem(13));
		int index = new Integer(this.pointer);
		int i = 0;
		while(i < baseSpins || BidColor.getByItemStack(rouletteItems.get(ListUtil.getIndexAfterIterations(rouletteItems, index, i))) != color) {
			i++;
		}
		
		Bukkit.broadcastMessage("Should land on index:" + ListUtil.getIndexAfterIterations(rouletteItems, index, i));
		
		return i;
		
	}
	
	public int getClosest(BidColor color, int index) {
		int iterations = 0;
		while (BidColor.getByItemStack(rouletteItems.get(index)) != color) {
			index = ListUtil.getIndexAfterIterations(rouletteItems, index, 1);
			iterations++;
		}
		return iterations;
	}
	
	public int getClosest2(BidColor color, int index, int spins) {
		while(BidColor.getByItemStack(rouletteItems.get(ListUtil.getIndexAfterIterations(rouletteItems, index, spins))) != color) {
			spins++;
		}
		return spins;
	}

	public Inventory getInventory() {
		return this.rouletteInventory;
	}
	
}
