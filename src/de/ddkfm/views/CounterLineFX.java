package de.ddkfm.views;

import java.awt.Point;

import de.ddkfm.models.CounterLine;
import de.ddkfm.models.LogicValue;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class CounterLineFX extends LogicNode{
	private BooleanProperty value = new SimpleBooleanProperty(false);
	public CounterLineFX() {
		Path path = new Path();
		path.getElements().add(new MoveTo(275,227));
		path.getElements().add(new LineTo(220,227));
		int count = 0;
		setPoints(count, path);
		this.getChildren().add(path);
		value.addListener((obs, oldValue, newValue)->{
			changeCount();
		});
	}
	private void changeCount(){
		CounterLine counterline = (CounterLine)logic;
		Path path = (Path) this.getChildren().get(0);
		int count = counterline.getCount();
		setPoints(count, path);
	}
	private void setPoints(int count, Path path){
		Point p1 = new Point(5 + 12 * 15 + 35, 80 + 15 * count + 8);
		Point p2 = new Point(5 + 12 * 15 + 20, 80 + 15 * count + 8);
		Point p3 = new Point(12, 80 + 15 * count);
		Point p4 = new Point(5 + 12 * 15 + 20, 80 + 15 * count);
		Point p5 = new Point(5 + 12 * 15 + 20, 80 + 15 * count + 15);
		Point p6 = new Point(12, 80 + 15 * count + 15);
		if(path.getElements().size() > 2)
			path.getElements().remove(2,9);
		path.getElements().add(new LineTo(p1.getX(), p1.getY()));
		path.getElements().add(new LineTo(p2.getX(), p2.getY()));
		path.getElements().add(new MoveTo(p3.getX(), p3.getY()));
		path.getElements().add(new LineTo(p4.getX(), p4.getY()));
		path.getElements().add(new LineTo(p5.getX(), p5.getY()));
		path.getElements().add(new LineTo(p6.getX(), p6.getY()));
		path.getElements().add(new LineTo(p3.getX(), p3.getY()));
		path.setStroke(Color.RED);
	}
	public void setLogic(LogicValue logic) {
		super.setLogic(logic);
		value.bind(getLogic().getValueProperty(1));
	}
}
