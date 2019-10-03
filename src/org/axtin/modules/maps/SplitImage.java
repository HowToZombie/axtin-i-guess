package org.axtin.modules.maps;


import org.axtin.container.facade.Container;
import org.bukkit.plugin.Plugin;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Joseph on 7/7/2017.
 */
public class SplitImage {



    private int height,width;
    public int mapsHigh,mapsWide;

    public ArrayList<SoftReference<BufferedImage>> getImages(String s, AxtinMap.MapType m) throws IOException {
        ArrayList<BufferedImage> imgs = new ArrayList<>();
        ArrayList<SoftReference<BufferedImage>> cacheImage = new ArrayList<>();


        if (m.equals(AxtinMap.MapType.URL)) {
            BufferedImage imgTemp = getImageFromURL(s);

             height = imgTemp.getHeight();
             width = imgTemp.getWidth();

            if(height % 128 > 64){
                mapsHigh = (height/128) +1;
            }else{
                mapsHigh = height/128;
            }
            if(width % 128 > 64){
                mapsWide = (width/128) + 1;
            }else{
                mapsWide = width/128;
            }
            int measureH = height/mapsHigh, measureW = width/mapsWide;

            for(int i = mapsHigh; i > 0; i--){
                for(int a = 1; a <= mapsWide; a++){
                    imgs.add(imgTemp.getSubimage((a-1) * measureW,((i-1)*measureH),measureW,measureH));
                }
            }

            for(BufferedImage bi: imgs){
                cacheImage.add(new SoftReference<>(resize(bi, new Dimension(128, 128))));
            }
        } else {

            BufferedImage img = getImageFromDirectory(Container.get(Plugin.class).getDataFolder() + "/images/" + s + ".png");
            height = img.getHeight();
            width = img.getWidth();
            if(height % 128 > 64){
                mapsHigh = (height/128) +1;
            }else{
                mapsHigh = height/128;
            }
            if(width % 128 > 64){
                mapsWide = (width/128) + 1;
            }else{
                mapsWide = width/128;
            }
            int measureH = height/mapsHigh, measureW = width/mapsWide;

            for(int i = mapsHigh; i > 0; i--){
                for(int a = 1; a <= mapsWide; a++){
                    imgs.add(img.getSubimage((a-1) * measureW,((i-1)*measureH),measureW,measureH));
                }
            }

            for(BufferedImage bi: imgs){
                cacheImage.add(new SoftReference<>(resize(bi, new Dimension(128, 128))));
            }
        }
        return cacheImage;
    }

    public  BufferedImage getImageFromDirectory(String dir) throws IOException{
        boolean useCache = ImageIO.getUseCache();

        ImageIO.setUseCache(false);

        BufferedImage image = ImageIO.read(new File(dir));

        ImageIO.setUseCache(useCache);

        return image;
    }

    public  BufferedImage getImageFromURL(String url) throws IOException {
        boolean useCache = ImageIO.getUseCache();

        ImageIO.setUseCache(false);

        BufferedImage image = ImageIO.read(new URL(url));

        ImageIO.setUseCache(useCache);

        return image;
    }


    public  BufferedImage resize(final BufferedImage image, final Dimension size) throws IOException{
        final BufferedImage resized = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g = resized.createGraphics();
        g.drawImage(image, 0, 0, size.width, size.height, null);
        g.dispose();
        return resized;
    }
}
