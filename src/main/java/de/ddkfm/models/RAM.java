package de.ddkfm.models;

/**
 * the RAM store data from the alu calculation in 16 RAM registers
 * */
public class RAM extends LogicValue {

	public RAM(String name) {
		super(64, name);
	}


	@Override
	public void change(LogicValue sender, int index, boolean value) {
		String senderName = sender.getName();
		
		RAMLine ramline = (RAMLine) getConnection("ramregister2ram").getReference();
		int count = ramline.getCount();
		
		int lineNumber = -1;
		String senderWithoutDigits = senderName;
		if(Character.isDigit(senderName.charAt(senderName.length() - 1))){
			lineNumber = Integer.parseInt(senderName.substring(senderName.length()-1));
			senderWithoutDigits = senderName.substring(0,senderName.length() - 2);
		}
		switch(senderWithoutDigits){
		case "ramregister2ram":
			for(int i = 0 ; i < 4 ; i++)
				this.change(this, 4 * count + i, getValue(4 * count + i));
			break;
		case "ram2ramswitch":
			this.setValue(4 * count + lineNumber, value);
			this.change(this, 4 * count + lineNumber, value);
			break;
		case "ram":
			setValue(index, value);
			String ioOutput = "ram2io_" + (index % 4);
			String ramOutput = "ram2ramswitch_" + (index % 4);
			LogicValue ioLine = getConnection(ioOutput).getReference();
			ioLine.change(this, 0, getValue(index % 4));
			LogicValue ramswitchLine = getConnection(ramOutput).getReference();
			ramswitchLine.change(this,0,getValue(4 * count + (index % 4)));
			break;
		}
		
	}
	@Override
	public void changeByExtern(Object sender, int index, boolean value) {
		this.change(this,index,value);
	}

}
