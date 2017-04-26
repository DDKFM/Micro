package de.ddkfm.util.transform;

public class Instruction {
	private int id;
	private String mnemonic;
	private String addressing;
	private boolean[] microcode = new boolean[14];
	private String comment;
	public Instruction(int id){
		this.id = id;
	}
	public Instruction(int id, String mnemonic, String addressing, boolean[] microcode, String comment) {
		super();
		this.id = id;
		this.mnemonic = mnemonic;
		this.addressing = addressing;
		this.microcode = microcode;
		this.comment = comment;
	}
	public Instruction(int id, String mnemonic, String addressing, String microcode, String comment) {
		super();
		this.id = id;
		this.mnemonic = mnemonic;
		this.addressing = addressing;
		for(int i = 0 ; i < 14 ; i++)
			this.microcode[i] = microcode.charAt(i) == '1';
		this.comment = comment;
	}
	public String getMnemonic() {
		return mnemonic;
	}
	public void setMnemonic(String mnemonic) {
		this.mnemonic = mnemonic;
	}
	public String getAddressing() {
		return addressing;
	}
	public void setAddressing(String addressing) {
		this.addressing = addressing;
	}
	public boolean[] getMicrocode() {
		return microcode;
	}
	public void setMicrocode(boolean[] microcode) {
		this.microcode = microcode;
	}
	public void setMicrocodeSection(int index, boolean value){
		this.microcode[index] = value;
	}
	public boolean getMicrocodeSection(int index){
		return microcode[index];
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public int getId() {
		return id;
	}
	
}
