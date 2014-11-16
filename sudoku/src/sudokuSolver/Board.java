package sudokuSolver;
import java.util.*;
import sudokuSolver.Cell;
import util.*;

public class Board {	
	Cell[][] sudokuBoard;
	List<HashSet<Integer>> row;
	List<HashSet<Integer>> column;
	List<HashSet<Integer>> group;
	final Set<Integer> solved = new HashSet<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
	private static final int LENGTH = 9;
	
	public void makeBoard(int[][] sourceArray) {
		int groupIndex = 0;
		int value = 0;
		for(int i = 0; i < 9; i++) {
			groupIndex = (i / 3) * 3; //Integer Division			
			for(int j = 0; j < 9; j++) {
				if(j == 3)
					groupIndex++;
				if(j == 6)
					groupIndex++;
				sudokuBoard[i][j] = new Cell(i, j, groupIndex, 0);
				value = sourceArray[i][j];
				sudokuBoard[i][j].setInitialValue(value);
				if(value != 0) {
					row.get(i).add(value);
					column.get(j).add(value);
					group.get(groupIndex).add(value);
				}								
			}			
		}
		generateCellLists();
	}
	
	public Board() {
		sudokuBoard = new Cell[9][9];	
		row = new ArrayList<HashSet<Integer>>();
		column = new ArrayList<HashSet<Integer>>();
		group = new ArrayList<HashSet<Integer>>();
		for(int i = 0; i < LENGTH; i++) {
			row.add(new HashSet<Integer>());
			column.add(new HashSet<Integer>());
			group.add(new HashSet<Integer>());
		}
	}	
	
	public void generateCellLists() {
		Set<Integer> union = new HashSet<Integer>();
		Set<Integer> possible = new HashSet<Integer>(solved);
		int groupIndex = 0;
		for(int i = 0; i < LENGTH; i++) {
			groupIndex = (i / 3) * 3;
			for(int j = 0; j < LENGTH; j++) {
				if(j == 3) 
					groupIndex++;
				if(j == 6)
					groupIndex++;
				if(sudokuBoard[i][j].getCanModify()) {  //If not set by generator
					union.addAll(row.get(i));
					union.addAll(column.get(j));
					union.addAll(group.get(groupIndex));
					possible.removeAll(union);
				
					sudokuBoard[i][j].setPossibleValues(possible);
					possible.addAll(solved);
					union.removeAll(solved);
				}
			}						
		}		
	}
	
	
	
