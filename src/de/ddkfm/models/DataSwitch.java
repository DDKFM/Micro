package de.ddkfm.models;

public class DataSwitch extends LogicValue {

	public DataSwitch(String name) {
		super(1, name);
	}
	@Override
	public void change(LogicValue sender, int index, boolean value) {
		switch(sender.getName()){
		case "decoder2dataswitch":
			this.setValue(0, value);
			break;
		}
		sendValueToOutput(0, false);
	}
	@Override
	protected void sendValueToOutput(int index, boolean value) {
		if(!getValue(0))
			for(int i = 0; i < 4 ; i++){
				Connection currentConnection = getConnection("dataswitch2databus_" + i);
				currentConnection.getReference().change(this, 0, false);
			}
		else
			for(int i = 0; i < 4 ; i++){
				Connection inputConnection = getConnection("memory_data2dataswitch_" + i);
				Connection outputConnection = getConnection("dataswitch2databus_" + i);
				outputConnection.getReference().change(this, 0, inputConnection.getReference().getValue(0));
			}
	}
	@Override
	public void changeByExtern(Object sender, int index, boolean value) {
		setValue(0, value);
	}

}
