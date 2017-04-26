package de.ddkfm.models;

import de.ddkfm.util.MicroIIUtils;

public class Muxer extends LogicValue {
	public Muxer(String name) {
		super(5, name);
	}
	@Override
	public void change(LogicValue sender, int index, boolean value) {
		if(sender.getName().contains("decoder")){
			boolean[] inputValues = new boolean[4];
			inputValues[3] = this.getConnection("decoder2Muxer_" + 0).getReference().getValue(0);
			inputValues[2] = this.getConnection("decoder2Muxer_" + 1).getReference().getValue(0);
			inputValues[1] = this.getConnection("decoder2Muxer_" + 2).getReference().getValue(0);
			inputValues[0] = false;
			int input = MicroIIUtils.getIntegerByBinaryNotation(inputValues);
			System.out.println("ALU Input: " + input);
			for(int i = 0 ; i < 5 ; i++){
					setValue(i, i == input);
			}
		}
		multiplex();
	}
	@Override
	public void changeByExtern(Object sender, int index, boolean value) {
		if(index <= 5){
			for(int i = 0 ; i < 5 ; i++)
				setValue(i, value ? index == i : false);
		}
		multiplex();
	}
	private void multiplex(){
		boolean outputValue = getConnection("programswitch2muxer").getReference().getValue(0);
		if(getValue(0))
			outputValue = getConnection("flags2muxer_0").getReference().getValue(0);
		else
			if(getValue(1))
				outputValue = getConnection("flags2muxer_1").getReference().getValue(0);
			else
				if(getValue(2))
					outputValue = getConnection("flags2muxer_2").getReference().getValue(0);
				else
					if(getValue(3))
						outputValue = false;
					else
						if(getValue(4))
							outputValue = true;
		getConnection("programswitch2muxer").getReference().change(this, 0, outputValue);
	}
}
