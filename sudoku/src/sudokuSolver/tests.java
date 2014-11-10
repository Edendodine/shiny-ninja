package sudokuSolver;
import java.util.*;
import sudokuSolver.*;
import java.math.*;

public class tests {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Board mine = new Board();
		Random rand = new Random(53);
		/*
		ArrayList<Integer> row1 = new ArrayList<Integer>(Arrays.asList(6, 0, 2, 0, 0, 8, 0, 3, 0));
		ArrayList<Integer> row2 = new ArrayList<Integer>(Arrays.asList(1, 0, 8, 0, 3, 6, 7, 0, 2));
		ArrayList<Integer> row3 = new ArrayList<Integer>(Arrays.asList(7, 0, 0, 5, 0, 0, 8, 6, 0));
		ArrayList<Integer> row4 = new ArrayList<Integer>(Arrays.asList(9, 2, 0, 0, 6, 0, 3, 0, 0));
		ArrayList<Integer> row5 = new ArrayList<Integer>(Arrays.asList(0, 0, 1, 0, 0, 0, 6, 0, 0));
		ArrayList<Integer> row6 = new ArrayList<Integer>(Arrays.asList(0, 0, 3, 0, 4, 0, 0, 1, 5));
		ArrayList<Integer> row7 = new ArrayList<Integer>(Arrays.asList(0, 8, 9, 0, 0, 1, 0, 0, 6));
		ArrayList<Integer> row8 = new ArrayList<Integer>(Arrays.asList(3, 0, 5, 6, 2, 0, 1, 0, 9));
		ArrayList<Integer> row9 = new ArrayList<Integer>(Arrays.asList(0, 1, 0, 8, 0, 0, 5, 0, 3));		
			*/
		
		ArrayList<Integer> row1 = new ArrayList<Integer>(Arrays.asList(8, 0, 0, 7, 0, 0, 0, 0, 3));
		ArrayList<Integer> row2 = new ArrayList<Integer>(Arrays.asList(0, 7, 0, 9, 0, 0, 8, 0, 0));
		ArrayList<Integer> row3 = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 8, 0, 6, 0, 1, 0));
		ArrayList<Integer> row4 = new ArrayList<Integer>(Arrays.asList(5, 0, 2, 3, 0, 0, 4, 6, 0));
		ArrayList<Integer> row5 = new ArrayList<Integer>(Arrays.asList(0, 9, 0, 0, 0, 0, 0, 3, 0));
		ArrayList<Integer> row6 = new ArrayList<Integer>(Arrays.asList(0, 6, 4, 0, 0, 2, 1, 0, 9));
		ArrayList<Integer> row7 = new ArrayList<Integer>(Arrays.asList(0, 5, 0, 6, 0, 8, 0, 0, 0));
		ArrayList<Integer> row8 = new ArrayList<Integer>(Arrays.asList(0, 0, 1, 0, 0, 5, 0, 4, 0));
		ArrayList<Integer> row9 = new ArrayList<Integer>(Arrays.asList(6, 0, 0, 0, 0, 7, 0, 0, 5));
		
		/*
		ArrayList<Integer> row1 = new ArrayList<Integer>(Arrays.asList(0, 4, 8, 3, 0, 5, 9, 1, 0));
		ArrayList<Integer> row2 = new ArrayList<Integer>(Arrays.asList(0, 7, 0, 2, 0, 6, 0, 8, 0));
		ArrayList<Integer> row3 = new ArrayList<Integer>(Arrays.asList(0, 3, 1, 4, 0, 9, 5, 2, 0));
		ArrayList<Integer> row4 = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 2, 0, 0, 0, 0));
		ArrayList<Integer> row5 = new ArrayList<Integer>(Arrays.asList(8, 0, 0, 0, 0, 0, 0, 0, 1));
		ArrayList<Integer> row6 = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 8, 0, 3, 0, 0, 0));
		ArrayList<Integer> row7 = new ArrayList<Integer>(Arrays.asList(0, 0, 3, 0, 5, 0, 8, 0, 0));
		ArrayList<Integer> row8 = new ArrayList<Integer>(Arrays.asList(0, 8, 0, 0, 3, 0, 0, 6, 0));
		ArrayList<Integer> row9 = new ArrayList<Integer>(Arrays.asList(5, 0, 0, 0, 6, 0, 0, 0, 2));
		*/
		
		int[][] test = new int[9][9];
		for(int i = 0; i < test.length; i++) {
			for(int j = 0; j < test.length; j++) {
				//test[i][j] = 1 + rand.nextInt(9);
				switch(i) {
					case 0:
						test[i][j] = row1.get(j);
						break;
					case 1:
						test[i][j] = row2.get(j);
						break;
					case 2:
						test[i][j] = row3.get(j);
						break;
					case 3:
						test[i][j] = row4.get(j);
						break;
					case 4:
						test[i][j] = row5.get(j);
						break;
					case 5:
						test[i][j] = row6.get(j);
						break;
					case 6:
						test[i][j] = row7.get(j);
						break;
					case 7:
						test[i][j] = row8.get(j);
						break;
					case 8:
						test[i][j] = row9.get(j);
						break;
					
				}
				//System.out.print(test[i][j] + ", ");				
			}
			//System.out.println();
		}

		//System.out.println("test");
		mine.makeBoard(test);
		//mine.printPossibleValues();
		mine.testSolver();
		mine.printBoard();
		mine.printPossibleValues();
		/*
		mine.printPossibleValues();
		
		System.out.println();
		System.out.println();
		mine.printGroups();*/
		

	}

}
