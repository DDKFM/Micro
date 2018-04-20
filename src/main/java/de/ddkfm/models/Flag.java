package de.ddkfm.models;
/**
 * the flag displays if calculcation returns an "Error" such as Overflow or Underflow
 * */
public class Flag extends LogicValue {

	public Flag(String name) {
		super(1,name);
	}

	@Override
	public void change(LogicValue sender, int index, boolean value) {
		String senderName = sender.getName();
		int lineIndex = Integer.parseInt(senderName.substring(senderName.length() - 1));
		switch(getName()){
		case "zeFlag":
			if(lineIndex == 0)
				setValue(0, value);
			break;
		case "ovFlag":
			if(lineIndex == 1)
				setValue(0, value);
			break;
		case "neFlag":
			if(lineIndex == 2)
				setValue(0, value);
			break;
		}
	}
	/**
	 * depending on the flagtype will be set the 0th to 3th line
	 * */
	@Override
	protected void sendValueToOutput(int index, boolean value) {
		switch(getName()){
		case "zeFlag":
			getConnection("flags2muxer_0").getReference().change(this, 0, value);
			break;
		case "ovFlag":
			getConnection("flags2muxer_1").getReference().change(this, 0, value);
			break;
		case "neFlag":
			getConnection("flags2muxer_2").getReference().change(this, 0, value);
			break;
		}
	}
	@Override
	public void changeByExtern(Object sender, int index, boolean value) {
		this.setValue(index, value);
	}

}
