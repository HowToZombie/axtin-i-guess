package org.axtin.util;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.world.DataException;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.IOException;
import java.io.File;


public class PasteSchematics {


        @SuppressWarnings("deprecation")
        public static void loadArea(World world, File file, Location l){

            Vector origin = new Vector(l.getX(),l.getY(),l.getZ());
            EditSession es = new EditSession(new BukkitWorld(world), 999999999);
            try {
                CuboidClipboard cc = CuboidClipboard.loadSchematic(file);

                cc.paste(es, origin, true);
            } catch (DataException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (MaxChangedBlocksException e) {
                e.printStackTrace();
            }

        }


}
