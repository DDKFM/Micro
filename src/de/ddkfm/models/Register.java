package de.ddkfm.models;

import java.util.List;

public class Register extends LogicValue {
	public Register(String name) {
		super(4, name);
	}

	@Override
	public void changeByExtern(Object sender, int index, boolean value) {
		setValue(index, value);
	}

	public void acceptValues() {
		List<Connection> inputList = (List<Connection>) getInputConnection();
		String baseName = inputList.get(0).getReference().getName();
		baseName = baseName.substring(0,baseName.length() - 1);
		for(int i = 0 ; i < 4 ; i++)
			setValue(i,getConnection(baseName + i).getReference().getValue(0));
	}
	@Override
	protected void sendValueToOutput(int index, boolean value) {
		String baseName = "";
		try {
			List<Connection> list = (List)getOutputConnection();
			baseName = list.get(0).getReference().getName();
			baseName = baseName.substring(0,baseName.length() -1 );
			getConnection(baseName + index).getReference().change(this, 0, value);
		} catch (Exception e) {
			logger.error("Fehler: " + baseName + "_" , e);
		}
	}

}
