package core
/**
 * Created by jerransimpson on 7/11/15.
 */
class SudokuBoard {

    //collection of blocks of sudoku slots (each 3x3 square of 1-9)
    private SudokuBlock[][] boardBlocks

    //collection of columns on the sudoku board
    private SudokuSlotArray[] boardColumns

    //collection of rows on teh sudoku board
    private SudokuSlotArray[] boardRows

    //the full length of the board (standard sudoku is 9)
    private static int boardSize

    //the number of columns in each block in the sudoku puzzle (standard sudoku is 3)
    private static int boardBlockWidth

    //the number of rows in each block in the sudoku puzzle (standard sudoku is 3)
    private static int boardBlockHeight


    /**
     * Initializes the collection arrays and calls the initialize method to create the starting slots across the board
     * @param blockRowCount the number of rows per block on the board (standard sudoku is 3)
     * @param blockColumnCount the number of columns per block on the board (standard sudoku is 3)
     * @param values starting values for the sudoku puzzle
     */
    public SudokuBoard(int blockRowCount, int blockColumnCount, int[][] values) {
        boardSize = blockRowCount*blockColumnCount
        boardBlockWidth = blockColumnCount
        boardBlockHeight = blockRowCount
        boardBlocks = new SudokuBlock[blockRowCount][blockColumnCount]
        boardColumns = new SudokuSlotArray[boardSize]
        boardRows = new SudokuSlotArray[boardSize]

        //initialize the starting position of the board
        initializeSlots(blockRowCount,blockColumnCount,values)
    }

