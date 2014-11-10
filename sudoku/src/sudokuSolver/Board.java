package sudokuSolver;
import java.util.*;
import sudokuSolver.Cell;

public class Board {	
	Cell[][] sudokuBoard;
	Stack<ThreeTuple<Integer, Integer, Integer>> solveStack; //Key, old value, new value
	List<HashSet<Integer>> row;
	List<HashSet<Integer>> column;
	List<HashSet<Integer>> group;
	final Set<Integer> solved = new HashSet<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
	private boolean isBruteForced = false;
	private static final int BOARD_LENGTH = 9;
	
	public void makeBoard(int[][] sourceArray) {
		int groupIndex = 0;
		for(int i = 0; i < 9; i++) {
			groupIndex = (i / 3) * 3; //Integer Division			
			for(int j = 0; j < 9; j++) {
				if(j == 3)
					groupIndex++;
				if(j == 6)
					groupIndex++;
				sudokuBoard[i][j] = new Cell(i, j, groupIndex, 0);
				sudokuBoard[i][j].setInitialValue(sourceArray[i][j]);
				if(sourceArray[i][j] != 0) {
					row.get(i).add(sourceArray[i][j]);
					column.get(j).add(sourceArray[i][j]);
					group.get(groupIndex).add(sourceArray[i][j]);
				}								
			}			
		}
		generateCellLists();
	}
	
	public Board() {
		sudokuBoard = new Cell[9][9];	
		solveStack = new Stack<ThreeTuple<Integer, Integer, Integer>>();
		row = new ArrayList<HashSet<Integer>>();
		column = new ArrayList<HashSet<Integer>>();
		group = new ArrayList<HashSet<Integer>>();
		for(int i = 0; i < sudokuBoard.length; i++) {
			row.add(new HashSet<Integer>());
			column.add(new HashSet<Integer>());
			group.add(new HashSet<Integer>());
		}
	}	
	
