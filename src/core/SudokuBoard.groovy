package core
/**
 * Created by jerransimpson on 7/11/15.
 */
class SudokuBoard {

    //collection of all slots on the entire sudoku puzzle
    private SudokuSlot[][] boardSlots

    //collection of blocks of sudoku slots (each 3x3 square of 1-9)
    private SudokuSlotCollection[][] boardBlocks

    //collection of columns on the sudoku board
    private SudokuSlotCollection[] boardColumns

    //collection of rows on teh sudoku board
    private SudokuSlotCollection[] boardRows

    //the full length of the board (standard sudoku is 9)
    private static int boardSize

    private static int boardBlockWidth

    private static int boardBlockHeight

    private boolean progressMade = true

    /**
     * Initializes the collection arrays and calls the initialize method to create the empty slots across the board
     * @param blockRowCount the number of rows per block on the board (standard sudoku is 3)
     * @param blockColumnCount the number of columns per block on the board (standard sudoku is 3)
     * @param values starting values for the sudoku puzzle
     */
    public SudokuBoard(int blockRowCount, int blockColumnCount, int[][] values) {
        boardSize = blockRowCount*blockColumnCount
        boardBlockWidth = blockColumnCount
        boardBlockHeight = blockRowCount
        boardSlots = new SudokuSlot[boardSize][boardSize]
        boardBlocks = new SudokuSlotCollection[blockRowCount][blockColumnCount]
        boardColumns = new SudokuSlotCollection[boardSize]
        boardRows = new SudokuSlotCollection[boardSize]

        initializeSlots(blockRowCount,blockColumnCount,values)
    }

    public void solve() {
        calculatePossibleValuesByBlocks()
        calculatePossibleValuesByRowsAndColumns()
        calculatePossibleValueOccurrences()
        def counter = 0
        while (!isComplete() && counter < 20) {
            assignCorrectValues()
            counter++
        }
    }

    /**
     * Simply returns the length of one side of the square board
     * @return int length of one side of the sudoku board
     */
    public static int getBoardSize() {
        return boardSize
    }

    /**
     * Initializes all slots on the board as an empty slot,
     * contains logic to ensure that the row, column, block, and board collections all reference the same slots instead of duplicating slots between collections
     * @param blockRowCount the number of rows per block on the board (standard sudoku is 3)
     * @param blockColumnCount the number of columns per block on the board (standard sudoku is 3)
     * @param values starting values for the sudoku puzzle
     */
    private void initializeSlots(int blockRowCount, int blockColumnCount, int[][] values) {
        //start at the top left of the board and begin iterating through each row
        boardSlots.eachWithIndex{row, rowIndex ->
            //for each row, iterate through each slot, starting at the far left
            row.eachWithIndex{columnSlot, columnIndex ->
                //create a new slot, by default possible values are initialized to 1-9
                SudokuSlot newSlot = new SudokuSlot(values[rowIndex][columnIndex])
                //assign the new slot to the board
                boardSlots[rowIndex][columnIndex] = newSlot

                int blockRow = rowIndex/blockRowCount
                int blockColumn = columnIndex/blockColumnCount
                //determine which block the slot belongs to based upon the slot's row/column indices
                SudokuSlotCollection currentBlock = boardBlocks[blockRow][blockColumn]
                //ensure the block object exists, if not, create a new one and assign it to the expected location
                if(!currentBlock) {
                    currentBlock = new SudokuSlotCollection()
                    boardBlocks[blockRow][blockColumn] = currentBlock
                }
                //add the slot to the block
                currentBlock.addSlot(newSlot)

                //Assign the same slot to the correct row collection
                addSlotToCollection(boardRows, rowIndex, newSlot)

                //Assign the same slot to the correct column collection
                addSlotToCollection(boardColumns, columnIndex, newSlot)
            }
        }
    }

    /**
     * Somewhat generic method used to add a slot to a collection, unfortunately only designed to work with row and column collections
     * @param collectionArray the array of collections to add the slot to (rows or columns)
     * @param index the index of the row or column that the slot needs to be added to
     * @param newSlot the slot to add to the collection array
     */
    private void addSlotToCollection(SudokuSlotCollection[] collectionArray, int index, SudokuSlot newSlot) {
        //attempt to pull the row or column from the collection
        SudokuSlotCollection currentCollection = collectionArray[index]
        //if the collection does not exist, create it
        if(!currentCollection) {
            currentCollection = new SudokuSlotCollection()
            collectionArray[index] = currentCollection
        }
        //add the new slot to the collection
        currentCollection.addSlot(newSlot)
    }

