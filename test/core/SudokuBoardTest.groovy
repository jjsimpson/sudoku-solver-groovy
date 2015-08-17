package core

/**
 * Created by Jerran on 7/28/2015.
 */
class SudokuBoardTest extends GroovyTestCase {

    SudokuBoard testBoard1

    void setUp() {
        super.setUp()
        File puzzleFile = new File('test/testPuzzles/test_puzzle7(medium)')

        int rowIndex = 0
        int[][] initialValues = new int[9][9]
        puzzleFile.eachLine { line ->
            String[] row = line.split('\t')
            row.eachWithIndex { numberText, columnIndex ->
                initialValues[rowIndex][columnIndex] = Integer.parseInt(numberText)
            }
            rowIndex++
        }

        testBoard1 = new SudokuBoard(3,3, initialValues)
    }

    void tearDown() {
        testBoard1 = null
    }

    void testSolve() {
//        assert !testBoard1.isComplete()
//        testBoard1.solve()
//        assert testBoard1.isComplete()
    }

    void testGetBoardSize() {
        assert testBoard1.getBoardSize() == 9
    }

    void testIsComplete() {
//        assert !testBoard1.isComplete()
//        testBoard1.solve()
//        assert testBoard1.isComplete()
    }

    void testCalculatePossibleValuesByBlocks() {
        SudokuBlock[][] blocks = testBoard1.getBoardBlocks()
        blocks.each { blockRow ->
            blockRow.each { block ->
                SudokuSlot[][] slots = block.getSquares()
                slots.each { slotRow ->
                    slotRow.each { slot ->
                        if(slot.hasValue()) {
                            assert slot.possibleValues.empty
                        } else {
                            assert slot.possibleValues == [1,2,3,4,5,6,7,8,9] as Set
                        }
                    }
                }
            }
        }

        testBoard1.calculatePossibleValuesByBlocks()

        blocks.eachWithIndex { blockRow, blockRowIndex ->
            blockRow.eachWithIndex { block, blockColumnIndex ->
                SudokuSlot[][] slots = block.getSquares()
                slots.each { slotRow ->
                    slotRow.each { slot ->
                        if(slot.hasValue()) {
                            assert slot.possibleValues.empty
                        } else if(blockRowIndex == 0 && blockColumnIndex == 0){
                            assert slot.possibleValues == [2,5,6,7,8,9] as Set
                        } else if(blockRowIndex == 0 && blockColumnIndex == 1){
                            assert slot.possibleValues == [3,4,5,6,8,9] as Set
                        } else if(blockRowIndex == 0 && blockColumnIndex == 2){
                            assert slot.possibleValues == [1,2,3,5,8,9] as Set
                        } else if(blockRowIndex == 1 && blockColumnIndex == 0){
                            assert slot.possibleValues == [1,5,6,7,8,9] as Set
                        } else if(blockRowIndex == 1 && blockColumnIndex == 1){
                            assert slot.possibleValues == [1,2,3,6,7,8,9] as Set
                        } else if(blockRowIndex == 1 && blockColumnIndex == 2){
                            assert slot.possibleValues == [2,3,5,7,8,9] as Set
                        } else if(blockRowIndex == 2 && blockColumnIndex == 0){
                            assert slot.possibleValues == [1,2,3,4,6,9] as Set
                        } else if(blockRowIndex == 2 && blockColumnIndex == 1){
                            assert slot.possibleValues == [1,3,5,6,7,9] as Set
                        } else if(blockRowIndex == 2 && blockColumnIndex == 2){
                            assert slot.possibleValues == [1,4,5,6,7,8] as Set
                        }
                    }
                }
            }
        }
    }

