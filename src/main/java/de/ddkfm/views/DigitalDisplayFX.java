package de.ddkfm.views;

import java.util.HashMap;
import java.util.Map;

import de.ddkfm.models.LogicValue;
import de.ddkfm.util.ThemeUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class DigitalDisplayFX extends LogicNode {
	private Map<String, ImageView> segments = new HashMap<String, ImageView>();
	public DigitalDisplayFX() {}
	@Override
	public void setLogic(LogicValue logic) {
		super.setLogic(logic);
		double baseX = this.points.get(0).getX();
		double baseY = this.points.get(0).getY();
		for(int i = 0; i < 8 ; i++){
			ImageView imgView = new ImageView(new Image(ThemeUtils.getResourcePart("7segment_" + i)));
			imgView.setFitHeight(65);
			imgView.setFitWidth(35);
			imgView.setLayoutX(baseX);
			imgView.setLayoutY(baseY);
			imgView.setVisible(i == 0);
			segments.put("segment_" + i,imgView);
			getChildren().add(imgView);
		}
	}
	@Override
	protected void change(int index, boolean value) {
		segments.get("segment_" + (index + 1)).setVisible(value);
	}
	
}
