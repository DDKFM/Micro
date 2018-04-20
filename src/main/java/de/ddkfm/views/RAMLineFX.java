package de.ddkfm.views;

import java.awt.Point;

import de.ddkfm.models.LogicValue;
import de.ddkfm.models.RAMLine;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class RAMLineFX extends LogicNode{
	private BooleanProperty value = new SimpleBooleanProperty(false);
	public RAMLineFX() {
		Path path = new Path();
		path.setStroke(Color.RED);
		path.getElements().add(new MoveTo(900,175));
		path.getElements().add(new LineTo(880,175));
		int count = 0;
		setPoints(count, path);
		this.getChildren().add(path);
		value.addListener((obs, oldValue, newValue)->{
			changeCount();
		});
	}
	private void changeCount(){
		RAMLine ramline = (RAMLine)logic;
		Path path = (Path) this.getChildren().get(0);
		int count = ramline.getCount();
		setPoints(count, path);
	}
	private void setPoints(int count, Path path){
		Point p1 = new Point(800 + 4 * 15 + 20, 120 + 15 * count + 8);
		Point p2 = new Point(800 + 4 * 15, 120 + 15 * count + 8);
		Point p3 = new Point(804, 120 + 15 * count);
		Point p4 = new Point(804 + 4 * 15 + 1, 120 + 15 * count);
		Point p5 = new Point(804 + 4 * 15 + 1, 120 + 15 * count + 15);
		Point p6 = new Point(804, 120 + 15 * count + 15);
		if(path.getElements().size() > 2)
			path.getElements().remove(2,9);
		path.getElements().add(new LineTo(p1.getX(), p1.getY()));
		path.getElements().add(new LineTo(p2.getX(), p2.getY()));
		path.getElements().add(new MoveTo(p3.getX(), p3.getY()));
		path.getElements().add(new LineTo(p4.getX(), p4.getY()));
		path.getElements().add(new LineTo(p5.getX(), p5.getY()));
		path.getElements().add(new LineTo(p6.getX(), p6.getY()));
		path.getElements().add(new LineTo(p3.getX(), p3.getY()));
	}
	public void setLogic(LogicValue logic) {
		super.setLogic(logic);
		value.bind(getLogic().getValueProperty(1));
	}
}
