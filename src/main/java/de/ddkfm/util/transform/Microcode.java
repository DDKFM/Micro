package de.ddkfm.util.transform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * The Microcode class represents the Microcode for the decoder in the simulation
 * The Microcode contains 16 instructions
 * @see Instruction
 * By default the instructions will be loaded with default instructions
 * */
public class Microcode {
	private Instruction[] instructions = new Instruction[16];
	public static final Instruction LDA_CONST = new Instruction(0, "LDA", "const", "10000000011100", "L�dt Zahl X in Register A");
	public static final Instruction LDA_ADDRESS = new Instruction(1, "LDA", "address", "10000000011010", "L�dt Zahl X aus Adresse Y in Register A");
	public static final Instruction LDB_CONST = new Instruction(2, "LDB", "const", "01000000011100", "L�dt Zahl X in Register B");
	public static final Instruction LDB_ADDRESS = new Instruction(3, "LDB", "address", "01000000011010", "L�dt Zahl X aus Adresse Y in Register B");
	public static final Instruction MUL = new Instruction(4, "MUL", "", "00011100011000", "Multipiziert Zahl A und Zahl B");
	public static final Instruction DIV = new Instruction(5, "DIV", "", "00100100011000", "Dividiert Zahl A und Zahl B");
	public static final Instruction ADD = new Instruction(6, "ADD", "", "00001100011000", "Addiert Zahl A und Zahl B");
	public static final Instruction SUB = new Instruction(7, "SUB", "", "00010100011000", "Subtrahiert Zahl A und Zahl B");
	public static final Instruction STX = new Instruction(8, "STX", "address", "00000001011001", "Speichert Zahl aus Register X auf Adresse Y");
	public static final Instruction STA = new Instruction(9, "STA", "address", "00101010011001", "Speichert Zahl aus Register A auf Adresse Y");
	public static final Instruction STB = new Instruction(10, "STB", "address", "00110010011001", "Speichert Zahl aus Register B auf Adresse Y");
	public static final Instruction JMP = new Instruction(11, "JMP", "statement", "00000000100000", "Springt auf Befehl X");
	public static final Instruction JZE = new Instruction(12, "JZE", "statement", "00000000000000", "Springt auf Befehl X wenn Zero-Flag aktiv");
	public static final Instruction JOV = new Instruction(13, "JOV", "statement", "00000000001000", "Springt auf Befehl X wenn Overflow-Flag aktiv");
	public static final Instruction JNE = new Instruction(14, "JNE", "statement", "00000000010000", "Springt auf Befehl X wenn Negativ-Flag aktiv");
	public static final Instruction NOP = new Instruction(15, "NOP", "", "00000000011000", "No Operation");
	public Microcode() {
		instructions[0]  = LDA_CONST;
		instructions[1]  = LDA_ADDRESS;
		instructions[2]  = LDB_CONST;
		instructions[3]  = LDB_ADDRESS;
		instructions[4]  = MUL;
		instructions[5]  = DIV;
		instructions[6]  = ADD;
		instructions[7]  = SUB;
		instructions[8]  = STX;
		instructions[9]  = STA;
		instructions[10] = STB;
		instructions[11] = JMP;
		instructions[12] = JZE;
		instructions[13] = JOV;
		instructions[14] = JNE;
		instructions[15] = NOP;
	}
	public Instruction getInstruction(int index){
		return instructions[index];
	}
	public Instruction[] getInstructionArray(){
		return instructions;
	}
	/**
	 * returns a List of all instructions in a simple form( Mnmonic + Addressing)
	 * @return List of Instructions as String
	 * */
	public List<String> getInstructionStrings(){
		List<String> list = new ArrayList<String>();
		for(Instruction ins : getInstructionList())
			list.add(ins.getMnemonic() + (ins.getAddressing().equals("") ? "" :", ") + ins.getAddressing());
		return list;
	}
	public List<Instruction> getInstructionList(){
		return Arrays.asList(getInstructionArray());
	}
	public void setInstruction(int index, Instruction instruction){
		this.instructions[index] = instruction;
	}
}
