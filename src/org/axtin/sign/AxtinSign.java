package org.axtin.sign;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;

public interface AxtinSign {
    String getName();

    void onCreate(SignChangeEvent event);

    void onClick(Player player, Sign sign);

    void onDestroy(BlockBreakEvent event);
}
