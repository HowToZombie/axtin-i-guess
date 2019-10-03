package org.axtin.modules.gambling.coinflip;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.axtin.container.facade.Container;
import org.axtin.modules.gambling.roulette.RouletteManager;
import org.axtin.user.User;
import org.axtin.user.UserData;
import org.axtin.user.UserRepository;
import org.axtin.util.gui.paging.PageContainer;
import org.axtin.util.messages.MessageManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class CoinFlipManager {

	private List<CoinFlip> activeGames;
	private List<CoinFlipRequest> gameRequests;
	private List<GlobalCoinFlipRequest> globalRequests;
	private MessageManager manager;
	private PageContainer flips;
	//private ConfigSection test;
	
	public CoinFlipManager() {
		this.activeGames = new ArrayList<>();
		
		this.flips = new PageContainer(3);
		this.flips.createPages(new ArrayList<>());
		
		this.gameRequests = new ArrayList<>();
		
		this.globalRequests = new ArrayList<>();
		
		String propsPath = Container.get(Plugin.class).getDataFolder().getAbsolutePath() + "/Gambling/CoinFlip/Messages.properties";
		
		File propsFile = new File(propsPath);
		
		if(!propsFile.exists()) {
			try {
				MessageManager.createPropsFile(propsPath, getMessages());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			this.manager = new MessageManager(propsPath, ChatColor.GRAY, ChatColor.GOLD);
		} catch (IOException e) {
			this.manager = null;
			e.printStackTrace();
		}
//		File file = new File(Container.get(Plugin.class).getDataFolder().getAbsolutePath(), "test.yml");
//		Bukkit.broadcastMessage(file.getAbsolutePath());
//		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
//		this.test = new ConfigSection(config);
//		ConfigSection testSection = this.test.getValues().get("test").getConfigSection();
//		ConfigSection section = testSection.getValues().get("section").getConfigSection();
//		for(Entry<String, ConfigValue> e : section.getValues().entrySet()) {
//			System.out.println(e.getKey() + ": " + e.getValue().getObject().getClass().getName());
//		}
		
	}
	
	public CoinFlip getNewGame(Player player1, Player player2, double amount) {
		CoinFlip coinFlip = new CoinFlip(player1, player2, amount);
		RouletteManager manager = Container.get(RouletteManager.class);
		
		UserData pData = manager.getUser(player1).getData();
		UserData tData = manager.getUser(player2).getData();
		
		pData.setBalance(pData.getBalance() - amount);
		tData.setBalance(tData.getBalance() - amount);
		
		activeGames.add(coinFlip);
		return coinFlip;
	}
	
	public GlobalCoinFlipRequest createGlobalRequest(Player player, double amount) {
		GlobalCoinFlipRequest request = new GlobalCoinFlipRequest(player.getUniqueId(), amount);
		this.globalRequests.add(request);
		this.flips.addItem(request.getPageItem());
		return request;
	}
	
	public CoinFlipRequest getNewRequest(Player player1, Player player2, double amount) {
		CoinFlipRequest request = new CoinFlipRequest(player1, player2, amount);
		gameRequests.add(request);
		return request;
	}
	
	
	public User getUser(Player player) {
		UserRepository repo = Container.get(UserRepository.class);
		if(player.isOnline()) {
			return repo.getUser(player.getUniqueId());
		} else {
			return repo.offsetGet(player.getUniqueId());
		}
	}

	public void remove(CoinFlip coinFlip) {
		this.activeGames.remove(coinFlip);
		
	}
	
	public boolean requestExists(Player from, Player to) {
		for(CoinFlipRequest request : gameRequests) {
			if(request.getRequester() == from && request.getReceiver() == to)
				return true;
		}
		return false;
	}
	
	public boolean hasValidRequest(Player from, Player to) {
		for(CoinFlipRequest request : gameRequests) {
			if(request.getRequester() == from && request.getReceiver() == to && request.isValid())
				return true;
		}
		return false;
	}
	
	public CoinFlipRequest getRequest(Player from, Player to) {
		List<CoinFlipRequest> localRequests = new ArrayList<>();
		for(CoinFlipRequest request : gameRequests) {
			if(request.getRequester() == from && request.getReceiver() == to)
				localRequests.add(request);
		}
		
		if(localRequests.isEmpty())
			return null;
		
		if(localRequests.size() == 1)
			return localRequests.get(0);
		
		for(CoinFlipRequest request : localRequests) {
			if(request.isValid())
				return request;
		}
		
		return null;
		
	}
	
	private Map<String, String> getMessages() {
		Map<String, String> map = new HashMap<>();
		map.put("request.message", "{0} has requested a Coin Flip about {1}$. "
				+ "Do /coinflip accept {0} - to accept this coinflip. "
				+ "The request expires in {2} seconds.");
		
		map.put("player.notexists", "{0} does not exist.");
		map.put("player.notonline", "{0} is not online.");
		map.put("coinflip.challenge.usage", "/coinflip challenge {player} {amount}");
		map.put("player.balance.insufficent", "You do not have the required balance.");
		map.put("opponent.balance.insufficent", "{0} does not have the required balance.");
		map.put("target.not.requested", "{0} has not requested a Coin Flip.");
		map.put("target.request.expired", "Your request(s) from {0} has expired.");
		map.put("request.expired", "The request has expired.");
		map.put("player.accept.request", "You've accepted {0}'s request, let the game begin...");
		map.put("target.accept.request", "{0} has accepted your request, let the game begin...");
		map.put("coinflip.accept.usage", "/coinflip accept {player}");
		
		return map;
	}
	
	public MessageManager getMessageManager() {
		return this.manager;
	}
	
	public PageContainer getGlobalRequestPages() {
		return this.flips;
	}
	
	
}
