package org.axtin.modules.mining;

import org.axtin.container.facade.Container;
import org.axtin.deprecated.modules.customenchants.Enchantment;
import org.axtin.modules.mines.RankHolder;
import org.axtin.user.User;
import org.axtin.user.UserRepository;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Random;

import static org.bukkit.Material.*;

/**
 * Created by Joseph on 3/7/2017.
 */
public class MiningListener implements Listener {

    ArrayList<Block> mined = new ArrayList<>();

    @EventHandler
    public void MiningBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        User user = Container.get(UserRepository.class).getUser(p.getUniqueId());
        Enchantment fortune = new Enchantment("Fortune", 0, "fortune");
        for (RankHolder mine : RankHolder.data.values()) {
            if (mine.getCube().containsLocation(e.getBlock().getLocation())) {
                ArrayList<Material> rankDrop = user.getData().getMining().getDrops().getDrops();
                Random rand = new Random();
                int picked = rand.nextInt(100);
                if (fortune.detectEnchantment(p.getItemInHand())) {
                    int level = fortune.getLevel(p.getItemInHand());
                    int dropNumber = rand.nextInt(level + 2) - 1;
                    if (dropNumber == 0) {
                        dropNumber = 1;
                    }
                    for (int drops = 0; drops < dropNumber; drops++) {
                        if (!giveItem(p, new ItemStack(rankDrop.get(picked)))) {
                            notifyFull(p);
                        }
                    }
                } else {
                    if (!giveItem(p, new ItemStack(rankDrop.get(picked)))) {
                        notifyFull(p);
                    }
                }
                mined.add(e.getBlock());

                return;
            }
        }

        if (!p.isOp()) {
            e.setCancelled(true);
            p.sendMessage(ChatColor.RED + "You can not break here!");
        }
    }

    private boolean giveItem(Player p, ItemStack i){
        if(p.getInventory().addItem(i).isEmpty()){
            return true;
        }else{
            return false;
        }
    }

    private void notifyFull(Player p){
        String text = ChatColor.RED + "Your inventory is full";
        String subtext = ChatColor.RED + "Please run /sell to sell your items.";

        p.sendTitle(text,subtext);
        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_HARP, 100, 10);
        /*
        IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \""+ text + "\",\"color\":\"" + ChatColor.RED.name().toLowerCase() + "\"}");
        IChatBaseComponent subTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \""+ subtext + "\",\"color\":\"" + ChatColor.RED.name().toLowerCase() + "\"}");

        PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
        PacketPlayOutTitle subtitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, subTitle);
        PacketPlayOutTitle length = new PacketPlayOutTitle(5,20,5);

        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(subtitle);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(title);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(length);
*/

    }
    @EventHandler
    public void PlaceBlock(BlockPlaceEvent e){
        Player p = e.getPlayer();
        User user = Container.get(UserRepository.class).getUser(p.getUniqueId());
        for(RankHolder mine : RankHolder.data.values()){
            if(!mine.getCube().containsLocation(e.getBlock().getLocation())){
                if(p.isOp()){
                    return;
                }else{
                    e.setCancelled(true);
                    p.sendMessage(ChatColor.RED + "You can not build here!");
                }
            }
        }
    }

    @EventHandler
    public void DurabilityDamage(PlayerItemDamageEvent e){
        Player p = e.getPlayer();
        ItemStack inHand = p.getItemInHand();

        ArrayList<Material> pickaxes = new ArrayList<Material>();
        pickaxes.add(WOOD_PICKAXE);
        pickaxes.add(STONE_PICKAXE);
        pickaxes.add(IRON_PICKAXE);
        pickaxes.add(GOLD_PICKAXE);
        pickaxes.add(DIAMOND_PICKAXE);

        for (Material type : pickaxes) {
            if (inHand.getType() == type) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void BlockItemDrop(ItemSpawnEvent e) {
        Block itemLocation = e.getEntity().getLocation().getBlock();
        if (mined.contains(itemLocation)) {
            mined.remove(itemLocation);
            e.setCancelled(true);
        }
    }
}
