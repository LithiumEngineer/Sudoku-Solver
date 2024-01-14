package code;

public class SudokuSolver {

	public static void main(String[] args)throws Exception {
		
		Board puzzle = new Board();
		puzzle.loadPuzzle("hard"); 
		Board[] stack = new Board[81];
		stack[0] = puzzle;
		
		System.out.println("STARTING: ");
		stack[0].display();
		
		int cur = 1; 
		
		while(true) {
			stack[cur] = new Board();
			for(int i = 0; i < 9; i++) {
				for(int j = 0; j < 9; j++) {
					if(stack[cur - 1].getCell(i, j).getNumber() != 0)
						stack[cur].solve(i, j, stack[cur - 1].getCell(i, j).getNumber());
					else {
						for(int x = 1; x <= 9; x++) {
							if(!stack[cur - 1].getCell(i, j).canBe(x))
								stack[cur].getCell(i, j).cantBe(x);
						}
					}
				}
			}
			
			stack[cur].solve(stack[cur].firstUnsolvedX(), stack[cur].firstUnsolvedY(), stack[cur].firstUnsolvedGuess());
			stack[cur].logicCycles();
			stack[cur].display();
			
			while(stack[cur].errorFound()) {
				System.out.println("UH OH!!! BACKTRACK");
				stack[cur - 1].getCell(stack[cur - 1].firstUnsolvedX(), stack[cur - 1].firstUnsolvedY()).cantBe(stack[cur - 1].firstUnsolvedGuess());
				cur--;
			} 
			
			if(stack[cur].isSolved()) {
				System.out.println("I LOVE THE SUDOKU SOLVER!!!");
				stack[cur].display();
				break;
			}
			
			cur++;
		}
	}

}