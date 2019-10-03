package org.axtin;

import org.axtin.modules.warps.WarpManager;
import org.axtin.modules.companion.CompanionManager;
import org.axtin.modules.customenchants.CustomEnchantmentManager;
import org.axtin.modules.kitsystem.KitHandler;
import org.axtin.modules.luckycrate.LuckyCrateManager;
import org.axtin.modules.meteorite.MeteoriteManager;
import org.axtin.modules.shulkercrates.ShulkerHandler;
import org.axtin.command.AxtinCommand;
import org.axtin.container.AxtinContainer;
import org.axtin.container.facade.Container;
import org.axtin.database.Database;
import org.axtin.user.UserRepository;
import org.axtin.util.gui.GUIListener;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandMap;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public class AxtinEngine {
    private final Logger logger = LoggerFactory.getLogger(AxtinEngine.class);

    private AxtinContainer container;
    private Database database;
    private UserRepository users;

    public synchronized void start(Axtin plugin) {
        logger.info("Initializing startup sequence...");

        // Initialize database
        database = new Database();

        users = new UserRepository(database);
        
        // Initialize Economy with database
        
        // Initialize container
        container = new AxtinContainer();
        Container.swap(container);

        logger.info("Initialization completed");

        // Begin containing
        Container.put(JavaPlugin.class, plugin);
        Container.put(Plugin.class, plugin);
        Container.put(Server.class, plugin.getServer());
        Container.put(BukkitScheduler.class, plugin.getServer().getScheduler());
        Container.put(PluginManager.class, plugin.getServer().getPluginManager());
        Container.put(Database.class, database);
        Container.put(UserRepository.class, users);
        Container.put(KitHandler.class, new KitHandler());
        Container.put(ShulkerHandler.class, new ShulkerHandler());
        Container.put(WarpManager.class, new WarpManager());
        Container.put(GUIListener.class, new GUIListener());
        Container.put(MeteoriteManager.class, new MeteoriteManager());
        Container.put(LuckyCrateManager.class, new LuckyCrateManager());
        Container.put(CustomEnchantmentManager.class, new CustomEnchantmentManager());
        Container.put(CompanionManager.class, new CompanionManager());
        //Container.put(QuestHandler.class, new QuestHandler());
        
        // Begin world auto save
        autosave();

        //Kit System
        Container.get(KitHandler.class).init();
        Container.get(MeteoriteManager.class).init();
    }

    public synchronized void stop() {
        logger.info("Initializing shutdown sequence...");

        database.close();

        Container.swap(null);
        container = null;

        logger.info("Initialization completed");
    }

    public void command(Class<AxtinCommand>[] clazz) {
        try {
            Field f = Container.get(Server.class).getClass().getDeclaredField("commandMap");

            if (!f.isAccessible()) {
                f.setAccessible(true);
            }

            CommandMap map = (CommandMap) f.get(Container.get(Server.class));

            for (Class<AxtinCommand> command : clazz) {
                map.register("axtin:", command.newInstance());
            }
        } catch (NoSuchFieldException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    public void listener(Class<Listener>[] clazz) {
        try {
            for (Class<Listener> listener : clazz) {
                Container.get(PluginManager.class).registerEvents(listener.newInstance(), Container.get(Plugin.class));
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void autosave() {
        Container.get(BukkitScheduler.class).scheduleSyncRepeatingTask(Container.get(Plugin.class), () -> Bukkit.getWorlds().forEach(World::save), 12000, 12000);
    }
}
