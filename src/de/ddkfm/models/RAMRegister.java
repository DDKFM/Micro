package de.ddkfm.models;

import de.ddkfm.util.MicroIIUtils;

public class RAMRegister extends Register{
	public RAMRegister(String name) {
		super(name);
	}

	@Override
	public void changeByExtern(Object sender, int index, boolean value) {
		setValue(index, value);
	}
	public void acceptValues(){
		RAMLine ramline = (RAMLine) getConnection("ramregister2ram").getReference();
		boolean[] values = new boolean[4];
		for(int i = 0 ; i < 4 ; i++){
			values[i] = getConnection("addressbus2ramregister_" + i).getReference().getValue(0);
			setValue(i, values[i]);
		}
		ramline.changeCount(MicroIIUtils.getIntegerByBinaryNotation(values));
	}
}
