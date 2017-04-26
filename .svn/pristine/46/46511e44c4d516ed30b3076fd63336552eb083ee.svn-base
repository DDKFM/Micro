package de.ddkfm.models;

public class ProgramSwitch extends LogicValue {
	public ProgramSwitch(String name) {
		super(1, name);
	}
	public void passData(){
		String inputConnectionName = "";
		String outputConnectionName = "";
		if(this.getName().equals("programswitch")){
			inputConnectionName = getValue(0) ? "addressbus2programswitch" : "increment2programswitch";
			outputConnectionName = "programswitch2programregister";
		}else
			if(this.getName().equals("outputswitch")){
				inputConnectionName = getValue(0) ? "registerX2outputswitch" : "saveswitch2outputswitch";
				outputConnectionName = "outputswitch2databus";
			}
		boolean[] inputValues = new boolean[4];
		for(int i = 0 ; i < 4 ; i++){
			inputValues[i] = getConnection(inputConnectionName + "_" + i).getReference().getValue(0);
			getConnection(outputConnectionName + "_" + i).getReference().change(this, 0, inputValues[i]);
			getConnection(outputConnectionName + "_" + i).getReference().change(this, 0, !inputValues[i]);
			getConnection(outputConnectionName + "_" + i).getReference().change(this, 0, inputValues[i]);
		}
	}
	@Override
	public void change(LogicValue sender, int index, boolean value) {
		if(sender.getName().contains("muxer") || sender.getName().contains("decoder"))
			setValue(0, value);
		String inputConnectionName = "";
		String outputConnectionName = "";
		if(this.getName().equals("outputswitch"));
		else{
			if(this.getName().equals("programswitch")){
				inputConnectionName = getValue(0) ? "addressbus2programswitch" : "increment2programswitch";
				outputConnectionName = "programswitch2programregister";
			}else
				if(this.getName().equals("outputswitch")){
					inputConnectionName = getValue(0) ? "registerX2outputswitch" : "saveswitch2outputswitch";
					outputConnectionName = "outputswitch2databus";
				}
			boolean[] inputValues = new boolean[4];
			for(int i = 0 ; i < 4 ; i++){
				inputValues[i] = getConnection(inputConnectionName + "_" + i).getReference().getValue(0);
				getConnection(outputConnectionName + "_" + i).getReference().change(this, 0, inputValues[i]);
			}
		}
	}
	@Override
	protected void sendValueToOutput(int index, boolean value) {
		
	}
	@Override
	public void changeByExtern(Object sender, int index, boolean value) {
		setValue(index, value);
		change(this,0,value);
	}
}
