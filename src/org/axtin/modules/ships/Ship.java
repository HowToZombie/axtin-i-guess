package org.axtin.modules.ships;

import org.axtin.container.facade.Container;
import org.axtin.modules.mines.Cuboid;
import org.axtin.user.User;
import org.axtin.user.UserRepository;
import org.axtin.util.FileStorage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class Ship {
    private String fileName;
    private String directory;
    private Location teleport;
    private Location telepad;
    private Destinations dest;
    private Destinations loc;
    private int flyTime;
    private String shipName;
    private User owner;
    private Cuboid selector;
    public static HashMap<User, Ship> ships = new HashMap<>();

    public Ship(User u){

        this.telepad = new Location(Bukkit.getWorld("ShipWorld"),32.5,117,32.5,-180,0);
        this.teleport = new Location(Bukkit.getWorld("ShipWorld"),32.5,119,32.5,-180,0);
        this.shipName = "The Prometheus";
        this.owner = u;
        this.dest = Destinations.SPAWN;
        this.loc = Destinations.SPAWN;
        this.flyTime = -1;
        FileStorage f = new FileStorage(u.getData().getUniqueId().toString(), "ships");
        YamlConfiguration c = f.getConfig();
        this.fileName = u.getData().getUniqueId().toString();
        this.directory = "ships";
        this.selector = new Cuboid(new Location(Bukkit.getWorld("ShipWorld"), 31.0, 118, 22.0), new Location(Bukkit.getWorld("ShipWorld"), 34.0, 119.0, 19.0));
        c.set("Pad.World", "ShipWorld");
        c.set("Pad.X", 32.5);
        c.set("Pad.Y", 119);
        c.set("Pad.Z", 32.5);
        c.set("Pad.Yaw", -180);
        c.set("Pad.Pitch", 0);
        c.set("Selector.World", "ShipWorld");
        c.set("Selector.Pos1.X", 31.0);
        c.set("Selector.Pos1.Y", 118.0);
        c.set("Selector.Pos1.Z", 21.0);
        c.set("Selector.Pos2.X", 33.0);
        c.set("Selector.Pos2.Y", 119.0);
        c.set("Selector.Pos2.Z", 19.0);
        c.set("Name", "The Prometheus");
        c.set("OwnerUUID", u.getData().getUniqueId().toString());
        c.set("Destination", "SPAWN");
        c.set("Location", "SPAWN");
        c.set("FlyTime", -1);
        f.saveConfig();
        ships.put(u, this);

    }

    public Ship(Location l, String shipName, User u, Destinations d, Destinations loc, int time, FileStorage f, Cuboid cube){

        YamlConfiguration c = f.getConfig();
        this.teleport = l;
        this.telepad = l.clone().add(0, -2, 0);
        this.shipName = shipName;
        this.owner = u;
        this.dest = d;
        this.loc = loc;
        this.flyTime = time;
        this.selector = cube;
        this.fileName = u.getData().getUniqueId().toString();
        this.directory = "ships";
        ships.put(u, this);

    }

    public static void loadShip(UUID uuid) {
        FileStorage storage = new FileStorage(uuid.toString(), "ships");
        if (storage.configExists()) {
            YamlConfiguration c = storage.getConfig();
            User u = Container.get(UserRepository.class).getUser(uuid);
            Destinations d = Destinations.getDestination(c.getString("Destination"));
            Destinations l = Destinations.getDestination(c.getString("Location"));
            Location pad = new Location(Bukkit.getWorld(c.getString("Pad.World")), c.getDouble("Pad.X"), c.getDouble("Pad.Y"), c.getDouble("Pad.Z"), c.getInt("Pad.Yaw"), c.getInt("Pad.Pitch"));
            String name = c.getString("Name");
            Cuboid cuboid = new Cuboid(new Location(Bukkit.getWorld(c.getString("Selector.World")), c.getDouble("Selector.Pos1.X"), c.getDouble("Selector.Pos1.Y"), c.getDouble("Selector.Pos1.Z")),
                    new Location(Bukkit.getWorld(c.getString("Selector.World")), c.getDouble("Selector.Pos2.X"), c.getDouble("Selector.Pos2.Y"), c.getDouble("Selector.Pos2.Z")));
            int time = c.getInt("FlyTime");
            new Ship(pad, name, u, d, l, time, storage, cuboid);
        }
    }

    public Location getPad() {
        return this.telepad;
    }

    public Location getTeleport() {
        return this.teleport;
    }

    public String getName() {
        return this.shipName;
    }

    public Destinations getDestination() {
        return this.dest;
    }

    public Destinations getLocation() {
        return this.loc;
    }

    public int getTime() {
        FileStorage f = new FileStorage(fileName, directory);
        YamlConfiguration c = f.getConfig();
        return c.getInt("FlyTime");
    }

    public User getOwner() {
        return this.owner;
    }

    public void setTelepad(Location l) {
        FileStorage f = new FileStorage(fileName, directory);
        YamlConfiguration c = f.getConfig();
        this.teleport = l;
        this.telepad = l.clone().add(0, -2, 0);
        c.set("Pad.World", l.getWorld().getName());
        c.set("Pad.X", l.getX());
        c.set("Pad.Y", l.getY());
        c.set("Pad.Z", l.getZ());
        c.set("Pad.Yaw", l.getYaw());
        c.set("Pad.Pitch", l.getPitch());
        f.saveConfig();
        ships.put(this.owner, this);
    }

    public void setName(String name) {
        FileStorage f = new FileStorage(fileName, directory);
        YamlConfiguration c = f.getConfig();
        this.shipName = name;
        c.set("Name", name);
        f.saveConfig();
        ships.put(this.owner, this);
    }

    public void setDestination(Destinations d) {
        FileStorage f = new FileStorage(fileName, directory);
        YamlConfiguration c = f.getConfig();
        this.dest = d;
        c.set("Destination", d.toString());
        f.saveConfig();
        ships.put(this.owner, this);
    }

    public void setLocation(Destinations d) {
        FileStorage f = new FileStorage(fileName, directory);
        YamlConfiguration c = f.getConfig();
        this.loc = d;
        c.set("Location", d.toString());
        f.saveConfig();
        ships.put(this.owner, this);
    }

    public void setTime(int time) {
        FileStorage f = new FileStorage(fileName, directory);
        YamlConfiguration c = f.getConfig();
        this.flyTime = time;
        c.set("FlyTime", time);
        f.saveConfig();
        ships.put(this.owner, this);
    }

    public String getDestinationName() {
        String name = dest.toString().toLowerCase();
        Character c = name.toCharArray()[0];
        name = c.toString().toUpperCase() + name.substring(1);
        return name;

    }

    public String getLocationName() {
        String name = loc.toString().toLowerCase();
        Character c = name.toCharArray()[0];
        name = c.toString().toUpperCase() + name.substring(1);
        return name;

    }

    public Cuboid getSelector() {
        return selector;
    }

}
