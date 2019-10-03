package org.axtin.modules.misccommands;

import org.axtin.command.AxtinCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Matthew on 26/03/2017.
 */
public class AxtinHelp extends AxtinCommand {


    private final List<String> helpList;

    public AxtinHelp() {
        super("help", "Gives a useful list of help", "/help", "helpme", "ohgodineedhelp","thefuck");
        this.helpList = new LinkedList<>();
        this.configureHelpList();
    }

    //TODO: Consider making help list configurable in a config file
    private void configureHelpList(){
        helpList.add("&7---&6Axtin Help Page&7---");
        helpList.add("&6- &7Use /kits to see what kits you have!");
        helpList.add("&6- &7Use /kit {name} to obtain that kit!");
        helpList.add("&6- &7If you aren't in an airlock and you don't have a helmet on, you start suffocating!");
        helpList.add("&6- &7Helmets lose oxygen outside of airlocks, but refill inside of airlocks.");
        helpList.add("&6- &7You can craft custom pickaxes.");
        helpList.add("&6- &7Sneak and Right Click with your pickaxe in your hand to select what you mine.");
        helpList.add("&6- &7Sell your items with /sell.");
        helpList.add("&6- &7Rankup with /rankup.");
        helpList.add("&6- &7Sneak on a telepad to teleport!");
        helpList.add("&6- &7Telepads are marked with beacons.");
        //helpList.add("&6- &7Use /kits to see what kits you have!");

    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {

        this.helpList.forEach(m -> commandSender.sendMessage(message(m)));
        return true;

    }

    private String message(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
