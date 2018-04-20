package de.ddkfm.views;

import de.ddkfm.models.LogicValue;

public class MemoryFX extends LogicNode {
	public MemoryFX() {}
	@Override
	public void setLogic(LogicValue logic) {
		super.setLogic(logic);
		double baseX = this.points.get(0).getX();
		double baseY = this.points.get(0).getY();
		for(int y = 0; y < 16; y++){
			int h = 0;
			for(int x = 0 ; x < 12 ; x++){
				LogicButton button = new LogicButton(this.logic, 12 * y + x);
				if(x % 4 == 0)
					h += 5;
				button.setLayoutX(baseX + 15 * x + h);
				button.setLayoutY(baseY + 15 * y);
				this.getChildren().add(button);
			}
		}
	}
}
