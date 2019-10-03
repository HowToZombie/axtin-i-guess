package org.axtin.user.role;

import org.bukkit.Material;

import java.util.ArrayList;

/**
 * Created by Joseph on 3/22/2017.
 */
public class RankDrops {

    private ArrayList<Material> ratio = new ArrayList<>();
    private Material[] mat;
    private Double[] stat;

    public RankDrops(Material[] mat, Double[] d){
        this.mat = mat;
        this.stat = d;
        int i = 0;
        for(Material m : mat){
            for(double count = d[i] * 100; count > 0.0; count --){
                ratio.add(m);
            }
            i++;
        }
    }

    public ArrayList<Material> getDrops(){
        return ratio;
    }

    public Material[] rawMaterials(){
        return mat;
    }
    public Double[] rawStats(){
        return stat;
    }
}
