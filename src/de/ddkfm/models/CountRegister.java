package de.ddkfm.models;

import de.ddkfm.util.MicroIIUtils;

public class CountRegister extends Register{
	public CountRegister(String name) {
		super(name);
	}

	@Override
	public void changeByExtern(Object sender, int index, boolean value) {
		setValue(index, value);
	}
	@Override
	protected void sendValueToOutput(int index, boolean value) {
		getConnection("programregister2increment_" + index).getReference().change(this, 0, value);
		System.out.println("sendValueToOutput(" + index + "," + value + ");");
	}
	public void acceptValues(int index){
		CounterLine counterline = (CounterLine) getConnection("programregister2memory").getReference();
		boolean[] values = MicroIIUtils.getBinaryString(index);
		for(int i = 0 ; i < 4 ; i++){
			setValue(i, values[i]);
		}
		counterline.changeCount(MicroIIUtils.getIntegerByBinaryNotation(values));
	}
	@Override
	public void acceptValues() {
		CounterLine counterline = (CounterLine) getConnection("programregister2memory").getReference();
		boolean[] values = new boolean[4];
		for(int i = 0 ; i < 4 ; i++){
			values[i] = getConnection("programswitch2programregister_" + i).getReference().getValue(0);
			setValue(i, values[i]);
		}
		counterline.changeCount(MicroIIUtils.getIntegerByBinaryNotation(values));
	}
}
