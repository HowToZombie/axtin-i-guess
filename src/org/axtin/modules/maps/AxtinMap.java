package org.axtin.modules.maps;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.ref.SoftReference;

public class AxtinMap extends MapRenderer {

    private SoftReference<BufferedImage> cacheImage;
    private boolean hasRendered = false;

    public AxtinMap(SoftReference<BufferedImage> bi) throws IOException {
        this.cacheImage = bi;
    }

    public enum MapType {
        URL, IMAGE
    }

    @Override
    public void render(MapView view, MapCanvas canvas, Player player){
        if(this.hasRendered){
            return;
        }

        if(this.cacheImage.get() != null){
            canvas.drawImage(0, 0, this.cacheImage.get());
            this.hasRendered = true;
        }else{
            player.sendMessage(ChatColor.RED + "Attempted to render the image, but the cached image was null!");
            this.hasRendered = true;
        }
    }

}