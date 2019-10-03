package org.axtin.util.npc;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class NPCBuilder {
    private EntityPlayer entityPlayer = null;
    private MinecraftServer minecraftServer = null;
    private WorldServer worldServer = null;

    private EntityPlayer npc = null;

    private NPCProfile profile = null;
    private NPCAction action = NPCAction.ADD;

    private UUID uniqueId = UUID.randomUUID();
    private String name = "steve";
    private String skin = "steve";

    private double x = 0;
    private double y = 0;
    private double z = 0;

    private float yaw = 0F;
    private float pitch = 0F;

    public NPCBuilder uniqueId(String uniqueId) {
        this.uniqueId = UUID.fromString(uniqueId);
        return this;
    }

    public NPCBuilder name(String name) {
        this.name = name;
        return this;
    }

    public NPCBuilder skin(String skin) {
        this.skin = skin;
        return this;
    }

    public NPCBuilder action(NPCAction action) {
        this.action = action;
        return this;
    }

    public NPCBuilder x(double x) {
        this.x = x;
        return this;
    }

    public NPCBuilder y(double y) {
        this.y = y;
        return this;
    }

    public NPCBuilder z(double z) {
        this.z = z;
        return this;
    }

    public NPCBuilder yaw(float yaw) {
        this.yaw = yaw;
        return this;
    }

    public NPCBuilder pitch(float pitch) {
        this.pitch = pitch;
        return this;
    }

    public NPCBuilder(Player player) {
        this.entityPlayer = ((CraftPlayer) player).getHandle();
        this.minecraftServer = ((CraftServer) Bukkit.getServer()).getServer();
        this.worldServer = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();
    }
    
    public String getName() {
    	return this.name;
    }
    
     public void destroy() {
        this.entityPlayer.playerConnection.sendPacket(new PacketPlayOutEntityDestroy(this.npc.getId()));
        this.entityPlayer.playerConnection.sendPacket(new PacketPlayOutPlayerInfo(NPCAction.REMOVE.getAction(), this.npc));
    }
     
    public void lookAtPlayer() {
    	
    }

    public void create() {
        this.profile = new NPCProfile(this.uniqueId.toString(), this.name, this.skin);
        this.npc = new EntityPlayer(this.minecraftServer, this.worldServer, this.profile.getProfile(), new PlayerInteractManager(this.worldServer));
        this.npc.setLocation(this.x, this.y, this.z, this.yaw, this.pitch);
        this.entityPlayer.playerConnection.sendPacket(new PacketPlayOutPlayerInfo(this.action.getAction(), this.npc));
        this.entityPlayer.playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(this.npc));
    }
}
