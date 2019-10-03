package org.axtin.modules.tokenshop;

import org.apache.commons.lang.StringUtils;
import org.axtin.command.AxtinCommand;
import org.axtin.container.facade.Container;
import org.axtin.user.User;
import org.axtin.user.UserData;
import org.axtin.user.UserRepository;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * Created by Jo on 3/1/2016.
 */
public class TokenCommand extends AxtinCommand {
    public TokenCommand() {
        super("tokens");
        this.setAliases(Arrays.asList(new String[]{"token"}));

    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        Player p = (Player) sender;
        User user = Container.get(UserRepository.class).getUser(p.getUniqueId());
        UserData data = user.getData();

        if(args.length == 0){
            p.sendMessage(ChatColor.GOLD + "Tokens: " + ChatColor.BLUE + data.getTokens());
            return true;
        }
        if(args[0].equalsIgnoreCase("balance") || args[0].equalsIgnoreCase("bal")){
            if(args.length > 1){
                if(!(Bukkit.getPlayer(args[1]) == null)) {
                    Player target = Bukkit.getPlayer(args[1]);
                    p.sendMessage(ChatColor.GOLD + target.getName() +"'s Tokens: "+ ChatColor.BLUE + data.getTokens());
                }else{
                    p.sendMessage(ChatColor.RED + "Player not found!");
                }
                }else{
                p.sendMessage(ChatColor.GOLD + "Tokens: "+ ChatColor.BLUE + data.getTokens());
            }
            return true;
        }
        if(args[0].equalsIgnoreCase("pay")){
            if(args.length > 2){
               if(!(Bukkit.getPlayer(args[1]) == null)){
                    Player target = Bukkit.getPlayer(args[1]);
                   if(StringUtils.isNumeric(args[2])){
                       Integer amount = Integer.valueOf(args[2]);
                       if(data.getTokens() >= amount){
                           data.setTokens(data.getTokens() - amount);
                           User to = Container.get(UserRepository.class).getUser(target.getUniqueId());
                           to.getData().setTokens(to.getData().getTokens() + amount);
                           p.sendMessage(ChatColor.GOLD + "You have given "+ ChatColor.BLUE + amount + ChatColor.GOLD + " of tokens to " + ChatColor.BLUE + target.getName());
                           target.sendMessage(ChatColor.GOLD + "You have received " +ChatColor.BLUE +amount + ChatColor.GOLD + " of tokens from " +ChatColor.BLUE + p.getName());
                           return true;
                       }

                   }
                }
            }else{
                p.sendMessage(ChatColor.RED + "Usage: /tokens pay <Name> Amount");
                return false;
            }
        }
        if(args[0].equalsIgnoreCase("give")){
            if(sender.hasPermission("TokenShop.give")){
                if(args.length > 2){
                    if(!(Bukkit.getPlayer(args[1])== null)){
                        if(StringUtils.isNumeric(args[2])){
                            Integer amount = Integer.valueOf(args[2]);
                            data.setTokens(data.getTokens() + amount);
                            Bukkit.getPlayer(args[1]).sendMessage(ChatColor.GOLD + "You have received " + ChatColor.BLUE +amount + ChatColor.GOLD + " tokens!");
                            sender.sendMessage(ChatColor.BLUE + "You have given "+ChatColor.GOLD +Bukkit.getPlayer(args[1]).getName() +" " + ChatColor.GREEN + amount + " Tokens");
                            return true;
                        }
                    }
                }
            }
        }
        p.sendMessage("Usage");

        return false;
    }
}
