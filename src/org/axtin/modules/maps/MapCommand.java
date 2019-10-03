package org.axtin.modules.maps;

import org.axtin.command.AxtinCommand;
import org.axtin.command.CommandErrors;
import org.axtin.container.facade.Container;
import org.axtin.modules.maps.ImageMap;
import org.axtin.user.User;
import org.axtin.user.UserRepository;
import org.axtin.modules.maps.AxtinMap;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.*;

/**
 * Created by zombi on 7/6/2017.
 */
public class MapCommand extends AxtinCommand {


    public static HashMap<Player,ImageMap> tempFrames = new HashMap<>();

    public MapCommand() {
        super("map");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {

        if (!(sender instanceof Player)) {

            return true;

        }

        Player p = (Player) sender;
        if (Container.get(UserRepository.class).getUser(p.getUniqueId()).getData().getStaffRole().getIdentifier() < 21) {
            p.sendMessage(CommandErrors.NO_PERMISSION.toString());
            return true;
        }
        User u = Container.get(UserRepository.class).getUser(p.getUniqueId());
        File dir = new File(Container.get(Plugin.class).getDataFolder() + "/images/");
        if (!dir.exists()) {
            dir.mkdir();
        }

        if (u.getData().getStaffRole().getIdentifier() < 21) {

            super.sendMessage(p, "&cYou do not have permission!");
            return true;

        }

        if (args.length == 0 || args.length == 1 || args.length == 2) {

            super.sendMessage(p, "&c/map link|file {link/file name} {name of map}");
            return true;

        }

        if (args[0].equalsIgnoreCase("link")) {
            tempFrames.put(p,  new ImageMap(args[2],args[1], AxtinMap.MapType.URL));
            ItemStack frame = new ItemStack(Material.ITEM_FRAME);
            ItemMeta meta = frame.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                    "&6" + args[2] + " [&5" + tempFrames.get(p).splitImage.mapsHigh + "x" + tempFrames.get(p).splitImage.mapsWide + "&6]"));
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
            frame.setItemMeta(meta);
            p.getInventory().addItem(frame);

        } else if (args[0].equalsIgnoreCase("file")) {
            tempFrames.put(p,  new ImageMap(args[2],args[1], AxtinMap.MapType.IMAGE));
            ItemStack frame = new ItemStack(Material.ITEM_FRAME);
            ItemMeta meta = frame.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                    "&6" + args[2] + " [&5" + tempFrames.get(p).splitImage.mapsHigh + "x" + tempFrames.get(p).splitImage.mapsWide + "&6]"));
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
            frame.setItemMeta(meta);
            p.getInventory().addItem(frame);

        } else {

            super.sendMessage(p, "&c/map link|file {link/file name} {name of map}");
            return true;

        }

        return true;

    }
}
