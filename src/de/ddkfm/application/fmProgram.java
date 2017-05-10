package de.ddkfm.application;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import de.ddkfm.models.Decoder;
import de.ddkfm.models.Memory;
import de.ddkfm.models.Processor;
import de.ddkfm.util.MicroIIUtils;
import de.ddkfm.util.ThemeUtils;
import de.ddkfm.util.transform.Instruction;
import de.ddkfm.util.transform.ProgramLine;
import de.ddkfm.util.transform.Transformator;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
/**
 * @author mschaedlich
 * The Stage for the Programeditor
 * */
public class fmProgram extends Stage {
	
	private Processor processor;
	
	public fmProgram(Processor processor) {
		this.processor = processor;
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root);
	 	try {
			scene.getStylesheets().add(ThemeUtils.getCSSFile().toExternalForm());
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
	 	this.setTitle("Programm bearbeiten");
	    this.getIcons().add(new Image(ThemeUtils.getResourcePart("symbol")));
	    
	    VBox mainBox = new VBox();
	    	 mainBox.setSpacing(10);
	    	 mainBox.setPadding(new Insets(10));
	    	HBox topBox = new HBox();
	    		 topBox.setSpacing(50);
	    		 topBox.setPadding(new Insets(10));
	    	ComboBox cbTemplates = new ComboBox<String>();
	    	cbTemplates.getItems().addAll("Addition","Subtraktion","Division","Multiplikation","Hochz\u00E4hlen","Runterz\u00E4hlen");
	    	List<String> templateList = Arrays.asList(new String[]{"Addition.xml","Subtraction.xml","Division.xml","Multiplication.xml","CountUp.xml","CountDown.xml"});
	    	Button btTemplateDecision = new Button("Vorlage laden");
	    	Button btCancel = new Button("Abbrechen");
	    	btTemplateDecision.setOnAction(e->{
	    		int index = cbTemplates.getSelectionModel().getSelectedIndex();
	    		try {
	    			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					DocumentBuilder builder = factory.newDocumentBuilder();
					URL xmlUrl = ClassLoader.getSystemResource("de/ddkfm/util/xml/templates/" + templateList.get(index));
					InputStream in = xmlUrl.openStream();
					Document doc = builder.parse(in);
					Transformator transe = new Transformator(doc);
					Memory mem = (Memory) processor.getLogicValueByName("memory");
					mem.setProgram(transe.getData().getProgram());
					mem.acceptProgram();
					btCancel.fire();
					
				} catch (ParserConfigurationException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (SAXException e1) {
					e1.printStackTrace();
				}
	    	});
	    	topBox.getChildren().addAll(cbTemplates,btTemplateDecision);
	    	
	    	HBox centerBox = new HBox();
	    		
	    		VBox box0to8 = new VBox();
	    			 box0to8.setPadding(new Insets(10));
	    			 box0to8.setSpacing(20);
	    		VBox box9to15 = new VBox();
	    			 box9to15.setPadding(new Insets(10));
	    			 box9to15.setSpacing(20);
	    		Decoder dec = (Decoder) processor.getLogicValueByName("decoder");
	    		Memory mem = (Memory) processor.getLogicValueByName("memory");
	    		ComboBox<String>[] cbs = new ComboBox[16];
	    		TextField[] txts = new TextField[16];
	    		for(int i = 0; i < 16 ; i++){
	    			VBox box = null;
	    			box = (i / 8 < 1) ? box0to8 : box9to15;
	    			
	    			ProgramLine cl = mem.getProgram().getProgramLine(i);
	    			Instruction ins = dec.getMicrocode().getInstruction(MicroIIUtils.getIntegerByBinaryNotation(cl.getInstruction()));
	    			cbs[i] = new ComboBox<String>();
	    			cbs[i].getItems().addAll(dec.getMicrocode().getInstructionStrings());
	    			cbs[i].getSelectionModel().select(ins.getId());
	    			txts[i] = new TextField();
	    			if (ins.getAddressing().equals("address") || ins.getAddressing().equals("statement"))
	    				txts[i].setText(Integer.toString(MicroIIUtils.getIntegerByBinaryNotation(cl.getAddress())));
	    			else
	    				txts[i].setText(Integer.toString(MicroIIUtils.getIntegerByBinaryNotation(cl.getData())));
	    			txts[i].setOnKeyReleased(e->{
	    				TextField sourceField = (TextField)e.getSource();
	    				if (Integer.parseInt(sourceField.getText()) > 15)
	    					sourceField.setText("0");
	    			});
	    			box.getChildren().add(new HBox(cbs[i],txts[i]));                                                                  
	    		}
	    		mem.acceptProgram();
	    	
	    	centerBox.getChildren().addAll(box0to8,box9to15);
	    	HBox bottomBox = new HBox();
	    		 bottomBox.setSpacing(60);
	    		 bottomBox.setPadding(new Insets(10));
	    	Button btDeleteProgram = new Button("Programm l\u00F6schen");
	    		   btDeleteProgram.setOnAction(e->{
	    			   for(int i = 0;i<16;i++){
	    				   cbs[i].getSelectionModel().select(0);
	    				   txts[i].setText("0");
	    			   }
	    		   });
	   	    	   Button btSave = new Button("Speichern");
	    		   btSave.setOnAction(e->{
	    			   for(int i = 0;i<16;i++){
	    				   int value = Integer.parseInt(txts[i].getText());
	    				   int insValue = cbs[i].getSelectionModel().getSelectedIndex();
	    				   String insType = dec.getMicrocode().getInstruction(insValue).getAddressing();
	    				   String address = "0000";
	    				   String data = "0000";
	    				   if (insType.equals("address") || insType.equals("statement"))
	    					   address = MicroIIUtils.getStringByBinaryNotation(MicroIIUtils.getBinaryString(value));
	    				   else
	    					   if (insType.equals("const")){
	    						   data = MicroIIUtils.getStringByBinaryNotation(MicroIIUtils.getBinaryString(value));
	    					   }
	    				   mem.getProgram().setProgramLine(i, new ProgramLine(MicroIIUtils.getStringByBinaryNotation(MicroIIUtils.getBinaryString(insValue)), address, data, ""));
	    			   }
	    			   mem.acceptProgram();
	    			   this.close();
	    		   });
	    		   btCancel.setOnAction(e->this.close());
	    	bottomBox.getChildren().addAll(btDeleteProgram,btSave,btCancel);
	    
	    mainBox.getChildren().addAll(topBox,centerBox,bottomBox);
	    root.setCenter(mainBox);
	 	this.setScene(scene);
	 	this.show();
	}
}
