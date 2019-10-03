package org.axtin.modules.luckycrate.executor;

import javax.annotation.Nullable;

/**
 * 
 * @author Marcel (MrEAlderson)
 * @date 11/9/2017
 */
public enum ExecutorType {
	
	SpawnEntity(SpawnEntityExecutor.class,
			new InputPair("type", InputType.ENTITYTYPE, true),
			new InputPair("x", InputType.DOUBLE, false),
			new InputPair("y", InputType.DOUBLE, false),
			new InputPair("z", InputType.DOUBLE, false),
			new InputPair("yaw", InputType.FLOAT, false),
			new InputPair("pitch", InputType.FLOAT, false),
			new InputPair("item-material", InputType.ITEMSTACK, false),
			new InputPair("velo-x", InputType.FLOAT, false),
			new InputPair("velo-y", InputType.FLOAT, false),
			new InputPair("velo-y", InputType.FLOAT, false),
			new InputPair("velo-randommultiplier", InputType.BOOLEAN, false),
			new InputPair("name", InputType.STRING, false),
			new InputPair("glowing", InputType.BOOLEAN, false),
			new InputPair("movespeed", InputType.DOUBLE, false),
			new InputPair("gravity", InputType.BOOLEAN, false)),
	
	SetBlock(SetBlockExecutor.class,
			new InputPair("type", InputType.MATERIAL, true),
			new InputPair("x", InputType.INTEGER, true),
			new InputPair("y", InputType.INTEGER, true),
			new InputPair("z", InputType.INTEGER, true)),
	
	PlaySound(PlaySoundExecutor.class,
			new InputPair("type", InputType.SOUND, true),
			new InputPair("volume", InputType.FLOAT, false),
			new InputPair("pitch", InputType.FLOAT, false),
			new InputPair("only-executor", InputType.BOOLEAN, false)),
	
	SendMessage(SendMessageExecutor.class,
			new InputPair("message", InputType.STRING, true),
			new InputPair("broadcast", InputType.BOOLEAN, false));
	
	public final Class<? extends PrizeExecutor> clazz;
	public final InputPair[] input;
	
	private <T1, T2> ExecutorType(Class<? extends PrizeExecutor> clazz, InputPair... input){
		this.clazz = clazz;
		this.input = input;
	}
	
	public @Nullable InputPair getPair(String name){
		for(InputPair pair:input){
			if(pair.name.equalsIgnoreCase(name))
				return pair;
		}
		
		return null;
	}
	
	public static @Nullable ExecutorType byName(String name){
		for(ExecutorType type:values()){
			if(type.name().equalsIgnoreCase(name))
				return type;
		}
		
		return null;
	}
}