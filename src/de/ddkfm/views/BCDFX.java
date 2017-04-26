package de.ddkfm.views;

import de.ddkfm.models.LogicValue;
import de.ddkfm.util.ThemeUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BCDFX extends LogicNode {
	private ImageView imgView = new ImageView();
	public BCDFX() {}
	@Override
	public void setLogic(LogicValue logic) {
		super.setLogic(logic);
		double baseX = this.points.get(0).getX();
		double baseY = this.points.get(0).getY();
		imgView.setLayoutX(baseX);
		imgView.setLayoutY(baseY);
		imgView.setImage(new Image(ThemeUtils.getResourcePart("bcd")));
		this.getChildren().add(imgView);
	}
	
}
