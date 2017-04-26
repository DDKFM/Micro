package de.ddkfm.util.transform;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.ddkfm.util.MicroIIUtils;

public class Transformator {
	private static Logger logger = LogManager.getLogger("Transformator");
	private Micro2Data data;

	public Transformator(){}
	public Transformator(Micro2Data data) {
		super();
		this.data = data;
	}
	public Transformator(Document data){
		this.data = new Micro2Data("");
		setXMLData(data);
	}
	public void setData(Micro2Data data) {
		this.data = data;
	}
	
	public Micro2Data getData() {
		return data;
	}
	public void setXMLData(Document xmlDoc) {
		Element rootElement = xmlDoc.getDocumentElement();
		NodeList author = rootElement.getElementsByTagName("Author");
		if(author.item(0) != null)
			data.setAuthor(author.item(0).getTextContent());
		NodeList dateCreated = rootElement.getElementsByTagName("DateCreated");
		if(dateCreated.item(0) != null){
			Date date;
			try {
				date = new SimpleDateFormat("dd.MM.yyyy hh:MM:ss").parse(dateCreated.item(0).getTextContent());
			} catch (Exception e){
				date = new Date();
			}
			data.setDateCreated(date);
		}
		NodeList description = rootElement.getElementsByTagName("Description");
		if(description.item(0) != null){
			data.setDescription(description.item(0).getTextContent());
		}
		NodeList programNode = xmlDoc.getElementsByTagName("Program");
		NodeList programNodelist = programNode.item(0).getChildNodes();
		for (int i = 0 ; i < programNodelist.getLength() ; i++){
			Node currentNode = programNodelist.item(i);
			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
				String instruction = currentNode.getAttributes().getNamedItem("statement").getNodeValue();
				String address = currentNode.getAttributes().getNamedItem("address").getNodeValue();
				String data = currentNode.getAttributes().getNamedItem("data").getNodeValue();
				String comment = currentNode.getAttributes().getNamedItem("comment").getNodeValue();
				ProgramLine pl = new ProgramLine(instruction, address, data, comment);
				this.data.getProgram().setProgramLine(Integer.parseInt(currentNode.getAttributes().getNamedItem("index").getNodeValue()), pl);
			}
		}
		NodeList microcodeNode = xmlDoc.getElementsByTagName("Microcode");
		NodeList microcodeList= microcodeNode.item(0).getChildNodes();
		for (int i = 0 ; i < microcodeList.getLength() ; i++){
			Node currentNode = microcodeList.item(i);
			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
				String mnemonic = currentNode.getAttributes().getNamedItem("mnemonic").getNodeValue();
				String addressing = currentNode.getAttributes().getNamedItem("addressing").getNodeValue();
				String microcode = currentNode.getAttributes().getNamedItem("microcode").getNodeValue();
				String comment = currentNode.getAttributes().getNamedItem("comment").getNodeValue();
				Instruction instruction = new Instruction(i, mnemonic, addressing, microcode, comment);
				this.data.getMicrocode().setInstruction(Integer.parseInt(currentNode.getAttributes().getNamedItem("index").getNodeValue()), instruction);
			}
		}
	}
	public Document getXMLData() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document xmlDoc = builder.newDocument();
			
			//Root-Element
			Element rootElement = xmlDoc.createElement("MicroIIData");
			xmlDoc.appendChild(rootElement);
			//Allgemeine Informationen
			Element author = xmlDoc.createElement("Author");
			author.setTextContent(data.getAuthor());
			rootElement.appendChild(author);
			
			Element dateCreated = xmlDoc.createElement("DateCreated");
			dateCreated.setTextContent(new SimpleDateFormat("dd.MM.yyyy hh:MM:ss").format(data.getDateCreated()));
			rootElement.appendChild(dateCreated);
		
			Element description = xmlDoc.createElement("Description");
			description.setTextContent(data.getDescription());
			rootElement.appendChild(description);
			
			//Program
			Element programNode = xmlDoc.createElement("Program");
			List<Element> programNodeList = new ArrayList<Element>();
			for (int i = 0 ; i < 16 ; i++){
				ProgramLine pl = data.getProgram().getProgramLine(i);
				
				Element currentNode = xmlDoc.createElement("Programrow");
				
				Node indexNode = xmlDoc.createAttribute("index");
				indexNode.setNodeValue(Integer.toString(i));
				
				Node statementNode = xmlDoc.createAttribute("statement");
				statementNode.setNodeValue(MicroIIUtils.getStringByBinaryNotation(pl.getInstruction()));
				
				Node addressNode = xmlDoc.createAttribute("address");
				addressNode.setNodeValue(MicroIIUtils.getStringByBinaryNotation(pl.getAddress()));
				
				Node dataNode = xmlDoc.createAttribute("data");
				dataNode.setNodeValue(MicroIIUtils.getStringByBinaryNotation(pl.getData()));
				
				Node commentNode = xmlDoc.createAttribute("comment");
				commentNode.setNodeValue(pl.getComment());
				
				currentNode.getAttributes().setNamedItem(indexNode);
				currentNode.getAttributes().setNamedItem(statementNode);
				currentNode.getAttributes().setNamedItem(addressNode);
				currentNode.getAttributes().setNamedItem(dataNode);
				currentNode.getAttributes().setNamedItem(commentNode);
				
				programNodeList.add(currentNode);
				programNode.appendChild(programNodeList.get(i));
			}
			
			rootElement.appendChild(programNode);
			
			//Microcode
			Element microcodeNode = xmlDoc.createElement("Microcode");
			List<Element> microcodeNodeList = new ArrayList<Element>();
			for (int i = 0 ; i < 16 ; i++){
				Instruction ins = data.getMicrocode().getInstruction(i);
				
				Element currentNode = xmlDoc.createElement("Instruction");
				
				Node indexNode = xmlDoc.createAttribute("index");
				indexNode.setNodeValue(Integer.toString(i));
				
				Node statementNode = xmlDoc.createAttribute("mnemonic");
				statementNode.setNodeValue(ins.getMnemonic());
				
				Node addressNode = xmlDoc.createAttribute("addressing");
				addressNode.setNodeValue(ins.getAddressing());
				
				Node dataNode = xmlDoc.createAttribute("microcode");
				dataNode.setNodeValue(MicroIIUtils.getStringByBinaryNotation(ins.getMicrocode()));
				
				Node commentNode = xmlDoc.createAttribute("comment");
				commentNode.setNodeValue(ins.getComment());
				
				currentNode.getAttributes().setNamedItem(indexNode);
				currentNode.getAttributes().setNamedItem(statementNode);
				currentNode.getAttributes().setNamedItem(addressNode);
				currentNode.getAttributes().setNamedItem(dataNode);
				currentNode.getAttributes().setNamedItem(commentNode);
				
				microcodeNodeList.add(currentNode);
				microcodeNode.appendChild(microcodeNodeList.get(i));
			}
			
			rootElement.appendChild(microcodeNode);
			return xmlDoc;
		} catch (ParserConfigurationException ex) {
			logger.error("Fehler beim Erstellen des XML-Dokumentes: ",ex);
		}
		return null;
		
	}
	
}
