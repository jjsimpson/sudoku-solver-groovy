package core;

/**
 * Represents a 1-section block of a sudoku puzzle (a standard sudoku block has 9 values)
 */
public class SudokuBlock {

    //The set of sudoku slots that belong in the block, sorted by rows and columns
    private SudokuSlot[][] squares
    //Indicates if all slots in the collection have a value other than 0
    private boolean isComplete = false
    //Tracks how many times a possible value occurs within the collection in order to narrow down values that only occur once
    private Map<Integer,Integer> possibleValueOccurrences = new HashMap<Integer,Integer>()

    /**
     * Creates a new block of the sudoku puzzle
     * @param height specifies the number of rows in the block
     * @param width specifies the number of columns in the block
     */
    public SudokuBlock(int height, int width) {
        squares = new SudokuSlot[height][width]
    }

    /**
     * Adds a new slot to the block
     * @param slot to add to the block
     * @param blockRow index of the row in the block the slot should be added to
     * @param blockColumn index of the column in the block the slot should be added to
     */
    public void addSlot(SudokuSlot slot, int blockRow, int blockColumn) {
        //only adds the slot to the specified index if that index does not already have a slot
        if(squares[blockRow][blockColumn] == null) {
            squares[blockRow][blockColumn] = slot
        }
    }

    /**
     * Compares all slots in the block to each other and removes any assigned values in a slot from the possible values list for all other slots in the block
     */
    public void calculatePossibleValues() {
        //iterates over all rows in the block
        squares.each{ blockRow ->
            //iterates over all slots in the row of the block
            blockRow.each { slot ->
                //for each slot in the block, compare to all other slots in the block
                //iterates over all rows in the block again
                squares.each {blockRow2 ->
                    //iterates over all slots in the row
                    blockRow2.each {slot2 ->
                        //remove the value in one slot from the possible values list of the other slot
                        slot2.removePossibleValue(slot.value)
                    }
                }
            }
        }
    }

    /**
     * Updates the list of possible values for squares based upon a new value assigned to a slot
     * @param assignedValue the value that was assigned to a slot in this block
     * @param previousPossibleValues values that were possible for the slot before a value was assigned to it
     */
    public void recalculatePossibleValues(int assignedValue, Set<Integer> previousPossibleValues) {
        //iterate over all rows in the block
        squares.each { slotRow ->
            //iterate over all slots in the row
            slotRow.each { slot ->
                //removes the newly assigned value from the possible values list for all slots in the block
                slot.removePossibleValue(assignedValue)
            }
        }
        // Now that the value has been assigned to a slot, remove the count of how many slots that value can belong to
        possibleValueOccurrences.remove(assignedValue)
        //iterate over all values that could've belonged to the slot that has now been assigned a value
        previousPossibleValues.each { possibleValue ->
            //decrement the count of how many times each of these possible values occurs in the block
            decrementPossibleValueCount(possibleValue)
        }
    }

