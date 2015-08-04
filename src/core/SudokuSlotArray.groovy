package core
/**
 * Created by jerransimpson on 7/15/15.
 *
 * Represents either a row or column of squares on a Sudoku board.
 */
class SudokuSlotArray {

    //The set of sudoku slots that belong in the collection
    private SudokuSlot[] squares
    //Indicates if all slots in the collection have a value other than 0
    private boolean isComplete = false
    //Tracks how many times a possible value occurs within the collection in order to narrow down values that only occur once
    private Map<Integer,Integer> possibleValueOccurrences = new HashMap<Integer,Integer>()

    //Initializes the collection with a size (should equal the length of one side of the board)
    public SudokuSlotArray(int length) {
        squares = new SudokuSlot[length]
    }

    /**
     * Returns the Set of SudokuSlots
     * @return SudokuSlot[] of all slots belonging to this collection
     */
    public SudokuSlot[] getSlots() {
        return squares
    }

    /**
     * Adds a new slot to the collection
     * @param slot the slot to add to the set of slots
     */
    public void addSlot(SudokuSlot slot, int index) {
        if(squares[index] == null) {
            squares[index] = slot
        }
    }

    /**
     * calculates the initial possible values for each slot within the row or column
     */
    public void calculatePossibleValues() {
        //iterate over all slots in the collection
        squares.each{ slot ->
            //iterate over all slots again
            squares.each { slot2 ->
                //compare all slots to each other and if a slot has a value, remove it from the other slot's possible values list
                slot2.removePossibleValue(slot.value)
            }
        }


    }

    /**
     * Recalculate all possible values and possible value counts for all slots in the collection, every time a slot is assigned a value
     * @param assignedValue the value assigned to a slot
     * @param previousPossibleValues the list of possible values that could've belonged to the slot before it was assigned a value
     */
    public void recalculatePossibleValues(int assignedValue, Set<Integer> previousPossibleValues) {
        //iterate over all slots in the collection
        squares.each {slot ->
            //remove the value that was assigned to a slot from the possible values list of all slots in the collection
            slot.removePossibleValue(assignedValue)
        }
//        calculatePossibleValueOccurrences()
        //since the value has now been assigned to a slot, remove that value from the map of how many times it occurs in the collection
        possibleValueOccurrences.remove(assignedValue)
        previousPossibleValues.each { possibleValue ->
            //for all values that could've belonged to the slot that now has a value, decrement how many times that value occurs in the collection
            decrementPossibleValueCount(possibleValue)
        }
    }

    /**
     * initial calculation of how many times each possible value occurs within the collection
     */
    public void calculatePossibleValueOccurrences() {
        possibleValueOccurrences = new HashMap<Integer,Integer>()
        //iterate over all slots in the collection
        squares.each { slot ->
            //iterate over all possible values for the slot
            slot.possibleValues.each { value ->
                //get the count of how many times that value occurs in the collection
                def valueCount = possibleValueOccurrences.get(value)
                //if this value exists in the map
                if(valueCount) {
                    //increment its count by 1
                    possibleValueOccurrences.put(value, (valueCount+1))
                }  else {
                    //otherwise add a new entry for that value to the map, initialize its count at 1
                    possibleValueOccurrences.put(value, 1)
                }
            }
        }
    }

    /**
     * returns the map of each possible value in the collection and how many times that possible value occurs
     * @return map of possible value to the count of how many times it occurs in the collection
     */
    public Map<Integer,Integer> getPossibleValueOccurrences() {
        return possibleValueOccurrences
    }

    /**
     * Performs all of the logic of assigning values to each slot within the row or column, based upon the values of other slots in the collection
     * @return Map of the slots that were assigned values, to the possible values that could've belonged to that slot before it was assigned a value
     */
    public Map<SudokuSlot,Set<Integer>> assignCorrectValues() {
        Map<SudokuSlot,Set<Integer>> modifiedSlots = new HashMap<SudokuSlot,Set<Integer>>()
        //iterate over all slots in the collection
        squares.each{slot ->
            //if the slot only has one possible value
            if(slot.possibleValues.size() == 1) {
                //add that slot to the list of slots we assigned a value to
                modifiedSlots.put(slot,slot.possibleValues.clone())
                //assign the value to the slot because it is the only value that can belong to that slot
                slot.setValue(slot.possibleValues.iterator().next())
            }
        }
        //iterate over all possible values in the collection
        possibleValueOccurrences.keySet().each{ key ->
            //if a possible value exists only 1 time throughout the entire collection
            if(possibleValueOccurrences.get(key) == 1) {
                //iterate over all slots in the collection
                squares.each{slot ->
                    //find the only slot that the value could belong to
                    if(slot.possibleValues.contains(key.intValue())) {
                        //add this slot to the list of slots we assigned a value to
                        modifiedSlots.put(slot,slot.possibleValues.clone())
                        //assign this value to this slot because its the only slot this value can belong to
                        slot.setValue(key.intValue())
                    }
                }
            }
        }
        return modifiedSlots
    }

    /**
     * Calculates if each slot in the collection has a value or not and returns result
     * @return true if all slots in the collection have a value other than 0, otherwise returns false
     */
    public boolean isComplete() {
        if(!isComplete) {
            boolean hasValue = true
            //loop through all slots in the collection, if one is found without a value, set isComplete to false.
            for(int i = 0; i < squares.length; i++) {
                def slot = squares[i]
                if(slot == null || !slot.hasValue()) {
                    hasValue = false
                    break
                }
            }
            isComplete = hasValue
        }
        return isComplete
    }

    /**
     * decrements by 1, the number of times a possible value occurs in the collection
     * @param value the value who's count should be decremented
     */
    public void decrementPossibleValueCount(int value) {
        //get the count of how many times the value occurs in the collection
        Integer occurrenceCount = possibleValueOccurrences.get(value)
        //if the value exists in the collection
        if(occurrenceCount && occurrenceCount != 0) {
            //if the count of the value is only 1...
            if(occurrenceCount == 1) {
                //remove it from the collection
                possibleValueOccurrences.remove(value)
            } else {
                //if the count is greater than 1, decrement it by 1
                possibleValueOccurrences.put(value, (occurrenceCount - 1))
            }
        }
    }
}
