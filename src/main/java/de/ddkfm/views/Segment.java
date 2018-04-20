package de.ddkfm.views;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Segment extends Polygon {
	public Segment(double startX, double startY, double width, double height) {
		getPoints().addAll(new Double[]{startX + width / 2,startY});
		getPoints().addAll(new Double[]{startX,startY + height * 0.1});
		getPoints().addAll(new Double[]{startX,startY + height * 0.9});
		getPoints().addAll(new Double[]{startX + width / 2,startY + height});
		getPoints().addAll(new Double[]{startX + width, startY + 0.1 * height});
		getPoints().addAll(new Double[]{startX + width,startY + 0.9 * height});
		setFill(Color.RED);
	}
}
