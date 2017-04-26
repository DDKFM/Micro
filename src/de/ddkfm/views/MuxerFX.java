package de.ddkfm.views;

import de.ddkfm.models.LogicValue;
import de.ddkfm.util.ThemeUtils;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MuxerFX extends LogicNode {
	private ImageView imgView = new ImageView();
	private Label lblMuxer = new Label("");
	public MuxerFX() {}
	@Override
	public void setLogic(LogicValue logic) {
		super.setLogic(logic);
		double baseX = this.points.get(0).getX();
		double baseY = this.points.get(0).getY();
		imgView.setLayoutX(baseX);
		imgView.setLayoutY(baseY);
		imgView.setImage(new Image(ThemeUtils.getResourcePart("multiplexer")));
		this.getChildren().add(imgView);
		for (int i = 0; i < 5; i++) {
			LogicButton button = new LogicButton(this.logic, i);
			button.setLayoutX(baseX + 12);
			button.setLayoutY(baseY + 22 + 15 * i);
			this.getChildren().add(button);
		}
		lblMuxer.setLayoutX(baseX + 4);
		lblMuxer.setLayoutY(baseY + 50);
		this.getChildren().add(lblMuxer);
	}
	@Override
	protected void change(int index, boolean value) {
		lblMuxer.setText(logic.getValue(3) ? "0" : (logic.getValue(4) ? "1" : ""));
	}
	
}
