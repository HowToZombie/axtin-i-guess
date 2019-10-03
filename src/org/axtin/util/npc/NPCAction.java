package org.axtin.util.npc;

import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo;

public enum NPCAction {
    ADD(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER),
    REMOVE(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER);

    private PacketPlayOutPlayerInfo.EnumPlayerInfoAction action = null;

    NPCAction(PacketPlayOutPlayerInfo.EnumPlayerInfoAction action) {
        this.action = action;
    }

    public PacketPlayOutPlayerInfo.EnumPlayerInfoAction getAction() {
        return this.action;
    }
}
