package org.axtin.modules.pickaxe.upgradepoints;

import org.axtin.container.facade.Container;
import org.axtin.user.UserRepository;
import org.axtin.user.role.PrisonRole;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

/**
 * Created by Joseph on 5/7/2017.
 */
public class UpgradeGUI implements Listener {

    //Zombie - tell me if you use prison roles, thanks!

    @EventHandler
    public void GUI(InventoryClickEvent e){
        if(e.getWhoClicked().getOpenInventory().getTopInventory().getName().contains("Upgrade")){
            if(e.getSlot() == 2){
                if(e.getCurrentItem() != null) {
                	
                }
            }
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

    private void openUpgradeInventory(Player p){
        Inventory inv = Bukkit.createInventory(null, 5, "" + ChatColor.BLUE + ChatColor.UNDERLINE + "Upgrade Selection");
        ItemStack filler = new ItemStack(Material.STAINED_GLASS_PANE,1,(short) 14);
        inv.setItem(0,filler);
        inv.setItem(1,filler);
        inv.setItem(3,filler);
        inv.setItem(4,filler);



        UserRepository repo = Container.get(UserRepository.class);




       // for(int i = 0; i <=rank+1 ; i++){
            //PrisonRole current = PrisonRole.getRole(i-1);
           // ItemStack item = new ItemStack(current.getDrops().getDrops().get(0));
           // ItemMeta meta = item.getItemMeta();
          //  meta.setDisplayName(ChatColor.BLUE + current.getName());
         //   ArrayList<String> lore = new ArrayList<>();
/*
            if(current.getDrops().rawMaterials().length < 2){
                lore.add(0, getProperName(current.getDrops().rawMaterials()[0]) + "- " + getProperStats(current.getDrops().rawStats()[0]));
            }else{
                lore.add(0, getProperName(current.getDrops().rawMaterials()[0]) + "- " + getProperStats(current.getDrops().rawStats()[0]));
                lore.add(1, getProperName(current.getDrops().rawMaterials()[1]) + "- " + getProperStats(current.getDrops().rawStats()[1]));
                lore.add(2, getProperName(current.getDrops().rawMaterials()[2]) + "- " + getProperStats(current.getDrops().rawStats()[2]));
            }
            */
          //  meta.setLore(lore);
           // item.setItemMeta(meta);
          //  inv.setItem(i,item);
       // }
        p.openInventory(inv);

    }

    private ItemStack addBookEnchantment(ItemStack item, Enchantment enchantment, int level){
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
        meta.addStoredEnchant(enchantment, level, true);
        item.setItemMeta(meta);
        return item;
    }
}
