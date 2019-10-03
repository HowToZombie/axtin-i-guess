package org.axtin.util.gui.paging;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.netty.util.internal.ThreadLocalRandom;

public class PageUtil {

	/*
	 * 
	 * 
	 */
	
	public PageContainer test;
	
	
	public PageUtil() {
		this.test = new PageContainer(1);
		List<PageItem> items = getTestPageItems(10000);
		this.test.createPages(items);
		this.test.fillItems();
		Bukkit.broadcastMessage("Last global slot: " + this.test.getLastValidGlobalSlot());
	}
	
	public static ItemStack getTestItemStack() {
		ItemStack is = new ItemStack( getRandomMaterial());
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(randomName());
		is.setItemMeta(im);
		return is;
	}
	
	public static String randomName() {
		int randomNum = ThreadLocalRandom.current().nextInt(1000, 9999 + 1);
		return String.valueOf(randomNum);
	}
	
	public void open(Player player) {
		this.test.open(player);
	}
	
	private List<PageItem> getTestPageItems(int amount) {
		List<PageItem> list = new ArrayList<>();
		for(int i = 0; i < amount; i++) {
			list.add(getTestPageItem());
		}
		return list;
	}
	
	public static PageItem getTestPageItem() {
		PageItem item = new PageItem(getTestItemStack()) {
			
			private String def = "Current time: ";
			private SimpleDateFormat format = new SimpleDateFormat("h:mm:ss a");
			
			@Override
			public void update() {
				ItemMeta im = this.getItemMeta();
				String time = format.format(new Date());
				String full = def + time;
				im.setLore(Arrays.asList(full));
				this.setItemMeta(im);
			}
			
			@Override
			public void onClick(Player player, Inventory inv, ItemStack item) {
				player.sendMessage(item.getType().toString());
				this.remove();
			}
		};
		return item;
	}
	
	public FastPage getFastPage(int page) {
		return new FastPage(test.getPage(page));
	}
	
	private static List<Material> invisibleMaterials() {
		return Arrays.asList(Material.POTATO,
				Material.COCOA, 
				Material.IRON_DOOR_BLOCK,
				Material.REDSTONE_COMPARATOR_OFF,
				Material.CAULDRON,
				Material.SIGN_POST,
				Material.NETHER_WARTS,
				Material.STATIONARY_LAVA,
				Material.FROSTED_ICE,
				Material.TRIPWIRE,
				Material.DAYLIGHT_DETECTOR_INVERTED,
				Material.ENDER_PORTAL,
				Material.FLOWER_POT,
				Material.DARK_OAK_DOOR,
				Material.STANDING_BANNER,
				Material.PURPUR_DOUBLE_SLAB,
				Material.BIRCH_DOOR,
				Material.REDSTONE_LAMP_ON,
				Material.BEETROOT_BLOCK,
				Material.PISTON_EXTENSION,
				Material.WOOD_DOUBLE_STEP,
				Material.DOUBLE_STEP,
				Material.DIODE_BLOCK_OFF,
				Material.FIRE,
				Material.PISTON_MOVING_PIECE,
				Material.CROPS,
				Material.GLOWING_REDSTONE_ORE,
				Material.END_GATEWAY,
				Material.SPRUCE_DOOR,
				Material.ACACIA_DOOR,
				Material.SKULL,
				Material.MELON_STEM,
				Material.SUGAR_CANE_BLOCK,
				Material.REDSTONE_WIRE,
				Material.BREWING_STAND,
				Material.CARROT,
				Material.STATIONARY_WATER,
				Material.AIR,
				Material.REDSTONE_COMPARATOR_ON,
				Material.BED_BLOCK,
				Material.BURNING_FURNACE,
				Material.WALL_BANNER,
				Material.WALL_SIGN,
				Material.PORTAL,
				Material.WOODEN_DOOR,
				Material.DOUBLE_STONE_SLAB2,
				Material.CAKE_BLOCK,
				Material.PUMPKIN_STEM,
				Material.REDSTONE_TORCH_OFF,
				Material.DIODE_BLOCK_ON,
				Material.LAVA,
				Material.WATER,
				Material.JUNGLE_DOOR);
	}
	
	private static Material getRandomMaterial() {
		Material mat = randomEnum(Material.class);
		
		if(!invisibleMaterials().contains(mat))
			return mat;
		else
			return getRandomMaterial();
	}
	
	private static <T extends Enum<?>> T randomEnum(Class<T> clazz){
        SecureRandom random = new SecureRandom();
		int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }
	
	public static List<Page> getPages(int items, PageContainer container) {
		List<Page> pages = new ArrayList<>();
		int pageAmount = (items + 45 - 1) / 45;
		for(int i = 0; i < pageAmount; i++) {
			pages.add(new Page(container, pages.size() + 1));
		}
		return pages;
	}
	
	public static Inventory createPage(String name) {
		Inventory inv = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&', name));
		inv.setItem(48, getArrow("Back"));
		inv.setItem(50, getArrow("Next"));
		return inv;
	}
	
	private static ItemStack getArrow(String name) {
		ItemStack is = new ItemStack(Material.ARROW);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(name);
		is.setItemMeta(im);
		return is;
	}
	
	/*private static int getSpaces(List<Page> pages) {
		int i = 0;
		if(!pages.isEmpty()) {
			i = pages.size() * 45;
		}
		return i;
	}*/
	
	public static <T> Stream<List<T>> batches(List<T> source, int length) {
	    if (length <= 0)
	        throw new IllegalArgumentException("length = " + length);
	    int size = source.size();
	    if (size <= 0)
	        return Stream.empty();
	    int fullChunks = (size - 1) / length;
	    return IntStream.range(0, fullChunks + 1).mapToObj(
	        n -> source.subList(n * length, n == fullChunks ? size : (n + 1) * length));
	}
	
}
