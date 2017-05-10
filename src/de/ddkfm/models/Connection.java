package de.ddkfm.models;
/**
 * The connection class which contains the corresponding LogicValue and the type of the connection (IN or OUT)
 * TODO: check whether the connection type is needed yet
 * */
public class Connection {
	public static final boolean CONNECTION_OUT = true;
	public static final boolean CONNECTION_IN = false;
	
	private LogicValue reference;
	private boolean type;
	private String name;
	public Connection(LogicValue reference, boolean type) {
		super();
		this.reference = reference;
		this.type = type;
		this.name = reference != null ? reference.getName() : "";
	}
	public LogicValue getReference() {
		return reference;
	}
	public void setReference(LogicValue reference) {
		this.reference = reference;
	}
	public boolean getType() {
		return type;
	}
	public void setType(boolean type) {
		this.type = type;
	}
	public void setName(String name){
		this.name = name;
	}
	public String getName(){
		return name;
	}
	
}
