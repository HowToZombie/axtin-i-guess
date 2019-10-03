package org.axtin.modules.luckycrate;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.axtin.container.facade.Container;
import org.axtin.marcely.configmanager2.ConfigFile;
import org.axtin.marcely.configmanager2.objects.Config;
import org.axtin.marcely.configmanager2.objects.Tree;
import org.axtin.modules.luckycrate.executor.ExecutorType;
import org.axtin.modules.luckycrate.executor.InputPair;
import org.axtin.modules.luckycrate.executor.PrizeExecutor;
import org.axtin.util.ItemStackUtil;
import org.axtin.util.Util;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Marcel (MrEAlderson)
 * @date 11/9/2017
 */
public class LuckyCrateConfig {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LuckyCrateConfig.class);
	private static final ConfigFile FILE = new ConfigFile(new File(Container.get(Plugin.class).getDataFolder() + "/luckycrate.cm2"));
	
	public static void load(LuckyCrateManager manager){
		if(!FILE.exists()){
			saveDefault(manager);
			return;
		}
		
		FILE.load();
		
		final List<Prize> prizes = new ArrayList<>();
		
		for(Tree tree:FILE.getRootTree().getTreeChilds()){
			final Prize prize = new Prize();
			
			for(Config c:tree.getChilds()){
				// load configs of prize
				if(c.getType() == Config.TYPE_CONFIG){
					switch(c.getName().toLowerCase()){
					case "chance":
						if(Util.isInteger(c.getValue()))
							prize.chance = Integer.valueOf(c.getValue());
						else
							LOGGER.warn("Value of 'chance' (" + c.getValue() + ") isn't a number");
						break;
					}
				
				// load executors
				}else if(c.getType() == Config.TYPE_TREE){
					if(c.getName().toLowerCase().startsWith("exec-")){
						final String execName = c.getName().toLowerCase().replaceFirst("exec-", "");
						final ExecutorType type = ExecutorType.byName(execName);
						
						if(type != null){
							// load input data
							final Map<String, Object> inputData = new HashMap<String, Object>();
							
							for(Config c1:((Tree) c).getConfigChilds()){
								final InputPair pair = type.getPair(c1.getName());
								
								if(pair != null){
									final String value = c1.getValue();
									Object cValue = null;
									
									// get instance of input
									try{
										switch(pair.type){
										case STRING:
											cValue = value;
											break;
										case INTEGER:
											cValue = Integer.valueOf(value);
											break;
										case DOUBLE:
											cValue = Double.valueOf(value);
											break;
										case FLOAT:
											cValue = Float.valueOf(value);
											break;
										case BOOLEAN:
											cValue = Boolean.valueOf(value);
											break;
										case ENTITYTYPE:
											cValue = EntityType.valueOf(value);
											break;
										case ITEMSTACK:
											cValue = ItemStackUtil.toItemStack(value);
											break;
										case SOUND:
											cValue = Sound.valueOf(value);
											break;
										case MATERIAL:
											cValue = Material.valueOf(value);
											break;
										}
									}catch(Exception e){ }
									
									if(cValue != null)
										inputData.put(c1.getName().toLowerCase(), cValue);
									else
										LOGGER.warn("Wrong input type for key '" + c1.getName() + "' for type '" + execName + "'");
								}else
									LOGGER.warn("Unkown input key (" + c1.getName() + ") for type '" + execName + "'");
							}
							
							// check if every required data is inside
							boolean continu = true;
							
							for(InputPair pair:type.input){
								if(!inputData.containsKey(pair.name.toLowerCase())){
									LOGGER.warn("Missing input data '" + pair.name.toLowerCase() + "' for type '" + execName + "'");
									continu = false;
								}
							}
							
							// init executor
							if(continu){
								try{
									final Constructor<? extends PrizeExecutor> con = type.clazz.getConstructor(Prize.class, inputData.getClass());
									prize.executors.add((PrizeExecutor) con.newInstance(prize, inputData));
								}catch(NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e){
									e.printStackTrace();
								}
							}
						}else
							LOGGER.warn("Unkown executor '" + execName + "'");
					}
				}
			}
			
			prizes.add(prize);
		}
		
		manager.prizes = prizes;
		
		FILE.clear();
	}
	
	private static void saveDefault(LuckyCrateManager manager){
		FILE.clear();
		
		FILE.getPicker().addComment("===========================");
		FILE.getPicker().addComment("EXAMPLE:");
		FILE.getPicker().addComment(" {");
		FILE.getPicker().addComment("	chance: 1");
		FILE.getPicker().addComment("	exec-spawnentity {");
		FILE.getPicker().addComment(" 		type: primed_tnt");
		FILE.getPicker().addComment(" 		x: 0");
		FILE.getPicker().addComment(" 		y: 1");
		FILE.getPicker().addComment(" 		z: 0");
		FILE.getPicker().addComment(" 		yaw: 0");
		FILE.getPicker().addComment(" 		pitch: 0");
		FILE.getPicker().addComment(" 		velo-x: 0.5");
		FILE.getPicker().addComment(" 		velo-y: 0.5");
		FILE.getPicker().addComment(" 		velo-z: 0.5");
		FILE.getPicker().addComment(" 	}");
		FILE.getPicker().addComment(" 	exec-playsound {");
		FILE.getPicker().addComment(" 		type: BLOCK_ANVIL_HIT");
		FILE.getPicker().addComment(" 	}");
		FILE.getPicker().addComment(" }");
		FILE.getPicker().addComment("");
		FILE.getPicker().addComment("");
		FILE.getPicker().addComment("===========================");
		FILE.getPicker().addComment("AVAILABLE INPUT DATA FOR EXECUTORS:");
		FILE.getPicker().addComment("for all:");
		FILE.getPicker().addComment(" -> chance (Int)");
		FILE.getPicker().addComment(" -> amount-min (Int)");
		FILE.getPicker().addComment(" -> amount-max (Int)");
		FILE.getPicker().addComment("spawnentity:");
		FILE.getPicker().addComment(" -> type (EntityType) [MUST ADD]");
		FILE.getPicker().addComment(" -> x (Double)");
		FILE.getPicker().addComment(" -> y (Double)");
		FILE.getPicker().addComment(" -> z (Double)");
		FILE.getPicker().addComment(" -> yaw (Float)");
		FILE.getPicker().addComment(" -> pitch (Float)");
		FILE.getPicker().addComment(" -> item-material (ItemStack (e.g. STONE:1))");
		FILE.getPicker().addComment(" -> velo-x (Float)");
		FILE.getPicker().addComment(" -> velo-y (Float)");
		FILE.getPicker().addComment(" -> velo-z (Float)");
		FILE.getPicker().addComment(" -> velo-randommultiplier (Boolean)");
		FILE.getPicker().addComment(" -> name (String)");
		FILE.getPicker().addComment(" -> glowing (Boolean)");
		FILE.getPicker().addComment(" -> movespeed (Double)");
		FILE.getPicker().addComment(" -> gravity (Boolean)");
		FILE.getPicker().addComment("");
		FILE.getPicker().addComment("setblock");
		FILE.getPicker().addComment(" -> type (Material (e.g. STONE)) [MUST ADD]");
		FILE.getPicker().addComment(" -> x (Int) [MUST ADD]");
		FILE.getPicker().addComment(" -> y (Int) [MUST ADD]");
		FILE.getPicker().addComment(" -> z (Int) [MUST ADD]");
		FILE.getPicker().addComment("");
		FILE.getPicker().addComment("playsound");
		FILE.getPicker().addComment(" -> type (Sound) [MUST ADD]");
		FILE.getPicker().addComment(" -> volume (Float)");
		FILE.getPicker().addComment(" -> pitch (Float)");
		FILE.getPicker().addComment(" -> only-executor (Boolean)");
		FILE.getPicker().addComment("");
		FILE.getPicker().addComment("sendmessage");
		FILE.getPicker().addComment(" -> message (String) [MUST ADD]");
		FILE.getPicker().addComment(" -> broadcast (Boolean)");
		FILE.getPicker().addComment("===========================");
		FILE.getPicker().addComment("Start creating your configs here:");
		FILE.getPicker().addEmptyLine();
		FILE.getPicker().addEmptyLine();
		
		FILE.save();
	}
}
