package org.axtin.modules.mining;

import org.axtin.command.AxtinCommand;
import org.axtin.container.facade.Container;
import org.axtin.user.UserRepository;
import org.axtin.user.role.PrisonRole;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

/**
 * Created by Joseph on 4/25/2017.
 */
public class MiningSelectorCommand extends AxtinCommand implements Listener{
    public MiningSelectorCommand() {
        super("mine");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        Player p = (Player) commandSender;
        openInventory(p);
        return false;
    }

    @EventHandler
    public void GUI(InventoryClickEvent e){
            if(e.getWhoClicked().getOpenInventory().getTopInventory().getName().contains("Mining")){
            e.setCancelled(true);
            if(e.getCurrentItem().getType() !=null){
                ItemStack i = e.getCurrentItem();
                if(i.hasItemMeta()){
                    ItemMeta meta = i.getItemMeta();
                    String rank = ChatColor.stripColor(meta.getDisplayName());
                    Container.get(UserRepository.class).getUser(e.getWhoClicked().getUniqueId()).getData().setMining(PrisonRole.getRole(rank.toLowerCase()));
                    e.getWhoClicked().closeInventory();
                    e.getWhoClicked().sendMessage(ChatColor.GOLD  + "You have selected the "  + ChatColor.BLUE + meta.getDisplayName() + " Mine.");
                }
            }
        }
    }

    @EventHandler
    public void ShiftRightClick(PlayerInteractEvent e){
        if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            if(e.getPlayer().isSneaking()){
                if(e.getPlayer().getItemInHand() !=null){
                   ItemStack hand =  e.getPlayer().getItemInHand();
                    if(hand.hasItemMeta()){
                        if(hand.getItemMeta().hasLore()){
                            if(hand.getItemMeta().getLore().get(0).contains("Level")){
                                openInventory(e.getPlayer());
                            }
                        }
                    }
                }
            }
        }
    }

    private void openInventory(Player p){
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.UNDERLINE + "" + ChatColor.GOLD + "Mining Material Selection");
        UserRepository repo = Container.get(UserRepository.class);
        int rank = repo.getUser(p.getUniqueId()).getData().getPrisonRole().getIdentifier();
        //if (rank != 0) {
            int offset = 0;
            for (int i = 0; i <= rank + 1; i++) {
                if (i != 1) {
                    PrisonRole current = PrisonRole.getRole(i - 1);
                    ItemStack item = new ItemStack(current.getDrops().getDrops().get(0));
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ChatColor.BLUE + current.getName());
                    ArrayList<String> lore = new ArrayList<>();
                    if (current.getDrops().rawMaterials().length < 2) {
                        lore.add(0, getProperName(current.getDrops().rawMaterials()[0]) + "- " + getProperStats(current.getDrops().rawStats()[0]));
                    } else {
                        lore.add(0, getProperName(current.getDrops().rawMaterials()[0]) + "- " + getProperStats(current.getDrops().rawStats()[0]));
                        lore.add(1, getProperName(current.getDrops().rawMaterials()[1]) + "- " + getProperStats(current.getDrops().rawStats()[1]));
                        lore.add(2, getProperName(current.getDrops().rawMaterials()[2]) + "- " + getProperStats(current.getDrops().rawStats()[2]));
                    }
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    inv.setItem(i - offset, item);
                } else {
                    offset = 1;
                }
            }
        /*} else {
            PrisonRole current = PrisonRole.A;
            ItemStack item = new ItemStack(current.getDrops().getDrops().get(0));
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.BLUE + "Starter");
            ArrayList<String> lore = new ArrayList<>();
            if (current.getDrops().rawMaterials().length < 2) {
                lore.add(0, getProperName(current.getDrops().rawMaterials()[0]) + "- " + getProperStats(current.getDrops().rawStats()[0]));
            } else {
                lore.add(0, getProperName(current.getDrops().rawMaterials()[0]) + "- " + getProperStats(current.getDrops().rawStats()[0]));
                lore.add(1, getProperName(current.getDrops().rawMaterials()[1]) + "- " + getProperStats(current.getDrops().rawStats()[1]));
                lore.add(2, getProperName(current.getDrops().rawMaterials()[2]) + "- " + getProperStats(current.getDrops().rawStats()[2]));
            }
            meta.setLore(lore);
            item.setItemMeta(meta);
            inv.setItem(0, item);
        }*/
        p.openInventory(inv);

    }

    private String getProperStats(Double d){
        double a = d * 100;
        int b = (int) a;
        return b + "%";
    }
    private String getProperName(Material m){
        String name = m.toString();
        String returning = "";
        name = name.replaceAll("_"," ");
        String[] spacing = name.split(" ");
        for(String s : spacing){
           returning += s.substring(0,1) + s.substring(1).toLowerCase() + " ";
        }
        return returning;
    }
}
