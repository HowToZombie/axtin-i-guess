package org.axtin.modules.launch;

import org.axtin.command.AxtinCommand;
import org.axtin.command.CommandErrors;
import org.axtin.container.facade.Container;
import org.axtin.user.UserRepository;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;


/**
 * Created by Joseph on 3/4/2017.
 */
public class LaunchCommand extends AxtinCommand {
    public LaunchCommand() {
        super("launch");
    }


    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        Player p = (Player) commandSender;
        if (Container.get(UserRepository.class).getUser(p.getUniqueId()).getData().getStaffRole().getIdentifier() < 21) {
            p.sendMessage(CommandErrors.NO_PERMISSION.toString());
            return true;
        }
        Location l = p.getLocation();
        Launch(p, new Location(p.getWorld(), 0, 80, 0));
        return false;
    }

    public void Launch(Player p, Location secLoc) {
        Vector v = calculateVelocity(p.getLocation().toVector(), secLoc.toVector(), 20);
        double horiz = v.getX();
        v.setX(horiz);
        p.setVelocity(v);
    }

    public static Vector calculateVelocity(Vector from, Vector to, int heightGain)
    {

        double gravity = 0.037;
        // Block locations
        int endGain = to.getBlockY() - from.getBlockY();
        double horizDist = Math.sqrt(distanceSquared(from, to));
        // Height gain
        int gain = heightGain;
        double maxGain = gain > (endGain + gain) ? gain : (endGain + gain);
        // Solve quadratic equation for velocity
        double a = -horizDist * horizDist / (4 * maxGain);
        double b = horizDist;
        double c = -endGain;
        double slope = -b / (2 * a) - Math.sqrt(b * b - 4 * a * c) / (2 * a);
        // Vertical velocity
        double vy = Math.sqrt(maxGain * gravity);
        // Horizontal velocity
        double vh = vy / slope;
        // Calculate horizontal direction
        int dx = to.getBlockX() - from.getBlockX();
        int dz = to.getBlockZ() - from.getBlockZ();
        double mag = Math.sqrt(dx * dx + dz * dz);
        double dirx = dx / mag;
        double dirz = dz / mag;
        // Horizontal velocity components
        double vx = vh * dirx;
        double vz = vh * dirz;
        return new Vector(vx, vy, vz).normalize().multiply(24);
    }

    private static double distanceSquared(Vector from, Vector to)
    {
        double distanceX = to.getBlockX() - from.getBlockX();
        double distanceZ = to.getBlockZ() - from.getBlockZ();
        return distanceX * distanceX + distanceZ * distanceZ;
    }

}
