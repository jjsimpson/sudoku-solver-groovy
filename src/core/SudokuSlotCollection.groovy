package core
/**
 * Created by jerransimpson on 7/15/15.
 *
 * Represents some sort of collection of squares on a Sudoku board.
 * 3 main examples are a Row, a Column, and a Block on a Sudoku board (A block on a standard Sudoku board is a square containing the unique values of 1-9).
 */
class SudokuSlotCollection {

    //The set of sudoku slots that belong in the collection, these values are not sorted because the sorting is so far unnecessary
    private Set<SudokuSlot> squares = new HashSet<SudokuSlot>()
    //Indicates if all slots in the collection have a value other than 0
    private boolean isComplete = false

    /**
     * Returns the Set of SudokuSlots
     * @return Set<SudokuSlot> of all slots belonging to this collection
     */
    public Set<SudokuSlot> getSlots() {
        return squares
    }

    /**
     * Adds a new slot to the collection
     * @param slot the slot to add to the set of slots
     */
    public void addSlot(SudokuSlot slot) {
        squares.add(slot)
    }

    /**
     * Calculates if each slot in the collection has a value or not and returns result
     * @return true if all slots in the collection have a value other than 0, otherwise returns false
     */
    public boolean isComplete() {
        if(isComplete) {
            //do not recalculate isComplete if the collection was already determined to be complete
            return isComplete
        } else {
            boolean hasValue = true
            //loop through all slots in the collection, if one is found without a value, set isComplete to false.
            squares.each{ slot ->
                if(!slot.hasValue()) {
                    hasValue = false
                }
            }
            isComplete = hasValue
        }
    }
}
