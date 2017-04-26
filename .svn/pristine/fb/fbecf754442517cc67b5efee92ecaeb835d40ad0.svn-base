package de.ddkfm.application;


import java.net.MalformedURLException;

import de.ddkfm.models.Decoder;
import de.ddkfm.models.Processor;
import de.ddkfm.util.MicroIIUtils;
import de.ddkfm.util.ThemeUtils;
import de.ddkfm.util.transform.Instruction;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class fmMicrocode extends Stage {
	
	private Processor processor;
	private Decoder dec;
	public fmMicrocode(Processor processor) {
		this.processor = processor;
		this.dec = (Decoder)processor.getLogicValueByName("decoder");
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root);
		try {
			scene.getStylesheets().add(ThemeUtils.getCSSFile().toExternalForm());
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	 	this.setTitle("Microcode bearbeiten");
	    this.getIcons().add(new Image(ThemeUtils.getResourcePart("symbol")));
	    
	    VBox mainBox = new VBox();
	    
	    	HBox topBox = new HBox();
	    		 topBox.setPadding(new Insets(10,10,0,10));
	    	topBox.getChildren().addAll(
	    						new Label("ID                        "),
	    						new Label("Mnemonic          "),
	    						new Label("Adressing    "),
	    						new Label("A/B       "),
	    						new Label("ALU      "),
	    						new Label("Res      "),
	    						new Label("X      "),
	    						new Label("Flag       "),
	    						new Label("Prg   "),
	    						new Label("RAM")
	    			);
	    	topBox.setStyle("-fx-font-size:17pt;");
	    	
	    	HBox centerBox = new HBox();
	    		 centerBox.setSpacing(20);
	    		 centerBox.setPadding(new Insets(10));
	    		VBox IDBox = new VBox();
	    			TextField[] edsID = new TextField[16];
	    			for(int i = 0;i<16;i++)
	    				edsID[i] = new TextField(MicroIIUtils.getStringByBinaryNotation(MicroIIUtils.getBinaryString(i)));
	    		IDBox.getChildren().addAll(edsID);
	    		
	    		VBox MnemonicBox = new VBox();
		    		TextField[] edsMnemonic = new TextField[16];
		    		for (int i = 0;i<16;i++)
		    			edsMnemonic[i] = new TextField(dec.getMicrocode().getInstruction(i).getMnemonic());
		    		MnemonicBox.getChildren().addAll(edsMnemonic);
		    		
	    		VBox AddressingBox = new VBox();
	    			ComboBox<String>[] cbsAddressing = new ComboBox[16];
	    			for (int i = 0;i<16;i++){
	    				cbsAddressing[i] = new ComboBox<>();
	    				cbsAddressing[i].getItems().addAll("Const","Address","Statement","");
	    				switch(dec.getMicrocode().getInstruction(i).getAddressing()){
	    					case "const":cbsAddressing[i].getSelectionModel().select(0);break;
	    					case "addr":cbsAddressing[i].getSelectionModel().select(1);break;
	    					case "stat":cbsAddressing[i].getSelectionModel().select(2);break;
	    					case "":cbsAddressing[i].getSelectionModel().select(3);break;
	    				}
	    			}
	    		AddressingBox.getChildren().addAll(cbsAddressing);
	    		
	    		VBox microcodeBox = new VBox();
	    			 microcodeBox.setPadding(new Insets(3));
	    		     microcodeBox.setSpacing(8);
	    			CheckBox[][] microcode = new CheckBox[16][];
	    				for(int i = 0;i<16;i++){
	    					microcode[i] = new CheckBox[14];
	    					for(int j = 0;j<14;j++){
	    						microcode[i][j] = new CheckBox();
	    						microcode[i][j].setSelected(dec.getMicrocode().getInstruction(i).getMicrocodeSection(j));
	    					}
	    					HBox hBox = new HBox(
	    										new HBox(
	    												microcode[i][0],
	    												microcode[i][1]
	    												),
	    										new HBox(
	    												microcode[i][2],
	    											    microcode[i][3],
	    											    microcode[i][4]),
	    										new HBox(
	    												microcode[i][5],
	    												microcode[i][6]
	    												),
	    										microcode[i][7],
	    										new HBox(
	    												microcode[i][8],
	    												microcode[i][9],
	    												microcode[i][10]),
	    										microcode[i][11],
	    										new HBox(
	    												microcode[i][12],
	    												microcode[i][13]
	    												)
	    										);
	    						hBox.setSpacing(30);
	    					microcodeBox.getChildren().add(hBox);
	    				}
	    		
	    	centerBox.getChildren().addAll(IDBox,MnemonicBox,AddressingBox,microcodeBox);
	    	
	    	HBox bottomBox = new HBox();
	    		 bottomBox.setSpacing(50);
	    		 bottomBox.setPadding(new Insets(10));
	    		Button btSave = new Button("Speichern");
	    			   btSave.setOnAction(e->{
	    				   for(int i = 0; i<16;i++){
	    					   int ID = i;
	    					   String mnemonic = edsMnemonic[i].getText();
	    					   String addressing = cbsAddressing[i].getSelectionModel().getSelectedItem();
	    					   switch(addressing){
	    					   case "Address":addressing = "addr";break;
	    					   case "Const":addressing = "const";break;
	    					   case "Statement":addressing = "stat";break;
	    					   default:
	    						   addressing = "";
	    						   break;
	    					   }
	    					   String microcodeAsString = "";
	    					   for(int j = 0;j<14;j++)
	    						   if (microcode[i][j].isSelected())
	    							   microcodeAsString += '1';
	    						   else
	    							   microcodeAsString += '0';
	    					   dec.getMicrocode().setInstruction(i, new Instruction(ID, mnemonic, addressing, microcodeAsString, ""));
	    				   }
	    			   });
	    		Button btCancel = new Button("Abbrechen");
	    		       btCancel.setOnAction(e->this.close());
	    	bottomBox.getChildren().addAll(btSave, btCancel);
	    	
	    mainBox.getChildren().addAll(topBox,centerBox,bottomBox);
	    root.setCenter(mainBox);
	 	this.setScene(scene);
	 	this.show();
	}
}
