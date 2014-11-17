package sudokuSolver;
import java.util.*;
import sudokuSolver.Cell;
//import util.*;

public class Board {	
	Cell[][] sudokuBoard;
	List<HashSet<Integer>> rowSets;
	List<HashSet<Integer>> columnSets;
	List<HashSet<Integer>> groupSets;
	final Set<Integer> solved = new HashSet<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
	private static final int LENGTH = 9;
	
	public void makeBoard(int[][] sourceArray) {
		int groupIndex = 0;
		int value = 0;
		for(int row = 0; row < 9; row++) {
			for(int column = 0; column < 9; column++) {
				groupIndex = getGroupIndex(row, column);
				sudokuBoard[row][column] = new Cell(row, column, groupIndex, 0);
				value = sourceArray[row][column];
				sudokuBoard[row][column].setInitialValue(value);
				if(value != 0) {
					rowSets.get(row).add(value);
					columnSets.get(column).add(value);
					groupSets.get(groupIndex).add(value);
				}								
			}			
		}
		generateCellLists();
	}
	
	public void clearBoard() {
		int group = 0;
		int oldValue = 0;
		for(int row = 0; row < LENGTH; row++) {
			for(int column = 0; column < LENGTH; column++) {
				if(sudokuBoard[row][column].getCanModify()) {
					oldValue = sudokuBoard[row][column].getValue();
					sudokuBoard[row][column].clear();
					group = getGroupIndex(row, column);
					rowSets.get(row).remove(oldValue);
					columnSets.get(column).remove(oldValue);
					groupSets.get(group).remove(oldValue);
				}
			}			
		}
		generateCellLists();
	}
	
	public Board() {
		sudokuBoard = new Cell[9][9];	
		rowSets = new ArrayList<HashSet<Integer>>();
		columnSets = new ArrayList<HashSet<Integer>>();
		groupSets = new ArrayList<HashSet<Integer>>();
		for(int i = 0; i < LENGTH; i++) {
			rowSets.add(new HashSet<Integer>());
			columnSets.add(new HashSet<Integer>());
			groupSets.add(new HashSet<Integer>());
		}
	}	
	
	public void generateCellLists() {
		Set<Integer> union = new HashSet<Integer>();
		Set<Integer> possible = new HashSet<Integer>(solved);
		int groupIndex = 0;
		for(int row = 0; row < LENGTH; row++) {			
			for(int column = 0; column < LENGTH; column++) {
				groupIndex = getGroupIndex(row, column);
				if(sudokuBoard[row][column].getCanModify()) {  //If not set by generator
					union.addAll(rowSets.get(row));
					union.addAll(columnSets.get(column));
					union.addAll(groupSets.get(groupIndex));
					possible.removeAll(union);
				
					sudokuBoard[row][column].setPossibleValues(possible);
					possible.addAll(solved);
					union.removeAll(solved);
				}
			}						
		}		
	}
	
	
	
	private void updateCellLists(int rowIndex, int columnIndex) {
		Set<Integer> union = new HashSet<Integer>();
		Set<Integer> possible = new HashSet<Integer>(solved);
		int index = 0, baseRowIndex = 0, baseColumnIndex = 0;
		int groupIndex = getGroupIndex(rowIndex, columnIndex);
		for(int i = 0; i < LENGTH; i++) {
			index = (rowIndex / 3) * 3 + i / 3;
			if(sudokuBoard[rowIndex][i].getCanModify()) {  //If not set by generator
				union.addAll(rowSets.get(rowIndex));
				union.addAll(columnSets.get(i));
				union.addAll(groupSets.get(index));
				possible.removeAll(union);
			
				sudokuBoard[rowIndex][i].setPossibleValues(possible);
				possible.addAll(solved);
				union.removeAll(solved);
			}
			index = (i / 3) * 3 + columnIndex / 3;
			if(sudokuBoard[i][columnIndex].getCanModify()) {  //If not set by generator
				union.addAll(rowSets.get(i));
				union.addAll(columnSets.get(columnIndex));
				union.addAll(groupSets.get(index));
				possible.removeAll(union);
			
				sudokuBoard[i][columnIndex].setPossibleValues(possible);
				possible.addAll(solved);
				union.removeAll(solved);
			}						
		}
		baseRowIndex = rowIndex - (rowIndex % 3);
		baseColumnIndex = columnIndex - (columnIndex % 3);
		for(int x = baseRowIndex; x < baseRowIndex + 3; x++) {
			for(int y = baseColumnIndex; y < baseColumnIndex + 3; y++) {
				if(sudokuBoard[x][y].getCanModify()) {  //If not set by generator
					union.addAll(rowSets.get(x));
					union.addAll(columnSets.get(y));
					union.addAll(groupSets.get(groupIndex));
					possible.removeAll(union);
				
					sudokuBoard[x][y].setPossibleValues(possible);
					possible.addAll(solved);
					union.removeAll(solved);
				}					
			}
		}
	}
	
