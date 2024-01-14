package code;

public class Cell {
	private int number; 
	private boolean[] potential = {false, true, true, true, true, true, true, true, true, true};
	private int boxID; 
	
	public boolean canBe(int number){
		return potential[number];
	}

	public void cantBe(int number){
		potential[number] = false;
	}
	

	public int numberOfPotentials(){
		int result = 0;
		for(int i = 1; i <= 9; i++) {
			if(potential[i]) result++;
		}
		return result;
	}
	
	public int getFirstPotential(){
		for(int i = 1; i <= 9; i++) {
			if(potential[i]) return i;
		}
		return -1;
	}

	public int getNumber() {
		return number;
	}
	
	public void setNumber(int number) {
		this.number = number;
		
	}
	
	
	
	public boolean[] getPotential() {
		return potential;
	}
	public void setPotential(boolean[] potential) {
		this.potential = potential;
	}
	public int getBoxID() {
		return boxID;
	}
	public void setBoxID(int boxID) {
		this.boxID = boxID;
	}

}