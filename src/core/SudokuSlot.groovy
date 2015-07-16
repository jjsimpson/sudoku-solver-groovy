package core
/**
 * Created by jerransimpson on 7/11/15.
 */
class SudokuSlot {

    private int number
    private Set<Integer> possibleValues

    public SudokuSlot() {
        number = 0
        possibleValues = new HashSet(1..SudokuBoard.boardSize)
    }

    public SudokuSlot(int value) {
        number = value
        possibleValues = new ArrayList<Integer>()
    }

    public void setValue(int value) {
        number = value
        possibleValues = new ArrayList<Integer>()
    }

    public void removePossibleValue(int value) {
        possibleValues.remove(value)
    }

    public Set<Integer> getPossibleValues() {
        return possibleValues
    }

    public int getValue() {
        return number
    }

    public boolean hasValue() {
        return value != 0
    }

    @Override
    public String toString() {
        return "${number}"
    }

}
