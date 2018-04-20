package de.ddkfm.models;
/**
 * the RAM switch controls the data direction to the RAM. it control whether the RAM is reading or writing
 * */
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
	/**
	 * pass the data through the switch, depending on the control signals from the decoder
	 * */
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

	/**
	 * block the ramswitch from doing the method recognize
	 * */
	public void block(){
		this.blocked = true;
	}

	/**
	 * unlock the Ram Switch
	 * */
	public void unblock(){
		this.blocked = false;

	}

	@Override
	public void changeByExtern(Object sender, int index, boolean value) {
		setValue(index, value);
	}
}
