package org.axtin.util.gui;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.ChatComponentText;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.IMerchant;
import net.minecraft.server.v1_12_R1.MerchantRecipe;
import net.minecraft.server.v1_12_R1.MerchantRecipeList;
import net.minecraft.server.v1_12_R1.ScoreboardTeam;
import net.minecraft.server.v1_12_R1.World;

public class VillagerGUI extends SimpleGUI {
	
	private String title;
	private List<VillagerOffer> offers;
	
	public VillagerGUI(){
		this(new ArrayList<VillagerOffer>());
	}
	
	public VillagerGUI(String title){
		this(title, new ArrayList<VillagerOffer>());
	}
	
	public VillagerGUI(List<VillagerOffer> offers){
		this("Villager", offers);
	}
	
	public VillagerGUI(String title, List<VillagerOffer> offers){
		this.title = title;
		this.offers = offers;
	}
	
	public void addOffer(VillagerOffer offer){
		this.offers.add(offer);
	}
	
	public boolean removeOffer(VillagerOffer offer){
		return this.offers.remove(offer);
	}
	
	@Override
	public void open(Player player){
		final EntityPlayer ep = ((CraftPlayer) player).getHandle();
		final IMerchant im = new IMerchant(){
			@Override
			public void a(MerchantRecipe mr){ }
			@Override
			public MerchantRecipeList getOffers(EntityHuman arg0){
				MerchantRecipeList mrl = new MerchantRecipeList();
				
				for(VillagerOffer offer:offers)
					mrl.add(new MerchantRecipe(CraftItemStack.asNMSCopy(offer.getPrice()[0].getItemStack()), CraftItemStack.asNMSCopy(offer.getPrice()[1].getItemStack()), CraftItemStack.asNMSCopy(offer.getResult().getItemStack()), 0, offer.getMaxUses()));
				
				return mrl;
			}
			@Override
			public IChatBaseComponent getScoreboardDisplayName(){ return new ChatComponentText(ScoreboardTeam.getPlayerDisplayName(null, getTitle())); }
			@Override
			public EntityHuman getTrader(){ return ep; }
			@Override
			public void a(net.minecraft.server.v1_12_R1.ItemStack arg0){ }
			@Override
			public void setTradingPlayer(EntityHuman arg0){ }
			@Override
			public World u_(){ return ep.world; }
			@Override
			public BlockPosition v_(){ return ep.bedPosition; }
		};
		
		ep.openTrade(im);
		
		openInventories.put(player, this);
	}
	
	@Override
	public void onClose(Player player){ }
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public List<VillagerOffer> getOffers(){
		return this.offers;
	}
	
	
	public static class VillagerOffer {
		
		private final DecGUIItem result;
		private final DecGUIItem price[];
		
		private int maxUses = Integer.MAX_VALUE;
		
		public VillagerOffer(DecGUIItem result, DecGUIItem price1){
			this(result, price1, null);
		}
		
		public VillagerOffer(DecGUIItem result, DecGUIItem price1, @Nullable DecGUIItem price2){
			this.result = result;
			this.price = new DecGUIItem[2];
			this.price[0] = price1;
			this.price[1] = price2;
		}
		
		public void setMaxUses(int maxUses){
			this.maxUses = maxUses;
		}
		
		public DecGUIItem getResult(){
			return this.result;
		}
		
		public DecGUIItem[] getPrice(){
			return this.price;
		}
		
		public int getMaxUses(){
			return this.maxUses;
		}
	}



	@Override
	public boolean isCancellable(){
		return false;
	}

	@Override
	public boolean hasAntiDrop(){
		return false;
	}
}
