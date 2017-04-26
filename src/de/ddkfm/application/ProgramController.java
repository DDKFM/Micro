package de.ddkfm.application;

import java.net.URL;
import java.util.ResourceBundle;

import de.ddkfm.models.Decoder;
import de.ddkfm.models.Memory;
import de.ddkfm.util.transform.Instruction;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class ProgramController implements Initializable{
	@FXML
	private GridPane gridPane;
	
	private Decoder decoder;
	private Memory memory;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
	}
	public void load(Decoder decoder, Memory memory){
		this.decoder = decoder;
		this.memory = memory;
		for(Node node : gridPane.getChildren()){
			if(node instanceof ComboBox){
				ComboBox<String> cb = (ComboBox) node;
				cb.getItems().clear();
				for(Instruction ins : decoder.getMicrocode().getInstructionList()){
					cb.getItems().add(ins.getMnemonic() + (ins.getAddressing().equals("") ? "": ", ") + ins.getAddressing());
				}
				cb.getSelectionModel().select(0);
			}else
				if(node instanceof TextField){
					TextField txt = (TextField) node;
					txt.setText("0");
					txt.setOnKeyReleased(e->{
						String input = txt.getText();
						int inputNumber = 0;
						try {
							inputNumber = Integer.parseInt(input);
						} catch (NumberFormatException e1) {
							inputNumber = 0;
						}
						if(inputNumber > 15)
							inputNumber %= 16;
						if(! Integer.toString(inputNumber).equals(input))
							txt.setText(Integer.toString(inputNumber));
					});
				}
		}
	}

}
