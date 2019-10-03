package org.axtin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.axtin.container.facade.Container;
import org.axtin.deprecated.modules.customenchants.Enchantment;
import org.axtin.deprecated.modules.customenchants.EnchantmentCommand;
import org.axtin.deprecated.modules.customenchants.enchantments.ExampleEnchantment;
import org.axtin.modules.economy.RankUpCommand;
import org.axtin.modules.economy.SellCommand;
import org.axtin.modules.messaging.AxtinMessage;
import org.axtin.modules.messaging.AxtinReply;
import org.axtin.modules.messaging.SocialSpyCommand;
import org.axtin.modules.meteorite.MeteoriteCommand;
import org.axtin.modules.meteorite.MeteoriteManager;
import org.axtin.modules.misccommands.*;
import org.axtin.modules.misccommands.afk.AxtinAfk;
import org.axtin.modules.managing.*;
import org.axtin.modules.managing.moderation.InventorySeeCommand;
import org.axtin.modules.managing.moderation.punishments.*;
import org.axtin.modules.hologram.HologramCommand;
import org.axtin.modules.maps.MapCommand;
import org.axtin.modules.maps.StopBreaking;
import org.axtin.modules.mining.MiningListener;
import org.axtin.modules.asteroid.ChangeDetect;
import org.axtin.modules.economy.BalanceCommand;
import org.axtin.modules.economy.PayCommand;
import org.axtin.modules.launch.LaunchCommand;
import org.axtin.modules.luckycrate.LuckyCrateManager;
import org.axtin.modules.asteroid.SummonBall;
import org.axtin.modules.companion.CompanionManager;
import org.axtin.modules.customenchants.CustomEnchantmentCommand;
import org.axtin.modules.customenchants.CustomEnchantmentManager;
import org.axtin.modules.mines.HitListener;
import org.axtin.modules.mines.MineLoad;
import org.axtin.modules.mines.MineRegen;
import org.axtin.modules.mines.SetCosts;
import org.axtin.modules.mining.MiningSelectorCommand;
import org.axtin.modules.oxygen.*;
import org.axtin.modules.password.PasswordManager;
import org.axtin.modules.pickaxe.PickaxeManager;
import org.axtin.modules.pickaxe.XPGainListener;
import org.axtin.modules.ships.*;
import org.axtin.modules.shulkercrates.ShulkerCommand;
import org.axtin.modules.tokenshop.*;
import org.axtin.modules.stopweather.StopWeatherListener;
import org.axtin.modules.shulkercrates.ShulkerListener;
import org.axtin.modules.spawnwarping.SetCommand;
import org.axtin.user.UserListener;
import org.axtin.modules.kitsystem.KitCommand;
import org.axtin.modules.kitsystem.KitHandler;
import org.axtin.modules.kitsystem.KitsCommand;
import org.axtin.modules.kitsystem.KitsReloadCommand;
import org.axtin.modules.warps.WarpManager;
import org.axtin.user.UserRepository;
import org.axtin.util.Hologram;
import org.axtin.util.TabList;
import org.axtin.util.gui.GUIListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.WorldCreator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;

public class Axtin extends JavaPlugin {
    private final Logger logger = LoggerFactory.getLogger(Axtin.class);

    private AxtinEngine engine = new AxtinEngine();

    Enchantment efficiency = new Enchantment("Efficiency", 0, "efficiency");

