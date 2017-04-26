package de.ddkfm.util.transform;

import java.util.Arrays;
import java.util.List;

import de.ddkfm.models.Memory;

public class Program {
	private ProgramLine[] program = new ProgramLine[16];
	public Program() {
		for(int i = 0 ; i < 16 ; i++){
			program[i] = new ProgramLine("0000", "0000", "0000", "");
			setProgramLine(i, program[i]);
		}
	}
	public List<ProgramLine> getProgramLines(){
		return Arrays.asList(this.program);
	}
	public ProgramLine getProgramLine(int index){
		return program[index];
	}
	public void setProgramLine(int index, ProgramLine programLine){
		this.program[index] = programLine;
		for(int i = 0 ; i < 12 ; i++){
			boolean value = false;
			if(i < 4){
				value = programLine.getInstruction()[i % 4];
			}else
				if(i < 8){
					value = programLine.getAddress()[i % 4];
				}else
					if(i < 12){
						value = programLine.getData()[i % 4];	
					}
		}
	}
}
