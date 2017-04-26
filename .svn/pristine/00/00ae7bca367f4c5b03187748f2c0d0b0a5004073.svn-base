package de.ddkfm.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class LogicValue {
	private String name;
	private List<BooleanProperty> values = new ArrayList<BooleanProperty>();
	private Map<String, Connection> connections = new HashMap<String, Connection>();
	protected Logger logger;
	public LogicValue(int countValues, String name){
		this.setName(name);
		logger = LogManager.getLogger("LogicValue(" + getName() + ")");
		for(int i = 0 ; i < countValues ; i++){
			this.values.add(new SimpleBooleanProperty(false));
			BooleanProperty currentProperty = this.values.get(i);
			currentProperty.addListener((obs,oldValue,newValue)->{
				this.sendValueToOutput(values.indexOf(currentProperty), newValue);
			});
		}
	}
	public void addConnection(Connection c){
		this.connections.put(c.getName(), c);
	}
	public Connection getConnection(String name){
		return this.connections.get(name);
	}
	public Collection<Connection> getConnections(){
		return connections.values();
	}
	public Collection<Connection> getInputConnection(){
		List<Connection> list = new ArrayList<Connection>();
		for(Connection conn: getConnections())
			if(conn.getType() == Connection.CONNECTION_IN)
				list.add(conn);
		return list;
	}
	public Collection<Connection> getOutputConnection(){
		List<Connection> list = new ArrayList<Connection>();
		for(Connection conn: getConnections())
			if(conn.getType() == Connection.CONNECTION_OUT)
				list.add(conn);
		return list;
	}
	public BooleanProperty getValueProperty(int i){
		return this.values.get(i);
	}
	public boolean getValue(int i){
		return getValueProperty(i).get();
	}
	public void setValue(int i, boolean value){
		this.getValueProperty(i).set(value);
	}
	public String getName(){return this.name;}
	public int getValueCount(){return this.values.size();}
	public void setName(String name){this.name = name;}
	public void change(LogicValue sender, int index, boolean value){}
	public void changeByExtern(Object sender, int index, boolean value){}
	protected void sendValueToOutput(int index, boolean value){}
}
