package core
/**
 * Created by jerransimpson on 7/15/15.
 */
class SudokuSlotCollection {

    private Set<SudokuSlot> squares = new HashSet<SudokuSlot>()
    private boolean isComplete = false

    public Set<SudokuSlot> getSlots() {
        return squares
    }

    public void addSlot(SudokuSlot slot) {
        squares.add(slot)
    }

    public boolean isComplete() {
        if(isComplete) {
            return isComplete
        } else {
            boolean hasValue = true
            squares.each{ slot ->
                if(!slot.hasValue()) {
                    hasValue = false
                }
            }
            isComplete = hasValue
        }
    }
}
