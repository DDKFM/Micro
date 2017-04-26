package de.ddkfm.models;

public class CounterLine extends LogicValue {
	private int count;
	public CounterLine(String name) {
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
		getConnection("memory").getReference().change(this, 1,getValue(1));
	}
	public int getCount(){
		return count;
	}

}