	private void updateCellLists(int rowIndex, int columnIndex, int groupIndex) {
		Set<Integer> union = new HashSet<Integer>();
		Set<Integer> possible = new HashSet<Integer>(solved);
		int index = 0, baseRowIndex = 0, baseColumnIndex = 0;
		for(int i = 0; i < LENGTH; i++) {
			index = (rowIndex / 3) * 3 + i / 3;
			if(sudokuBoard[rowIndex][i].getCanModify()) {  //If not set by generator
				union.addAll(row.get(rowIndex));
				union.addAll(column.get(i));
				union.addAll(group.get(index));
				possible.removeAll(union);
			
				sudokuBoard[rowIndex][i].setPossibleValues(possible);
				possible.addAll(solved);
				union.removeAll(solved);
			}
			index = (i / 3) * 3 + columnIndex / 3;
			if(sudokuBoard[i][columnIndex].getCanModify()) {  //If not set by generator
				union.addAll(row.get(i));
				union.addAll(column.get(columnIndex));
				union.addAll(group.get(index));
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
					union.addAll(row.get(x));
					union.addAll(column.get(y));
					union.addAll(group.get(groupIndex));
					possible.removeAll(union);
				
					sudokuBoard[x][y].setPossibleValues(possible);
					possible.addAll(solved);
					union.removeAll(solved);
				}					
			}
		}
	}
	
	public void printBoard() {
		for(int i = 0; i < LENGTH; i++) {
			for(int j = 0; j < LENGTH; j++) {
				System.out.print(sudokuBoard[i][j].getValue());
				System.out.print(" ");
			}
			System.out.println();
		}		
	}
	
	public void printPossibleValues() {
		for(int i = 0; i < LENGTH; i++) {
			for(int j = 0; j < LENGTH; j++) {
				if(!sudokuBoard[i][j].getIsSolved()) {
					System.out.println("Cell: " + i + j);
					System.out.println(sudokuBoard[i][j].getPossibleValues());
				}				
			}
		}	
	}
	
	public void printSolveableCells() {
		for(int i = 0; i < LENGTH; i++) {
			for(int j = 0; j < LENGTH; j++) {
				if(sudokuBoard[i][j].canSolve()) {
					System.out.println("Cell: " + i + j);
					System.out.println(sudokuBoard[i][j].getPossibleValues());
				}				
			}
		}
	}
	
	public void printGroups() {
		for(int i = 0; i < LENGTH; i++) {
			System.out.println(group.get(i));
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
		int groupIndex = (rowIndex / 3) * 3 + columnIndex / 3;
		if(isInsertion) {
			row.get(rowIndex).add(value);
			column.get(columnIndex).add(value);
			group.get(groupIndex).add(value);
		}
		else {
			row.get(rowIndex).remove(value);
			column.get(columnIndex).remove(value);
			group.get(groupIndex).remove(value);
		}
		updateCellLists(rowIndex, columnIndex, groupIndex);		
	}
	
	public boolean delete(int rowIndex, int columnIndex) {
		int result = sudokuBoard[rowIndex][columnIndex].delete();
		if(result != 0) 
			updatePossibleValues(rowIndex, columnIndex, result, false);
		return result != 0;		
	}	
	
	public int getGroupIndex(int r, int c) {
		return (r / 3) * 3 + c / 3;
	}
	
	public boolean solve() {
		Solver solver = new Solver();
		return solver.solve();
	}
	
	private class Solver {
		util.Stack<LinkedList<StackNode>> solveStack = new util.Stack<LinkedList<StackNode>>(); //Key, old value, new value
		LinkedList<StackNode> values = new LinkedList<StackNode>();
		private boolean isBruteForced = false;
		private class StackNode {
			public final int oldValue;
			public final int newValue;
			public final int rowIndex;
			public final int columnIndex;
						
			public StackNode(int oldValue, int newValue, int rowIndex, int columnIndex) {
				this.oldValue = oldValue;
				this.newValue = newValue;
				this.columnIndex = columnIndex;
				this.rowIndex = rowIndex;				
			}		
		}
		
		private boolean checkCanSolve() {
			for(int i = 0; i < LENGTH; i++) {
				for(int j = 0; j < LENGTH; j++) {
					if(!sudokuBoard[i][j].canSolve()) {
						return false;
					}
				}
			}
			return true;			
		}
		
		public boolean checkSolution() {
			for(int i = 0; i < LENGTH; i++) {
				for(int j = 0; j < LENGTH; j++) {
					if(!sudokuBoard[i][j].getIsSolved())
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
					System.out.println("He's dead Jim!");
					System.exit(1);
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
			for(int i = helper.newValue + 1; i <= LENGTH; i++) {
				if(possible.contains(i)) {
					forceInsert(helper.rowIndex, helper.columnIndex, i);					
					return true;
				}					
			}
			//We can try no new value
			return false;
		}
		
		private boolean verifySolution() {
			for(int i = 0; i < LENGTH; i++) {
				if(!row.get(i).containsAll(solved))
					return false;
				if(!column.get(i).containsAll(solved))
					return false;
				if(!group.get(i).containsAll(solved))
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
					System.out.println("Brute Forcing this mofo");
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
				
			for(int i = 0; i < LENGTH; i++) {
				for(int j = 0; j < LENGTH; j++) {
					if(!sudokuBoard[i][j].getIsSolved()) {
						rIndex = i;
						cIndex = j;
						i = 10;
						j = 10;
					}						
				}
			}
			if(rIndex != -1 && cIndex != -1) {
				possible = sudokuBoard[rIndex][cIndex].getPossibleValues();
				for(int x = 1; x <= LENGTH; x++) {
					if(possible.contains(x)) {
						forceInsert(rIndex, cIndex, x);
						return true;
					}
				}
			}
			return false;			
		}
		
		private void simpleSolve() {
			Set<Integer> x;
			
			out:
			while(true) {
				for(int i = 0; i < LENGTH; i++) {				
					if(checkSolved(i, BoardSection.ROW))
						continue;
					for(int j = 0; j < LENGTH; j++) {
						if(checkSolved(j, BoardSection.COLUMN))
							continue;
						if(!sudokuBoard[i][j].getIsSolved()) {
							x = sudokuBoard[i][j].getPossibleValues();
							if(x.size() == 1) {
								for(int y : x) {
									if(isBruteForced) {
										forceInsert(i, j, y);
										continue out;
									}
									else {
										insert(i, j, y);
										continue out;
									}
								}
							}
							if(checkUnions(i, j)) 
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
			StackNode stackNode = new StackNode(sudokuBoard[rowIndex][columnIndex].getValue(), value, rowIndex, columnIndex);
			values.addFirst(stackNode);
			
			return insert(rowIndex, columnIndex, value);			
		}			
		
		public boolean checkCanOptimize(int index, BoardSection bg) {
			switch(bg) {
				case ROW:
					if(row.get(index).containsAll(solved))
						return false;
					if(row.get(index).size() >= 7)
						return false;
					break;
				case COLUMN:
					if(column.get(index).containsAll(solved))
						return false;
					if(column.get(index).size() >= 7)
						return false;
					break;
				case GROUP:
					if(group.get(index).containsAll(solved))
						return false;
					if(group.get(index).size() >= 7)
						return false;
					break;
				default:
					return true;
			}
			return true;
		}
		
		public boolean checkSolved(int index, BoardSection bg) {
			switch(bg) {
				case ROW:
					if(row.get(index).containsAll(solved))
						return true;				
					break;
				case COLUMN:
					if(column.get(index).containsAll(solved))
						return true;
					break;
				case GROUP:
					if(group.get(index).containsAll(solved))
						return true;				
					break;
				default:
					return false;
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
					for(int i = loop + 1; i < LENGTH; i++) {
						if(sudokuBoard[index][i].getIsSolved())
							continue;
						numVals = sudokuBoard[index][i].getNumberOfPossibleValues();
						if(numVals == 2) {
							opt2 = sudokuBoard[index][i].getPossibleValues();
							if(opt1.containsAll(opt2)) {
								for(int j = 0; j < LENGTH; j++) {
									if(j == loop || j == i)
										continue;
									if(!sudokuBoard[index][j].getIsSolved()) {
										if(sudokuBoard[index][j].removePossibleValues(opt1)) {
											isOptimized = true;										
										}
									}
								}
								i = LENGTH;
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
					for(int i = loop + 1; i < LENGTH; i++) {
						if(sudokuBoard[i][index].getIsSolved())
							continue;
						numVals = sudokuBoard[i][index].getNumberOfPossibleValues();
						if(numVals == 2) {
							opt2 = sudokuBoard[i][index].getPossibleValues();
							if(opt1.containsAll(opt2)) {
								for(int j = 0; j < LENGTH; j++) {
									if((j == loop) || (j == i))
										continue;
									if(!sudokuBoard[j][index].getIsSolved()) {
										if(sudokuBoard[j][index].removePossibleValues(opt1)) {
											isOptimized = true;
										}
									}
								}
								i = LENGTH;
							}							
						}
					}				
				}
			}		
			return isOptimized;
		}
	}
}

