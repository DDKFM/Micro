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
/**
 * the main class the the microprocessorsimulation which is the superclass of all parts of the simulator
 * @author mschaedlich
 * */
public class LogicValue {
	private String name;
	private List<BooleanProperty> values = new ArrayList<BooleanProperty>();
	private Map<String, Connection> connections = new HashMap<String, Connection>();
	protected Logger logger;
	/**
	 * Constructor of the class
	 * @param countValues the maximum number of boolean values for the LogicValue
	 * @param name the name of the LogicValue
	 * */
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

	/**
	 * add a given Connection to the LogicValue which is then accessible by name
	 * @param c Connection which is added to the LogicValue
	 * */
	public void addConnection(Connection c){
		this.connections.put(c.getName(), c);
	}

	/**
	 * return the Connection specified by the name as String
	 * @param name name of the connection(name of the related LogicValue)
	 * @return the Connection
	 * */
	public Connection getConnection(String name){
		return this.connections.get(name);
	}

	/**
	 * return all connections that are added to the LogicValue as a Collection of Connections
	 * @return Collection of Connections
	 * */
	public Collection<Connection> getConnections(){
		return connections.values();
	}

	/**
	 * return all connections which are incoming connections(type = CONNECTION_IN)
	 * @return all incoming connections
	 * */
	public Collection<Connection> getInputConnection(){
		List<Connection> list = new ArrayList<Connection>();
		for(Connection conn: getConnections())
			if(conn.getType() == Connection.CONNECTION_IN)
				list.add(conn);
		return list;
	}

	/**
	 * return all connections which are outgoint connections(type = CONNECTION_OUT)
	 * @return all outgoing connections
	 * */
	public Collection<Connection> getOutputConnection(){
		List<Connection> list = new ArrayList<Connection>();
		for(Connection conn: getConnections())
			if(conn.getType() == Connection.CONNECTION_OUT)
				list.add(conn);
		return list;
	}

	/**
	 * return the valueProperty specified by an index(to add Listeners to this BooleanProperty)
	 * @param i index of the BooleanProperty( should by less than the valuesCount)
	 * @return BooleanProperty
	 * */
	public BooleanProperty getValueProperty(int i){
		return this.values.get(i);
	}
	/**
	 * return the BooleanProperty as a simple boolean object
	 * @param i index of the associated BooleanProperty
	 * @return simple boolean object
	 * */
	public boolean getValue(int i){
		return getValueProperty(i).get();
	}

	/**
	 * set the BooleanProperty to the given boolean value
	 * @param i index of the associated BooleanProperty
	 * @param value boolean to which the BooleanProperty can be set
	 * */
	public void setValue(int i, boolean value){
		this.getValueProperty(i).set(value);
	}

	/**
	 * return the name from the LogicVale
	 * @return name as String
	 * */
	public String getName(){return this.name;}

	/**
	 * return the Size of the intern BooleanProperts array or the Count of the values
	 * */
	public int getValueCount(){return this.values.size();}

	/**
	 * set the name of the LogicValue
	 * */
	public void setName(String name){this.name = name;}

	/**
	 * change will trigger the LogicValue that a incoming connection changed its value
	 * @param sender the logicValue which fired the change method
	 * @param index which index should be changed
	 * @param value to which value should the index be set
	 * */
	public void change(LogicValue sender, int index, boolean value){}

	/**
	 * same function like change but in this the case can be every object( normally a Button)
	 * @param the object which fired the change method
	 * @param index which index should be changed
	 * @param value to which value should the index be set
	 * */
	public void changeByExtern(Object sender, int index, boolean value){}

	/**
	 * protected method which should write the specified internal value to all (or a part of) outputs
	 * @param index index that should be write to the output
	 * @param value value that should be wirte to the output
	 * */
	protected void sendValueToOutput(int index, boolean value){}
}
