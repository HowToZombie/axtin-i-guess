package org.axtin.modules.managing.moderation.punishments;

import org.axtin.command.CommandErrors;
import org.axtin.util.ArrayToMessage;
import org.axtin.util.UUIDFetcher;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;

/**
 * Created by Matthew on 26/03/2017.
 */
public class AxtinBan extends PunishmentManager{

    public AxtinBan() {
        super("ban", "Bans a player", "/ban <player> <reason>", "pb", "smitetheedown");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {

        if(!this.doChecks(commandSender, strings, 2, "axtin.ban")) return true;

        Player player = Bukkit.getPlayer(strings[0]);

        if(player==null){
            this.banOfflinePlayer(commandSender,strings);
            return true;
        }

        String banMessage = ChatColor.translateAlternateColorCodes('&',"&c" + ArrayToMessage.formatStringsStatic(Arrays.copyOfRange(strings, 1, strings.length)));

        this.issueBan(player.getName(), banMessage, commandSender.getName());
        player.kickPlayer(banMessage);

        return true;


    }

    private void banOfflinePlayer(CommandSender sender, String[] strings){

        String playerName = "";

        try {
            UUID uuid = UUIDFetcher.getUUIDOf(strings[0]);
            playerName = Bukkit.getOfflinePlayer(uuid).getName();
        } catch (Exception e) {
            sender.sendMessage(CommandErrors.NO_TARGET.toString());
            return;
        }

        String banMessage = ChatColor.translateAlternateColorCodes('&',"&c" + ArrayToMessage.formatStringsStatic(Arrays.copyOfRange(strings, 1, strings.length)));

        this.issueBan(playerName, banMessage, sender.getName());

    }

    private void issueBan(String playerName, String banMessage, String banner){
        Bukkit.getBanList(BanList.Type.NAME).addBan(playerName,banMessage,null,banner);
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&c" + playerName + "has been banned for " + banMessage + " by " + banner));
    }

}
