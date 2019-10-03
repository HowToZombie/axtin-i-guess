
package org.axtin.modules.quests;

/**
 *
 * @author Alan Tavakoli
 */
public enum TaskType {
    KILL_MOBS, KILL_PLAYERS, MINE_BLOCKS, REACH_LOCATION;
    
    public boolean requiresInteger() {
        switch(this) {
            case KILL_MOBS:
                return true;
            case KILL_PLAYERS:
                return true;
            case MINE_BLOCKS:
                return true;
            case REACH_LOCATION:
                return true;
            default:
                throw new AssertionError(this.name());
        }
    }
}
