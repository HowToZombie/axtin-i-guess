package org.axtin.util.messages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.jline.internal.InputStreamReader;

public class MessageManager {

	private Properties props;
	private ChatColor cPrimary;
	private ChatColor cSecondary;
	
	public MessageManager(String path, ChatColor cPrimary, ChatColor cSecondary) throws IOException {
		try(InputStream utf8in = new FileInputStream(path)) {
			Reader reader = new InputStreamReader(utf8in, "UTF-8");
			props = new Properties();
			props.load(reader);
			this.cPrimary = cPrimary;
			this.cSecondary = cSecondary;
			utf8in.close();
		} 
	}
	
	public String getString(String key, Object... args) {
		String s = props.getProperty(key);
		boolean sNull = false;
		if(s == null) {
			s = "{" + key + "}";
			sNull = true;
		}
		
		if(args.length > 0 && !sNull) {
			s = processString(s);
			MessageFormat m = new MessageFormat(s);
			s = m.format(args);
		}
		
		return s;
	}
	
	private String processString(String s) {
		s = cPrimary + s;
		s = s.replaceAll("\\{", cSecondary + "{");
		s = s.replaceAll("\\}", "}" + cPrimary);
		return s;
	}
	
	public static boolean createPropsFile(String path, Map<String, String> values) throws IOException {
		
		File file = new File(path);
		file.getParentFile().mkdirs();
		file.createNewFile();
		Properties prop = new Properties();
		InputStream in = new FileInputStream(path);
		prop.load(in);
		
		for(Entry<String, String> entry : values.entrySet()) {
			prop.setProperty(entry.getKey(), entry.getValue());
		}
		
		prop.store(new FileOutputStream(path), null);
		
		in.close();
		return file.exists();
		
	}
	
	
}
