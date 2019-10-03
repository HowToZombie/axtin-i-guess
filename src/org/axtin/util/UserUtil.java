package org.axtin.util;

import org.bukkit.entity.Player;

public class UserUtil {
    public static int getPing(Player player) {
        try {
            return (Integer) ReflectionUtil.execute("getHandle().ping", player).fetch();
        } catch (Exception ignore) {
            return 0;
        }
    }

    public static String getLocale(Player player) {
        try {
            return (String) ReflectionUtil.execute("getHandle().locale", player).fetch();
        } catch (Exception ignore) {
            return "en_US";
        }
    }
}
