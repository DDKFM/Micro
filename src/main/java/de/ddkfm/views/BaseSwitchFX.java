package de.ddkfm.views;

import de.ddkfm.models.LogicValue;
import de.ddkfm.util.ThemeUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BaseSwitchFX extends LogicNode {
	private ImageView imgView = new ImageView();
	public BaseSwitchFX() {}
	@Override
	public void setLogic(LogicValue logic) {
		super.setLogic(logic);
		double baseX = this.points.get(0).getX();
		double baseY = this.points.get(0).getY();
		imgView.setLayoutX(baseX);
		imgView.setLayoutY(baseY);
		imgView.setImage(new Image(ThemeUtils.getResourcePart("baseswitch_2")));
		this.getChildren().add(imgView);
		imgView.setOnMouseClicked(e->{
			double x = e.getX();
			double y = e.getY();
			if((y >= 5 && y <= 19) && (x >= 5 && x <= 25)){
				logic.setValue(0,true);
				logic.setValue(1, false);
			}
			else if((y >= 30 && y <= 44) && (x >= 5 && x <= 25)){
				logic.setValue(0,false);
				logic.setValue(1, false);
			}
			else if((y >= 56 && y <= 70) && (x >= 5 && x <= 25)){
				logic.setValue(0,false);
				logic.setValue(1, true);
			}
		});
	}
	@Override
	protected void change(int index, boolean value) {
		boolean value0 = logic.getValue(0);
		boolean value1 = logic.getValue(1);
		if(value0 && !value1)
			imgView.setImage(new Image(ThemeUtils.getResourcePart("baseswitch_10")));
		else
			if(!value0 && value1)
				imgView.setImage(new Image(ThemeUtils.getResourcePart("baseswitch_16")));
			else
				imgView.setImage(new Image(ThemeUtils.getResourcePart("baseswitch_2")));
	}
	
}
