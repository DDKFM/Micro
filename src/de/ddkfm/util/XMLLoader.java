package de.ddkfm.util;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLLoader {
	private static Logger logger = LogManager.getLogger("XMLLoader");
	private List<LogicValueRepresentation> elementData = new ArrayList<LogicValueRepresentation>();
	public XMLLoader(URL xmlURL) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(xmlURL.toExternalForm());
			
			//Valididitätsprüfung
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			URL schemaFile = ClassLoader.getSystemResource("de/ddkfm/util/xml/AttributesSchema.xsd");
			Schema schema = schemaFactory.newSchema(schemaFile);
			
			Validator validator = schema.newValidator();
			try {
				validator.validate(new DOMSource(doc));
			} catch (Exception e) {
				logger.error("Fehler beim Validieren der XML-Datei. Sie entspricht nicht den Anforderungen: " + e.getMessage());
				throw e;
			}
			
			Element root = doc.getDocumentElement();
			NodeList logicValues = root.getElementsByTagName("LogicValue");
			for(int i = 0 ; i < logicValues.getLength() ; i++){
				Node childNode = logicValues.item(i);
				LogicValueRepresentation repres = new LogicValueRepresentation(childNode);
				elementData.add(repres);
			}
		} catch (Exception e) {
			logger.error("Fehler beim Lesen der XML-Datei: ",e);
		}
	}
	public List<LogicValueRepresentation> getXMLElements(){
		return elementData;
	}
}
