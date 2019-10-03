package org.axtin.modules.misccommands;

import org.axtin.command.AxtinCommand;
import org.axtin.util.UserUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class LanguageCommand extends AxtinCommand {
    public LanguageCommand() {
        super("language");

        this.setAliases(Arrays.asList("lang", "locale"));
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        Player player = this.search(sender.getName());
        player.sendMessage(ChatColor.GRAY + "Language: " + ChatColor.GREEN + UserUtil.getLocale(player));

        return true;
    }
}
