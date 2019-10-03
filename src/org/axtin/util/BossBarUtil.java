package org.axtin.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

/**
 * Created by zombi on 6/19/2017.
 */
public class BossBarUtil {

    public static BossBar makeBossBar(Player p, String msg, BarColor color, BarStyle division, BossBar oldBar) {
        oldBar.removePlayer(p);
        String s = ChatColor.translateAlternateColorCodes('&', msg);
        BossBar b = Bukkit.getServer().createBossBar(s, color, division);
        b.addPlayer(p);
        return b;
    }

    public static void removeBossBar(Player p, BossBar b) {
        b.removePlayer(p);
    }

}
