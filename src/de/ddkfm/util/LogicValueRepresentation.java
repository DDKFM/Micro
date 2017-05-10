package de.ddkfm.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Node;
/**
 * LogicValueRepresentation represents a <LogicValue> node in the XML-Document with name, typen, connections, ...
 * */
public class LogicValueRepresentation {
	private String name;
	private String type;
	private Map<String, Map<String, String>> connections;
	private List<Map<String, String>> points;
	private static Logger logger = LogManager.getRootLogger();

	public LogicValueRepresentation(Node node) {
		this.name = "";
		this.type = "";
		this.connections = new HashMap<String, Map<String, String>>();
		this.points = new ArrayList<Map<String, String>>();
		this.name = node.getAttributes().getNamedItem("name").getNodeValue();
		for (int i = 0; i < node.getChildNodes().getLength(); i++) {
			Node childNode = node.getChildNodes().item(i);
			if (childNode.getNodeType() != Node.TEXT_NODE) {
				switch (childNode.getNodeName().toLowerCase()) {
				case "connections":
					for (int j = 0; j < childNode.getChildNodes().getLength(); j++) {
						Node connection = childNode.getChildNodes().item(j);
						Map<String, String> connectionMap = new HashMap<String, String>();
						if (connection.getNodeType() != Node.TEXT_NODE) {
							connectionMap.put("name", connection.getAttributes().getNamedItem("name").getNodeValue());
							connectionMap.put("type", connection.getAttributes().getNamedItem("type").getNodeValue());
							this.connections.put(connectionMap.get("name"), connectionMap);
						}
					}
					break;
				case "layout":
					for (int j = 0; j < childNode.getChildNodes().getLength(); j++) {
						Node point = childNode.getChildNodes().item(j);
						Map<String, String> pointMap = new HashMap<String, String>();
						if (point.getNodeType() != Node.TEXT_NODE) {
							pointMap.put("x", point.getAttributes().getNamedItem("x").getNodeValue());
							pointMap.put("y", point.getAttributes().getNamedItem("y").getNodeValue());
							this.points.add(pointMap);
						}
					}
					break;
				case "type": {
					this.type = node.getTextContent().trim();
					break;
				}
				}
			}
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, Map<String, String>> getConnections() {
		return connections;
	}

	public void setConnections(Map<String, Map<String, String>> connections) {
		this.connections = connections;
	}

	public List<Map<String, String>> getPoints() {
		return points;
	}

	public void setPoints(List<Map<String, String>> points) {
		this.points = points;
	}

}
