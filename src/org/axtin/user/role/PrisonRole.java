package org.axtin.user.role;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;

import static org.bukkit.Material.COBBLESTONE;

public enum PrisonRole {
    WOOD(-1,"wood","Wood", 0, new RankDrops(new Material[]{Material.LOG},new Double[]{1.0})),
    A(0, "a", "A",150000, new RankDrops(new Material[]{Material.COAL,Material.COAL_BLOCK,Material.IRON_INGOT},new Double[]{.80,.15,.05})),
    B(1, "b", "B",300000, new RankDrops(new Material[]{Material.COAL,Material.COAL_BLOCK,Material.IRON_INGOT},new Double[]{.80,.15,.05})),
    C(2, "c", "C",450000, new RankDrops(new Material[]{Material.COAL,Material.COAL_BLOCK,Material.IRON_INGOT},new Double[]{.80,.15,.05})),
    D(3, "d", "D",600000, new RankDrops(new Material[]{Material.COAL_BLOCK,Material.IRON_INGOT,Material.GOLD_INGOT},new Double[]{.80,.15,.05})),
    E(4, "e", "E",900000, new RankDrops(new Material[]{Material.COAL_BLOCK,Material.IRON_INGOT,Material.GOLD_INGOT},new Double[]{.80,.15,.05})),

    F(5, "f", "F",2600000, new RankDrops(new Material[]{Material.COAL_BLOCK,Material.IRON_INGOT,Material.GOLD_INGOT},new Double[]{.80,.15,.05})),
    G(6, "g", "G",3100000, new RankDrops(new Material[]{Material.IRON_INGOT,Material.GOLD_INGOT,Material.GOLD_BLOCK},new Double[]{.80,.15,.05})),
    H(7, "h", "H",3640000, new RankDrops(new Material[]{Material.IRON_INGOT,Material.GOLD_INGOT,Material.GOLD_BLOCK},new Double[]{.80,.15,.05})),
    I(8, "i", "I",4300000, new RankDrops(new Material[]{Material.IRON_INGOT,Material.GOLD_INGOT,Material.GOLD_BLOCK},new Double[]{.80,.15,.05})),
    J(9, "j", "J",4300000, new RankDrops(new Material[]{Material.GOLD_INGOT,Material.GOLD_BLOCK,Material.DIAMOND},new Double[]{.80,.15,.05})),
    K(10, "k", "K",4300000, new RankDrops(new Material[]{Material.GOLD_INGOT,Material.GOLD_BLOCK,Material.DIAMOND},new Double[]{.80,.15,.05})),
    L(11, "l", "L",4300000, new RankDrops(new Material[]{Material.GOLD_INGOT,Material.GOLD_BLOCK,Material.DIAMOND},new Double[]{.80,.15,.05})),
    M(12, "m", "M",4300000, new RankDrops(new Material[]{Material.GOLD_BLOCK,Material.DIAMOND,Material.DIAMOND_BLOCK},new Double[]{.80,.15,.05})),
    N(13, "n", "N",4300000, new RankDrops(new Material[]{Material.GOLD_BLOCK,Material.DIAMOND,Material.DIAMOND_BLOCK},new Double[]{.80,.15,.05})),
    O(14, "o", "O",4300000, new RankDrops(new Material[]{Material.GOLD_BLOCK,Material.DIAMOND,Material.DIAMOND_BLOCK},new Double[]{.80,.15,.05})),
    P(15, "p", "P",4300000, new RankDrops(new Material[]{Material.DIAMOND,Material.DIAMOND_BLOCK,Material.EMERALD},new Double[]{.80,.15,.05})),
    Q(16, "q", "Q",4300000, new RankDrops(new Material[]{Material.DIAMOND,Material.DIAMOND_BLOCK,Material.EMERALD},new Double[]{.80,.15,.05})),
    R(17, "r", "R",4300000, new RankDrops(new Material[]{Material.DIAMOND,Material.DIAMOND_BLOCK,Material.EMERALD},new Double[]{.80,.15,.05})),
    S(18, "s", "S",4300000, new RankDrops(new Material[]{Material.DIAMOND_BLOCK,Material.EMERALD,Material.EMERALD_BLOCK},new Double[]{.80,.15,.05})),
    T(19, "t", "T",4300000, new RankDrops(new Material[]{Material.DIAMOND_BLOCK,Material.EMERALD,Material.EMERALD_BLOCK},new Double[]{.80,.15,.05})),
    U(20, "u", "U",4300000, new RankDrops(new Material[]{Material.DIAMOND_BLOCK,Material.EMERALD,Material.EMERALD_BLOCK},new Double[]{.80,.15,.05})),
    V(21, "v", "V",4300000, new RankDrops(new Material[]{Material.EMERALD,Material.EMERALD_BLOCK,Material.OBSIDIAN},new Double[]{.80,.15,.05})),
    W(22, "w", "W",4300000, new RankDrops(new Material[]{Material.EMERALD,Material.EMERALD_BLOCK,Material.OBSIDIAN},new Double[]{.80,.15,.05})),
    X(23, "x", "X",4300000, new RankDrops(new Material[]{Material.EMERALD,Material.EMERALD_BLOCK,Material.OBSIDIAN},new Double[]{.80,.15,.05})),
    Y(24, "y", "Y",4300000, new RankDrops(new Material[]{Material.EMERALD_BLOCK,Material.OBSIDIAN,Material.GOLD_INGOT},new Double[]{.80,.15,.05})),
    Z(25, "z", "Z",4300000, new RankDrops(new Material[]{Material.EMERALD_BLOCK,Material.OBSIDIAN,Material.GOLD_INGOT},new Double[]{.80,.15,.05}));

    private int identifier;
    private String registry;
    private String rank;
    private RankDrops drops;
    private int rankup;

    PrisonRole(int identifier, String registry, String rank, int rankup, RankDrops drops) {
        this.identifier = identifier;
        this.registry = registry;
        this.rank = rank;
        this.drops = drops;
        this.rankup = rankup;

    }

    public static PrisonRole getRole(String registry) {
        for (PrisonRole role : PrisonRole.values()) {
            if (role.getRegistry().equalsIgnoreCase(registry)) {
                return role;
            }
        }

        return PrisonRole.A;
    }

    public static PrisonRole getRole(int identifier) {
        for (PrisonRole role : PrisonRole.values()) {
            if (role.getIdentifier() == identifier) {
                return role;
            }
        }

        return PrisonRole.A;
    }

    public int getIdentifier() {
        return this.identifier;
    }

    public String getRegistry() {
        return this.registry;
    }

    public String getName() {
        return this.rank;
    }

    public RankDrops getDrops() {
        return this.drops;
    }

    public int getRankup(){return rankup;}

}
