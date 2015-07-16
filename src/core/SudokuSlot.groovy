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

    /**
     * initializes a blank slot, setting the possible values to be all values that can possible fit on the board
     */
    public SudokuSlot() {
        number = 0
        possibleValues = new HashSet(1..SudokuBoard.boardSize)
    }

    /**
     * sets a value in the slot, removing all possible values from the possibleValues list
     * @param value the value to assign to the slot
     */
    public void setValue(int value) {
        number = value
        if(value != 0) {
            possibleValues = new ArrayList<Integer>()
        }
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
