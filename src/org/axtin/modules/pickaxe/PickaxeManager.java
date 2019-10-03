package org.axtin.modules.pickaxe;

import org.axtin.command.AxtinCommand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

/**
 * Created by Joseph on 4/26/2017.
 */
public enum PickaxeManager {

    NONE(-1,0, Material.BARRIER),
    WOOD(0,64,Material.WOOD_PICKAXE),
    STONE(1,256,Material.STONE_PICKAXE),
    IRON(2,1024,Material.IRON_PICKAXE),
    GOLD(3,4096,Material.GOLD_PICKAXE),
    DIAMOND(4,16384,Material.DIAMOND_PICKAXE);


    private int identifier;
    private int basexp;
    private Material material;

    PickaxeManager(int identifier,int xp, Material m){
        this.identifier = identifier;
        this.material = m;
        this.basexp = xp;
    }

    public static PickaxeManager getPickaxe(int identifier){
        for(PickaxeManager pm : PickaxeManager.values()){
            if(pm.identifier == identifier){
                return pm;
            }
        }
        return PickaxeManager.WOOD;
    }

    public static PickaxeManager getPickaxe(Material m){
        for(PickaxeManager pm : PickaxeManager.values()){
            if(pm.material == m){
                return pm;
            }
        }
            return PickaxeManager.NONE;
    }
    public static String getXpBar(int earned, int t){
        String xpbar = ChatColor.BLUE + "[";
        double total = t;
        int colored = (int) ((earned/total)*10);
        for(int i = 0; i <10; i++){
            if(colored !=0){
                xpbar += ChatColor.GREEN + "|";
                colored --;
            }else{
                xpbar += ChatColor.RED + "|";
            }
        }
        xpbar += ChatColor.BLUE + "]";
        return xpbar;
    }

    public int getIdentifier(){
        return identifier;
    }

    public int getBasexp(){ return basexp;}


    public ItemStack getPickaxeWithLevel(int level, int points){
        ItemStack i = new ItemStack(material, 1);
        ItemMeta meta = i.getItemMeta();
       // meta.setUnbreakable(true);
        ArrayList<String> lore = new ArrayList<>();
        lore.add(0,ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Level " + level);
        lore.add(1,getXpBar(0,basexp*level));
        lore.add(2,ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "XP: 0/" + basexp*level);
        lore.add(3, ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Upgrade Points: " + points);
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        i.setItemMeta(meta);
        return i;
    }

    public ItemStack getPickaxeWithLevel(int level){ return getPickaxeWithLevel(level, 0); }
}
/*
Level 1
[||||||||||]
XP: 0/64
Available Upgrade Points: 0
 */

/*
Shift Right Click Pick to open upgrade GUI
[EFF1][F1][C1][Cb1][Silk1]
[EFF2][F2][C2][Cb2[]

 */