	public void printBoard() {
		for(int row = 0; row < LENGTH; row++) {
			for(int column = 0; column < LENGTH; column++) {
				System.out.print(sudokuBoard[row][column].getValue());
				System.out.print(" ");
			}
			System.out.println();
		}		
	}
	
	public void printPossibleValues() {
		for(int row = 0; row < LENGTH; row++) {
			for(int column = 0; column < LENGTH; column++) {
				if(!sudokuBoard[row][column].getIsSolved()) {
					System.out.println("Cell: " + row + column);
					System.out.println(sudokuBoard[row][column].getPossibleValues());
				}				
			}
		}	
	}
	
	public void printSolveableCells() {
		for(int row = 0; row < LENGTH; row++) {
			for(int column = 0; column < LENGTH; column++) {
				if(sudokuBoard[row][column].canSolve()) {
					System.out.println("Cell: " + row + column);
					System.out.println(sudokuBoard[row][column].getPossibleValues());
				}				
			}
		}
	}
	
	public void printGroups() {
		for(int group = 0; group < LENGTH; group++) {
			System.out.println(groupSets.get(group));
		}
	}	
	
	public boolean insert(int rowIndex, int columnIndex, int value) {
		boolean result = sudokuBoard[rowIndex][columnIndex].insert(value);
		if(result)
			updatePossibleValues(rowIndex, columnIndex, value, true);
		return result;				
	}
			
	public int getValue(int rowIndex, int columnIndex) {
		return sudokuBoard[rowIndex][columnIndex].getValue();
	}
				
	private void updatePossibleValues(int rowIndex, int columnIndex, int value, boolean isInsertion) {
		updateSets(rowIndex, columnIndex, value, isInsertion);
		updateCellLists(rowIndex, columnIndex);		
	}
	
	public boolean delete(int rowIndex, int columnIndex) {
		int result = sudokuBoard[rowIndex][columnIndex].delete();
		if(result != 0) 
			updatePossibleValues(rowIndex, columnIndex, result, false);
		return result != 0;		
	}	
	
	public int getGroupIndex(int row, int column) {
		return (row / 3) * 3 + column / 3;
	}
	
	public boolean solve(boolean useSlowSolver) {
		Solver solver = new Solver();
		if(useSlowSolver) {
			long startTime = System.nanoTime();
			solver.slowSolve();	
			long endTime = System.nanoTime();
			double duration = (double)(endTime - startTime) / 1000000.0;
			System.out.println("SlowSolver took: " + duration);
		}
		else {
			long startTime = System.nanoTime();
			solver.solve();
			long endTime = System.nanoTime();			
			double duration = (double)(endTime - startTime) / 1000000.0;
			System.out.println("Solver took: " + duration);
		}		
		return true; 
	}
	
	private void updateSets(int rowIndex, int columnIndex, int value, boolean isInsertion) {
		int groupIndex = getGroupIndex(rowIndex, columnIndex);
		if(isInsertion) {
			rowSets.get(rowIndex).add(value);
			columnSets.get(columnIndex).add(value);
			groupSets.get(groupIndex).add(value);
		}
		else {
			rowSets.get(rowIndex).remove(value);
			columnSets.get(columnIndex).remove(value);
			groupSets.get(groupIndex).remove(value);
		}
	}
	
	private class Solver {
		util.Stack<LinkedList<StackNode>> solveStack = new util.Stack<LinkedList<StackNode>>(); //Key, old value, new value
		LinkedList<StackNode> values = new LinkedList<StackNode>();
		private boolean isBruteForced = false;
		private class StackNode {
			public final int value;
			public final int rowIndex;
			public final int columnIndex;
						
