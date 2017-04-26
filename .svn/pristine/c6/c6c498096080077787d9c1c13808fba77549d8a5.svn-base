package de.ddkfm.models;

public class DigitalDisplay extends LogicValue {
	
	public DigitalDisplay(String name) {
		super(7, name);
	}
	@Override
	public void change(LogicValue sender, int index, boolean value) {
		if(sender.getName().contains("bcd2display")){
			int segmentIndex = Integer.parseInt(sender.getName().substring(sender.getName().length()-1));
			setValue(segmentIndex, value);
		}
		if(getName().equals("display3")){
			this.getConnection("");
		}
		
	}
	@Override
	protected void sendValueToOutput(int index, boolean value) {
		
	}
	@Override
	public void changeByExtern(Object sender, int index, boolean value) {
		setValue(index, value);
	}
}
