package de.ddkfm.views;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.ddkfm.models.LogicValue;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Parent;

public abstract class LogicNode extends Parent{
	protected LogicValue logic = null;
	protected List<Point> points = new ArrayList<Point>();
	protected List<BooleanProperty> values = new ArrayList<BooleanProperty>();
	protected Logger logger;
	public LogicValue getLogic() {
		return this.logic;
	}
	public void setLogic(LogicValue logic) {
		this.logic = logic;
		logger = LogManager.getLogger(logic.getName() + "(FX)");
		for(int i = 0 ; i < logic.getValueCount() ; i++){
				values.add(new SimpleBooleanProperty(false));
				BooleanProperty currentProperty = values.get(i);
				currentProperty.bind(logic.getValueProperty(i));
				currentProperty.addListener((abs,oldValue, newValue)->{
					this.change(values.indexOf(currentProperty), newValue);
				});
		}
	}
	public void addPoint(Point p) {
		this.points.add(p);
	}
	public void addPoint(double x, double y) {
		Point p = new Point();
		p.setLocation(x, y);
		this.addPoint(p);
	}
	protected void change(int index, boolean value){}
	
}