	public void generateCellLists() {
		Set<Integer> union = new HashSet<Integer>();
		Set<Integer> possible = new HashSet<Integer>(solved);
		int groupIndex = 0;
		for(int i = 0; i < sudokuBoard.length; i++) {
			groupIndex = (i / 3) * 3;
			for(int j = 0; j < sudokuBoard.length; j++) {
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
	
	public boolean checkCanOptimize(int index, BoardGroup bg) {
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
	
	public boolean checkSolved(int index, BoardGroup bg) {
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
	
	private void updateCellLists(int rowIndex, int columnIndex, int groupIndex) {
		Set<Integer> union = new HashSet<Integer>();
		Set<Integer> possible = new HashSet<Integer>(solved);
		int index = 0, baseRowIndex = 0, baseColumnIndex = 0;
		for(int i = 0; i < sudokuBoard.length; i++) {
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
	}
	
	public void printBoard() {
		for(int i = 0; i < sudokuBoard.length; i++) {
			for(int j = 0; j < sudokuBoard.length; j++) {
				System.out.print(sudokuBoard[i][j].getValue());
				System.out.print(" ");
			}
			System.out.println();
		}		
	}
	
	public void printPossibleValues() {
		for(int i = 0; i < sudokuBoard.length; i++) {
			for(int j = 0; j < sudokuBoard.length; j++) {
				if(!sudokuBoard[i][j].getIsSolved()) {
					System.out.println("Cell: " + i + j);
					System.out.println(sudokuBoard[i][j].getPossibleValues());
				}				
			}
		}	
	}
	
	public void printGroups() {
		for(int i = 0; i < sudokuBoard.length; i++) {
			System.out.println(group.get(i));
		}
	}
	
	public boolean checkSolution() {
		for(int i = 0; i < sudokuBoard.length; i++) {
			for(int j = 0; j < sudokuBoard.length; j++) {
				if(!sudokuBoard[i][j].getIsSolved())
					return false;									
			}
		}
		if(!verifySolution())
			return false;
		return true;
	}
	
	private boolean verifySolution() {
		for(int i = 0; i < sudokuBoard.length; i++) {
			if(!row.get(i).containsAll(solved))
				return false;
			if(!column.get(i).containsAll(solved))
				return false;
			if(!group.get(i).containsAll(solved))
				return false;
		}
		return true;
	}	
	
	public boolean insert(int rowIndex, int columnIndex, int value) {
		boolean result = sudokuBoard[rowIndex][columnIndex].insert(value);
		if(result)
			updatePossibleValues(rowIndex, columnIndex, value, true);
		return result;				
	}
	
	public boolean forceInsert(int rowIndex, int columnIndex, int value) {
		int key = 10 * rowIndex + columnIndex;
		solveStack.push(new ThreeTuple<Integer, Integer, Integer>(key, getValue(rowIndex, columnIndex), value));
		return insert(rowIndex, columnIndex, value);		
	}
	
	public int getValue(int rowIndex, int columnIndex) {
		return sudokuBoard[rowIndex][columnIndex].getValue();
	}
	
	public boolean forceDelete(int rowIndex, int columnIndex, int value) {
		ThreeTuple<Integer, Integer, Integer> failed = solveStack.pop();
		TwoTuple<Integer, Integer> index = getIndexFromKey(failed.first);
		delete(index.first, index.second);
		if(sudokuBoard[index.first][index.second].removePossibleValue(failed.third)) {
			Set<Integer> x = sudokuBoard[index.first][index.second].getPossibleValues();
			if(x.size() == 1) {
				for(int element : x)
					insert(index.first, index.second, element);
				return true;
			}				
		}		
		return false;
	}
	
	public TwoTuple<Integer, Integer> getIndexFromKey(int key) {
		int row = key / 10;
		int column = key % 10;
		return new TwoTuple<Integer, Integer>(row, column);
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
	
	public void testSolver() {
		Set<Integer> x;
		boolean b = true;
		out:
		while(true) {
			for(int i = 0; i < sudokuBoard.length; i++) {				
				if(checkSolved(i, BoardGroup.ROW))
					continue;
				for(int j = 0; j < sudokuBoard.length; j++) {
					if(checkSolved(j, BoardGroup.COLUMN))
						continue;
					//System.out.println("Checking " + i + j);
					if(sudokuBoard[i][j].getCanModify() && !sudokuBoard[i][j].getIsSolved()) {
						x = sudokuBoard[i][j].getPossibleValues();
						if(x.size() == 1) {
							for(int y : x) {
								this.insert(i, j, y);
								continue out; 
							}
						}
						if(checkUnions(i, j)) 
							continue out;					
					}					
				}				
			}			
			for(int z = 0; z < BOARD_LENGTH; z++) {
				if(checkCanOptimize(z, BoardGroup.ROW)) {
					if(optimizeRows(z))
						continue out;
				}
				if(checkCanOptimize(z, BoardGroup.COLUMN)) {
					if(optimizeColumns(z))
						continue out;
				}
			}
			b = checkSolution();
			System.out.println("Check solution " + b);
			if(b) 
				return;
			isBruteForced = true;
			if(forceSolveCell())
				continue out;			
			break;
		}
	}
	
	private boolean forceSolveCell() {
		return false;
	}
	
	private boolean checkUnions(int rowIndex, int columnIndex) {
		int groupIndex = getGroupIndex(rowIndex, columnIndex);
		if(checkGroupUnion(rowIndex, columnIndex, groupIndex))
			return true;		
		if(checkRowUnion(rowIndex, columnIndex))
			return true;
		if(checkColumnUnion(rowIndex, columnIndex))
			return true;
		
		return false;
	}
	
	public int getGroupIndex(int r, int c) {
		return (r / 3) * 3 + c / 3;
	}
	
	private boolean checkGroupUnion(int rowIndex, int columnIndex, int groupIndex) {
		HashSet<Integer> possible = new HashSet<Integer>();
		HashSet<Integer> reducedSet = new HashSet<Integer>(sudokuBoard[rowIndex][columnIndex].getPossibleValues());
		int cIndex = 3 * (groupIndex % 3);
		int rIndex = groupIndex - (groupIndex % 3);
		for(int r = rIndex; r < (rIndex + 3); r++) {
			for(int c = cIndex; c < (cIndex + 3); c++) {				
				if((r != rowIndex || c != columnIndex) && sudokuBoard[r][c].getCanModify() && !sudokuBoard[r][c].getIsSolved()) {
					possible.addAll(sudokuBoard[r][c].getPossibleValues());					
				}
			}
		}
		reducedSet.removeAll(possible);
		if(reducedSet.size() == 1) {
			for(int element : reducedSet) {
				//System.out.println("GUnion" + rowIndex + columnIndex);
				//System.out.println(reducedSet);
				insert(rowIndex, columnIndex, element);
			}
			return true;
		}			
		return false;
	}
	
	private boolean checkRowUnion(int rowIndex, int columnIndex) {
		HashSet<Integer> possible = new HashSet<Integer>();
		HashSet<Integer> reducedSet = new HashSet<Integer>(sudokuBoard[rowIndex][columnIndex].getPossibleValues());
		
		for(int c = 0; c < sudokuBoard.length; c++) {
			if(c != columnIndex && sudokuBoard[rowIndex][c].getCanModify() && !sudokuBoard[rowIndex][c].getIsSolved()) {
					possible.addAll(sudokuBoard[rowIndex][c].getPossibleValues());
			}			
		}
		reducedSet.removeAll(possible);
		if(reducedSet.size() == 1) {
			for(int element : reducedSet) {
				//System.out.println("RUnion" + rowIndex + columnIndex);
				//System.out.println(reducedSet);
				insert(rowIndex, columnIndex, element);
			}
			return true;
		}			
		return false;
	}
	
	private boolean checkColumnUnion(int rowIndex, int columnIndex) {
		HashSet<Integer> possible = new HashSet<Integer>();
		HashSet<Integer> reducedSet = new HashSet<Integer>(sudokuBoard[rowIndex][columnIndex].getPossibleValues());
		
		for(int r = 0; r < sudokuBoard.length; r++) {
			if(r != rowIndex && sudokuBoard[r][columnIndex].getCanModify() && !sudokuBoard[r][columnIndex].getIsSolved()) {
					possible.addAll(sudokuBoard[r][columnIndex].getPossibleValues());
			}			
		}
		reducedSet.removeAll(possible);
		if(reducedSet.size() == 1) {
			for(int element : reducedSet) {
				//System.out.println("CUnion" + rowIndex + columnIndex);
				//System.out.println(reducedSet);
				//if(isBruteForced)
				//	forceInsert
				insert(rowIndex, columnIndex, element);
			}
			return true;
		}
		//checkColumnOptimizations(index);
		return false;
	}
	
	private boolean optimizeColumns(int index) {
		int loop = 0;
		int numVals = 0;
		boolean isOptimized = false;
		
		Set<Integer> opt1, opt2;
		
		for(int n = loop; n < BOARD_LENGTH; n++) {
			if(sudokuBoard[n][index].getIsSolved())
				continue;
			numVals = sudokuBoard[n][index].getNumberOfPossibleValues();
			if(numVals == 2) {
				opt1 = sudokuBoard[n][index].getPossibleValues();
				loop = n;
				for(int i = loop + 1; i < BOARD_LENGTH; i++) {
					if(sudokuBoard[i][index].getIsSolved())
						continue;
					numVals = sudokuBoard[i][index].getNumberOfPossibleValues();
					if(numVals == 2) {
						opt2 = sudokuBoard[i][index].getPossibleValues();
						if(opt1.containsAll(opt2)) {
							for(int j = 0; j < BOARD_LENGTH; j++) {
								if((j == loop) || (j == i))
									continue;
								if(!sudokuBoard[j][index].getIsSolved()) {
									if(sudokuBoard[j][index].removePossibleValues(opt1)) {
										isOptimized = true;
									}
								}
							}
							i = BOARD_LENGTH;
						}							
					}
				}				
			}
		}		
		return isOptimized;
	}
	
	private boolean optimizeRows(int index) {
		int loop = 0;
		int numVals = 0;
		boolean isOptimized = false;
		
		Set<Integer> opt1, opt2;
		
		for(int n = loop; n < BOARD_LENGTH; n++) {
			if(sudokuBoard[index][n].getIsSolved())
				continue;
			numVals = sudokuBoard[index][n].getNumberOfPossibleValues();
			if(numVals == 2) {
				opt1 = sudokuBoard[index][n].getPossibleValues();
				System.out.println("opt1: " + opt1 + " " + n);
				loop = n;
				for(int i = loop + 1; i < BOARD_LENGTH; i++) {
					if(sudokuBoard[index][i].getIsSolved())
						continue;
					numVals = sudokuBoard[index][i].getNumberOfPossibleValues();
					if(numVals == 2) {
						opt2 = sudokuBoard[index][i].getPossibleValues();
						System.out.println("opt2: " + opt2);
						if(opt1.containsAll(opt2)) {
							System.out.println("At: " + index + i);
							for(int j = 0; j < BOARD_LENGTH; j++) {
								if(j == loop || j == i)
									continue;
								if(!sudokuBoard[index][j].getIsSolved()) {
									if(sudokuBoard[index][j].removePossibleValues(opt1)) {
										System.out.println(opt1 + " at " + index + j);
										isOptimized = true;										
									}
								}
							}
							i = BOARD_LENGTH;
						}							
					}
				}				
			}
		}		
		return isOptimized;
	}
	
	
}
