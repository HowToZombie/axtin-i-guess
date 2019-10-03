package org.axtin.modules.managing.moderation.punishments;

import org.axtin.command.AxtinCommand;
import org.axtin.command.CommandErrors;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Created by Matthew on 26/03/2017.
 */
public abstract class PunishmentManager extends AxtinCommand {

    private final String usage;

    public String getUsage(){
        return this.usage;
    }

    public PunishmentManager(String name) {
        super(name);
        this.usage = ChatColor.translateAlternateColorCodes('&',"&c/" + name);
    }

    public PunishmentManager(String name, String description, String usage, List<String> aliases) {
        super(name, description, usage, aliases);
        this.usage = usage;
    }

    public PunishmentManager(String name, String description, String usage, String... aliases){
        super(name, description, usage, aliases);
        this.usage = usage;
    }

    public boolean doChecks(CommandSender commandSender, String[] strings, int minArgs, String permission){
        if(!commandSender.hasPermission(permission)){
            commandSender.sendMessage(CommandErrors.NO_PERMISSION.toString());
            return false;
        }

        if(strings.length<minArgs){
            commandSender.sendMessage(this.getUsage());
            return false;
        }
        return true;
    }


}
