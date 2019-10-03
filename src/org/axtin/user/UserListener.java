package org.axtin.user;

import org.axtin.container.facade.Container;
import org.axtin.modules.managing.MaintenanceCommand;
import org.axtin.modules.hologram.HologramLoader;
import org.axtin.modules.maps.MapLoader;
import org.axtin.modules.mines.RankHolder;
import org.axtin.modules.ships.Destinations;
import org.axtin.modules.ships.Ship;
import org.axtin.modules.ships.ShipListener;
import org.axtin.modules.ships.Storage;
import org.axtin.modules.warps.WarpCommand;
import org.axtin.user.User;
import org.axtin.user.UserRepository;
import org.axtin.user.role.PrisonRole;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

public class UserListener implements Listener {
    private UserRepository repository = Container.get(UserRepository.class);

    private boolean firstJoin = true;

    @EventHandler
    public void on(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!repository.offsetContains(player.getUniqueId())) {
            repository.offsetInsert(player.getUniqueId());
        }

        // Double check before adding user to the system
        // Lets add the user to the system now
        if (!repository.contains(player.getUniqueId())) {
            repository.add(player.getUniqueId(), repository.offsetGet(player.getUniqueId()));
        }

        User u = repository.getUser(player.getUniqueId());

        if(!player.hasPlayedBefore()){
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                    "&f&l&kI&6&l[&5&lWelcome to Axtin, and please enjoy your stay " + player.getName() + "!&6&l]&f&l&kI"));
            new Ship(u);
            //player.teleport(new Location(Bukkit.getWorld("world"), 5.5, 102, 1000.5, 90, 0));
            player.teleport(Destinations.SPAWN.getTP());
            u.getData().setPrisonRole(PrisonRole.A);
        }

        //player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION,1000000,1));

        if (firstJoin) {
            firstJoin = false;
            new MapLoader().load();
            new HologramLoader().load();
        }

        Ship.loadShip(player.getUniqueId());
        Storage.loadFile(player.getUniqueId());

        if (player.getWorld().getName().equalsIgnoreCase("ShipWorld")) {
            for (Player p : Bukkit.getWorld("ShipWorld").getPlayers()) {
                p.hidePlayer(player);
                player.hidePlayer(p);
            }
        }

        for (RankHolder r : RankHolder.data.values()) {
            if (r.getCube().containsLocation(player.getLocation())) {
                Location n = new Location(player.getWorld(), player.getLocation().getX(), r.getCube().getUpperY() + 2, player.getLocation().getZ());
                player.teleport(n);
            }
        }


    }

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent e) {

        /*if (!repository.offsetContains(e.getPlayer().getUniqueId())) {
            repository.offsetInsert(e.getPlayer().getUniqueId());
        }

        // Double check before adding user to the system
        // Lets add the user to the system now
        if (!repository.contains(e.getPlayer().getUniqueId())) {
            repository.add(e.getPlayer().getUniqueId(), repository.offsetGet(e.getPlayer().getUniqueId()));
        }*/

        //if (repository.getUser(e.getPlayer().getUniqueId()).getData().getStaffRole().getIdentifier() < 1) {
        if (!MaintenanceCommand.whitelisted.contains(e.getUniqueId())) {
            if (MaintenanceCommand.maintenance) {
                e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatColor.translateAlternateColorCodes('&',MaintenanceCommand.errorMessage));
            }
        }
       //}
    }

    @EventHandler
    public void on(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        // Double check UserRepository to make sure they are in the system, otherwise we have an issue.
        if (repository.contains(player.getUniqueId())) {
            repository.updateSQL(repository.getUser(player.getUniqueId()));
            repository.remove(player.getUniqueId());
        }
        WarpCommand.mining.remove(player.getName());
    }

    @EventHandler
    public void respawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        User u = repository.getUser(player.getUniqueId());
        Ship s = Ship.ships.get(u);

        Location respawn;

        if (player.getWorld().getName().equalsIgnoreCase("ShipWorld")) {
            if (s.getTime() == -1) {
                respawn = s.getLocation().getRespawn();
            } else {
                respawn = s.getDestination().getRespawn();
                s.setTime(-1);
                ShipListener.timers.get(u).cancel();
                ShipListener.timers.remove(u);
            }
        } else {
            respawn = s.getLocation().getRespawn();
        }
        event.setRespawnLocation(respawn);

    }

}
