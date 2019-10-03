package org.axtin.modules.mines;

import java.util.HashMap;

/**
 * Created by Jo on 11/10/2015.
 */
public class RankHolder {
    public static HashMap<String, RankHolder> data = new HashMap<String, RankHolder>();

    protected String rank;
    protected Cuboid region;


    public RankHolder(String s, Cuboid cub) {
        this.rank = s;
        this.region = cub;


    }

    public String getRank() {
        return rank;
    }

    public Cuboid getCube() {
        return region;
    }
}

