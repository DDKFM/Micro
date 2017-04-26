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

public class MicroIIUtils {
	private static Logger logger = LogManager.getRootLogger();
	public static String VERSION = "1.1.0";
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
	public static String getStringByBinaryNotation(boolean[] values){
		String result = "";
		for(boolean b : values)
			result += b ? "1" : "0";
		return result;
	}
	public static boolean[] getBinaryNotationByString(String inputString){
		if(inputString.matches("(0|1)*")){
			boolean[] result = new boolean[inputString.length()];
			for(int i = 0 ; i < inputString.length() ; i++)
				result[i] = inputString.charAt(i) == '1';
			return result;
		}else
			return null;
	}
	public static int getIntegerByBinaryNotation(boolean[] values){
		int integer = 0;
		for(int i = 3 ; i >= 0 ; i--){
			integer += (values[3-i] ? 1 : 0) * Math.pow(2, i);
		}
		return integer;
	}
	public static int getIntegerByBinaryString(String binaryString){
		return Integer.parseInt(binaryString,2);
	}
	private static boolean[] invert(boolean[] values){
		boolean[] returnValues = new boolean[4];
		for(int i = 0 ; i < 4 ; i++)
			returnValues[i] = !values[i];
		return returnValues;
	}
	public static boolean[] twosComplement(int digit){
		if(digit >= 0)
			return MicroIIUtils.getBinaryString(digit);
		else{
			boolean[] positiveValues = MicroIIUtils.getBinaryString(Math.abs(digit));
			System.out.println(MicroIIUtils.printValues(positiveValues));
			boolean[] negativeValues = MicroIIUtils.invert(positiveValues);
			System.out.println(MicroIIUtils.printValues(negativeValues));
			boolean[] outputValues   = MicroIIUtils.getBinaryString(MicroIIUtils.getIntegerByBinaryNotation(negativeValues) + 1);
			System.out.println(MicroIIUtils.printValues(outputValues));
			return outputValues;
		}
	}
	public static String printValues(boolean[] values){
		String result = "";
		for(boolean b : values)
			result += b ? "1" : "0";
		return result;
	}
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
	public static String getProperty(Properties prop, String key){
		return prop.getProperty(key);
	}
	public static String getApplicationProperty(String key){
		return MicroIIUtils.getProperty(MicroIIUtils.getApplicationProperties(), key);
	}
	public static String getThemePath(){
		String path = MicroIIUtils.getApplicationProperty("micro2.config.themes.path");
		if(path.equals("%jar%"))
			return ClassLoader.getSystemResource("de/ddkfm/themes").toString();
		else
			try {
				return new File(path).toURI().toURL().toString();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "";
			}
	}
	public static URL getAttributesXML(){
		URL attributesFile = ClassLoader.getSystemResource("de/ddkfm/util/xml/Attributes.xml");
		return attributesFile;
	}
	
}
