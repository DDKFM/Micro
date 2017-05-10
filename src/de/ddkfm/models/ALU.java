package de.ddkfm.models;

import de.ddkfm.util.MicroIIUtils;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
/**
 * The Aritmetic Logic Unit which calculate the Result by Register A and B
 * */
public class ALU extends LogicValue {
	public ALU(String name) {
		super(6, name);
	}

	/**
	 * Only incoming signals from the decoder will be recognized
	 * */
	@Override
	public void change(LogicValue sender, int index, boolean value) {
		if(sender.getName().contains("decoder")){
			boolean[] inputValues = new boolean[4];
			inputValues[3] = this.getConnection("decoder2ALU_" + 0).getReference().getValue(0);
			inputValues[2] = this.getConnection("decoder2ALU_" + 1).getReference().getValue(0);
			inputValues[1] = this.getConnection("decoder2ALU_" + 2).getReference().getValue(0);
			inputValues[0] = false;
			int input = MicroIIUtils.getIntegerByBinaryNotation(inputValues);
			for(int i = 0 ; i < 6 ; i++){
				if(input > 0)
					setValue(i, i == (input - 1));
				else
					setValue(i, false);
			}
		}
	}
	@Override
	protected void sendValueToOutput(int index, boolean value) {
		
	}
	/**
	 * calculate will trigger the calculation, that means the alu fetch the data from register A and B an write the Result to the Output
	 * */
	public void calculate(){
		boolean[] inputValuesA = new boolean[4];
		boolean[] inputValuesB = new boolean[4];
		for(int i = 0 ; i < 4 ; i++){
			inputValuesA[i] = getConnection("registerA2ALU_" + i).getReference().getValue(0);
			inputValuesB[i] = getConnection("registerB2ALU_" + i).getReference().getValue(0);
		}
		int a = MicroIIUtils.getIntegerByBinaryNotation(inputValuesA);
		int b = MicroIIUtils.getIntegerByBinaryNotation(inputValuesB);
		int c = 0;
		if(getValue(0))
			c = a + b;
			else 
				if(getValue(1))
					c = a - b;
				else 
					if(getValue(2))
						c = a * b;	
					else 
						if(getValue(3))
							c = a / b;
						else 
							if(getValue(4))
								c = a;
							else
								if(getValue(5))
									c = b;
								else
									c = -255;//No Calculation

		System.out.println(a + " " + b + " " + c);
		if(c == 91) {
			//Easter-egg: Hier werden grafische Klassen in der Logik verwendet --> nur für diese Anwendung hier drin
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			DialogPane pane = new DialogPane();

			pane.setHeaderText("7x13");
			Label lblQuote = new Label("So ich drucke euch jetzt mal die Seite aus der Präsentation aus. \n" +
					"SCHEIßE, das war die ganze Präsentation");
			lblQuote.setFont(Font.font("Georgia", FontWeight.BOLD, 20));

			Label lblAuthor = new Label("  \u00AD Ulrich Reinhold");
			lblAuthor.setFont(Font.font("Georgia", FontPosture.ITALIC, 20));

			pane.setContent(new VBox(lblQuote, lblAuthor));
			pane.getButtonTypes().add(ButtonType.CANCEL);

			alert.setDialogPane(pane);
			alert.show();
		}
		if (c != -255) {
			getConnection("ALU2Flags_0").getReference().change(this, 0, c == 0);
			getConnection("ALU2Flags_1").getReference().change(this, 0, c > 15);
			getConnection("ALU2Flags_2").getReference().change(this, 0, c < 0);
		}
		if(c > 15)
			c %= 16;
		boolean[] outputValues;
		if(c < 0 && c != -255)
			outputValues = MicroIIUtils.twosComplement(c);
		else
			outputValues = MicroIIUtils.getBinaryString(c != -255 ? c : 0);
		for(int i = 0; i < 4 ; i++)
			getConnection("ALU2saveswitch_" + i).getReference().change(this, 0, outputValues[i]);
		
	}
	/**
	 * only one logicValue can be displayed at once. the other values will automatically set to false except for the given index
	 */
	@Override
	public void changeByExtern(Object sender, int index, boolean value) {
		if(index <= 6){
			for(int i = 0 ; i < 6 ; i++)
				setValue(i, value ? index == i : false);
		}
	}
}
