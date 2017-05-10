package de.ddkfm.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * Utility class with useful methodes which are occurs many times in the Micro application
 * */
public class MicroIIUtils {
	private static Logger logger = LogManager.getRootLogger();
	public static String VERSION = "1.2.008 DEV";
	/***
	 * loading the Logger from a outer configuration(config/logging.xml) or from the inner configuration und reload them
	 */
	public static void loadingLogger(){
		File outerLoggingConfig = new File("config" + File.separator + "logging.xml");
		if(outerLoggingConfig.exists() && !outerLoggingConfig.isDirectory()){
			System.setProperty("log4j.configurationFile",outerLoggingConfig.toURI().toString());
		}else{
			String innerLoggingConfig = ClassLoader.getSystemClassLoader().getResource("de/ddkfm/util/xml/log4j2.xml").toExternalForm();
			System.setProperty("log4j.configurationFile", innerLoggingConfig);
		}
		LogManager.shutdown();
		logger = LogManager.getRootLogger();
		logger.info("Logging-Konfiguration geladen von:" + System.getProperty("log4j.configurationFile"));
	}
	/**
	 * return the binary notion from a given integer value (only 4 bit)
	 * @param i the given integer value
	 * @return returns the binary notation as an arrays of boolean values
	 * */
	public static boolean[] getBinaryString(int i){
		boolean[] ret = new boolean[4];
		int lengthBin = Integer.toBinaryString(i).length();
		String h = "";
		for (int j = 0;j<(4-lengthBin);j++)
			h += "0";
		h += Integer.toBinaryString(i);
		int j = 0;
		for(char s : h.toCharArray()){
			ret[j] = s == '1' ? true : false;
			j++;
		}
		return ret;
	}
	/**
	 * Returns a String representation of a given boolean array
	 * @param values an array of boolean values
	 * @return the binary String (as regex: (0|1)*)
	 * */
	public static String getStringByBinaryNotation(boolean[] values){
		String result = "";
		for(boolean b : values)
			result += b ? "1" : "0";
		return result;
	}
	/**
	 * the reverse function of getStringByBinaryNotation which gets a boolean array on a given binary string
	 * @param inputString a given binary which should be in binary notation( in regex: (0|1)*)
	 * @return the boolean array, if the binary String is not matched with the regex then is will be return null
	 * */
	public static boolean[] getBinaryNotationByString(String inputString){
		if(inputString.matches("(0|1)*")){
			boolean[] result = new boolean[inputString.length()];
			for(int i = 0 ; i < inputString.length() ; i++)
				result[i] = inputString.charAt(i) == '1';
			return result;
		}else
			return null;
	}
	/**
	 * returns the integer value of a boolean array
	 * @param values given boolean values
	 * @return a integer value. if the boolean array is empty then 0 will return
	 * */
	public static int getIntegerByBinaryNotation(boolean[] values){
		int integer = 0;
		for(int i = 3 ; i >= 0 ; i--){
			integer += (values[3-i] ? 1 : 0) * Math.pow(2, i);
		}
		return integer;
	}
	/**
	 * @see Integer parseInt(binaryString, 2)
	 * */
	public static int getIntegerByBinaryString(String binaryString){
		return Integer.parseInt(binaryString,2);
	}
	/**
	 * invert a given boolean array (0->1, 1->0) (one's complement)
	 * @param values a boolean array
	 * @return the inverted boolean array
	 * */
	private static boolean[] invert(boolean[] values){
		boolean[] returnValues = new boolean[4];
		for(int i = 0 ; i < 4 ; i++)
			returnValues[i] = !values[i];
		return returnValues;
	}
	/**
	 * return a boolean array by a given integer value
	 * @param digit if digit is positive then the binary notation will return
	 *              else
	 *           		the digit will be transformed into binary, inverted and added by one
	 * @return if the given digit is negative then the two's complement will be return
	 * */
	public static boolean[] twosComplement(int digit){
		if(digit >= 0)
			return MicroIIUtils.getBinaryString(digit);
		else{
			boolean[] positiveValues = MicroIIUtils.getBinaryString(Math.abs(digit));
			System.out.println(MicroIIUtils.getStringByBinaryNotation(positiveValues));
			boolean[] negativeValues = MicroIIUtils.invert(positiveValues);
			System.out.println(MicroIIUtils.getStringByBinaryNotation(negativeValues));
			boolean[] outputValues   = MicroIIUtils.getBinaryString(MicroIIUtils.getIntegerByBinaryNotation(negativeValues) + 1);
			return outputValues;
		}
	}
	/**
	 * return the application.properties(config/application.properties or /de/ddkfm/util/application.properties) as Properties object
	 * @return properties will be empty if the application.properties not exists
	 * */
	public static Properties getApplicationProperties(){
		Properties prop = new Properties();
		File applicationFile = new File("config" + File.separator + "application.properties");
		try {
			if(applicationFile.exists() && !applicationFile.isDirectory()){
				prop.load(new FileInputStream("config" + File.separator + "application.properties"));
			}else{
				prop.load(ClassLoader.getSystemResource("de/ddkfm/util/application.properties").openStream());
			}
		} catch (FileNotFoundException e) {
			logger.error("Datei nicht gefunden: ",e);
		} catch (IOException e) {
			logger.error("IOException: ",e);
		}
		return prop;
	}

	/***
	 * return the specific Property from application.properties by key
	 */
	public static String getApplicationProperty(String key){
		return MicroIIUtils.getApplicationProperties().getProperty(key);
	}
	/***
	 * return the Path for the Themes depending on the application.properties
	 */
	public static String getThemePath(){
		String path = MicroIIUtils.getApplicationProperty("micro2.config.themes.path");
		if(path.equals("%jar%"))
			return ClassLoader.getSystemResource("de/ddkfm/themes").toString();
		else
			try {
				return new File(path).toURI().toURL().toString();
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return "";
			}
	}
	/**
	 * return the Attributes.xml URL (File with the LogicValueRepresentations)
	 * */
	public static URL getAttributesXML(){
		URL attributesFile = ClassLoader.getSystemResource("de/ddkfm/util/xml/Attributes.xml");
		return attributesFile;
	}
	
}
