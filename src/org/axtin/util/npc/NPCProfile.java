package org.axtin.util.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.UUID;

public class NPCProfile {
    private String uniqueId = null;
    private String name = null;
    private String skin = null;

    public NPCProfile(String uniqueId, String name) {
        this(uniqueId, name, name);
    }

    public NPCProfile(String uniqueId, String name, String skin) {
        this.uniqueId = uniqueId.replaceAll("-", "");
        this.name = name;
        this.skin = skin;
    }

    public GameProfile getProfile() {
        GameProfile profile = new GameProfile(parse(this.uniqueId), this.name);
        this.setProfile(profile);

        return profile;
    }

    private void setProfile(GameProfile profile) {
        String skinUniqueId = getUniqueId(this.skin);

        try {
            URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + skinUniqueId + "?unsigned=false");
            URLConnection connection = url.openConnection();

            connection.setUseCaches(false);
            connection.setDefaultUseCaches(false);
            connection.addRequestProperty("User-Agent", "Mozilla/5.0");
            connection.addRequestProperty("Cache-Control", "no-cache, no-store, must-revalidate");
            connection.addRequestProperty("Pragma", "no-cache");

            String result = new Scanner(connection.getInputStream(), "UTF-8").useDelimiter("\\A").next();
            JSONParser parser = new JSONParser();
            Object object = parser.parse(result);
            JSONArray properties = (JSONArray) ((JSONObject) object).get("properties");

            for (int i = 0; i < properties.size(); i++) {
                JSONObject property = (JSONObject) properties.get(i);
                String name = (String) property.get("name");
                String value = (String) property.get("value");
                String signature = property.containsKey("signature") ? (String) property.get("signature") : null;

                if (signature != null) {
                    profile.getProperties().put(name, new Property(name, value, signature));
                } else {
                    profile.getProperties().put(name, new Property(name, value));
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private String getUniqueId(String name) {
        return Bukkit.getOfflinePlayer(name).getUniqueId().toString().replaceAll("-", "");
    }

    private UUID parse(String uniqueId) {
        String[] components = new String[]{uniqueId.substring(0, 8), uniqueId.substring(8, 12), uniqueId.substring(12, 16), uniqueId.substring(16, 20), uniqueId.substring(20, uniqueId.length())};

        StringBuilder builder = new StringBuilder();

        for (String component : components) {
            builder.append(component).append('-');
        }

        builder.setLength(builder.length() - 1);
        return UUID.fromString(builder.toString());
    }
}
