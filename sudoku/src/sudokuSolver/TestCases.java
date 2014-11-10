package sudokuSolver;

import java.util.*;

public class TestCases {
	class Cell {
		private final int rowIndex;
		private final int columnIndex;
		private int value;
		private boolean isSetByGenerator;
		private List<Integer> possibleValues;
		private int key;
		
		public void setValue(int value) { 
			this.value = value; 
		}
		
		public int getValue() { return value; }
		
		private Cell(int rowIndex, int columnIndex, int value) {
			this.columnIndex = columnIndex;
			this.rowIndex = rowIndex;
			if(value > 0 && value <= 9)
				this.value = value;
			else
				value = 0;
			key = 10 * rowIndex + columnIndex;
		}
		
		public void makeSudokuBoard(int[][] array) {
			
			for(int i = 0; i < 9; i++) {
				for(int j = 0; j < 9; i++) {
					Cell e = new Cell(i, j, array[i][j]);
					board.put(e.key, e);
					rows[i].add(e);
					columns[j].add(e);
					if(i < 3) {
						if(j < 3)
							groups[0].add(e);
						else if(j < 6)
							groups[1].add(e);
						else
							groups[2].add(e);
					}
					else if(i < 6) {
						if(j < 3)
							groups[3].add(e);
						else if(j < 6)
							groups[4].add(e);
						else
							groups[5].add(e);
					}
					else {
						if(j < 3)
							groups[6].add(e);
						else if(j < 6)
							groups[7].add(e);
						else
							groups[8].add(e);
					}
				}
			}			
		}
	}
	static Map<Integer, Cell> board;
	static ArrayList<Cell>[] rows;
	static ArrayList<Cell>[] columns;
	static ArrayList<Cell>[] groups;
	
	static boolean checkRow(int value, int index) {
		if(rows[index].contains(value))
			return true;		
		return false;
	}
	
	static boolean checkColumn(int value, int index) {
		if(columns[index].contains(value))
				return true;		
		return false;
	}
		
	static boolean checkGroup(int value, int index) {
		if(columns[index].contains(value))
			return true;
		return false;
	}
	
	public static void main(String[] args) {
		rows[0] = new ArrayList<Cell>();
		
	}

}
