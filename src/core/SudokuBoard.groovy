package core
/**
 * Created by jerransimpson on 7/11/15.
 */
class SudokuBoard {

    private SudokuSlot[][] boardSlots
    private SudokuSlotCollection[][] boardBlocks
    private SudokuSlotCollection[] boardColumns
    private SudokuSlotCollection[] boardRows
    private static int boardSize

    public SudokuBoard(int blockRowCount, int blockColumnCount) {
        boardSize = blockRowCount*blockColumnCount
        boardSlots = new SudokuSlot[boardSize][boardSize]
        boardBlocks = new SudokuSlotCollection[blockRowCount][blockColumnCount]
        boardColumns = new SudokuSlotCollection[boardSize]
        boardRows = new SudokuSlotCollection[boardSize]

        initializeSlots(blockRowCount, blockColumnCount)
    }

    public static int getBoardSize() {
        return boardSize
    }


    private void initializeSlots(int blockRowCount, int blockColumnCount) {
        boardSlots.eachWithIndex{row, rowIndex ->
            row.eachWithIndex{columnSlot, columnIndex ->
                SudokuSlot newSlot = new SudokuSlot()
                boardSlots[rowIndex][columnIndex] = newSlot

                //Assign slot object to corresponding block
                int blockRow = rowIndex/blockRowCount
                int blockColumn = columnIndex/blockColumnCount
                SudokuSlotCollection currentBlock = boardBlocks[blockRow][blockColumn]
                if(!currentBlock) {
                    currentBlock = new SudokuSlotCollection()
                    boardBlocks[blockRow][blockColumn] = currentBlock
                }
                currentBlock.addSlot(newSlot)

                //Assign slot to row
                addSlotToCollection(boardRows, rowIndex, newSlot)

                //Assign slot to column
                addSlotToCollection(boardColumns, columnIndex, newSlot)
            }
        }
    }

    private void addSlotToCollection(SudokuSlotCollection[] collectionArray, int index, SudokuSlot newSlot) {
        SudokuSlotCollection currentColumn = collectionArray[index]
        if(!currentColumn) {
            currentColumn = new SudokuSlotCollection()
            collectionArray[index] = currentColumn
        }
        currentColumn.addSlot(newSlot)
    }

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

    public void calculatePossibleValuesByBlocks() {
        boardBlocks.each{ blockRow ->
            blockRow.each{ block ->
                if(!block.isComplete()) {
                    Set<SudokuSlot> slots = block.getSlots()
                    Set<Integer> blockValues = new HashSet<Integer>()
                    slots.each { slot ->
                        int value = slot.getValue()
                        if (value != 0) {
                            blockValues.add(value)
                        }
                    }
                    slots.each { slot ->
                        blockValues.each { value ->
                            slot.removePossibleValue(value)
                        }
                    }
                }
            }
        }
    }

    public void calculatePossibleValuesByRowsAndColumns() {
        calculatePossibleValuesByCollection(boardRows)
        calculatePossibleValuesByCollection(boardColumns)
    }

    private void calculatePossibleValuesByCollection(SudokuSlotCollection[] collectionArray) {
        collectionArray.each{ collection ->
            if(!collection.isComplete()) {
                Set<Integer> collectionValues = new HashSet<Integer>()
                collection.slots.each { slot ->
                    int value = slot.getValue()
                    if (value != 0) {
                        collectionValues.add(value)
                    }
                }

                collection.slots.each { slot ->
                    if (!slot.hasValue()) {
                        collectionValues.each { value ->
                            slot.removePossibleValue(value)
                        }
                    }
                }
            }
        }
    }

    public void assignCorrectValues() {
        boardSlots.eachWithIndex { row, rowIndex ->
            row.eachWithIndex{ slot, columnIndex ->
                if(!slot.hasValue()) {
                    if(slot.possibleValues.size() == 1){
                        def value = slot.getPossibleValues().iterator().next()
                        slot.setValue(value)
                    }
                }
            }
        }
    }

    public void assignValue(int row, int column, int value) {
        boardSlots[row][column].setValue(value)
    }

    public void printBoard() {
        print '\n'
        boardSlots.each{ row ->
            row.each{ slot ->
                print "${slot.getValue()}\t"
            }
            print '\n'
        }
    }

    public void printSegments() {
        boardBlocks.eachWithIndex{ blockRow, rowIndex ->
            blockRow.eachWithIndex{ block, columnIndex ->
                println "Row: ${rowIndex+1} Column: ${columnIndex+1}"
                println "${block.getSlots().toArray()}"
            }
        }
    }

    public void printPossibleValues() {
        boardSlots.eachWithIndex{ row, rowIndex ->
            row.eachWithIndex{ slot, columnIndex ->
                println "Row: ${rowIndex+1} Column: ${columnIndex+1}"
                println "Possible Values: ${slot.getPossibleValues()}"
            }
            print '\n'
        }
    }

}


/*              0           1           2
        0   1   2   3   4   5   6   7   8
    0   00  00  00  01  01  01  02  02  02
    1   00  00  00  01  01  01  02  02  02
0   2   00  00  00  01  01  01  02  02  02
    3   10  10  10  11  11  11  12  12  12
    4   10  10  10  11  11  11  12  12  12
1   5   10  10  10  11  11  11  12  12  12
    6   20  20  20  21  21  21  22  22  22
    7   20  20  20  21  21  21  22  22  22
2   8   20  20  20  21  21  21  22  22  22
 */