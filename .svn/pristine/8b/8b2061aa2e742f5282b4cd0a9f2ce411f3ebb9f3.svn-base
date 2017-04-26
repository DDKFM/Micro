package de.ddkfm.models;

public class Dataline extends LogicValue {

	public Dataline(String name) {
		super(1,name);
	}
	@Override
	public void change(LogicValue sender, int index, boolean value) {
		logger.debug("AUFRUF(" + sender.getName() + "):" + index + ", " + value);
		this.setValue(index, value);
	}
	@Override
	protected void sendValueToOutput(int index, boolean value) {
		for(Connection conn : getConnections()){
			if(conn.getType() == Connection.CONNECTION_OUT){
				conn.getReference().change(this, index, value);
			}
		}
	}

}
