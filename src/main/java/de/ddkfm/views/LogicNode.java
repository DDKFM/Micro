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
/**
 * the class which represents the logic from LogicValue
 * */
public abstract class LogicNode extends Parent{
	protected LogicValue logic = null;
	protected List<Point> points = new ArrayList<Point>();
	protected List<BooleanProperty> values = new ArrayList<BooleanProperty>();
	protected Logger logger;
	/**
	 * returns the underlying LogicValue
	 * */
	public LogicValue getLogic() {
		return this.logic;
	}

	/**
	 * assign the logic to the Graphical Node
	 * */
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
	/**
	 * adding a graphical point to which the Node is displayed
	 * @param p Point(x and y)
	 * */
	public void addPoint(Point p) {
		this.points.add(p);
	}

	/**
	 * adding a graphical point to which the Node is displayed
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * */
	public void addPoint(double x, double y) {
		Point p = new Point();
		p.setLocation(x, y);
		this.addPoint(p);
	}
	/**
	 * method to fire (normally) the logic at the same index and with the same value
	 * @param index index that should by changed
	 * @param value value that should by changed
	 * */
	protected void change(int index, boolean value){}
	
}
