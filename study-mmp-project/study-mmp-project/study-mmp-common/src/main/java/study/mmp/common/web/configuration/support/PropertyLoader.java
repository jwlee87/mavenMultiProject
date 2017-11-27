package study.mmp.common.web.configuration.support;

import java.io.IOException;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

@Slf4j
public class PropertyLoader {
	
	private Properties properties = new Properties();	//web.xml property 주입용

	public PropertyLoader(String propertyFilePath) {
		load(propertyFilePath);
	}
	
	public PropertyLoader(String... buildPropertyFilePaths) {
		for (String propertyFilePath : buildPropertyFilePaths) {
			load(propertyFilePath);
		}
	}
	
	private void load(String path) {
		try {
			ClassPathResource cpr = new ClassPathResource(path);
            properties = PropertiesLoaderUtils.loadProperties(cpr);
			log.info("!property file load " + cpr.getPath());
		} catch (IOException ex) {
			throw new IllegalStateException("Could not load 'properties': " + ex.getMessage());
		}
	}
	
	public String getProp(String key) {
		return properties.getProperty(key);
	}
	
	public String getProp(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}
	
	public Properties getProperties() {
		return properties;
	}
}
