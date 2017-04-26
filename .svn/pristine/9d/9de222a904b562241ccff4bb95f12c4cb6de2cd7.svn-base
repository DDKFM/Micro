package de.ddkfm.models;

import de.ddkfm.util.MicroIIUtils;

public class ALU extends LogicValue {
	public ALU(String name) {
		super(6, name);
	}
	@Override
	public void change(LogicValue sender, int index, boolean value) {
		if(sender.getName().contains("decoder")){
			boolean[] inputValues = new boolean[4];
			inputValues[3] = this.getConnection("decoder2ALU_" + 0).getReference().getValue(0);
			inputValues[2] = this.getConnection("decoder2ALU_" + 1).getReference().getValue(0);
			inputValues[1] = this.getConnection("decoder2ALU_" + 2).getReference().getValue(0);
			inputValues[0] = false;
			int input = MicroIIUtils.getIntegerByBinaryNotation(inputValues);
			for(int i = 0 ; i < 6 ; i++){
				if(input > 0)
					setValue(i, i == (input - 1));
				else
					setValue(i, false);
			}
		}
	}
	@Override
	protected void sendValueToOutput(int index, boolean value) {
		
	}
	public void calculate(){
		boolean[] inputValuesA = new boolean[4];
		boolean[] inputValuesB = new boolean[4];
		for(int i = 0 ; i < 4 ; i++){
			inputValuesA[i] = getConnection("registerA2ALU_" + i).getReference().getValue(0);
			inputValuesB[i] = getConnection("registerB2ALU_" + i).getReference().getValue(0);
		}
		int a = MicroIIUtils.getIntegerByBinaryNotation(inputValuesA);
		int b = MicroIIUtils.getIntegerByBinaryNotation(inputValuesB);
		int c = 0;
		if(getValue(0))
			c = a + b;
			else 
				if(getValue(1))
					c = a - b;
				else 
					if(getValue(2))
						c = a * b;	
					else 
						if(getValue(3))
							c = a / b;
						else 
							if(getValue(4))
								c = a;
							else
								if(getValue(5))
									c = b;
								else
									c = -255;//No Calculation
		System.out.println(a + " " + b + " " + c);
		if (c != -255) {
			getConnection("ALU2Flags_0").getReference().change(this, 0, c == 0);
			getConnection("ALU2Flags_1").getReference().change(this, 0, c > 15);
			getConnection("ALU2Flags_2").getReference().change(this, 0, c < 0);
		}
		if(c > 15)
			c %= 16;
		boolean[] outputValues;
		if(c < 0 && c != -255)
			outputValues = MicroIIUtils.twosComplement(c);
		else
			outputValues = MicroIIUtils.getBinaryString(c != -255 ? c : 0);
		for(int i = 0; i < 4 ; i++)
			getConnection("ALU2saveswitch_" + i).getReference().change(this, 0, outputValues[i]);
		
	}
	@Override
	public void changeByExtern(Object sender, int index, boolean value) {
		if(index <= 6){
			for(int i = 0 ; i < 6 ; i++)
				setValue(i, value ? index == i : false);
		}
	}
}
