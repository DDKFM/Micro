package de.ddkfm.views;

import java.awt.Point;

import de.ddkfm.models.LogicValue;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class DatalineFX extends LogicNode{
	private BooleanProperty value = new SimpleBooleanProperty(false);
	public DatalineFX() {
		Path path = new Path();
		this.getChildren().add(path);
		value.addListener((obs,oldValue,newValue)->{
			path.setStroke(newValue? Color.RED : Color.WHITE);
		});
	}

	@Override
	public void addPoint(Point p) {
		Path path = (Path)this.getChildren().get(0);
		if(this.points.size() == 0){
			this.setLayoutX(p.getX());
			this.setLayoutY(p.getY());
		}
		path.getElements().add(this.points.size() == 0 
								? new MoveTo(0,0) 
								: new LineTo(p.getX() - this.getLayoutX(), p.getY() - this.getLayoutY())
								);
		super.addPoint(p);
	}
	public void setLogic(LogicValue logic) {
		super.setLogic(logic);
		value.bind(getLogic().getValueProperty(0));
	}
}
