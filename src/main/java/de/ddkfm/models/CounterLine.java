package de.ddkfm.models;
/**
 * represents the Connection from the programregister to the memory
 * */
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

	/**
	 * change the pointer to the memory to the given index
	 * @param index given index between 0 and 15
	 * */
	public void changeCount(int index){
		this.count = index;
		this.setValue(1, !getValue(1));
		getConnection("memory").getReference().change(this, 1,getValue(1));
	}

	/**
	 * return the index to which the line is pointed
	 * */
	public int getCount(){
		return count;
	}

}
