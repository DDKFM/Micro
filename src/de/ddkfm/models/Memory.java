package de.ddkfm.models;

import de.ddkfm.util.transform.Program;
import de.ddkfm.util.transform.ProgramLine;
import de.ddkfm.views.LogicButton;
/**
 * contains all program data for the microprocessor
 * */
public class Memory extends LogicValue {
	private Program program;
	public Memory(String name) {
		super(192, name);
		program = new Program();
	}
	/**
	 * returns the internal program of human readable instructions
	 * */
	public Program getProgram(){
		return this.program;
	}

	/**
	 * set the internal program
	 * */
	public void setProgram(Program program){
		this.program = program;
	}

	/**
	 * force update the internal program by the memory data
	 * */
	public void acceptMemory(){
		for(int i = 0 ; i < 16 ; i++){
			String instruction = "";
			String address = "";
			String data = "";
			for(int k = 0 ; k < 4 ; k++){
				instruction += getValue(12 * i + k) ? "1" : "0";
				address += getValue(12 * i + 4 + k) ? "1" : "0";
				data += getValue(12 * i + 8 + k) ? "1" : "0";
			}
			getProgram().setProgramLine(i, new ProgramLine(instruction, address, data, ""));
		}
	}
	/**
	 * force update the memory by the internal program
	 * */
	public void acceptProgram(){
		for(int i = 0 ; i < 16 ; i++){
			ProgramLine pl = getProgram().getProgramLine(i);
			for(int k = 0 ;  k < 4 ; k++){
				if((12 * i + k ) > 190){
					int a = 0;
				}
				setValue(12 * i + k, pl.getInstruction()[k]);
				setValue(12 * i + 4 + k, pl.getAddress()[k]);
				setValue(12 * i + 8 + k, pl.getData()[k]);
			}
		}
	}
	@Override
	public void change(LogicValue sender, int index, boolean value) {
		if(sender.getName().equals("programregister2memory")){
			CounterLine counterline = (CounterLine) sender;
			int count = counterline.getCount();
			for(int i = 0 ; i < 12 ; i++){
				String connectionName = "";
				if( i < 4)
					connectionName = "memory_instruction2instructionbus";
				else
					if(i > 3 && i < 8)
						connectionName = "memory_address2addressbus";
					else
						connectionName = "memory_data2dataswitch";
				LogicValue currentDataline = getConnection(connectionName + "_" + i % 4).getReference();
				currentDataline.setValue(0, getValue(12 * count + i));
			}
		}
	}
	@Override
	public void changeByExtern(Object sender, int index, boolean value) {
		if(sender instanceof LogicButton || sender instanceof Program){
			this.setValue(index, value);
		}
	}

}