			public StackNode(int value, int rowIndex, int columnIndex) {
				this.value = value;
				this.columnIndex = columnIndex;
				this.rowIndex = rowIndex;				
			}		
		}
		
		private Set<Integer> getValues(int rowIndex, int columnIndex) {
			int groupIndex = getGroupIndex(rowIndex, columnIndex);
			Set<Integer> eliminated = new HashSet<Integer>();
			Set<Integer> result = new HashSet<Integer>(solved);
			eliminated.addAll(rowSets.get(rowIndex));
			eliminated.addAll(columnSets.get(columnIndex));
			eliminated.addAll(groupSets.get(groupIndex));
			result.removeAll(eliminated);
			return result;
		}
		
		private int getBaseCell() {
			for(int row = 0; row < LENGTH; row++) {
				for(int column = 0; column < LENGTH; column++) {
					if(sudokuBoard[row][column].getCanModify()) {
						return row * 9 + column;
					}
				}
			}
			return -1;
		}
		
		private boolean slowSolve() {
			Set<Integer> possible = null;
			final int END = LENGTH * LENGTH;
			int oldValue = 0;
			int row = 0;
			int column = 0;
			int baseCell = getBaseCell();
			int cell = baseCell;
			boolean isBackTrack = false;
						
			out:
			while(cell != END && cell >= baseCell) {
				row = cell / 9;
				column = cell % 9;				
				//System.out.println("AT: " + row + column + " cell: " + cell);
				if(sudokuBoard[row][column].getCanModify()) {
					oldValue = sudokuBoard[row][column].getValue();
					possible = getValues(row, column);
					//System.out.println(possible + " and " + oldValue);
					if(possible.isEmpty()) {
						if(oldValue != 0) {
							updateSets(row, column, oldValue, false);
							sudokuBoard[row][column].setValue(0);
							cell--;
							isBackTrack = true;
							continue;
						}
						else {
							cell--;
							isBackTrack = true;
							continue;
						}
					}
					
					int nextGuess = oldValue + 1;
					for(int element : possible) {
						//System.out.println(possible + " current: " + element);
						if(element >= nextGuess) {
							sudokuBoard[row][column].setValue(element);
							updateSets(row, column, oldValue, false);
							updateSets(row, column, element, true);
							cell++;
							isBackTrack = false;
							continue out;
						}
					}					
					
					//Possible value, but we exhausted it					
					sudokuBoard[row][column].setValue(0);
					updateSets(row, column, oldValue, false);
					cell--;
					isBackTrack = true;
					continue;
				}
				if(isBackTrack) {
					cell--;
				}
				else {
					cell++;
				}
			}
			return cell == END;
		}
		
		private boolean checkCanSolve() {
			for(int row = 0; row < LENGTH; row++) {
				for(int column = 0; column < LENGTH; column++) {
					if(!sudokuBoard[row][column].canSolve()) {
						return false;
					}
				}
			}
			return true;			
		}
		
		public boolean checkSolution() {
			for(int row = 0; row < LENGTH; row++) {
				for(int column = 0; column < LENGTH; column++) {
					if(!sudokuBoard[row][column].getIsSolved())
						return false;					
				}
			}
			if(!verifySolution())
				return false;
			return true;
		}
		
		private void deBork() {
			if(values.isEmpty()) {
				if(solveStack.empty()) {
					/*System.out.println("He's dead Jim!");
					System.exit(1);*/
					clearBoard();
					slowSolve();
					return;
				}
				values = solveStack.pop();				
			}
			if(!undoValues())
				deBork();
		}
		
		private boolean undoValues() {
			StackNode helper = null;
			//Delete the values in this list
			while(!values.isEmpty()) {
				helper = values.removeFirst();
				delete(helper.rowIndex, helper.columnIndex);
			}
			//Otherwise, try a new value
			Set<Integer> possible = sudokuBoard[helper.rowIndex][helper.columnIndex].getPossibleValues();
			for(int element : possible) {
				if(element > helper.value) {
					forceInsert(helper.rowIndex, helper.columnIndex, element);					
					return true;
				}
			}			
			//We can try no new value
			return false;
		}
		
