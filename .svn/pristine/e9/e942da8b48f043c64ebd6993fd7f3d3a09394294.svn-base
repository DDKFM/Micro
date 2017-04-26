package de.ddkfm.models;

public class RAMLine extends LogicValue {
	private int count;
	public RAMLine(String name) {
		super(2,name);
		setValue(0, true);
	}
	@Override
	public void change(LogicValue sender, int index, boolean value) {
		setValue(0, true);
	}
	public void changeCount(int index){
		this.count = index;
		this.setValue(1, !getValue(1));
		getConnection("ram").getReference().change(this, 1,getValue(1));
	}
	public int getCount(){
		return count;
	}

}
