package sudokuSolver;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class Cell {
	private final int rowIndex;
	private final int columnIndex;
	private final int groupIndex;
	private int value;
	private boolean canModify = true;
	private boolean isSolved = false;
	private boolean hasSolution = true;
	private Set<Integer> possibleValues;
	private int key;
		
	public void setPossibleValues(Set<Integer> values) {
		this.possibleValues = new HashSet<Integer>(values); 
		if(possibleValues.isEmpty())
			hasSolution = false;
	}
	
	public Set<Integer> getPossibleValues() {
		return new HashSet<Integer>(possibleValues);
	}
	
	public int getNumberOfPossibleValues() {
		return possibleValues.size();
	}
	
	public boolean removePossibleValue(int value) {
		if(possibleValues.remove(value))
			return true;
		if(possibleValues.isEmpty())
			hasSolution = false;
		return false;
	}
	
	public boolean removePossibleValues(Set<Integer> value) {
		if(possibleValues.removeAll(value))
			return true;
		if(possibleValues.isEmpty())
			hasSolution = false;
		return false;		
	}
	
	public boolean canSolve() {
		return hasSolution;
	}
	
	public void setInitialValue(int value) {
		if(value != 0) {
			canModify = false;
			isSolved = true;
			hasSolution = true;
			possibleValues.clear();
		}
		this.value = value;		
	}
	
	public void setValue(int value) {
		this.value = value;
		if(value != 0)
			isSolved = true;
	}
	
	public int getValue() { 
		return value; 
	}
	
	public void setCanModify() {
		canModify = false;
	}
	
	public boolean getCanModify() {
		return canModify;
	}
	
	public boolean getIsSolved() {
		return isSolved;
	}
	
	public boolean insert(int value) {
		//System.out.println("Insert " + value + " at " + rowIndex + columnIndex);
		if(canModify) {
			if(possibleValues.contains(value)) {
				this.value = value;
				isSolved = true;
			}
			else {
				this.value = value;
				isSolved = true;
				hasSolution = false;
				//isSolved = false;
				//notifyInccorrect();
			}
			return true;  //Inserted, but not necessarily correct				
		}
		return false;
	}
	
	public int delete() {
		if(canModify) {
			int result = value;
			value = 0;
			isSolved = false;
			hasSolution = true;
			return result;
		}
		return 0;
	}
		
	protected Cell(int rowIndex, int columnIndex, int groupIndex, int value) {
		this.columnIndex = columnIndex;
		this.rowIndex = rowIndex;
		this.groupIndex = groupIndex;
		if(value > 0 && value <= 9)
			this.value = value;
		else
			value = 0;
		key = 10 * rowIndex + columnIndex;
		this.possibleValues = new HashSet<Integer>();
	}		
}