    /**
     * entry point for solving the sudoku puzzle
     */
    public void solve() {
        //calculate the initial possible values for the slots based upon the blocks they are in
        calculatePossibleValuesByBlocks()
        //calculate the initial possible values for  the slots based upon the row and column they are in
        calculatePossibleValuesByRowsAndColumns()
        //calculate how many times each possible value occurs within each row, column, and block in the puzzle
        calculatePossibleValueOccurrences()
        def counter = 0
        //while the puzzle is not complete, attempt to solve the puzzle (for now we quit after 50 rounds to prevent infinite loops for puzzles too difficult for the logic to solve)
        while (!isComplete() && counter < 50) {
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
     * Initializes all slots on the board, either empty or with the value that slot should start with
     * contains logic to ensure that the row, column, and block collections all reference the same slots instead of duplicating slots between collections
     * @param blockRowCount the number of rows per block on the board (standard sudoku is 3)
     * @param blockColumnCount the number of columns per block on the board (standard sudoku is 3)
     * @param values starting values for the sudoku puzzle
     */
    private void initializeSlots(int blockRowCount, int blockColumnCount, int[][] values) {
        //start at the top left of the board and begin iterating through each row
        values.eachWithIndex{row, rowIndex ->
            //for each row, iterate through each value, starting at the far left
            row.eachWithIndex{value, columnIndex ->
                //create a new slot with its starting value (0 if the slot doesn't have a value)
                SudokuSlot newSlot = new SudokuSlot(value,rowIndex, columnIndex)

                //calculate the row index of the block in the block collection that this value belongs to
                int blockRow = rowIndex/blockRowCount
                //calculate the column index of the block in the block collection that this value belongs to
                int blockColumn = columnIndex/blockColumnCount
                //determine which block the slot belongs to based upon the slot's row/column indices
                SudokuBlock currentBlock = boardBlocks[blockRow][blockColumn]
                //ensure the block object exists, if not, create a new one and assign it to the expected location
                if(!currentBlock) {
                    currentBlock = new SudokuBlock(boardBlockHeight,boardBlockWidth)
                    boardBlocks[blockRow][blockColumn] = currentBlock
                }
                //add the slot to the block, do some math to determine the row and column indices of the slot within the block
                currentBlock.addSlot(newSlot,(rowIndex%boardBlockHeight), (columnIndex%boardBlockWidth))

                //Assign the same slot to the correct row collection
                addSlotToArray(boardRows, rowIndex, columnIndex, newSlot)

                //Assign the same slot to the correct column collection
                addSlotToArray(boardColumns, columnIndex, rowIndex, newSlot)
            }
        }
    }

    /**
     * Somewhat generic method used to add a slot to a collection, unfortunately only designed to work with row and column collections
     * @param collectionArray the array of collections to add the slot to (rows or columns)
     * @param collectionIndex the index of the row or column that the slot needs to be added to
     * @param slotIndex the index of the slot within the collection that the slot is added to
     * @param newSlot the slot to add to the collection array
     */
    private void addSlotToArray(SudokuSlotArray[] collectionArray, int collectionIndex, int slotIndex, SudokuSlot newSlot) {
        //attempt to pull the row or column from the collection
        SudokuSlotArray currentCollection = collectionArray[collectionIndex]
        //if the collection does not exist, create it
        if(!currentCollection) {
            currentCollection = new SudokuSlotArray(boardSize)
            collectionArray[collectionIndex] = currentCollection
        }
        //add the new slot to the collection
        currentCollection.addSlot(newSlot,slotIndex)
    }

    /**
     * Iterates through all blocks in the board to determine if the whole puzzle is complete
     * @return boolean true if board is complete
     */
    public boolean isComplete() {
        boolean complete = true
        boardBlocks.each{ blockRow ->
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
        boardBlocks.each {blockRow ->
            blockRow.each {block ->
                block.calculatePossibleValues()
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
    private void calculatePossibleValuesByCollection(SudokuSlotArray[] collectionArray) {
        collectionArray.each { collection ->
            collection.calculatePossibleValues()
        }
    }

    /**
     * Calls collection-level methods to recalculate the possible values and their counts for each slot on the board
     * @param modifiedSlotsMap
     */
    private void recalculatePossibleValues(Map<SudokuSlot,Set<Integer>> modifiedSlotsMap) {
        //iterate over all slots that were modified in the last round of assigning values
        modifiedSlotsMap.keySet().each{ slot ->
            //get the list of possible values that could've been assigned to that slot before its value was determined
            def possibleValues = modifiedSlotsMap.get(slot)
            def rowIndex = slot.rowIndex
            def columnIndex = slot.columnIndex
            //recalculate the possible values and their counts for all slots in the same row as this slot
            boardRows[rowIndex].recalculatePossibleValues(slot.value, possibleValues)
            //recalculate the possible values and their counts for all slots in the same column as this slot
            boardColumns[columnIndex].recalculatePossibleValues(slot.value, possibleValues)
            //determine the indices of the block that this slot belongs to
            int blockRow = (rowIndex/boardBlockHeight)
            int blockColumn = (columnIndex/boardBlockWidth)
            //recalculate the possible values and their counts for all slots in the same block as this slot
            boardBlocks[blockRow][blockColumn].recalculatePossibleValues(slot.value, possibleValues)
        }
    }

    /**
     * iterates over all collections (rows, columns, blocks) in the board and calls their internal methods to calculate correct values for their slots
     */
    public void assignCorrectValues() {
        boardRows.each {row ->
            if(!row.isComplete()) {
                row.calculatePossibleValueOccurrences()
                def modifiedSlots = row.assignCorrectValues()
                recalculatePossibleValues(modifiedSlots)
                row.accountForMatchingSlots()
            }
        }

        boardColumns.each {column ->
            if(!column.isComplete()) {
                column.calculatePossibleValueOccurrences()
                def modifiedSlots = column.assignCorrectValues()
                recalculatePossibleValues(modifiedSlots)
                column.accountForMatchingSlots()
            }
        }

        boardBlocks.each {blockRow ->
            blockRow.each {block ->
                if(!block.isComplete()) {
                    block.calculatePossibleValueOccurrences()
                    def modifiedSlots = block.assignCorrectValues()
                    recalculatePossibleValues(modifiedSlots)
                    block.accountForMatchingSlots()
                }
            }
        }
    }

    /**
     * method to calculate the original count of how many times possible values occur in each row, each column, and each block
     */
    public void calculatePossibleValueOccurrences() {
        //for each block in the puzzle, calculate how many times each possible value occurs
        boardBlocks.each{blockRow ->
            blockRow.each{block ->
                if(!block.isComplete()) {
                    block.calculatePossibleValueOccurrences()
                }
            }
        }
        //for each row in the puzzle, calculate how many times each possible value occurs
        boardRows.each { row ->
            if(!row.isComplete()) {
                row.calculatePossibleValueOccurrences()
            }
        }
        //for each column in the puzzle, calculate how many times each possible value occurs
        boardColumns.each { column ->
            if(!column.isComplete()) {
                column.calculatePossibleValueOccurrences()
            }
        }
    }

    /**
     * prints out the entire sudoku board to the console, columns are separated by tab characters and rows are separated by new line characters, 0's indicate empty slots
     */
    public void printBoard() {
        //print a new line character just in case a board was printed above this one in the console
        print '\n'
        def rowCount = 0
        def columnCount = 0
        //iterate over all rows on the board
        print '-------------------------------------------------\n'
        boardRows.each{ row ->
            //iterate over each slot in the row
            print '|\t'
            row.slots.each{ slot ->
                //print the slot's value, followed by a tab character to separate it from the next column
                print "${slot.getValue()}\t"

                columnCount++
                if(columnCount % 3 == 0) {
                    print '|\t'
                }
            }
            //print a new line character to end the row
            print '\n'

            rowCount++
            if(rowCount % 3 == 0) {
                print '-------------------------------------------------\n'
            }
        }
    }

    /**
     * prints out each slot's possible values to the console for debugging/testing purposes
     */
    public void printPossibleValues() {
        //iterate over each row on the board
        boardRows.eachWithIndex{ row, rowIndex ->
            //iterate over each slot in the row
            row.slots.eachWithIndex{ slot, columnIndex ->
                //print the row and column number of the slot
                println "Row: ${rowIndex+1} Column: ${columnIndex+1}"
                //print the list of the slot's possible values, if no possible values, then the slot should have a non-0 value assigned
                println "Possible Values: ${slot.getPossibleValues()}"
            }
            print '\n'
        }
    }

    /**
     * prints out each collection's possible values and how many times each possible value occurs in the collection
     */
    public void printPossibleValueOccurrences() {
        boardRows.eachWithIndex{row, rowIndex ->
            println "Row ${rowIndex+1} Possible Value Occurrences: ${row.possibleValueOccurrences}"
        }

        boardColumns.eachWithIndex{ column, columnIndex ->
            println "Column ${columnIndex+1} Possible Value Occurrences: ${column.possibleValueOccurrences}"
        }

        boardBlocks.eachWithIndex{blockRow, blockRowIndex->
            blockRow.eachWithIndex {block, blockColumnIndex ->
                println "Block Row: ${blockRowIndex+1} Block Column: ${blockColumnIndex+1} Possible Value Occurrences: ${block.possibleValueOccurrences}"
            }
        }
    }

}