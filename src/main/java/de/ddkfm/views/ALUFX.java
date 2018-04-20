package de.ddkfm.views;

import de.ddkfm.models.LogicValue;
import de.ddkfm.util.ThemeUtils;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;

import java.io.InputStream;

public class ALUFX extends LogicNode {
	private ImageView imgView = new ImageView();
	public ALUFX() {
		setOnMouseClicked(e -> {
			if(e.getClickCount() == 5) {
				InputStream is = ThemeUtils.getResourcePart("aluf");
				Alert al = new Alert(Alert.AlertType.INFORMATION);

				DialogPane pane = new DialogPane();
				Label lbl = new Label("Nicht zu verwechseln mit ALUfolie");

				Image image = new Image(is);
				ImageView imgView = new ImageView();
				imgView.setImage(image);
				pane.setContent(new VBox(lbl,imgView));

				pane.getButtonTypes().add(ButtonType.CLOSE);

				al.setDialogPane(pane);
				al.initModality(Modality.WINDOW_MODAL);
				al.show();
			}
		});
	}
	@Override
	public void setLogic(LogicValue logic) {
		super.setLogic(logic);
		double baseX = this.points.get(0).getX();
		double baseY = this.points.get(0).getY();
		imgView.setLayoutX(baseX);
		imgView.setLayoutY(baseY);
		imgView.setImage(new Image(ThemeUtils.getResourcePart("alu")));
		this.getChildren().add(imgView);
		for (int i = 0; i < 6; i++) {
			LogicButton button = new LogicButton(this.logic, i);
			button.setLayoutX(baseX + 12 + 15 * i);
			button.setLayoutY(baseY + 23);
			this.getChildren().add(button);
		}
		Label lblMuxer = new Label("");
		lblMuxer.setLayoutX(baseX + 4);
		lblMuxer.setLayoutY(baseY + 50);
		this.getChildren().add(lblMuxer);
	}
	@Override
	protected void change(int index, boolean value) {
		
	}
	
}
