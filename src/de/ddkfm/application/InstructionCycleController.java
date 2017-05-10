package de.ddkfm.application;

import java.util.HashMap;
import java.util.Map;

import de.ddkfm.models.InstructionCycle;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
/**
 * The Controlelr for the InstructionCycle-Window
 * */
public class InstructionCycleController {
    
	@FXML
    private Circle step_2;

    @FXML
    private Button btStop;

    @FXML
    private Circle step_1;

    @FXML
    private Circle step_0;

    @FXML
    private Label lblInterval;

    @FXML
    private Button btStart;

    @FXML
    private Circle step_6;

    @FXML
    private Circle step_5;

    @FXML
    private Slider sliderInterval;

    @FXML
    private Button btSingleStep;

    @FXML
    private Circle step_4;

    @FXML
    private Circle step_3;

    @FXML
    private Rectangle paneStopped;
    private InstructionCycle instructioncycle;
    private BooleanProperty stopped = new SimpleBooleanProperty(true);
    private IntegerProperty steps = new SimpleIntegerProperty(0);
    private Map<String, Circle> stepCircles = new HashMap<String, Circle>();
    /**
     * this method load the InstructionCycle into the Window and fill the components with data and functions
     * @param instructioncycle the Window need a instance of the Instructioncycle
     * */
    public void load(InstructionCycle instructioncycle){
    	this.instructioncycle =  instructioncycle;
    	btStart.setOnAction(e->instructioncycle.start());
    	btStop.setOnAction(e->instructioncycle.cancel());
    	btSingleStep.setOnAction(e->instructioncycle.step());
    	
    	stepCircles.put("step_0", step_0);
    	stepCircles.put("step_1", step_1);
    	stepCircles.put("step_2", step_2);
    	stepCircles.put("step_3", step_3);
    	stepCircles.put("step_4", step_4);
    	stepCircles.put("step_5", step_5);
    	stepCircles.put("step_6", step_6);
    	
    	steps.bind(instructioncycle.getStepsProperty());
    	showStep(instructioncycle.getStep());
    	stopped.bind(instructioncycle.getStoppedProperty());
    	paneStopped.setFill(instructioncycle.isStopped() ? Color.RED : Color.GREEN);
    	stopped.addListener((obs,oldValue,newValue)->{
    		paneStopped.setFill(newValue ? Color.RED : Color.GREEN);
    	});
    	steps.addListener((obs,oldValue,newValue)->{
    		int step = newValue.intValue();
    		showStep(step);
    	});
    	sliderInterval.valueProperty().bindBidirectional(instructioncycle.getIntervalProperty());
    	lblInterval.textProperty().bind(Bindings.concat("Millisekunden pro Takt: ").concat(instructioncycle.getIntervalProperty()));
    }
    /**
     * set the Color of all Circles to Color.RED if the step is not active and to Color.GREEN if the step is active
     * @param step the current active step
     * */
    private void showStep(int step){
    	for(int i = 0; i < 7 ; i++){
    		Circle currentCircle = stepCircles.get("step_" + i);
    		if(i != step)
    			currentCircle.setFill(Color.RED);
    		else
    			currentCircle.setFill(Color.GREEN);
    	}
    }
    
}
