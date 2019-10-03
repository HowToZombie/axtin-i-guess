package org.axtin.util.gui;

import org.axtin.util.ItemStackUtil;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.Blocks;
import net.minecraft.server.v1_12_R1.ChatMessage;
import net.minecraft.server.v1_12_R1.ContainerAnvil;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.PacketPlayOutOpenWindow;

public class AnvilGUI extends SimpleGUI {
	
	private final String defaultMessage;
	
	private WroteListener wroteListener;
	
	public AnvilGUI(){
		this("");
	}
	
	public AnvilGUI(String defaultMessage){
		this.defaultMessage = defaultMessage;
	}
	
	@Override
	public boolean isCancellable(){
		return true;
	}
	
	@Override
	public boolean hasAntiDrop(){
		return true;
	}
	
	@Override
	public void open(Player player){
		final EntityPlayer ep = ((CraftPlayer) player).getHandle();
		final ContainerAnvil anvil = new ContainerAnvil(ep.inventory, ep.world, new BlockPosition(0, 0, 0), ep){
	        @Override
	        public boolean a(EntityHuman human){
	            return true;
	        }
		};
		final Inventory inv = anvil.getBukkitView().getTopInventory();
		inv.setItem(0, ItemStackUtil.rename(new ItemStack(Material.PAPER), getDefaultMessage()));
		final int id = ep.nextContainerCounter();
		
		final PacketPlayOutOpenWindow p = new PacketPlayOutOpenWindow(id, "minecraft:anvil", new ChatMessage(Blocks.ANVIL.a() + ".name"));
		ep.playerConnection.sendPacket(p);
		
		ep.activeContainer = anvil;
		anvil.windowId = id;
		anvil.addSlotListener(ep);
		
		openInventories.put(player, this);
		openInventoriesDelayed.put(player, this);
	}

	@Override
	public void onClose(Player player){ }

	@Override
	@Deprecated
	public void setTitle(String title){ }

	@Override
	@Deprecated
	public String getTitle(){
		return null;
	}
	
	public void setWroteListener(WroteListener listener){
		this.wroteListener = listener;
	}
	
	public String getDefaultMessage(){
		return this.defaultMessage;
	}
	
	public WroteListener getWroteListener(){
		return this.wroteListener;
	}
	
	public static interface WroteListener {
		public void run(String msg);
	}
}
