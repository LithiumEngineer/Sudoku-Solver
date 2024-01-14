package code;

import java.io.File;
import java.util.Scanner;

public class Board{

	private Cell[][] board = new Cell[9][9];
	
	private String level = "";

	public Board(){
		for(int x = 0; x < 9; x++)
			for(int y = 0 ; y < 9; y++)
			{
				board[x][y] = new Cell();
				board[x][y].setBoxID( 3*(x/3) + (y)/3+1);
			}
	}
	

	
	public void loadPuzzle(String level) throws Exception{
		this.level = level;
		String fileName = "easyPuzzle.txt";
		if(level.contentEquals("medium"))
			fileName = "mediumPuzzle.txt";
		else if(level.contentEquals("hard"))
			fileName = "hardPuzzle.txt";
		else if(level.contentEquals("evil"))
			fileName = "evilPuzzle.txt";
		
		Scanner input = new Scanner (new File(fileName));
		
		for(int x = 0; x < 9; x++)
			for(int y = 0 ; y < 9; y++)
			{
				int number = input.nextInt();
				
				if(number != 0)
					solve(x, y, number);
				
			}
						
		input.close();
		
	}

	
	public boolean isSolved(){
		boolean ok = true;
		for(int x = 0; x < 9; x++) {
			for(int y = 0; y < 9; y++) {
				if(board[x][y].getNumber() == 0) ok = false;
			}
		}
		return ok;
	}

	public Cell getCell(int x, int y) {
		return board[x][y];
	}

	public void display(){
		
		System.out.println();
		for(int i = 0; i < 9; i++) {
			if(i % 3 == 0) {
				for(int j = 0; j < 25; j++) {
					if(j % 8 == 0) 
						System.out.print("+");
					else 
						System.out.print("-");
				}
				System.out.println();
			}
			for(int j = 0; j < 9; j++) {
				if(j % 3 == 0) System.out.print("| ");
				System.out.print(board[i][j].getNumber() + " ");
			}
			System.out.println("|");
		}
		for(int i = 0; i < 25; i++) {
			if(i % 8 == 0) 
				System.out.print("+");
			else 
				System.out.print("-");
		}
		System.out.println();
		
	}
	

