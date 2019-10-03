/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.axtin.modules.quests.conversation;

import net.md_5.bungee.api.chat.ClickEvent.Action;

/**
 *
 * @author admin
 */
public enum ClickActionType {
    OPEN_URL(Action.OPEN_URL), RUN_COMMAND(Action.RUN_COMMAND), SUGGEST_COMMAND(Action.SUGGEST_COMMAND);
    
    private final Action action;
    
    ClickActionType(Action action) {
        this.action = action;
    }
    
    public Action getValue() {
        return this.action;
    }
    
}
