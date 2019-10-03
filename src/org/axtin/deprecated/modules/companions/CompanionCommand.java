package org.axtin.deprecated.modules.companions;

import org.axtin.command.AxtinCommand;
import org.axtin.container.facade.Container;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CompanionCommand extends AxtinCommand {

	public CompanionCommand() {
		super("companion");
	}

	@Override
	public boolean execute(CommandSender sender, String arg1, String[] args) {
		
		Container.get(CompanionHandler.class).createCompanion((Player) sender);
		
		return true;
	}

	
	
}
