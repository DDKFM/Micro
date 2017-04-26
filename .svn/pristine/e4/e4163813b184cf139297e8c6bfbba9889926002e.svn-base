package de.ddkfm.models;

import de.ddkfm.util.MicroIIUtils;

public class Increment extends LogicValue {
	public Increment(String name) {
		super(0, name);
	}
	@Override
	public void change(LogicValue sender, int index, boolean value) {
		boolean[] inputValues = new boolean[4];
		for(int i = 0 ; i < 4 ; i++){
			LogicValue lv = this.getConnection("programregister2increment_" + i).getReference();
			inputValues[i] = lv.getValue(0);
		}
		int input = MicroIIUtils.getIntegerByBinaryNotation(inputValues);
		int output = (input + 1) % 16;
		boolean[] outputValues = MicroIIUtils.getBinaryString(output);
		for(int i = 0 ; i < 4 ; i++)
			this.sendValueToOutput(i, outputValues[i]);
	}
	@Override
	protected void sendValueToOutput(int index, boolean value) {
		getConnection("increment2programswitch_" + index).getReference().change(this,0,value);
	}
}
