package de.ddkfm.views;

import de.ddkfm.models.LogicValue;
import de.ddkfm.util.ThemeUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class RegisterFX extends LogicNode {
	private ImageView imgView = new ImageView();
	public RegisterFX() {}
	@Override
	public void setLogic(LogicValue logic) {
		super.setLogic(logic);
		double baseX = this.points.get(0).getX();
		double baseY = this.points.get(0).getY();
		imgView.setLayoutX(baseX);
		imgView.setLayoutY(baseY);
		imgView.setImage(new Image(ThemeUtils.getResourcePart("register")));
		this.getChildren().add(imgView);
		for (int i = 0; i < 4; i++) {
			LogicButton button = new LogicButton(this.logic, i);
			button.setLayoutX(baseX + 10 + 15 * i);
			button.setLayoutY(baseY + 2);
			this.getChildren().add(button);
		}
	}
	@Override
	protected void change(int index, boolean value) {
	}
	
}
