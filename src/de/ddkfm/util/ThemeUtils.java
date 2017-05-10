package de.ddkfm.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * Utilty class for all theme-related functions
 * */
public class ThemeUtils {
	private static Logger logger = LogManager.getLogger("ThemeUtils");
	/**
	 * return the Properties object for the themes.properties
	 * */
	public static Properties getThemeProperties(){
		Properties prop = new Properties();
		String themePath = MicroIIUtils.getThemePath();
		try {
			prop.load(new URL(themePath + "/" + "themes.properties").openStream());
		} catch (IOException e) {
			logger.error("Fehler beim Laden der application.properties-Datei",e);
		}
		return prop;
	}
	/**
	 * get a simple Property from the themes.properties by key
	 * */
	public static String getThemeProperty(String key){
		return ThemeUtils.getThemeProperties().getProperty(key);
	}
	/**
	 * get the Color for the active LogicValues e.g. Dataline
	 * */
	public static Color getSignalHighColor() {
		String color = getThemeProperty("micro2.theme.color.on");
		return getColor(color);
	}
	/**
	 * get the Color for the active LogicValues e.g. Dataline
	 * */
	public static Color getSignalLowColor() {
		String color = getThemeProperty("micro2.theme.color.off");
		return getColor(color);
	}
	/**
	 * return the Color object by the given hexadecimal Colorstring. if the String is not valid Color.WHITE is returned
	 * */
	private static Color getColor(String color) {
		if(color.matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")) {
			return Color.web(color);
		} else {
			logger.error("micro2.theme.color.on ist nicht valide");
		}
		return Color.WHITE;
	}
	/**
	 * get the default theme which is specified in the themes.properties
	 * */
	public static String getDefaultTheme(){
		return ThemeUtils.getThemeProperty("micro2.theme.default");
	}

	/**
	 * get the style.css file depending on the default theme
	 * */
	public static URL getCSSFile() throws MalformedURLException{
		String themePath = MicroIIUtils.getThemePath();
		String theme = ThemeUtils.getDefaultTheme();
		return new URL(themePath + "/" + theme + "/" + "style.css");
	}
	/**
	 * get the resource from the current theme specified by the filename(without extension)
	 * */
	public static InputStream getResourcePart(String resourceName){
		String resourceDirectory = MicroIIUtils.getThemePath() + "/" + 
								   ThemeUtils.getDefaultTheme();
		URL resourceFile = null;
		try {
			resourceFile = new URL(resourceDirectory + "/" + resourceName + ".png");
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		try {
			return resourceFile.openStream();
		} catch (IOException e) {
			return null;
		}
	}
}
