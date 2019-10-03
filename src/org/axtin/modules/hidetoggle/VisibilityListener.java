package org.axtin.modules.hidetoggle;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class VisibilityListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		VisibilityToggleCommand.checkHides();
	}
	
	
}
