package de.ddkfm.util.transform;

import java.util.Date;
/**
 * the MicroIIData represents the Program, the Microcode and meta information like author or description
 * Is could be transformated into XML with the Transformator class
 * @see Transformator
 * */
public class Micro2Data {
	private String author;
	private Date dateCreated;
	private String description;
	private Program program;
	private Microcode microcode;
	public Micro2Data(String author, Date dateCreated, String description, Program program, Microcode microcode){
		this.author = author;
		this.dateCreated = dateCreated;
		this.description = description;
		this.program = program;
		this.microcode = microcode;
	}
	public Micro2Data(String author, Date dateCreated, String description){
		this(author, dateCreated, description, new Program(), new Microcode());
	}
	public Micro2Data(String author, String description){
		this(author, new Date(), description);
	}
	public Micro2Data(String author){
		this(author, "Keine Beschreibung verf�gbar");
	}
	public Micro2Data(){
		this("Anonymous");
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Program getProgram() {
		return program;
	}
	public void setProgram(Program program) {
		this.program = program;
	}
	public Microcode getMicrocode() {
		return microcode;
	}
	public void setMicrocode(Microcode microcode) {
		this.microcode = microcode;
	}
	
	
}
