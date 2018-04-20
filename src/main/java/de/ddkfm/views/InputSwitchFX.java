package de.ddkfm.views;

import de.ddkfm.models.LogicValue;
import de.ddkfm.util.ThemeUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class InputSwitchFX extends LogicNode {
	private ImageView imgView = new ImageView();
	public InputSwitchFX() {}
	@Override
	public void setLogic(LogicValue logic) {
		super.setLogic(logic);
		double baseX = this.points.get(0).getX();
		double baseY = this.points.get(0).getY();
		imgView.setLayoutX(baseX);
		imgView.setLayoutY(baseY);
		imgView.setImage(new Image(ThemeUtils.getResourcePart("switch_off")));
		this.getChildren().add(imgView);
		for (int i = 0; i < 2; i++) {
			LogicButton button = new LogicButton(this.logic, i);
			button.setLayoutX(baseX  + 60 * i);
			button.setLayoutY(baseY + 3);
			this.getChildren().add(button);
		}
	}
	@Override
	protected void change(int index, boolean value) {
		boolean value1 = logic.getValue(0);
		boolean value2 = logic.getValue(1);
		if (!value1 && !value2)
			imgView.setImage(new Image(ThemeUtils.getResourcePart("switch_off")));
		else
			if(value1 && !value2)
				imgView.setImage(new Image(ThemeUtils.getResourcePart("switch_right")));
			else
				if(!value1 && value2)
					imgView.setImage(new Image(ThemeUtils.getResourcePart("switch_left")));
				else
					if(value1 && value2)
						this.logic.setValue((index + 1) % 2, false);
	}
	
}
