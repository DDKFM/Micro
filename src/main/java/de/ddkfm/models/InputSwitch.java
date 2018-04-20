package de.ddkfm.models;
/**
 * the inputswitch controls where the data from the databus will go. to registerA, registerB or its blocked
 * */
public class InputSwitch extends LogicValue {
	public InputSwitch(String name) {
		super(2, name);
	}
	@Override
	public void change(LogicValue sender, int index, boolean value) {
		if(sender.getName().contains("decoder2inputswitch") 
		|| sender.getName().contains("decoder2saveswitch")){
			int lineNumber = Integer.parseInt(sender.getName().substring(sender.getName().length() - 1));
			setValue(lineNumber, value);
		}
	}
	/**
	 * passData from the databus through the switch to register A oder B, or will block that
	 * */
	public void passData(){
		String inputConnectionName = getName().equals("inputswitch") ? "databus2inputswitch" : "ALU2saveswitch";
		String outputConnectionName = getName().equals("inputswitch")
										? (getValue(0)
												? "inputswitch2registerA"
												: "inputswitch2registerB")
										:(getValue(1)
												? "saveswitch2outputswitch"
												:"saveswitch2registerX");
		if(!getValue(0) && !getValue(1));
		else{
			for(int i = 0 ; i < 4 ; i++){
				LogicValue outputConnection = getConnection(outputConnectionName + "_" + i).getReference();
				outputConnection.change(this, 0, getConnection(inputConnectionName + "_" + i).getReference().getValue(0));
			}
		}
	}
	@Override
	public void changeByExtern(Object sender, int index, boolean value) {
		setValue(index, value);
	}
}
