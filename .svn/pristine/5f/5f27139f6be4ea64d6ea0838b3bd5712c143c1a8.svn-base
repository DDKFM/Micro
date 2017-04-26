package de.ddkfm.views;

import de.ddkfm.models.LogicValue;

public class RAMFX extends LogicNode {
	public RAMFX() {}
	@Override
	public void setLogic(LogicValue logic) {
		super.setLogic(logic);
		double baseX = this.points.get(0).getX();
		double baseY = this.points.get(0).getY();
		for(int y = 0; y < 16; y++){
			for(int x = 0 ; x < 4 ; x++){
				LogicButton button = new LogicButton(this.logic, 4 * y + x);
				button.setLayoutX(baseX + 15 * x);
				button.setLayoutY(baseY + 15 * y /*+ ((y != 0) ? 25 : 0)*/);
				this.getChildren().add(button);
			}
		}
	}
}
