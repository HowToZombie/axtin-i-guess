package org.axtin.modules.ships;

import net.minecraft.server.v1_12_R1.InventoryClickType;
import org.axtin.container.facade.Container;
import org.axtin.modules.oxygen.Airlock;
import org.axtin.user.User;
import org.axtin.user.UserData;
import org.axtin.user.UserRepository;
import org.axtin.util.FileStorage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by zombi on 7/3/2017.
 */
public class Storage {

    public static HashMap<User, Storage> storages = new HashMap<>();

    private int rows;
    private User owner;
    private HashMap<Integer, ItemStack> items = new HashMap<>();
    private String fileName;
    private String directory;

    private static ItemStack back = new ItemStack(Material.REDSTONE);
    private static ItemStack next = new ItemStack(Material.ARROW);
    private static ItemStack page = new ItemStack(Material.EYE_OF_ENDER);
    private static ItemStack border = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);

    public Storage(User u) {
        this.rows = 3;
        this.owner = u;
        this.fileName = u.getPlayer().getUniqueId().toString();
        this.directory = "storage";
        FileStorage f = new FileStorage(u.getPlayer().getUniqueId().toString(), "storage");
        YamlConfiguration config = f.getConfig();
        config.set("OwnerName", u.getPlayer().getName());
        config.set("OwnerUUID", u.getData().getUniqueId().toString());
        config.set("Space", rows);
        ItemMeta borderMeta = border.getItemMeta();
        borderMeta.setDisplayName(" ");
        border.setItemMeta(borderMeta);
        f.saveConfig();
        storages.put(u, this);
    }

    public Storage(User u, HashMap<Integer, ItemStack> i, int r, FileStorage f) {
        this.fileName = u.getData().getUniqueId().toString();
        this.directory = "storage";
        this.rows = r;
        this.owner = u;
        this.items = i;
        storages.put(u, this);
        ItemMeta borderMeta = border.getItemMeta();
        borderMeta.setDisplayName(" ");
        border.setItemMeta(borderMeta);
    }

    public void setRows(int i) {
        FileStorage f = new FileStorage(fileName, directory);
        YamlConfiguration config = f.getConfig();
        this.rows = i;
        config.set("Space", this.rows);
        f.saveConfig();
        storages.put(this.owner, this);
    }

    public int getRows() {
        return this.rows;
    }

    public User getOwner() {
        return this.owner;
    }

    public HashMap<Integer, ItemStack> getItems() {
        return this.items;
    }

    public int getAmount() {
        return this.items.size();
    }

    public ItemStack getItem(int i) {
        return this.items.get(i);
    }

    public void addItem(ItemStack i, int pos) {
        FileStorage f = new FileStorage(fileName, directory);
        YamlConfiguration config = f.getConfig();
        this.items.put(pos, i);
        config.set("Items." + pos, i);
        f.saveConfig();
        storages.put(this.owner, this);
    }

    public static void loadFile(UUID uuid) {
        File dir = new File(Container.get(Plugin.class).getDataFolder() + "/storage/");

        FileStorage storage = new FileStorage(uuid.toString(), "storage");

        if (storage.configExists()) {
            YamlConfiguration config = storage.getConfig();

            int items = config.getInt("Space") * 9;
            HashMap<Integer, ItemStack> newItems = new HashMap<>();
            for (int i = 0; i < items; i ++) {
                if (config.contains("Items." + i)) {
                    newItems.put(i, config.getItemStack("Items." + i));
                }
            }

            User u = Container.get(UserRepository.class).getUser(uuid);

            new Storage(u, newItems, config.getInt("Space"), storage);

        }
    }

    public static void openStorage(User u, Player p) {

        if (!storages.containsKey(u)) {
            new Storage(u);
        }

        Inventory inv;

        Storage s = storages.get(u);
        int rows = s.getRows();

        if (rows > 5) {
            inv = Bukkit.createInventory(null, 6 * 9, ChatColor.translateAlternateColorCodes('&',
                    "&6" + u.getPlayer().getName() + "'s Storage - Page 1"));
        } else {
            inv = Bukkit.createInventory(null, rows * 9, ChatColor.translateAlternateColorCodes('&',
                    "&6" + u.getPlayer().getName() + "'s Storage"));
        }

        if (rows < 6) {
            for (int i = 0; i < rows * 9; i++) {
                if (s.getItems().containsKey(i)) {
                    inv.setItem(i, s.getItem(i));
                }
            }
        } else {

            ItemMeta backMeta = back.getItemMeta();
            backMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                    "&c&lBack"));
            back.setItemMeta(backMeta);

            ItemMeta nextMeta = next.getItemMeta();
            nextMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                    "&e&lNext Page"));
            next.setItemMeta(nextMeta);
            next.setAmount(2);

            ItemMeta pageMeta = page.getItemMeta();
            pageMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                    "&6&lPage 1"));
            page.setItemMeta(pageMeta);
            page.setAmount(1);

            for (int i = 0; i < 45; i++) {
                if (s.getItems().containsKey(i)) {
                    inv.setItem(i, s.getItem(i));
                }
            }
            inv.setItem(45, border);
            inv.setItem(46, border);
            inv.setItem(47, border);
            inv.setItem(48, border);
            inv.setItem(49, page);
            inv.setItem(50, border);
            inv.setItem(51, border);
            inv.setItem(52, border);
            inv.setItem(53, next);

        }

        p.openInventory(inv);

    }

    public static void openStorage(User u, Player p, int pageNum) {

        if (!storages.containsKey(u)) {
            new Storage(u);
        }

        Inventory inv;

        Storage s = storages.get(u);
        int rows = s.getRows();
        int pages = new Double(Math.ceil(rows / 5.0)).intValue();
        int lastPageRows = (rows % 5);
        if (lastPageRows == 0) {
            lastPageRows = 5;
        }

        if (pageNum != pages) {
            inv = Bukkit.createInventory(null, 6 * 9, ChatColor.translateAlternateColorCodes('&',
                    "&6" + u.getPlayer().getName() + "'s Storage - Page " + pageNum));
        } else if (rows > 5) {
            inv = Bukkit.createInventory(null, (lastPageRows + 1) * 9, ChatColor.translateAlternateColorCodes('&',
                    "&6" + u.getPlayer().getName() + "'s Storage - Page " + pageNum));
        } else {
            inv = Bukkit.createInventory(null, rows * 9, ChatColor.translateAlternateColorCodes('&',
                    "&6" + u.getPlayer().getName() + "'s Storage"));
        }

        if (rows < 6) {
            for (int i = 0; i < s.getItems().size(); i++) {
                if (s.getItems().containsKey(i)) {
                    inv.setItem(i, s.getItem(i));
                }
            }
        } else {

            if (pageNum != 1 && pageNum != pages) {
                ItemMeta backMeta = back.getItemMeta();
                backMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                        "&c&lBack"));
                back.setItemMeta(backMeta);
                back.setAmount(pageNum - 1);
                inv.setItem(45, back);
            } else if (pageNum == pages) {
                ItemMeta backMeta = back.getItemMeta();
                backMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                        "&c&lBack"));
                back.setItemMeta(backMeta);
                back.setAmount(pageNum - 1);
                inv.setItem(inv.getSize() - 9, back);
            } else {
                inv.setItem(45, border);
            }

            if (pageNum != pages) {
                ItemMeta nextMeta = next.getItemMeta();
                nextMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                        "&e&lNext Page"));
                next.setItemMeta(nextMeta);
                next.setAmount(pageNum + 1);
                inv.setItem(53, next);
            } else if (pageNum == pages) {
                inv.setItem(lastPageRows * 9 + 8, border);
            } else {

            }

            ItemMeta pageMeta = page.getItemMeta();
            pageMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                    "&6&lPage " + pageNum));
            page.setItemMeta(pageMeta);
            page.setAmount(pageNum);

            ItemMeta borderMeta = border.getItemMeta();
            borderMeta.setDisplayName("");
            border.setItemMeta(borderMeta);

            if (pageNum != pages) {
                for (int i = 45 * (pageNum - 1); i < 45 * pageNum; i++) {
                    if (s.getItems().containsKey(i)) {
                        inv.setItem(i - ((pageNum - 1) * 45), s.getItem(i));
                    }
                }
                inv.setItem(46, border);
                inv.setItem(47, border);
                inv.setItem(48, border);
                inv.setItem(49, page);
                inv.setItem(50, border);
                inv.setItem(51, border);
                inv.setItem(52, border);
            } else {
                for (int i = 45 * (pageNum - 1); i < ((pageNum - 1) * 45) + inv.getSize() - 9; i++) {
                    if (s.getItems().containsKey(i)) {
                        inv.setItem(i - ((pageNum - 1) * 45), s.getItem(i));
                    }
                }
                inv.setItem(inv.getSize() - 8, border);
                inv.setItem(inv.getSize() - 7, border);
                inv.setItem(inv.getSize() - 6, border);
                inv.setItem(inv.getSize() - 5, page);
                inv.setItem(inv.getSize() - 4, border);
                inv.setItem(inv.getSize() - 3, border);
                inv.setItem(inv.getSize() - 2, border);
            }

        }

        p.openInventory(inv);

    }

}