		private boolean verifySolution() {
			for(int i = 0; i < LENGTH; i++) {
				if(!rowSets.get(i).containsAll(solved))
					return false;
				if(!columnSets.get(i).containsAll(solved))
					return false;
				if(!groupSets.get(i).containsAll(solved))
					return false;
			}
			return true;
		}	
		
		public boolean solve() {
			while(true) {
				simpleSolve();
				if(checkOptimizations())
					continue;
				if(checkSolution())
					return true;
				//Simple methods did not work
				
				if(!isBruteForced) {
					//System.out.println("Brute Forcing this mofo");
					isBruteForced = true;
					if(!bruteForce()) {
						return false;
					}
					continue;
				}
				
				if(!checkCanSolve()) {
					deBork();
					if(!checkCanSolve()) {
						return false;
					}
					continue;
				}
				
				if(bruteForce()) {
					continue;
				}
				else {
					if(!checkSolution())
						return false;
				}				
			}
			
			//return true;
		}
		
		private boolean bruteForce() {
			if(!values.isEmpty()) {
				solveStack.push(values);
				values = new LinkedList<StackNode>();
			}
			
			Set<Integer> possible;
			int rIndex = -1, cIndex = -1;
			
			out:
			for(int row = 0; row < LENGTH; row++) {
				for(int column = 0; column < LENGTH; column++) {
					if(!sudokuBoard[row][column].getIsSolved()) {
						rIndex = row;
						cIndex = column;
						break out;
					}						
				}
			}
			
			if(rIndex != -1 && cIndex != -1) {
				possible = sudokuBoard[rIndex][cIndex].getPossibleValues();
				for(int element : possible) {
					forceInsert(rIndex, cIndex, element);
					return true;
				}
			}
			return false;			
		}
		
		private void simpleSolve() {
			Set<Integer> values;
			
			out:
			while(true) {
				for(int row = 0; row < LENGTH; row++) {				
					if(checkSolved(row, BoardSection.ROW))
						continue;
					for(int column = 0; column < LENGTH; column++) {
						if(checkSolved(column, BoardSection.COLUMN))
							continue;
						if(!sudokuBoard[row][column].getIsSolved()) {
							values = sudokuBoard[row][column].getPossibleValues();
							if(values.size() == 1) {
								for(int element : values) {
									if(isBruteForced) {
										forceInsert(row, column, element);
										continue out;
									}
									else {
										insert(row, column, element);
										continue out;
									}
								}
							}
							if(checkUnions(row, column)) 
								continue out;					
						}					
					}				
				}
				break;
			}
		}
		
		private boolean checkOptimizations() {
			for(int z = 0; z < LENGTH; z++) {
				if(checkCanOptimize(z, BoardSection.ROW)) {
					if(optimizeRows(z))
						return true;
				}
				if(checkCanOptimize(z, BoardSection.COLUMN)) {
					if(optimizeColumns(z))
						return true;
				}
			}
			return false;
		}
		
		private boolean forceInsert(int rowIndex, int columnIndex, int value) {
			StackNode stackNode = new StackNode(value, rowIndex, columnIndex);
			values.addFirst(stackNode);
			
			return insert(rowIndex, columnIndex, value);			
		}			
		
		public boolean checkCanOptimize(int index, BoardSection bg) {
			if(checkSolved(index, bg))
				return false;
			switch(bg) {
				case ROW:
					if(rowSets.get(index).size() >= 7)
						return false;					
				case COLUMN:
					if(columnSets.get(index).size() >= 7)
						return false;					
				case GROUP:
					if(groupSets.get(index).size() >= 7)
						return false;									
			}
			return true;
		}
		
		public boolean checkSolved(int index, BoardSection bg) {
			switch(bg) {
				case ROW:
					if(rowSets.get(index).containsAll(solved))
						return true;					
				case COLUMN:
					if(columnSets.get(index).containsAll(solved))
						return true;					
				case GROUP:
					if(groupSets.get(index).containsAll(solved))
						return true;
			}
			return false;
		}
		
