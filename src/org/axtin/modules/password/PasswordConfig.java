package org.axtin.modules.password;

import java.io.File;
import java.util.Random;

import org.axtin.container.facade.Container;
import org.axtin.marcely.configmanager2.ConfigFile;
import org.axtin.marcely.configmanager2.objects.Config;
import org.bukkit.plugin.Plugin;

public class PasswordConfig {
	
	private static final ConfigFile FILE = new ConfigFile(new File(Container.get(Plugin.class).getDataFolder() + "/passwords.cm2"));
	
	public static void load(PasswordManager manager){
		manager.getPasswords().clear();
		
		if(!FILE.exists()){
			saveDefault(manager);
			return;
		}
		
		FILE.load();
		
		if(FILE.getRootTree().getTreeChild("passwords") != null){
			for(Config c:FILE.getRootTree().getTreeChild("passwords").getChilds()){
				if(c.getType() == Config.TYPE_LISTITEM)
					manager.getPasswords().add(c.getValue());
			}
		}else
			saveDefault(manager);
		
		FILE.clear();
	}
	
	public static void save(PasswordManager manager){
		FILE.clear();
		
		for(String pw:manager.getPasswords())
			FILE.getPicker().addListItem("passwords", pw);
		
		FILE.getPicker().addEmptyLine();
		
		FILE.getPicker().addComment("Random generated passwords");
		for(int i=0; i<20; i++)
			FILE.getPicker().addListItem("random-gen", randomPassword());
		
		FILE.save();
	}
	
	private static void saveDefault(PasswordManager manager){
		manager.getPasswords().add("");
		
		save(manager);
		
		manager.getPasswords().clear();
	}
	
	private static String randomPassword(){
		final String availableChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!§$%&/()" + '"';
		String pw = "";
		
		for(int i=0; i<8; i++)
			pw += availableChars.charAt(new Random().nextInt(availableChars.length()));
		
		return pw;
	}
}