    /**
     * Iterates through all blocks in the board to determine if the whole puzzle is complete
     * @return boolean true if board is complete
     */
    public boolean isComplete() {
        boolean complete = true
        boardBlocks.eachWithIndex{ blockRow, blockRowIndex ->
            blockRow.each {block ->
                if(!block.isComplete()) {
                    complete = false
                }
            }
        }
        return complete
    }

    /**
     * Iterates over each slot on the board and, based upon the other slots in the block, determines the possible values for that slot
     */
    public void calculatePossibleValuesByBlocks() {
        //iterate over each row of blocks in the 2D-array of blocks
        boardBlocks.each{ blockRow ->
            //iterate over each block within the row
            blockRow.each{ block ->
                //check to see if all slots within the block already have a value
                if(!block.isComplete()) {
                    //if the block is incomplete, retrieve all slots from the block
                    Set<SudokuSlot> slots = block.getSlots()
                    Set<Integer> blockValues = new HashSet<Integer>()
                    //iterate over all slots within the block, and (if the slot's value is not 0) add the slot's value to the Set of already assigned values
                    slots.each { slot ->
                        int value = slot.getValue()
                        if (value != 0) {
                            blockValues.add(value)
                        }
                    }
                    //iterate over all slots in the block
                    slots.each { slot ->
                        if(!slot.hasValue()) {
                            //then iterate over all not-possible values for the block, and remove each value from all slots within the block
                            blockValues.each { value ->
                                slot.removePossibleValue(value)
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * High-level method to determine possible values within each slot based upon the other values in the same row or column
     */
    public void calculatePossibleValuesByRowsAndColumns() {
        calculatePossibleValuesByCollection(boardRows)
        calculatePossibleValuesByCollection(boardColumns)
    }

    /**
     * Takes an array of slotCollections (rows or columns) and determines the possible values for each slot within the collection, based upon the already determined values of the other slots in the collection
     * @param collectionArray the array of collections to perform the logic on
     */
    private void calculatePossibleValuesByCollection(SudokuSlotCollection[] collectionArray) {
        //iterate over each collection within the collection array
        collectionArray.each{ collection ->
            //check to see if all slots in the collection have been assigned a value
            if(!collection.isComplete()) {
                //if not
                Set<Integer> collectionValues = new HashSet<Integer>()
                //iterate over all slots in the collection and create a set of already assigned (non-0) values within that collection
                collection.slots.each { slot ->
                    int value = slot.getValue()
                    if (value != 0) {
                        collectionValues.add(value)
                    }
                }
                //iterate over all slots within the collection
                collection.slots.each { slot ->
                    //if the slot does not already have an assigned value
                    if (!slot.hasValue()) {
                        //iterate over the set of already assigned values in the collection and remove them from the list of possible values in that slot
                        collectionValues.each { value ->
                            slot.removePossibleValue(value)
                        }
                    }
                }
            }
        }
    }

    /**
     * iterates over all slots on the board and if only 1 value is possible for that slot, that value is assigned to that slot
     */
    public void assignCorrectValues() {
        boardRows.eachWithIndex {row, rowIndex ->
            if(!row.isComplete()) {
                def possibleValueOccurrences = row.possibleValueOccurrences
                row.slots.eachWithIndex { slot, columnIndex ->
                    if (!slot.hasValue()) {
                        def possibleValues = slot.possibleValues.clone()
                        possibleValues.each { possibleValue ->
                            def valueOccurrence = possibleValueOccurrences.get(possibleValue)
                            if(valueOccurrence == 1) {
                                assignValue(rowIndex,columnIndex,possibleValue)
                            }
                        }
                    }
                }
            }
        }
        boardColumns.eachWithIndex {column, columnIndex ->
            if(!column.isComplete()) {
                def possibleValueOccurrences = column.possibleValueOccurrences
                column.slots.eachWithIndex { slot, rowIndex ->
                    if (!slot.hasValue()) {
                        def possibleValues = slot.possibleValues
                        possibleValues.each { possibleValue ->
                            def valueOccurrence = possibleValueOccurrences.get(possibleValue)
                            if(valueOccurrence == 1) {
                                assignValue(rowIndex,columnIndex,possibleValue)
                            }
                        }
                    }
                }
            }
        }
//        boardBlocks.eachWithIndex { blockRow, blockRowIndex ->
//            blockRow.eachWithIndex {block, blockColumnIndex ->
//                if(!block.isComplete()) {
//                    def possibleValueOccurrences = block.possibleValueOccurrences
//                    block.slots.eachWithIndex{ slot, int i -> }
//                }
//            }
//        }
        //iterate over all rows on the board
        boardSlots.eachWithIndex { row, rowIndex ->
            //iterate over all slots within the row
            row.eachWithIndex{ slot, columnIndex ->
                //confirm that the slot doesn't already have a value
                if(!slot.hasValue()) {
                    //if the slot only has one possible value
                    if(slot.possibleValues.size() == 1){
                        //assign the value to the slot
                        def value = slot.getPossibleValues().iterator().next()
                        assignValue(rowIndex,columnIndex,value)
                    }
                }
            }
        }

    }

    /**
     * Assigns the specified value to the specified row and column on the sudoku board
     * @param rowIndex the row to assign the value to
     * @param column the column to assign the value to
     * @param value the value to be assigned
     */
    public void assignValue(int rowIndex, int column, int value) {
        progressMade = true
        boardSlots[rowIndex][column].setValue(value)
        removeFromPossibleValues(rowIndex, column, value)
        calculatePossibleValueOccurrences()
    }

    public void calculatePossibleValueOccurrences() {
        boardBlocks.each{blockRow ->
            blockRow.each{block ->
                if(!block.isComplete()) {
                    block.calculatePossibleValueOccurrences()
                }
            }
        }
        boardRows.each { row ->
            if(!row.isComplete()) {
                row.calculatePossibleValueOccurrences()
            }
        }
        boardColumns.each { column ->
            if(!column.isComplete()) {
                column.calculatePossibleValueOccurrences()
            }
        }
    }

    /**
     * Removes a value from the possible values list of all slots in the same row, column, and block as the provided indeces
     * @param rowIndex index of the row to modify
     * @param columnIndex index of the column to modify
     * @param value the value to remove from the possible values lists
     */
    private void removeFromPossibleValues(int rowIndex, int columnIndex, int value) {
        def row = boardRows[rowIndex]
        row.slots.each { slot ->
            slot.removePossibleValue(value)
        }
        def column = boardColumns[columnIndex]
        column.slots.each { slot ->
            slot.removePossibleValue(value)
        }
        int blockRow = rowIndex/boardBlockHeight
        int blockColumn = columnIndex/boardBlockWidth
        def block = boardBlocks[blockRow][blockColumn]
        block.slots.each { slot ->
            slot.removePossibleValue(value)
        }
    }

    /**
     * prints out the entire sudoku board to the console, columns are separated by tab characters and rows are separated by new line characters, 0's indicate empty slots
     */
    public void printBoard() {
        //print a new line character just in case a board was printed above this one in the console
        print '\n'
        //iterate over all rows on the board
        boardSlots.each{ row ->
            //iterate over each slot in the row
            row.each{ slot ->
                //print the slot's value, followed by a tab character to separate it from the next column
                print "${slot.getValue()}\t"
            }
            //print a new line character to end the row
            print '\n'
        }
    }

    /**
     * prints out each slot's possible values to the console for debugging/testing purposes
     */
    public void printPossibleValues() {
        //iterate over each row on the board
        boardSlots.eachWithIndex{ row, rowIndex ->
            //iterate over each slot in the row
            row.eachWithIndex{ slot, columnIndex ->
                //print the row and column number of the slot
                println "Row: ${rowIndex+1} Column: ${columnIndex+1}"
                //print the list of the slot's possible values, if no possible values, then the slot should have a non-0 value assigned
                println "Possible Values: ${slot.getPossibleValues()}"
            }
            print '\n'
        }
    }

}