		private boolean checkUnion(int rowIndex, int columnIndex, BoardSection bs) {
			HashSet<Integer> possible = new HashSet<Integer>();
			HashSet<Integer> reducedSet = new HashSet<Integer>(sudokuBoard[rowIndex][columnIndex].getPossibleValues());
			
			switch(bs) {
				case ROW:
					for(int c = 0; c < LENGTH; c++) {
						if(c != columnIndex && !sudokuBoard[rowIndex][c].getIsSolved()) {
								possible.addAll(sudokuBoard[rowIndex][c].getPossibleValues());
						}			
					}
					break;
				case GROUP:
					int groupIndex = getGroupIndex(rowIndex, columnIndex);
					int cIndex = 3 * (groupIndex % 3);
					int rIndex = groupIndex - (groupIndex % 3);
					for(int r = rIndex; r < (rIndex + 3); r++) {
						for(int c = cIndex; c < (cIndex + 3); c++) {				
							if((r != rowIndex || c != columnIndex) && !sudokuBoard[r][c].getIsSolved()) {
								possible.addAll(sudokuBoard[r][c].getPossibleValues());					
							}
						}
					}
					break;
				case COLUMN:
					for(int r = 0; r < LENGTH; r++) {
						if(r != rowIndex && !sudokuBoard[r][columnIndex].getIsSolved()) {
								possible.addAll(sudokuBoard[r][columnIndex].getPossibleValues());
						}			
					}
					break;
			}
			reducedSet.removeAll(possible);
			if(reducedSet.size() == 1) {
				for(int element : reducedSet) {
					if(isBruteForced) {
						forceInsert(rowIndex, columnIndex, element);
					}
					else {
						insert(rowIndex, columnIndex, element);
					}
				}
				return true;
			}			
			return false;
		}
		
		private boolean checkUnions(int rowIndex, int columnIndex) {
			if(checkUnion(rowIndex, columnIndex, BoardSection.GROUP))
				return true;		
			if(checkUnion(rowIndex, columnIndex, BoardSection.COLUMN))
				return true;
			if(checkUnion(rowIndex, columnIndex, BoardSection.ROW))
				return true;
			
			return false;
		}
		
		private boolean optimizeRows(int index) {
			int loop = 0;
			int numVals = 0;
			boolean isOptimized = false;
			
			Set<Integer> opt1, opt2;
			
			for(int n = loop; n < LENGTH; n++) {
				if(sudokuBoard[index][n].getIsSolved())
					continue;
				numVals = sudokuBoard[index][n].getNumberOfPossibleValues();
				if(numVals == 2) {
					opt1 = sudokuBoard[index][n].getPossibleValues();
					loop = n;
					for(int column = loop + 1; column < LENGTH; column++) {
						if(sudokuBoard[index][column].getIsSolved())
							continue;
						numVals = sudokuBoard[index][column].getNumberOfPossibleValues();
						if(numVals == 2) {
							opt2 = sudokuBoard[index][column].getPossibleValues();
							if(opt1.containsAll(opt2)) {
								for(int i = 0; i < LENGTH; i++) {
									if(i == loop || i == column)
										continue;
									if(!sudokuBoard[index][i].getIsSolved()) {
										if(sudokuBoard[index][i].removePossibleValues(opt1)) {
											isOptimized = true;										
										}
									}
								}
								column = LENGTH;
							}							
						}
					}				
				}
			}		
			return isOptimized;
		}
		
		private boolean optimizeColumns(int index) {
			int loop = 0;
			int numVals = 0;
			boolean isOptimized = false;
			
			Set<Integer> opt1, opt2;
			
			for(int n = loop; n < LENGTH; n++) {
				if(sudokuBoard[n][index].getIsSolved())
					continue;
				numVals = sudokuBoard[n][index].getNumberOfPossibleValues();
				if(numVals == 2) {
					opt1 = sudokuBoard[n][index].getPossibleValues();
					loop = n;
					for(int row = loop + 1; row < LENGTH; row++) {
						if(sudokuBoard[row][index].getIsSolved())
							continue;
						numVals = sudokuBoard[row][index].getNumberOfPossibleValues();
						if(numVals == 2) {
							opt2 = sudokuBoard[row][index].getPossibleValues();
							if(opt1.containsAll(opt2)) {
								for(int i = 0; i < LENGTH; i++) {
									if((i == loop) || (i == row))
										continue;
									if(!sudokuBoard[i][index].getIsSolved()) {
										if(sudokuBoard[i][index].removePossibleValues(opt1)) {
											isOptimized = true;
										}
									}
								}
								row = LENGTH;
							}							
						}
					}				
				}
			}		
			return isOptimized;
		}
	}
}

