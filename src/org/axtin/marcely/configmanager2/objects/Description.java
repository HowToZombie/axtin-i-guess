package org.axtin.marcely.configmanager2.objects;

public class Description extends Config {
	
	private final boolean base;
	
	public Description(Tree rootTree, String name, String value){
		this(rootTree, name, value, false);
	}
	
	public Description(Tree rootTree, String name, String value, boolean base){
		super("!name: " + value, name, rootTree, value);
		
		this.base = base;
	}
	
	@Override
	public byte getType(){
		return TYPE_DESCRIPTION;
	}
	
	public boolean isBase(){
		return this.base;
	}
}