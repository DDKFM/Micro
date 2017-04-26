package de.ddkfm.models;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.ddkfm.util.LogicValueRepresentation;

public class Processor {
	private static Logger logger = LogManager.getLogger("Processor");
	private Map<String, LogicValue> elements = new HashMap<String, LogicValue>();
	private InstructionCycle instructionCycle;

	public Processor(List<LogicValueRepresentation> elementData) {
		for (LogicValueRepresentation lvr : elementData) {
			switch (lvr.getType()) {
			case "2WayDataline":
			case "3WayDataline":
			case "4WayDataline":
			case "7WayDataline":
				int maxLines = Integer.parseInt(lvr.getType().substring(0, 1));
				for (int i = 0; i < maxLines; i++) {
					Constructor<?> c1;
					try {
						c1 = Class.forName("de.ddkfm.models.Dataline").getConstructor(String.class);
						LogicValue logicValue = (LogicValue) c1.newInstance(lvr.getName() + "_" + i);

						elements.put(lvr.getName() + "_" + i, logicValue);
						logger.info(
								"Element: " + lvr.getName() + "_" + i + ": " + lvr.getType() + " wurde verarbeitet");
					} catch (NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException
							| IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
						logger.error("Fehler beim Erstellen des Elements: " + lvr.getName());
					}
				}
				break;
			default:
				Constructor<?> c0;
				try {
					c0 = Class.forName("de.ddkfm.models." + lvr.getType()).getConstructor(String.class);
					LogicValue logicValue = (LogicValue) c0.newInstance(lvr.getName());
					elements.put(lvr.getName(), logicValue);
					logger.info("Element: " + lvr.getName() + ": " + lvr.getType() + " wurde verarbeitet");
				} catch (NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException
						| IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
					logger.error("Fehler beim Erstellen des Elements: " + lvr.getName(),e1);
				}
				break;
			}
		}
		// Zweite For-Schleife für Connections
		for (LogicValueRepresentation lvr : elementData) {
			switch (lvr.getType()) {

			// Im Falle Bundle, Referenzen auf entweder einzelne Datalines aus
			// Connection oder gleiche Referenz
			// für alle(wenn Bauteil). sonst einfache Namensreferenz ohne Index

			// Merken: Flags in eine Klasse bündeln, sonst Redundanz bei
			// Datalines(eine Line würde alle 3 Flags setzen
			case "2WayDataline":
			case "3WayDataline":
			case "4WayDataline":
			case "7WayDataline":
				int maxLines = Integer.parseInt(lvr.getType().substring(0, 1));
				for (int i = 0; i < maxLines; i++) {
					try {
						for (Map<String, String> connMap : lvr.getConnections().values()) {
							LogicValueRepresentation connReferenceLVR = Processor.getLVRByName(elementData,
									connMap.get("name"));
							String connReferenceType = connReferenceLVR.getType();

							LogicValue connectionLogic;
							if (connReferenceType.matches("(2|3|4)WayDataline")) {
								connectionLogic = getLogicValueByName(connReferenceLVR.getName() + "_" + i);
							} else {
								connectionLogic = getLogicValueByName(connReferenceLVR.getName());
							}
							LogicValue currentLogicValue = getLogicValueByName(lvr.getName() + "_" + i);
							boolean connType = connMap.get("type").equals("out") ? Connection.CONNECTION_OUT
									: Connection.CONNECTION_IN;
							currentLogicValue.addConnection(new Connection(connectionLogic, connType));
						}
						logger.info("Dem Element " + lvr.getName() + " wurden Verbindungen hinzugefügt("
								+ lvr.getConnections().size() + ")");
					} catch (Exception e1) {
						logger.error("Fehler beim Zuweisen der Verbindungen zum Element: " + lvr.getName());
					}
				}
				break;
			default:
				for (Map<String, String> connMap : lvr.getConnections().values()) {
					LogicValueRepresentation connReferenceLVR = Processor.getLVRByName(elementData,
							connMap.get("name"));
					String connReferenceType = connReferenceLVR.getType();

					boolean connType = connMap.get("type").equals("out") ? Connection.CONNECTION_OUT
							: Connection.CONNECTION_IN;
					LogicValue currentLogicValue = getLogicValueByName(lvr.getName());
					if (connReferenceType.matches("(2|3|4|7)WayDataline")) {
						int maximumLines = Integer.parseInt(connReferenceType.substring(0, 1));
						for (int i = 0; i < maximumLines; i++) {
							LogicValue connectionLogic = getLogicValueByName(connReferenceLVR.getName() + "_" + i);
							if (currentLogicValue != null) {
								currentLogicValue.addConnection(new Connection(connectionLogic, connType));
							}
						}
					} else {
						LogicValue connectionLogic = getLogicValueByName(connReferenceLVR.getName());
						if (currentLogicValue != null) {
							currentLogicValue.addConnection(new Connection(connectionLogic, connType));
						}
					}
				}
			}
		}
		instructionCycle = new InstructionCycle(this);
	}

	public LogicValue getLogicValueByName(String name) {
		return this.elements.get(name);
	}

	private static LogicValueRepresentation getLVRByName(List<LogicValueRepresentation> elementData, String name) {
		int i = 0;
		boolean found = false;
		while (i < elementData.size() && !found) {
			found = elementData.get(i).getName().equals(name);
			i++;
		}
		return (found ? elementData.get(i - 1) : null);
	}

	public Map<String, LogicValue> getAllLogicValues() {
		return elements;
	}

	public InstructionCycle getInstructionCycle() {
		return instructionCycle;
	}
}
