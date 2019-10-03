package org.axtin.data;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

public class BlockData {
	
	private final Block block;
	
	private BlockState d_state;
	
	public BlockData(Block block){
		this.block = block;
	}
	
	public Block getBlock(){
		return this.block;
	}
	
	public void read(){
		this.d_state = block.getState();
	}
	
	public void write(){
		d_state.update(true);
	}
}
