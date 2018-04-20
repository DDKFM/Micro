package de.ddkfm.views;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.ddkfm.models.CountRegister;
import de.ddkfm.models.Processor;
import de.ddkfm.util.LogicValueRepresentation;
import javafx.scene.layout.Pane;

public class ProcessorFX extends Pane{
	private Map<String, LogicNode> elements = new HashMap<String, LogicNode>();
	private static Logger logger = LogManager.getLogger("ProcessorFX");
	public ProcessorFX(List<LogicValueRepresentation> elementData, Processor processor) {
		super();
		setPrefWidth(1050);
		setPrefHeight(600);
		for(LogicValueRepresentation lvr : elementData){
			switch(lvr.getType()){
				case "2WayDataline":
				case "3WayDataline":
				case "4WayDataline":{
					int maxLines = Integer.parseInt(lvr.getType().substring(0, 1));
					for(int i = 0 ; i < maxLines ; i++){
						Constructor<?> c0;
						try {
							c0 = Class.forName("de.ddkfm.views.DatalineFX").getConstructor();
							LogicNode lv = (LogicNode) c0.newInstance();
							for(Map<String, String> point  : lvr.getPoints()){
								String xString = point.get("x");
								String yString = point.get("y");
								double absolutX = 0;
								if(xString.contains("a")){
									double baseX = Double.parseDouble(xString.substring(0, xString.indexOf("a")-1));
									absolutX = baseX;
									if(xString.contains("+"))
										absolutX += 10*i;
									else
										absolutX += -10*i;
								}else{
									absolutX = Double.parseDouble(xString);
								}
								double absolutY = 0;
								if(yString.contains("a")){
									double baseY = Double.parseDouble(yString.substring(0, yString.indexOf("a")-1));
									absolutY = baseY;
									if(yString.contains("+"))
										absolutY += +10*i;
									else
										absolutY += -10*i;
								}else{
									absolutY = Double.parseDouble(yString);
								}
								lv.addPoint(absolutX, absolutY);
							}
							lv.setLogic(processor.getLogicValueByName(lvr.getName() + "_" + i));
							this.getChildren().add(lv);
							this.elements.put(lvr.getName() + "_" + i, lv);
							logger.info("Element: " + lvr.getName() + ": " + lvr.getType() + " wurde verarbeitet(" + lvr.getPoints() +  ")");
						} catch (NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
							logger.error("Fehler beim Erstellen des Elements: " + lvr.getName(),e1);
						}
					}
					break;
				}
				case "7WayDataline":{
					int maxLines = Integer.parseInt(lvr.getType().substring(0, 1));
					for(int i = 0 ; i < maxLines ; i++){
						Constructor<?> c0;
						try {
							c0 = Class.forName("de.ddkfm.views.DatalineFX").getConstructor();
							LogicNode lv = (LogicNode) c0.newInstance();
							for(Map<String, String> point  : lvr.getPoints()){
								String xString = point.get("x");
								String yString = point.get("y");
								double absolutX = 0;
								if(xString.contains("a")){
									double baseX = Double.parseDouble(xString.substring(0, xString.indexOf("a")-1));
									absolutX = baseX;
									if(xString.contains("+"))
										absolutX += 5*i;
									else
										absolutX += -5*i;
								}else{
									absolutX = Double.parseDouble(xString);
								}
								double absolutY = 0;
								if(yString.contains("a")){
									double baseY = Double.parseDouble(yString.substring(0, yString.indexOf("a")-1));
									absolutY = baseY;
									if(yString.contains("+"))
										absolutY += +5*i;
									else
										absolutY += -5*i;
								}else{
									absolutY = Double.parseDouble(yString);
								}
								lv.addPoint(absolutX, absolutY);
							}
							lv.setLogic(processor.getLogicValueByName(lvr.getName() + "_" + i));
							this.getChildren().add(lv);
							this.elements.put(lvr.getName() + "_" + i, lv);
							logger.info("Element: " + lvr.getName() + ": " + lvr.getType() + " wurde verarbeitet(" + lvr.getPoints() +  ")");
						} catch (NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
							logger.error("Fehler beim Erstellen des Elements: " + lvr.getName(),e1);
						}
					}
					break;
				}
				case "CountRegister":
				case "RAMRegister":
					Constructor<?> c0;
					try {
						c0 = Class.forName("de.ddkfm.views." + "Register" + "FX").getConstructor();
						LogicNode lv = (LogicNode) c0.newInstance();
						for(Map<String, String> point  : lvr.getPoints()){
							double x = Double.parseDouble(point.get("x"));
							double y = Double.parseDouble(point.get("y"));
							lv.addPoint(x, y);
						}
						lv.setLogic(processor.getLogicValueByName(lvr.getName()));
						this.getChildren().add(lv);
						this.elements.put(lvr.getName(), lv);
						logger.info("Element: " + lvr.getName() + ": " + lvr.getType() + " wurde verarbeitet(" + lvr.getPoints() +  ")");
					} catch (NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
						logger.error("Fehler beim Erstellen des Elements: ");
					}
					break;	
				default:
					Constructor<?> c2;
					try {
						c2 = Class.forName("de.ddkfm.views." + lvr.getType() + "FX").getConstructor();
						LogicNode lv = (LogicNode) c2.newInstance();
						for(Map<String, String> point  : lvr.getPoints()){
							double x = Double.parseDouble(point.get("x"));
							double y = Double.parseDouble(point.get("y"));
							lv.addPoint(x, y);
						}
						lv.setLogic(processor.getLogicValueByName(lvr.getName()));
						this.getChildren().add(lv);
						this.elements.put(lvr.getName(), lv);
						logger.info("Element: " + lvr.getName() + ": " + lvr.getType() + " wurde verarbeitet(" + lvr.getPoints() +  ")");
					} catch (NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
						logger.error("Fehler beim Erstellen des Elements: " + lvr.getName());
					}
					break;	
			}
		}		
		CountRegister cr = (CountRegister) processor.getLogicValueByName("programregister");
		cr.acceptValues(15);
			
	}
}
