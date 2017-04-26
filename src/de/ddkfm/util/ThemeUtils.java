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

public class ThemeUtils {
	private static Logger logger = LogManager.getLogger("ThemeUtils");
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
	public static String getThemeProperty(String key){
		return MicroIIUtils.getProperty(ThemeUtils.getThemeProperties(), key);
	}



	public static Color getSignalHighColor() {
		String color = getThemeProperty("micro2.theme.color.on");
		return getColor(color);
	}
	public static Color getSignalLowColor() {
		String color = getThemeProperty("micro2.theme.color.off");
		return getColor(color);
	}
	private static Color getColor(String color) {
		if(color.matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")) {
			return Color.web(color);
		} else {
			logger.error("micro2.theme.color.on ist nicht valide");
		}
		return Color.WHITE;
	}
	public static String getDefaultTheme(){
		return ThemeUtils.getThemeProperty("micro2.theme.default");
	}
	public static URL getCSSFile() throws MalformedURLException{
		String themePath = MicroIIUtils.getThemePath();
		String theme = ThemeUtils.getDefaultTheme();
		return new URL(themePath + "/" + theme + "/" + "style.css");
	}
	public static InputStream getResourcePart(String resourceName){
		String resourceDirectory = MicroIIUtils.getThemePath() + "/" + 
								   ThemeUtils.getDefaultTheme();
		URL resourceFile = null;
		try {
			resourceFile = new URL(resourceDirectory + "/" + resourceName + ".png");
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			return resourceFile.openStream();
		} catch (IOException e) {
			return null;
		}
	}
}
