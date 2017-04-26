package de.ddkfm.models;

public class RAMSwitch extends LogicValue {
	private boolean blocked = true;
	public RAMSwitch(String name) {
		super(2, name);
	}
	@Override
	public void change(LogicValue sender, int index, boolean value) {
		logger.info("AUFRUF: " + sender.getName() + "(" + index + "): " + value);
		if(sender.getName().contains("decoder")){
			setValue(sender.getName().equals("decoder2ramswitch_0") ? 0 : 1, value);
		}
		if(!(sender.getName().contains("databus") && getValue(0) 
				|| sender.getName().contains("ram2") && getValue(1)))
			recognize();
	}
	public void recognize(){
		if(!blocked){
			String inputConnectionName = "";
			String outputConnectionName = "";
			if(getValue(0)){
				inputConnectionName = "ram2ramswitch";
				outputConnectionName = "databus2ramswitch";
			}else 
				if(getValue(1)){
					inputConnectionName = "databus2ramswitch";
					outputConnectionName = "ram2ramswitch";
				}
			if(!(inputConnectionName.equals("") && outputConnectionName.equals("")))
				for(int i = 0 ; i < 4 ; i++){
					LogicValue inputLogicValue = getConnection(inputConnectionName + "_" + i).getReference();
					getConnection(outputConnectionName + "_" + i).getReference().change(this, 0, inputLogicValue.getValue(0));
				}
		}
	}
	public void unblock(){
		this.blocked = false;
		
	}
	public void block(){
		this.blocked = true;
	}
	@Override
	public void changeByExtern(Object sender, int index, boolean value) {
		setValue(index, value);
	}
}
