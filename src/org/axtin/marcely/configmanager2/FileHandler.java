package org.axtin.marcely.configmanager2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.axtin.marcely.configmanager2.objects.*;

public class FileHandler {
	
	private final ConfigFile file;
	
	public FileHandler(ConfigFile file){
		this.file = file;
	}
	
	public int load(){
		final File f = file.getFile();
		
		// check if everything is ok
		if(!f.exists())
			return IOResult.RESULT_FAILED_LOAD_MISSINGFILE;
		else if(!f.canRead())
			return IOResult.RESULT_FAILED_LOAD_NOPERMS;
		
		// prepare
		file.getRootTree().clear();
		
		// start
		try{
			final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF8"));
			
			String line = null;
			
			Tree tree = file.getRootTree();
			
			while((line = reader.readLine()) != null){
				line = replaceFirstSpaces(line);
				
				if(line.length() == 0){
					tree.addChild(new EmptyLine(tree));
					continue;
				}
				
				final char firstChar = line.charAt(0);
				final char lastChar = line.charAt(line.length()-1);
				
				if(firstChar == '#')
					tree.addChild(new Comment(tree, replaceFirstSpaces(line.substring(1))));
				else if(line.contains(":")){
					final String[] strs = line.split(":");
					String value = "";
					
					for(int i=1; i<strs.length; i++){
						value += strs[i];
						
						if(i+1<strs.length)
							value += ":";
					}
					
					if(!line.startsWith("!"))
						tree.addChild(new Config(line, replaceLastSpaces(strs[0]), tree, replaceFirstSpaces(value)));
					else
						tree.addChild(new Description(tree, strs[0], value));
				}else if(lastChar == '{'){
					final String name = replaceLastSpaces(line.substring(0, line.length()-1));
					
					tree = new Tree(name, tree);
					tree.getParent().addChild(tree);
				}else if(lastChar == '}'){
					tree = tree.getParent();
					
					if(tree == null){
						reader.close();
						return IOResult.RESULT_FAILED_LOAD_NOTVALID;
					}
					
				}else if(!replaceLastSpaces(replaceFirstSpaces(line)).isEmpty()){
					final String value = replaceLastSpaces(line);
					
					tree.addChild(new ListItem(value, tree));
				}else
					tree.addChild(new EmptyLine(tree));
			}
			
			reader.close();
			
			if(!tree.equals(file.getRootTree()))
				return IOResult.RESULT_FAILED_LOAD_NOTVALID;
			
		}catch(IOException e){
			e.printStackTrace();
			
			return IOResult.RESULT_FAILED_UNKOWN;
		}
		
		return IOResult.RESULT_SUCCESS;
	}
	
	public int save(){
		final File f = file.getFile();
		
		// check if everything is ok
		if(f.exists() && !f.canWrite())
			return IOResult.RESULT_FAILED_SAVE_NOPERMS;
		
		// start
		try{
			// recreate
			if(f.exists())
				f.delete();
			f.createNewFile();
			
			// check if everything is ok again
			if(!f.canWrite())
				return IOResult.RESULT_FAILED_SAVE_NOPERMS;
			
			final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "UTF8"));
			final List<String> lines = getLines(file.getRootTree(), "");
			
			for(String line:lines){
				writer.write(line);
				writer.newLine();
			}
			
			writer.close();
		}catch(IOException e){
			e.printStackTrace();
			
			return IOResult.RESULT_FAILED_UNKOWN;
		}
		
		
		return IOResult.RESULT_SUCCESS;
	}
	
	private List<String> getLines(Tree tree, String currentPrefix){
		return getLines(tree, currentPrefix, tree.equals(file.getRootTree()));
	}
	
	private List<String> getLines(Tree tree, String currentPrefix, boolean root){
		final List<String> lines = new ArrayList<String>();
		
		for(Config c:tree.getChilds()){
			if(c.getType() == Config.TYPE_TREE){
				
				lines.add(currentPrefix + c.getName() + " {");
				lines.addAll(getLines((Tree) c, currentPrefix + "	", false));
				lines.add(currentPrefix + "}");
				
			}else if(c.getType() == Config.TYPE_COMMENT)
				lines.add(currentPrefix + "# " + c.getValue());
			else if(c.getType() == Config.TYPE_EMPTYLINE)
				lines.add("");
			else if(c.getType() == Config.TYPE_DESCRIPTION)
				lines.add(currentPrefix + "!" + c.getName() + ": " + c.getValue());
			else if(c.getType() == Config.TYPE_LISTITEM)
				lines.add(currentPrefix + c.getValue());
			else
				lines.add(currentPrefix + c.getName() + ": " + c.getValue());
		}
		
		return lines;
	}
	
	private String replaceFirstSpaces(String str){
		while(str.startsWith(" ") || str.startsWith("	"))
			str = str.substring(1, str.length());
		
		return str;
	}
	
	private String replaceLastSpaces(String str){
		while(str.endsWith(" ") || str.endsWith("	"))
			str = str.substring(0, str.length()-1);
		
		return str;
	}
	
	public ConfigFile getFile(){
		return this.file;
	}
}