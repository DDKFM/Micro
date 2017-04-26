package de.ddkfm.models;

public class BaseSwitch extends LogicValue {
	public BaseSwitch(String name) {
		super(2, name);
	}
	@Override
	public void change(LogicValue sender, int index, boolean value) {
		this.setValue(index, value);
	}
	@Override
	protected void sendValueToOutput(int index, boolean value) {
		getConnection("baseswitch2bcd_" + index).getReference().change(this, 0, value);
	}
}