    void testCalculatePossibleValuesByRowsAndColumns() {
        Set[][] result = new Set[9][9]
        result[0][0] = [1,6,7,8,9] as Set
        result[0][1] = [1,4,5,6,7,9] as Set
        result[0][3] = [1,3,6,7,9] as Set
        result[0][4] = [4,5,6,7,8,9] as Set
        result[0][6] = [1,5,7,8] as Set
        result[0][7] = [1,4,5,6,8,9] as Set
        result[0][8] = [5,7,8,9] as Set

        result[1][0] = [2,8,9] as Set
        result[1][1] = [2,3,5,9] as Set
        result[1][3] = [2,3,9] as Set
        result[1][5] = [3,5,8,9] as Set
        result[1][6] = [5,8] as Set

        result[2][0] = [2,6,8,9] as Set
        result[2][1] = [2,3,4,5,6,9] as Set
        result[2][3] = [2,3,6,9] as Set
        result[2][4] = [3,5,6,8,9] as Set
        result[2][7] = [2,3,5,6,8,9] as Set
        result[2][8] = [2,3,5,8,9] as Set

        result[3][1] = [2,3,5,7,9] as Set
        result[3][2] = [5,8,9] as Set
        result[3][3] = [2,3,7,9] as Set
        result[3][4] = [3,5,7,8,9] as Set
        result[3][5] = [3,5,8,9] as Set
        result[3][7] = [2,3,5,8,9] as Set

        result[4][0] = [] as Set
        result[4][1] = [] as Set
        result[4][2] = [] as Set
        result[4][3] = [] as Set
        result[4][4] = [] as Set
        result[4][5] = [] as Set
        result[4][6] = [] as Set
        result[4][7] = [] as Set
        result[4][8] = [] as Set

        result[5][0] = [] as Set
        result[5][1] = [] as Set
        result[5][2] = [] as Set
        result[5][3] = [] as Set
        result[5][4] = [] as Set
        result[5][5] = [] as Set
        result[5][6] = [] as Set
        result[5][7] = [] as Set
        result[5][8] = [] as Set

        result[6][0] = [] as Set
        result[6][1] = [] as Set
        result[6][2] = [] as Set
        result[6][3] = [] as Set
        result[6][4] = [] as Set
        result[6][5] = [] as Set
        result[6][6] = [] as Set
        result[6][7] = [] as Set
        result[6][8] = [] as Set

        result[7][0] = [] as Set
        result[7][1] = [] as Set
        result[7][2] = [] as Set
        result[7][3] = [] as Set
        result[7][4] = [] as Set
        result[7][5] = [] as Set
        result[7][6] = [] as Set
        result[7][7] = [] as Set
        result[7][8] = [] as Set

        result[8][0] = [] as Set
        result[8][1] = [] as Set
        result[8][2] = [] as Set
        result[8][3] = [] as Set
        result[8][4] = [] as Set
        result[8][5] = [] as Set
        result[8][6] = [] as Set
        result[8][7] = [] as Set
        result[8][8] = [] as Set

        SudokuSlotArray[] rows = testBoard1.getBoardRows()
        SudokuSlotArray[] columns = testBoard1.getBoardColumns()
        rows.each { row ->
            SudokuSlot[] slots = row.getSquares()
            slots.each { slot ->
                if(slot.hasValue()) {
                    assert slot.possibleValues.empty
                } else {
                    assert slot.possibleValues == [1,2,3,4,5,6,7,8,9] as Set
                }
            }
        }

        columns.each { column ->
            SudokuSlot[] slots = column.getSquares()
            slots.each { slot ->
                if(slot.hasValue()) {
                    assert slot.possibleValues.empty
                } else {
                    assert slot.possibleValues == [1,2,3,4,5,6,7,8,9] as Set
                }
            }
        }

        testBoard1.calculatePossibleValuesByRowsAndColumns()

        rows.eachWithIndex { row, rowIndex ->
            SudokuSlot[] slots = row.getSquares()
            slots.each { slot ->
                if(slot.hasValue()) {
                    assert slot.possibleValues.empty
                } else if(rowIndex == 0){
                    if(slot.columnIndex == 0) {
                        assert slot.possibleValues == [] as Set
                    } ellse
                } else if(rowIndex == 1){
                    assert slot.possibleValues == [] as Set
                } else if(rowIndex == 2){
                    assert slot.possibleValues == [] as Set
                } else if(rowIndex == 3){
                    assert slot.possibleValues == [] as Set
                } else if(rowIndex == 4){
                    assert slot.possibleValues == [] as Set
                } else if(rowIndex == 5){
                    assert slot.possibleValues == [] as Set
                } else if(rowIndex == 6){
                    assert slot.possibleValues == [] as Set
                } else if(rowIndex == 7){
                    assert slot.possibleValues == [] as Set
                } else if(rowIndex == 8){
                    assert slot.possibleValues == [] as Set
                }
            }
        }
    }

    void testAssignCorrectValues() {

    }

    void testCalculatePossibleValueOccurrences() {

    }

//    void testPrintBoard() {
//
//    }
//
//    void testPrintPossibleValues() {
//
//    }
//
//    void testPrintPossibleValueOccurrences() {
//
//    }
}
