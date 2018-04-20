package de.ddkfm.models;

import de.ddkfm.util.MicroIIUtils;

public class BCD extends LogicValue {
	public BCD(String name) {
		super(0, name);
	}
	@Override
	public void change(LogicValue sender, int index, boolean value) {
		sendValueToOutput(index, value);
	}
	@Override
	protected void sendValueToOutput(int index, boolean value) {
		boolean[] inputValues = new boolean[4];
		for(int i = 0 ; i < 4 ; i++){
			inputValues[i] = getConnection("ram2io_" + i).getReference().getValue(0);
		}
		int input = MicroIIUtils.getIntegerByBinaryNotation(inputValues);
		int base;
		this.getConnection("");
		boolean base0 = getConnection("baseswitch2bcd_0").getReference().getValue(0);
		boolean base1 = getConnection("baseswitch2bcd_1").getReference().getValue(0);
		if(base0 && !base1)
			base = 10;
		else
			if(!base0 && !base1)
				base = 2;
			else
				base = 16;
		String convertedStringToBase = Integer.toString(input, base).toUpperCase();
		while(convertedStringToBase.length() < 4)
			convertedStringToBase = "0" + convertedStringToBase;
	
		for(int i = 0 ; i < 4 ; i++){
			boolean[] booleanValues = get7SegmentNotation(convertedStringToBase.charAt(i));
			for(int j = 0; j < 7 ; j++)
				getConnection("bcd2display" + i + "_" + j).getReference().change(this,0,booleanValues[j]);
		}
		
	}
	private boolean[] get7SegmentNotation(char character){
		String result = "";
		switch(character){
		case '0': result = "1111110";break;
		case '1': result = "0110000";break;
		case '2': result = "1101101";break;
		case '3': result = "1111001";break;
		case '4': result = "0110011";break;
		case '5': result = "1011011";break;
		case '6': result = "1011111";break;
		case '7': result = "1110000";break;
		case '8': result = "1111111";break;
		case '9': result = "1111011";break;
		case 'A': result = "1110111";break;
		case 'B': result = "0011111";break;
		case 'C': result = "1001110";break;
		case 'D': result = "0111101";break;
		case 'E': result = "1001111";break;
		case 'F': result = "1000111";break;
		}
		boolean[] booleanReturn = new boolean[7];
		for(int i = 0 ; i < 7 ; i++){
			booleanReturn[i] = result.charAt(i) == '1';
		}
		return booleanReturn;
	}
}
