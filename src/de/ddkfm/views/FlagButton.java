package de.ddkfm.views;

import de.ddkfm.models.LogicValue;
import de.ddkfm.util.ThemeUtils;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Button;

public class FlagButton extends Button{
	private final String CAPTION_OFF = ThemeUtils.getThemeProperty("micro2.logicnode.caption.off");
	private final String CAPTION_ON = ThemeUtils.getThemeProperty("micro2.logicnode.caption.on");
	private final String STYLECLASS_OFF = ThemeUtils.getThemeProperty("micro2.logicnode.style.off");
	private final String STYLECLASS_ON = ThemeUtils.getThemeProperty("micro2.logicnode.style.on");
	private BooleanProperty value;
	private String name;
	private LogicValue logic;
	private int index;
	public FlagButton(LogicValue logic, int index) {
		this.logic = logic;
		this.name = logic.getName();
		this.index = index;
		value = new SimpleBooleanProperty(logic.getValue(index));
		getStyleClass().add(this.STYLECLASS_OFF);
		setText(logic.getName().substring(0,2).toUpperCase());
		value.bind(logic.getValueProperty(index));
		value.addListener((observable, oldValue, newValue)->{
			if(newValue){
				if(getStyleClass().contains(this.STYLECLASS_OFF))
					getStyleClass().remove(this.STYLECLASS_OFF);
				getStyleClass().add(STYLECLASS_ON);
			}else{
				if(getStyleClass().contains(this.STYLECLASS_ON))
					getStyleClass().remove(this.STYLECLASS_ON);
				getStyleClass().add(STYLECLASS_OFF);
			}	
		});
		getStyleClass().add("Flagbutton");
		this.setOnMouseClicked(event->{
			this.logic.changeByExtern(this, this.index, !this.value.get());
		});
	}
	
	
}