    @Override
    @SuppressWarnings("unchecked")
    public void onEnable() {
        engine.start(this);
        MaintenanceCommand.loadMaintenance();
        engine.command(new Class[]{
                LagCommand.class,
                PingCommand.class,
                TopCommand.class,
                SpeedCommand.class,
                SummonBall.class,
                LaunchCommand.class,
                ShulkerCommand.class,
                SetCosts.class,
                BalanceCommand.class,
                PayCommand.class,
                KitCommand.class,
                KitsCommand.class,
                KitsReloadCommand.class,
                //WarpCommand.class,
                AxtinMessage.class,
                AxtinReply.class,
                AxtinHelp.class,
                AxtinList.class,
                AxtinMe.class,
                AxtinSeen.class,
                AxtinAfk.class,
                AxtinBan.class,
                AxtinMute.class,
                AxtinKick.class,
                AxtinUnban.class,
                AxtinUnmute.class,
                LanguageCommand.class,
                HologramCommand.class,
                SellCommand.class,
                RankUpCommand.class,
                SpawnCommand.class,
                HelmetCommand.class,
                InventorySeeCommand.class,
                TokenCommand.class,
                MiningSelectorCommand.class,
                SetCommand.class,
                ExampleCommand.class,
                EnchantmentCommand.class,
                AirLockCommand.class,
                StorageCommand.class,
                MapCommand.class,
                MaintenanceCommand.class,
                WhitelistCommand.class,
                SocialSpyCommand.class,
                MeteoriteCommand.class,
                CustomEnchantmentCommand.class
        });
        this.getServer().createWorld(new WorldCreator("SolarSystem"));
        new MineLoad().loadRankUps();
        new MineRegen();
        new LoadAirlocks().loadAreas();
        new OxygenChecker();

        ArrayList<Recipe> backup = new ArrayList<Recipe>();

        Iterator<Recipe> it = getServer().recipeIterator();

        while(it.hasNext()){
            Recipe recipe = it.next();
            switch(recipe.getResult().getType()) {
                case WOOD_PICKAXE:
                    break;
                case STONE_PICKAXE:
                    break;
                case IRON_PICKAXE:
                    break;
                case GOLD_PICKAXE:
                    break;
                case DIAMOND_PICKAXE:
                    break;
                default: backup.add(recipe);
            }
        }

        getServer().clearRecipes();
        for (Recipe r : backup)
            getServer().addRecipe(r);

        getServer().addRecipe(createRecipe(Material.WOOD, PickaxeManager.WOOD));
        getServer().addRecipe(createRecipe(Material.COBBLESTONE, PickaxeManager.STONE));
        getServer().addRecipe(createRecipe(Material.IRON_INGOT, PickaxeManager.IRON));
        getServer().addRecipe(createRecipe(Material.GOLD_INGOT, PickaxeManager.GOLD));
        getServer().addRecipe(createRecipe(Material.DIAMOND, PickaxeManager.DIAMOND));
                engine.listener(new Class[]{
                        StopBreaking.class,
                        UserListener.class,
                        ChangeDetect.class,
                        ShulkerListener.class,
                        MiningListener.class,
                        HitListener.class,
                        ShopTimerCancel.class,
                        SignClick.class,
                        SignPlace.class,
                        TokenShopListener.class,
                        HelmetListener.class,
                        StopWeatherListener.class,
                        ChatEvent.class,
                        ShipListener.class,
                        ChatFormating.class,
                        HungerListener.class,
                        MiningSelectorCommand.class,
                        XPGainListener.class,
                        GUIListener.class,
                        ExampleEnchantment.class,
                        AirlockListener.class,
                        TabList.class,
                        ReloadDisable.class,
                        StorageListener.class,
                        NoFallDamage.class,
                        Gravity.class,
                        GUIListener.class,
                        PasswordManager.class,
                        MeteoriteManager.class,
                        LuckyCrateManager.class,
                        CustomEnchantmentManager.class
                });

        Container.get(KitHandler.class).init();
    }

    private Recipe createRecipe(Material m, PickaxeManager pm){
        ItemStack i = pm.getPickaxeWithLevel(1);
        ShapedRecipe recipe = new ShapedRecipe(i);
        recipe.shape("***"," % "," % ");
        recipe.setIngredient('*', m);
        recipe.setIngredient('%', Material.STICK);

        return recipe;
    }
    @Override
    public void onDisable() {
        UserRepository repo = Container.get(UserRepository.class);
        Container.get(UserRepository.class).getUsers().values().forEach(repo::updateSQL);
        Container.get(KitHandler.class).shutDown();
        Container.get(WarpManager.class).save();
        Container.get(GUIListener.class).closeAll();
        Container.get(MeteoriteManager.class).removeAll();
        Container.get(LuckyCrateManager.class).reset();
        Container.get(CompanionManager.class).removeAll();
        engine.stop();
        Hologram.ShutDown();
    }

    public static WorldEditPlugin getWorldEdit() {
        Plugin p = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        if (p instanceof WorldEditPlugin) return (WorldEditPlugin) p;
        else return null;
    }
}