    /**
     * Checks for slots that have the exact same possible values and if it makes sense to do so, remove those values as possible values from other slots
     * ex. if two slots have the possible values 1,2 remove 1 and 2 as possible values from every slot in the collection as those two slots because 1 and 2 have to belong to those two slots
     */
    public void accountForMatchingSlots() {
        //iterate over all rows in the block
        squares.each { slotRow ->
            //iterate over all slots in the row
            slotRow.each { slot ->
                //create a collection to track the slots which have the exact same possible values as another slot
                ArrayList<SudokuSlot> matchingPossibleValues = new ArrayList<SudokuSlot>()
                squares.each { slotRow2 ->
                    //iterate over all slots in the row
                    slotRow2.each { slot2 ->
                        //if the current slot has the same possible values as slot2, keep track of slot2 for later
                        if(slot.possibleValues.size() > 0 && slot.possibleValues == slot2.possibleValues) {
                            matchingPossibleValues.add(slot2)
                        }
                    }
                }

                //If the number of possible values is exactly the same as the number of slots...
                //it means these values have to go in these slots. Remove these values from the possible values of all other slots
                if(matchingPossibleValues.size() > 0 && matchingPossibleValues.get(0).possibleValues.size() == matchingPossibleValues.size()) {
                    squares.each { slotRow2 ->
                        slotRow2.each { slot2 ->
                            // if slot doesn't have the exact same possible values as slot2, it means it is a slot we need to remove values from
                            if(slot.possibleValues != slot2.possibleValues) {
                                slot.possibleValues.each { value ->
                                    //remove all of the current slot's values from slot2's possible values list
                                    slot2.removePossibleValue(value)
                                    //decrement the possible value count for every possible value we remove
                                    decrementPossibleValueCount(value)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Calculates the initial count of how many times each number value exists as a possible value in the block
     */
    public void calculatePossibleValueOccurrences() {
        possibleValueOccurrences = new HashMap<Integer,Integer>()
        //iterate over all rows in the block
        squares.each {row ->
            //iterate over all slots in the row
            row.each {slot ->
                //iterate over all possible values for the slot
                slot.possibleValues.each {value ->
                    //check to see if the current possible value is already being tracked in the possible value count map
                    def valueCount = possibleValueOccurrences.get(value)
                    if(valueCount) {
                        //if the value count is already being tracked in the map, increment its count by 1
                        possibleValueOccurrences.put(value, (valueCount+1))
                    } else {
                        //if the value count is not being tracked in the map, create a new entry and start it at a count of 1
                        possibleValueOccurrences.put(value,1)
                    }
                }
            }
        }
    }

    /**
     * returns a map of all possible values in the block and how many times that possible value occurs
     * @return map of value in the block to how many times that value occurs in the block
     */
    public Map<Integer,Integer> getPossibleValueOccurrences() {
        return possibleValueOccurrences
    }

    /**
     * Calculates if each slot in the block has a value or not and returns result
     * @return true if all slots in the collection have a value other than 0, otherwise returns false
     */
    public boolean isComplete() {
        //if the block hasn't already been determined to be complete, begin calculating
        if(!isComplete) {
            boolean hasValue = true
            //loop through all slots in the collection, if one is found without a value, the block is determined to not be complete
            for(int r = 0; r < squares.length; r++) {
                for(int c = 0; c < squares[r].length; c++) {
                    def slot = squares[r][c]
                    if(!slot.hasValue()) {
                        hasValue = false
                        break
                    }
                }
                if(!hasValue) {
                    break
                }
            }
            isComplete = hasValue
        }
        return isComplete
    }

    /**
     * Performs all of the logic of assigning values to each slot within the block, based upon the values of other slots in the block
     * @return Map of the slots that were assigned values, to the possible values that could've belonged to that slot before it was assigned a value
     */
    public Map<SudokuSlot,Set<Integer>> assignCorrectValues() {
        //create a map to track which slots were assigned values, and what possible values could have been assigned to the slot, before the correct value was determined
        Map<SudokuSlot,Set<Integer>> modifiedSlots = new HashMap<SudokuSlot,Set<Integer>>()
        //iterate over all rows in the block
        squares.each { row ->
            //iterate over all slots in the row
            row.each { slot ->
                //if there is only one possible value for the slot
                if(slot.possibleValues.size() == 1) {
                    //add this slot to the map of slots we assigned values to
                    modifiedSlots.put(slot,slot.possibleValues.clone())
                    //assign the value to the slot since it is the only possible value for that slot
                    slot.setValue(slot.possibleValues.iterator().next())
                }
            }
        }

        //iterate over the map of the count of possible values in the block
        possibleValueOccurrences.keySet().each{key ->
            //if a possible value only exists in the block 1 time
            if(possibleValueOccurrences.get(key) == 1) {
                //iterate over all slots in the block
                squares.each { row ->
                    row.each { slot ->
                        //find the slot that the value could belong to
                        if(slot.possibleValues.contains(key.intValue())) {
                            //put that slot and its possible values in the map of slots we modify
                            modifiedSlots.put(slot,slot.possibleValues.clone())
                            //assign this value to this slot because its the only slot this value could belong to in the block
                            slot.setValue(key.intValue())
                        }
                    }
                }
            }
        }
        return modifiedSlots
    }

    /**
     * decrements by 1, the count of how many times a possible value occurs in the block
     * @param value of which you want to decrement the count
     */
    public void decrementPossibleValueCount(int value) {
        //get the current count of how many times the possible value occurs
        Integer occurrenceCount = possibleValueOccurrences.get(value)
        //if the value does exist in the possible values map
        if(occurrenceCount && occurrenceCount != 0) {
            //if the value only exists once, remove it from the collection entirely
            if(occurrenceCount == 1) {
                possibleValueOccurrences.remove(value)
            } else {
                //if the value exists more than once, decrement it by one
                possibleValueOccurrences.put(value, (occurrenceCount - 1))
            }
        }
    }
}