	public void solve(int x, int y, int number){
		board[x][y].setNumber(number);
		
		//Cell itself
		for(int i = 1; i <= 9; i++) {
			if(i != number)
				board[x][y].cantBe(i);
		}
		
		//Row
		for(int j = 0; j < 9; j++) {
			if(j != y)
				board[x][j].cantBe(number);
		}
		
		//Column
		for(int i = 0; i < 9; i++) {
			if(i != x)
				board[i][y].cantBe(number);
		}
		
		//Box
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				if(board[i][j].getBoxID() == board[x][y].getBoxID() && !(i == x && j == y)){
					board[i][j].cantBe(number);
				}
			}
		}
	}
	
	public int firstUnsolvedX() {
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				if(board[i][j].getNumber() == 0) {
					return i;
				}
			}
		}
		return -1;
	}
	
	public int firstUnsolvedY() {
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				if(board[i][j].getNumber() == 0) {
					return j;
				}
			}
		}
		return -1;
	}
	
	public int firstUnsolvedGuess() {
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				if(board[i][j].getNumber() == 0) {
					return board[i][j].getFirstPotential();
				}
			}
		}
		return -1;
	}
	

	public void logicCycles()throws Exception{
			int changesMade = 0;
			do
			{
				changesMade = 0;
				changesMade += logic1();
				changesMade += logic2();
				changesMade += logic3();
				changesMade += logic4();
				if(errorFound())
					break;
			} while(changesMade != 0);
	}
	
	
	public int logic1(){
		int changesMade = 0;
		for(int x = 0; x < 9; x++) {
			for(int y = 0; y < 9; y++) {
				
				if(board[x][y].numberOfPotentials() == 1 && board[x][y].getNumber() == 0) {
					solve(x, y, board[x][y].getFirstPotential());
					changesMade++;
				}
			}
		}
		return changesMade;		
	}
	

	public int logic2(){
		
		int changesMade = 0;
		for(int possibleNumber = 1; possibleNumber <= 9; possibleNumber++) {
			for(int x = 0; x < 9; x++) {
				int numberOfCells = 0, lastCell = -1;
				for(int y = 0; y < 9; y++) {
					if(board[x][y].canBe(possibleNumber)) {
						numberOfCells++;
						lastCell = y;
					}
				}
				
				if(numberOfCells == 1 && board[x][lastCell].getNumber() == 0) {
					solve(x, lastCell, possibleNumber);
					changesMade++;
				}
			}
		}
		
		for(int possibleNumber = 1; possibleNumber <= 9; possibleNumber++) {
			for(int y = 0; y < 9; y++) {
				int numberOfCells = 0, lastCell = -1;
				for(int x = 0; x < 9; x++) {
					if(board[x][y].canBe(possibleNumber)) {
						numberOfCells++;
						lastCell = x;
					}
				}
				
				if(numberOfCells == 1 && board[lastCell][y].getNumber() == 0) {
					solve(lastCell, y, possibleNumber);
					changesMade++;
				}
			}
		}
		
		return changesMade;
	}
	
	public int logic3(){
		int changesMade = 0;
		for(int boxID = 1; boxID <= 9; boxID++) {
			for(int possibleNumber = 1; possibleNumber <= 9; possibleNumber++) {
				
				int numberOfCells = 0, lastCellX = -1, lastCellY = -1;
				for(int x = 0; x < 9; x++) {
					for(int y = 0; y < 9; y++) {
						if(board[x][y].getBoxID() == boxID && board[x][y].canBe(possibleNumber)) {
							numberOfCells++;
							lastCellX = x;
							lastCellY = y;
						}
							
					}
				}
				
				if(numberOfCells == 1 && board[lastCellX][lastCellY].getNumber() == 0) {
					solve(lastCellX, lastCellY, possibleNumber);
					changesMade++;
				}
			}
			
		}
		return changesMade;
	}
	

	public int logic4(){
		int changesMade = 0;
		for(int x1 = 0; x1 < 9; x1++) {
			for(int y1 = 0; y1 < 9; y1++) {
				for(int x2 = 0; x2 < 9; x2++) {
					for(int y2 = 0; y2 < 9; y2++) {
						if((x1 == x2 || y1 == y2) && !(x1 == x2 && y1 == y2)) {
							int numberOfPotentialOne = 0, numberOfPotentialTwo = 0, numberOfPotentialMerged = 0;
							boolean[] mergedPotential = new boolean[10];
							for(int i = 1; i <= 9; i++) {
								if(board[x1][y1].canBe(i)) numberOfPotentialOne++;
								if(board[x2][y2].canBe(i)) numberOfPotentialTwo++;
								if(board[x1][y1].canBe(i) || board[x2][y2].canBe(i)) numberOfPotentialMerged++;
								mergedPotential[i] = (board[x1][y1].canBe(i) || board[x2][y2].canBe(i));
							}
							if(numberOfPotentialOne == 2 && numberOfPotentialTwo == 2 && numberOfPotentialMerged == 2) {
								for(int x3 = 0; x3 < 9; x3++) {
									for(int y3 = 0; y3 < 9; y3++){
										if(!(x3 == x1 && y3 == y1) && !(x3 == x2 && y3 == y2) && ((x1 == x2 && x2 == x3) || (y1 == y2 && y2 == y3))){
											for(int i = 1; i <= 9; i++) {
												if(mergedPotential[i] == true && board[x3][y3].canBe(i)) {
													board[x3][y3].cantBe(i);
													changesMade++;
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return changesMade;
	}
	
	public boolean errorFound(){
		for(int x = 0; x < 9; x++) {
			for(int y = 0; y < 9; y++) {
				if(board[x][y].numberOfPotentials() == 0) return true;
			}
		}
		return false;
	}
}