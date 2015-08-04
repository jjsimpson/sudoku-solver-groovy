package core
/**
 * Created by jerransimpson on 7/11/15.
 *
 * Represents a single square on a Sudoku puzzle
 */
class SudokuSlot {

    //stores the final, correct value for this slot
    private int number
    //stores all possible values that could belong in this slot
    private Set<Integer> possibleValues
    //stores the index of which row this slot belongs to
    private int rowIndex
    //stores the index of which column this slot belongs to
    private int columnIndex

    /**
     * initializes a blank slot, setting the possible values to be all values that can possible fit on the board
     * @param rowIndex the index of the row this slot belongs to
     * @param columnIndex the index of the column this slot belongs to
     */
    public SudokuSlot(int rowIndex, int columnIndex, int boardSize) {
        number = 0
        possibleValues = new HashSet(1..boardSize)
        this.rowIndex = rowIndex
        this.columnIndex = columnIndex
    }

    /**
     * initializes a slot with a value, setting the possible values to an empty list
     * @param value the value to initialize this slot to
     * @param rowIndex the index of the row this slot belongs to
     * @param columnIndex the index of the column this slot belongs to
     */
    public SudokuSlot(int value, int rowIndex, int columnIndex, int boardSize) {
        number = value
        this.rowIndex = rowIndex
        this.columnIndex = columnIndex
        //if the slot has a non-zero value, set the list of possible values to an empty list
        if(value != 0) {
            possibleValues = new HashSet<Integer>()
        } else {
            //if the slot has a value of zero this indicates the slot is empty and can possibly be any value
            possibleValues = new HashSet(1..boardSize)
        }
    }

    /**
     * sets a value in the slot, removing all possible values from the possibleValues list
     * @param value the value to assign to the slot
     */
    public void setValue(int value) {
        number = value
        if(value != 0) {
            possibleValues = new HashSet<Integer>()
        }
    }

    /**
     * retrieves the index of the row this slot belongs to
     * @return index of this slot's row
     */
    public int getRowIndex() {
        return rowIndex
    }

    /**
     * retrieves the index of the column this slot belongs to
     * @return index of this slot's column
     */
    public int getColumnIndex() {
        return columnIndex
    }

    /**
     * Removes a single value from the list of possible values for this slot
     * @param value the value to be removed from the possible values list
     */
    public void removePossibleValue(int value) {
        possibleValues.remove(value)
    }

    /**
     * Returns the entire Set of possible values
     * @return Set<Integer> of possible values that can belong to this slot
     */
    public Set<Integer> getPossibleValues() {
        return possibleValues
    }

    /**
     * Returns the value of this slot (if no value is assigned, values default to 0 if no value was set)
     * @return
     */
    public int getValue() {
        return number
    }

    /**
     * Determines if a value is set in this slot
     * @return true if a value other than the default 0 is set as the slot value
     */
    public boolean hasValue() {
        return value != 0
    }

    /**
     *
     * @return A String representation of the slot's value
     */
    @Override
    public String toString() {
        return "${number}"
    }

}
