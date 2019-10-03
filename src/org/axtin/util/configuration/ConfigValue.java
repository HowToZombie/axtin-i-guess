package org.axtin.util.configuration;

import java.util.ArrayList;
import java.util.List;

public class ConfigValue {

	private final Object object;
	private final String path;
	public static List<Object> acceptedClasses = null;
	
	
	public ConfigValue(String path, Object object) {
		this.object = object;
		this.path = path;
	}
	
	public static ConfigValue getConfigValue(String path, Object object) {
		if(getAcceptedClasses().contains(object.getClass())) {
			return new ConfigValue(path, object);
		} else {
			return null;
		}
	}

	public Object getObject() {
		return this.object;
	}
	
	@Deprecated
	public ConfigSection getConfigSection() {
		return (ConfigSection) this.object;
	}
	
	@SuppressWarnings("unchecked")
	@Deprecated
	public <T> T getT(T o) {
		return (T) this.object;
	}
	
	public String getPath() {
		return this.path;
	}
	
	public static void addAcceptedClasses() {
		if(acceptedClasses == null) {
			acceptedClasses = new ArrayList<>();
			acceptedClasses.add(Integer.class);
			acceptedClasses.add(Double.class);
			acceptedClasses.add(Short.class);
			acceptedClasses.add(Long.class);
			acceptedClasses.add(Float.class);
			acceptedClasses.add(Boolean.class);
			acceptedClasses.add(Character.class);
			acceptedClasses.add(String.class);
		}
	}
	
	public static List<Object> getAcceptedClasses() {
		addAcceptedClasses();
		return acceptedClasses;
	}
	
}
