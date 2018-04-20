package de.ddkfm.application;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import de.ddkfm.models.Memory;
import de.ddkfm.models.Processor;
import de.ddkfm.util.MicroIIUtils;
import de.ddkfm.util.transform.Instruction;
import de.ddkfm.util.transform.Micro2Data;
import de.ddkfm.util.transform.Microcode;
import de.ddkfm.util.transform.Program;
import de.ddkfm.util.transform.ProgramLine;
import de.ddkfm.util.transform.Transformator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
/***
 * the Controller for the Menuitem "LoadFromServer"
 */
public class LoadFromServerController implements Initializable{

	@FXML
	private Label lblAuthor;
	
	@FXML
	private Label lblName;
	
    @FXML
    private Button btLoadProgram;

    @FXML
    private Button btLoadData;

    @FXML
    private WebView taXML;

    @FXML
    private TreeView<String> tvData;

    @FXML
    private TextField edURL;

    private Processor processor;
    private URL serverURL;
    private List<Map<String, String>> dataMetaData = new ArrayList<Map<String, String>>();
	@Override
	/**
	 * Init-Method for the Window
	 * */
	public void initialize(URL location, ResourceBundle resources) {
		btLoadData.setDisable(true);
		edURL.textProperty().addListener((obs, oldV, newV) -> {
			String url = newV;
			String regex = "^((http:\\/\\/)|(https:\\/\\/))?(www.)?(([a-zA-Z0-9-]+)([\\.a-zA-Z0-9-]*)|(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}))?(:\\d+)?([\\/a-zA-Z0-9\\.-]*)$";
			String color = "";
			if(url.matches(regex)) {
				color = "green";
				btLoadData.setDisable(false);
			} else if(url.equals("")) {
				color = "transparent";
				btLoadData.setDisable(true);
			} else {
				color = "red";
				btLoadData.setDisable(true);
			}
			edURL.setStyle("-fx-control-inner-background: " + color);
		});
		btLoadData.setOnAction(e -> {
			try {
				String urlString = edURL.getText();
				if(urlString.endsWith("/"))
					urlString = urlString.substring(0, urlString.length() - 1);
				if(!( urlString.startsWith("http://") || urlString.startsWith("https://") ))
					urlString = "http://" + urlString;
				edURL.setText(urlString);
				serverURL = new URL(urlString);
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				URL dataURL = new URL(serverURL.toString() + "?id=all");
				Document doc = builder.parse(dataURL.openStream());
				Element documentElement = doc.getDocumentElement();
				tvData.setShowRoot(false);
				tvData.setRoot(new TreeItem<String>("ROOT"));
				for(int i = 0 ; i < documentElement.getChildNodes().getLength() ; i++){
					Node currentNode = documentElement.getChildNodes().item(i);
					if(currentNode.getNodeType() == Node.ELEMENT_NODE){
						String uid = currentNode.getAttributes().getNamedItem("username").getNodeValue();
						String id = currentNode.getAttributes().getNamedItem("dataID").getNodeValue();
						Map<String, String> map = new HashMap<String, String>();
						map.put("id", id);
						map.put("author", currentNode.getAttributes().getNamedItem("username").getNodeValue());
						map.put("name", currentNode.getAttributes().getNamedItem("name").getNodeValue());
						dataMetaData.add(map);
						List<TreeItem<String>> childTrees = tvData.getRoot().getChildren();
						childTrees.add(new TreeItem<String>(map.get("name")));
					}
				}
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			} catch (SAXException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ParserConfigurationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		tvData.getSelectionModel().selectedIndexProperty().addListener((obs,oldV,newV)->{
			try {
				TreeItem<String> currentItem = tvData.getTreeItem(newV.intValue());
				Map<String ,String> map = dataMetaData.get(newV.intValue());
				
				URL dataURL = new URL(serverURL.toString() + "?id=" + map.get("id"));
				BufferedReader reader = new BufferedReader(new InputStreamReader(dataURL.openStream()));
				String row = reader.readLine();
				String xmlString = "";
				while(row != null){
					xmlString += row;
					row = reader.readLine();
				}
				xmlString = formatXMLToPrettyXML(xmlString);
				WebEngine engine = taXML.getEngine();
				engine.loadContent(xmlString);
				lblAuthor.setText("Autor: " + map.get("author"));
				lblName.setText("Name: " + map.get("name"));
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
		});
		btLoadProgram.setOnAction(e->{
			String id = dataMetaData.get(tvData.getSelectionModel().getSelectedIndex()).get("id");
			try {
				URL dataURL = new URL(serverURL.toString() + "?id=" + id);
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document doc = builder.parse(dataURL.openStream());
				Transformator transe = new Transformator(doc);
				Memory mem = (Memory)processor.getLogicValueByName("memory");
				mem.setProgram(transe.getData().getProgram());
				mem.acceptProgram();
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ParserConfigurationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SAXException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
		});
	}
	/**
	 * format the given String to a pretty Form from XML
	 * @param xmlString given String(XML formatted)
	 * */
	private String formatXMLToPrettyXML(String xmlString) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new ByteArrayInputStream(xmlString.getBytes()));
			Micro2Data micro2Data = new Transformator(doc).getData();
			String htmlString = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("Micro2Data.html"), Charset.forName("UTF-8")));
			String row = reader.readLine();
			while( row != null) {
				htmlString += row;
				row = reader.readLine();
			}
			htmlString = htmlString.replace("##Author##", micro2Data.getAuthor());
			htmlString = htmlString.replace("##DateCreated##", new SimpleDateFormat("dd.MM.yyyy").format(micro2Data.getDateCreated()));
			htmlString = htmlString.replace("##Description##", micro2Data.getDescription());
			
			Program program = micro2Data.getProgram();
			for(int programIndex = 0 ; programIndex < micro2Data.getProgram().getProgramLines().size() ; programIndex++) {
				ProgramLine programLine = micro2Data.getProgram().getProgramLine(programIndex);
				htmlString = htmlString.replace("##IndexProgram_" + programIndex + "##", Integer.toString(programIndex));
				htmlString = htmlString.replace("##Address_" + programIndex + "##", MicroIIUtils.getStringByBinaryNotation(programLine.getAddress()));
				htmlString = htmlString.replace("##Statement_" + programIndex + "##", MicroIIUtils.getStringByBinaryNotation(programLine.getInstruction()));
				htmlString = htmlString.replace("##Data_" + programIndex + "##", MicroIIUtils.getStringByBinaryNotation(programLine.getData()));
				htmlString = htmlString.replace("##CommentProgram_" + programIndex + "##", programLine.getComment());
			}
			
			Microcode microcode = micro2Data.getMicrocode();
			for(int microcodeIndex = 0 ; microcodeIndex < micro2Data.getProgram().getProgramLines().size() ; microcodeIndex++) {
				Instruction microcodeLine = micro2Data.getMicrocode().getInstruction(microcodeIndex);
				htmlString = htmlString.replace("##IndexMicrocode_" + microcodeIndex + "##", Integer.toString(microcodeIndex));
				htmlString = htmlString.replace("##Addressing_" + microcodeIndex + "##", microcodeLine.getAddressing());
				htmlString = htmlString.replace("##Mnemonic_" + microcodeIndex + "##", microcodeLine.getMnemonic());
				htmlString = htmlString.replace("##Microcode_" + microcodeIndex + "##", MicroIIUtils.getStringByBinaryNotation(microcodeLine.getMicrocode()));
				htmlString = htmlString.replace("##CommentMicrocode_" + microcodeIndex + "##", microcodeLine.getComment());
			}
			
			return htmlString;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		return "";
	}
	/**
	 * load the processor-object into the window
	 * @param processor the Microprocessor
	 * */
	public void load(Processor processor){
		this.processor = processor;
	}

}
