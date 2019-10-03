package org.axtin.modules.luckycrate.executor;

/**
 * 
 * @author Marcel (MrEAlderson)
 * @date 11/9/2017
 */
public class InputPair {
	
	public final String name;
	public final InputType type;
	public final boolean isRequired;
	
	public InputPair(String name, InputType type, boolean required){
		this.name = name;
		this.type = type;
		this.isRequired = required;
	}
}
