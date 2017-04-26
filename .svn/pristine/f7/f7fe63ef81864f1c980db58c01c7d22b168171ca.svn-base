package de.ddkfm.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class InstructionCycle{
	
	private Timer timer;
	
	private IntegerProperty interval = new SimpleIntegerProperty(1000);
	private IntegerProperty steps = new SimpleIntegerProperty(0);
	private List<Integer> breakpoints = new ArrayList<Integer>();
	private BooleanProperty stopped = new SimpleBooleanProperty(true);
	
	private Processor processor;
	private CountRegister counterregister;
	private Decoder decoder;
	private RAMSwitch ramswitch;
	private RAMRegister ramregister;
	private Register registerA;
	private Register registerB;
	private InputSwitch inputswitch;
	private ALU alu;
	
	private ProgramSwitch outputswitch;
	private InputSwitch saveswitch;
	private Register registerX;
	
	public InstructionCycle(Processor processor) {
		this.processor = processor;
		counterregister = (CountRegister) processor.getLogicValueByName("programregister");
		decoder = (Decoder) processor.getLogicValueByName("decoder");
		ramswitch = (RAMSwitch) processor.getLogicValueByName("ramswitch");
		ramregister = (RAMRegister) processor.getLogicValueByName("ramregister");
		registerA = (Register) processor.getLogicValueByName("registerA");
		registerB = (Register) processor.getLogicValueByName("registerB");
		inputswitch = (InputSwitch) processor.getLogicValueByName("inputswitch");
		alu = (ALU) processor.getLogicValueByName("alu");
		saveswitch = (InputSwitch) processor.getLogicValueByName("saveswitch");
		outputswitch = (ProgramSwitch) processor.getLogicValueByName("outputswitch");
		registerX = (Register) processor.getLogicValueByName("registerX");
		processor.getLogicValueByName("programswitch2programregister_3").setValue(0, true);
	}
	public void start() {
		if(timer != null)
			timer.cancel();
		timer = new Timer(true);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						runInstructions();
					}
				});
			}
		},200,interval.get());
	}
	public void runInstructions() {
		stopped.set(false);
		if(breakpoints.contains(steps.get()))
			cancel();
		processStep(steps.get());
		steps.set((steps.get() + 1) % 7 );
	}
	public void step(){
		runInstructions();
		stopped.set(true);
	}
	public void cancel() {
		stopped.set(true);
		timer.cancel();
	}
	private void processStep(int index){
		switch(index){
		case 0:
			ramswitch.block();
			for(int i = 0 ; i < 4 ; i++)
				processor.getLogicValueByName("databus_" + i).change(new LogicValue(0,"dataswitch2databus_" + i), 0, false);
			counterregister.acceptValues();
			break;
		case 1:
			decoder.decode();
			break;
		case 2:
			ramswitch.unblock();
			ramregister.acceptValues();
			ramswitch.change(new LogicValue(0,"ram2ramswitch_0"), 0, processor.getLogicValueByName("ram2ramswitch_0").getValue(0));
			break;
		case 3:
			inputswitch.passData();
			break;
		case 4:
			registerA.acceptValues();
			registerB.acceptValues();
			break;
		case 5:
			alu.calculate();
			break;
		case 6:
			saveswitch.passData();
			registerX.acceptValues();
			outputswitch.passData();
			break;
		}
	}
	public IntegerProperty getStepsProperty(){
		return steps;
	}
	public int getStep(){
		return steps.get();
	}
	public void setState(int state){
		this.steps.set(state);
	}
	public BooleanProperty getStoppedProperty(){
		return stopped;
	}
	public boolean isStopped(){
		return stopped.get();
	}
	public void addBreakpoint(Integer index){
		if(!breakpoints.contains(index))
			breakpoints.add(index);
	}
	public void removeBreakpoint(Integer index){
		if(breakpoints.contains(index))
			breakpoints.remove(index);
	}
	public IntegerProperty getIntervalProperty(){
		return interval;
	}
	public int getInterval(){
		return interval.get();
	}
	public void setInterval(int interval){
		this.interval.set(interval);
	}
	
	
}
