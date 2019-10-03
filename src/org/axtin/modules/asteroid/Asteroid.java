package org.axtin.modules.asteroid;

import org.axtin.container.facade.Container;
import org.bukkit.Location;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Random;

/**
 * Created by charles on 2/24/2017.
 */
public class Asteroid {
    private int d;
    private List<ItemStack> mats;
    private Location pos;
    private Player player;
    private FallingBlock[] blocks;

    public Asteroid(int d, List<ItemStack> mats, Player player) {
        this.d = d;
        this.mats = mats;
        this.player = player;
        this.pos = player.getLocation();
        placeBlocks();
    }


    @SuppressWarnings("deprecation")
    private void placeBlocks() {
        pos.add(50,50,0);
        Location blockPos = new Location(player.getWorld(),pos.getX(), pos.getY(), pos.getZ());
        blocks = new FallingBlock[d*d*d];
        Random rand = new Random();
        int count = 0;
        for (int i = 0; i < d; i++) {
            for (int j = 0; j < d; j++) {
                for (int k = 0; k < d; k++) {
                    blockPos.setX(pos.getX() + i);
                    blockPos.setY(pos.getY() + j);
                    blockPos.setZ(pos.getZ() + k);
                    int pick = rand.nextInt(mats.size());
                    blocks[count] = player.getWorld().spawnFallingBlock(blockPos, mats.get(pick).getData());
                    blocks[count].setDropItem(false);
                    blocks[count].setGlowing(true);
                    ChangeDetect.falling.add(blocks[count].getEntityId());
                    count++;
                }
            }
        }
        blocks[0].setMetadata("explode", new FixedMetadataValue(Container.get(Plugin.class), "true"));
    }

    public void setVel(Vector v) {
        for(FallingBlock block : blocks) {
            block.setVelocity(v);
        }
    }
}
