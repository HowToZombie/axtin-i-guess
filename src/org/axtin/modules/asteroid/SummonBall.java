package org.axtin.modules.asteroid;

import org.axtin.command.AxtinCommand;
import org.axtin.command.CommandErrors;
import org.axtin.container.facade.Container;
import org.axtin.user.UserRepository;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class SummonBall extends AxtinCommand {
    public SummonBall() {
        super("ballofire");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {

        Integer size = Integer.valueOf(strings[0]);
        Player p = (Player) commandSender;

        if (Container.get(UserRepository.class).getUser(p.getUniqueId()).getData().getStaffRole().getIdentifier() < 21) {
            p.sendMessage(CommandErrors.NO_PERMISSION.toString());
            return true;
        }

        List<ItemStack> mat = new ArrayList<>();
        mat.add(new ItemStack(Material.STAINED_CLAY,1,DyeColor.BLACK.getWoolData()));
        mat.add(new ItemStack(Material.STAINED_CLAY,1, DyeColor.BROWN.getWoolData()));

        new Asteroid(size,mat,p)
                .setVel(new Vector(-2,-1,0));
        //FallingBlock fb = p.getWorld().spawnFallingBlock(p.getLocation().add(0, 50, 0), Material.BLACK_SHULKER_BOX, (byte) 0);
        //ChangeDetect.falling.add(fb.getEntityId());
        //Bukkit.broadcastMessage("Debug 6 " + fb.getEntityId());


       // fb.setDropItem(false);

       // fb.setVelocity(new Vector(-2, -1, 0));
        return false;
    }
}
