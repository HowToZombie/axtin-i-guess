/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.axtin.modules.quests.conversation;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;

/**
 *
 * @author Alan Tavakoli
 */
public class ClickAction {
    private String value;
    private ClickActionType type;
    private boolean progress;
    
    public ClickAction(String value, ClickActionType type, boolean progress) {
        this.value = value;
        this.type = type;
        this.progress = progress;
    }
    
    public ClickAction(String source) {
    	String[] parts = source.split("\\|");
    	type = ClickActionType.valueOf(parts[0]);
    	progress = Boolean.valueOf(parts[1]);
    	value = parts[2];
	}
    
    @Override
	public String toString() {
		return type+"|"+progress+"|"+value;
	}
    
    public ClickAction(String action, boolean progress) {
        if(action.contains("RUN_COMMAND")) {
            type = ClickActionType.RUN_COMMAND;
            action = action.replace("RUN_COMMAND", "");
        } else if(action.contains("SUGGEST_COMMAND")) {
            type = ClickActionType.SUGGEST_COMMAND;
            action = action.replace("SUGGEST_COMMAND", "");
        } else if(action.contains("OPEN_URL")) {
            type = ClickActionType.OPEN_URL;
            action = action.replace("OPEN_URL", "");
        }
        this.value = action.trim();
        this.progress = progress;
    }
    
    

	public boolean shouldProgress() {
        return progress;
    }
    
    public ClickEvent getEvent(int answerIndex) {
        switch(type) {
            case OPEN_URL:
                return new ClickEvent(Action.OPEN_URL, value);
            case RUN_COMMAND:
                return new ClickEvent(Action.RUN_COMMAND, "/conversation " + answerIndex + " " + value);
            case SUGGEST_COMMAND:
                return new ClickEvent(Action.SUGGEST_COMMAND, value);
            default:
                throw new AssertionError(type.name());
        }
    
    }
    
}
