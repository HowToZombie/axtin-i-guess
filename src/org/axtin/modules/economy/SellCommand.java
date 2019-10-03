package org.axtin.modules.economy;

import org.axtin.command.AxtinCommand;
import org.axtin.container.facade.Container;
import org.axtin.user.User;
import org.axtin.user.UserRepository;
import org.axtin.util.HologramsPacket;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Joseph on 4/13/2017.
 */

public class SellCommand extends AxtinCommand {

    private static HashMap<Material,Integer> shop = new HashMap<>();


    public SellCommand() {
        super("sell");
        fillShop();
    }

    private void fillShop(){
        shop.clear();
        shop.put(Material.COBBLESTONE, 64);
        shop.put(Material.COAL, 5120);
        shop.put(Material.COAL_BLOCK, 46080);
        shop.put(Material.IRON_INGOT, 15360);
        shop.put(Material.IRON_BLOCK, 138240);
        shop.put(Material.GOLD_INGOT, 38400);
        shop.put(Material.GOLD_BLOCK, 345600);
        shop.put(Material.DIAMOND, 76800);
        shop.put(Material.DIAMOND_BLOCK, 691200);
        shop.put(Material.EMERALD, 153600);
        shop.put(Material.EMERALD_BLOCK, 1382400);
        shop.put(Material.OBSIDIAN, 307200);
        /*shop.put(Material.COBBLESTONE, 1);
        shop.put(Material.IRON_NUGGET, 2);
        shop.put(Material.COAL, 4);
        shop.put(Material.GOLD_NUGGET, 8);
        shop.put(Material.IRON_INGOT, 18);
        shop.put(Material.REDSTONE, 32);
        shop.put(Material.GOLD_INGOT, 72);
        shop.put(Material.QUARTZ, 128);
        shop.put(Material.DIAMOND, 256);
        shop.put(Material.PRISMARINE_SHARD, 512);
        shop.put(Material.EMERALD, 1024);
        shop.put(Material.PRISMARINE_CRYSTALS, 2048);
        shop.put(Material.NETHER_WARTS, 4096);
        shop.put(Material.GLOWSTONE_DUST, 8192);
        shop.put(Material.NETHER_BRICK_ITEM, 16384);
        shop.put(Material.BLAZE_POWDER, 32768);
        shop.put(Material.BLAZE_ROD, 65536);
        shop.put(Material.FIREBALL, 131072);
        shop.put(Material.SLIME_BALL, 262144);
        shop.put(Material.MAGMA_CREAM, 524288);
        shop.put(Material.ENDER_PEARL, 1048576);
        shop.put(Material.CHORUS_FRUIT, 2097152);
        shop.put(Material.EYE_OF_ENDER, 4194304);
        shop.put(Material.CHORUS_FRUIT_POPPED, 8388608);
        shop.put(Material.SHULKER_SHELL, 16777216);
        shop.put(Material.NETHER_STAR, 33554432);
        shop.put(Material.END_CRYSTAL, 67108864);*/
    }
    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        Player p = (Player) commandSender;
        Inventory inv = p.getInventory();
        double gained = 0.0;
        for(ItemStack i: inv.getContents()){
            try {
                if (shop.containsKey(i.getType()) && !i.hasItemMeta()) {
                    int stackWorth = shop.get(i.getType());
                    double prize = (stackWorth / 64.0) * i.getAmount();
                    inv.remove(i);
                    gained += prize;

                }
            }catch(NullPointerException e){
                //Empty slot
            }
        }
        User user = Container.get(UserRepository.class).getUser(p.getUniqueId());
        user.getData().setBalance((user.getData().getBalance()/1.0) + gained);
        p.sendMessage("All items sold for $" + gained);
       // new HologramsPacket(p.getLocation(),test).display(p);
        //new HologramsPacket(ChatColor.GOLD + "" + ChatColor.BOLD + p.getName() + " Welcome to the server!", "Do /help if you need any help!").show(p.getLocation());


        return false;
    }
}
