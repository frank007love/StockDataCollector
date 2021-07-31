package org.tonylin.stock;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tonylin.util.io.Cleaner;

public class SystemConfig {

	private static Logger LOGGER = LoggerFactory.getLogger(SystemConfig.class);
	private static Properties mProp = null; 
	
	static {
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream("./config/sysConfig.properties");
			mProp = new Properties();
			mProp.load(inputStream);
		} catch( Exception e ){
			LOGGER.error("Init system config failed: {}", e.getMessage());
		} finally {
			Cleaner.close(inputStream);
		}
	}
	
	/**
	 * Get the output folder path.
	 * 
	 * @return
	 */
	static public String getOutputFolder(){
		Object output = mProp.get("outputFolder");
		return output == null ? "output" : (String)output;
	}
}
