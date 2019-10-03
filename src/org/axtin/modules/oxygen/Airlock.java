package org.axtin.modules.oxygen;

import org.axtin.modules.mines.Cuboid;
import org.bukkit.Location;

import java.util.HashMap;

/**
 * Created by zombi on 5/29/2017.
 */
public class Airlock {
    public static HashMap<String, Cuboid> areas = new HashMap<>();

    public Airlock(String name, Location minPoint, Location maxPoint) {
        Cuboid c = new Cuboid(minPoint, maxPoint);
        areas.put(name, c);
    }
}
