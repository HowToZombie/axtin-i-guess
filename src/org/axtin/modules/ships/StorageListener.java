package org.axtin.modules.ships;

import org.axtin.container.facade.Container;
import org.axtin.user.User;
import org.axtin.user.UserRepository;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static org.axtin.modules.ships.Storage.openStorage;

/**
 * Created by zombi on 7/4/2017.
 */
public class StorageListener implements Listener {

    @EventHandler
    public void inventoryClose(InventoryCloseEvent e) {

        if (e.getInventory().getName().contains("Storage")) {

            String name = ChatColor.stripColor(e.getInventory().getName());
            User u = Container.get(UserRepository.class).getUser(Bukkit.getPlayer(name.split("'")[0]).getUniqueId());
            int page;
            try {
                page = Integer.parseInt(name.split("\\s")[4]);
            } catch (ArrayIndexOutOfBoundsException ex) {
                page = 1;
            }
            Storage s = Storage.storages.get(u);
            int rows = s.getRows();

            if (e.getInventory().getSize() / 9 == 6) {
                for (int i = (page - 1) * 45; i < page * 45; i++) {
                    s.addItem(e.getInventory().getItem(i - ((page - 1) * 45)), i);
                }
            } else {
                if (page > 1) {
                    for (int i = (page - 1) * 45; i < ((page - 1) * 45) + e.getInventory().getSize() - 9; i++) {
                        s.addItem(e.getInventory().getItem(i - ((page - 1) * 45)), i);
                    }
                } else {
                    for (int i = 0; i < rows * 9; i++) {
                        s.addItem(e.getInventory().getItem(i), i);
                    }
                }
            }

        }

    }

    /*@EventHandler
    public void inventoryClickEvent(InventoryClickEvent e) {

        if (e.getClickedInventory().getName().contains("Storage")) {

            String name = ChatColor.stripColor(e.getClickedInventory().getName());
            User u = Container.get(UserRepository.class).getUser(Bukkit.getPlayer(name.split("'")[0]).getUniqueId());

            Storage s = Storage.storages.get(u);
            int rows = s.getRows();
            int pages = new Double(Math.ceil(rows / 5.0)).intValue();
            int lastPageRows = (rows % 5);
            if (lastPageRows == 0) {
                lastPageRows = 5;
            }

            ItemStack pageItem = new ItemStack(Material.EYE_OF_ENDER);
            ItemMeta pageMeta = pageItem.getItemMeta();
            pageMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                    "&6&lPage " + pages));
            pageItem.setItemMeta(pageMeta);
            pageItem.setAmount(pages);

            int page = 1;
            if (e.getClickedInventory().getSize() > 5 * 9 || e.getClickedInventory().contains(pageItem)) {
                page = Integer.parseInt(name.split("\\s")[4]);
            }

            if (e.getSlot() > 44) {

                e.setCancelled(true);

                if (e.getSlot() == 45 && page != 1) {

                    for (int i = (page - 1) * 45; i < page * 45; i++) {
                        s.addItem(e.getInventory().getItem(i - (page - 1) * 45), i);
                    }

                    int newPage = e.getCurrentItem().getAmount();
                    openStorage(u, (Player) e.getWhoClicked(), newPage);

                } else if (e.getSlot() == 53 && page != pages) {

                    for (int i = (page - 1) * 45; i < page * 45; i++) {
                        s.addItem(e.getInventory().getItem(i - (page - 1) * 45), i);
                    }

                    int newPage = e.getCurrentItem().getAmount();
                    openStorage(u, (Player) e.getWhoClicked(), newPage);

                }

            } else if (page == pages) {

                if (e.getSlot() >= e.getClickedInventory().getSize() - 9) {

                    e.setCancelled(true);

                    if (e.getSlot() == e.getClickedInventory().getSize() - 9) {

                        for (int i = (page - 1) * 45; i < ((page - 1) * 45) + e.getClickedInventory().getSize() - 9; i++) {
                            s.addItem(e.getInventory().getItem(i - ((page - 1) * 45)), i);
                        }

                        int newPage = e.getCurrentItem().getAmount();
                        openStorage(u, (Player) e.getWhoClicked(), newPage);

                    }

                }

            }
        }

    }*/

}
