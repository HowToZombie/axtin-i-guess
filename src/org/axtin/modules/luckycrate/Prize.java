package org.axtin.modules.luckycrate;

import java.util.ArrayList;
import java.util.List;

import org.axtin.modules.luckycrate.executor.PrizeExecutor;

/**
 * 
 * @author Marcel (MrEAlderson)
 * @date 11/9/2017
 */
public class Prize {
	
	public int chance;
	public List<PrizeExecutor> executors = new ArrayList<PrizeExecutor>();
	
	public Prize(){ }
}