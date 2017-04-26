package de.ddkfm.models;

import de.ddkfm.util.MicroIIUtils;
import de.ddkfm.util.transform.Instruction;
import de.ddkfm.util.transform.Microcode;

public class Decoder extends LogicValue {
	private Microcode microcode;
	public Decoder(String name) {
		super(0, name);
		microcode = new Microcode();
	}
	public void decode(){
		boolean[] inputValues = new boolean[4];
		for(int i = 0 ; i < 4 ; i++){
			LogicValue currentDataline = getConnection("instructionbus2decoder_" + i).getReference();
			inputValues[i] = currentDataline.getValue(0);
		}
		for(Connection conn : getOutputConnection()){
			conn.getReference().change(this, 0, false);
		}
		int instructionIndex = MicroIIUtils.getIntegerByBinaryNotation(inputValues);
		Instruction actualInstruction = microcode.getInstruction(instructionIndex);
		boolean[] instructioncode = actualInstruction.getMicrocode();
		getConnection("decoder2inputswitch_0").getReference().setValue(0, instructioncode[0]);
		getConnection("decoder2inputswitch_1").getReference().setValue(0, instructioncode[1]);
		getConnection("decoder2ALU_2").getReference().setValue(0, instructioncode[2]);
		getConnection("decoder2ALU_1").getReference().setValue(0, instructioncode[3]);
		getConnection("decoder2ALU_0").getReference().setValue(0, instructioncode[4]);
		getConnection("decoder2saveswitch_0").getReference().setValue(0, instructioncode[5]);
		getConnection("decoder2saveswitch_1").getReference().setValue(0, instructioncode[6]);
		getConnection("decoder2outputswitch").getReference().setValue(0, instructioncode[7]);
		getConnection("decoder2Muxer_2").getReference().setValue(0, instructioncode[8]);
		getConnection("decoder2Muxer_1").getReference().setValue(0, instructioncode[9]);
		getConnection("decoder2Muxer_0").getReference().setValue(0, instructioncode[10]);
		getConnection("decoder2dataswitch").getReference().setValue(0, instructioncode[11]);
		getConnection("decoder2ramswitch_0").getReference().setValue(0, instructioncode[12]);
		getConnection("decoder2ramswitch_1").getReference().setValue(0, instructioncode[13]);
	}
	public Microcode getMicrocode(){
		return this.microcode;
	}
	
}
