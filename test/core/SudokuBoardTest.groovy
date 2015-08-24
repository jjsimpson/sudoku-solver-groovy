package core

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
        result[0][2] = [] as Set
        result[0][3] = [1,6,7,9] as Set
        result[0][4] = [4,5,6,7,8,9] as Set
        result[0][5] = [] as Set
        result[0][6] = [1,5,7,8] as Set
        result[0][7] = [1,4,5,6,8,9] as Set
        result[0][8] = [5,7,8,9] as Set

        result[1][0] = [2,8,9] as Set
        result[1][1] = [2,3,5,9] as Set
        result[1][2] = [] as Set
        result[1][3] = [2,3,9] as Set
        result[1][4] = [] as Set
        result[1][5] = [3,5,8,9] as Set
        result[1][6] = [5,8] as Set
        result[1][7] = [] as Set
        result[1][8] = [] as Set

        result[2][0] = [2,6,8,9] as Set
        result[2][1] = [2,3,5,6,9] as Set
        result[2][2] = [] as Set
        result[2][3] = [2,3,6,9] as Set
        result[2][4] = [3,5,6,8,9] as Set
        result[2][5] = [] as Set
        result[2][6] = [] as Set
        result[2][7] = [2,3,5,6,8,9] as Set
        result[2][8] = [2,3,5,8,9] as Set

        result[3][0] = [] as Set
        result[3][1] = [2,3,5,7,9] as Set
        result[3][2] = [5,8,9] as Set
        result[3][3] = [2,3,7,9] as Set
        result[3][4] = [3,5,7,8,9] as Set
        result[3][5] = [3,5,8,9] as Set
        result[3][6] = [] as Set
        result[3][7] = [2,3,5,8,9] as Set
        result[3][8] = [] as Set

        result[4][0] = [1,2,6,7,8,9] as Set
        result[4][1] = [1,2,3,6,7,9] as Set
        result[4][2] = [6,8,9] as Set
        result[4][3] = [] as Set
        result[4][4] = [3,6,7,8,9] as Set
        result[4][5] = [] as Set
        result[4][6] = [1,7,8] as Set
        result[4][7] = [1,2,3,6,8,9] as Set
        result[4][8] = [2,3,7,8,9] as Set

        result[5][0] = [] as Set
        result[5][1] = [1,5,6,7,9] as Set
        result[5][2] = [] as Set
        result[5][3] = [1,6,7,9] as Set
        result[5][4] = [5,6,7,8,9] as Set
        result[5][5] = [1,5,6,8,9] as Set
        result[5][6] = [1,5,7,8] as Set
        result[5][7] = [1,5,6,8,9] as Set
        result[5][8] = [] as Set

        result[6][0] = [1,6,8,9] as Set
        result[6][1] = [1,3,5,6,9] as Set
        result[6][2] = [] as Set
        result[6][3] = [] as Set
        result[6][4] = [3,5,6,8,9] as Set
        result[6][5] = [1,3,5,6,8,9] as Set
        result[6][6] = [] as Set
        result[6][7] = [1,3,5,6,8,9] as Set
        result[6][8] = [3,5,8,9] as Set

        result[7][0] = [] as Set
        result[7][1] = [] as Set
        result[7][2] = [6] as Set
        result[7][3] = [1,3,6,7] as Set
        result[7][4] = [] as Set
        result[7][5] = [1,3,6] as Set
        result[7][6] = [] as Set
        result[7][7] = [1,3,4,6] as Set
        result[7][8] = [3,7] as Set

        result[8][0] = [1,2,6,7,9] as Set
        result[8][1] = [1,2,4,5,6,7,9] as Set
        result[8][2] = [5,6,9] as Set
        result[8][3] = [] as Set
        result[8][4] = [4,5,6,7,9] as Set
        result[8][5] = [1,5,6,9] as Set
        result[8][6] = [] as Set
        result[8][7] = [1,2,4,5,6,9] as Set
        result[8][8] = [2,5,7,9] as Set

        SudokuSlotArray[] rows = testBoard1.getBoardRows()
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

        testBoard1.calculatePossibleValuesByRowsAndColumns()

        rows.eachWithIndex { row, rowIndex ->
            SudokuSlot[] slots = row.getSquares()
            slots.eachWithIndex { slot, columnIndex ->
                assert slot.possibleValues == result[rowIndex][columnIndex]
            }
        }
    }

    //TODO
    void testAssignCorrectValues() {
        testBoard1.calculatePossibleValuesByBlocks()
        testBoard1.calculatePossibleValuesByRowsAndColumns()
        testBoard1.calculatePossibleValueOccurrences()
    }

    //TODO
    void testRecalculatePossibleValues() {

    }

    void testCalculatePossibleValueOccurrences() {
        Map[] rowPossibleValueOccurrences = new Map[9]
        rowPossibleValueOccurrences[0] = [1:2, 4:1, 5:5, 6:4, 7:2, 8:5, 9:6]
        rowPossibleValueOccurrences[1] = [2:2, 3:2, 5:3, 8:3, 9:4]
        rowPossibleValueOccurrences[2] = [2:4, 3:4, 5:4, 6:4, 8:4, 9:6]
        rowPossibleValueOccurrences[3] = [2:2, 3:4, 5:3, 7:3, 8:4, 9:6]
        rowPossibleValueOccurrences[4] = [1:2, 2:2, 3:3, 6:4, 7:5, 8:6, 9:6]
        rowPossibleValueOccurrences[5] = [1:3, 5:3, 6:4, 7:4, 8:4, 9:5]
        rowPossibleValueOccurrences[6] = [1:4, 3:3, 5:4, 6:5, 8:2, 9:4]
        rowPossibleValueOccurrences[7] = [1:3, 3:2, 4:1, 6:4, 7:2]
        rowPossibleValueOccurrences[8] = [1:4, 2:2, 4:2, 5:4, 6:6, 7:2, 9:5]


        Map[] columnPossibleValueOccurrences = new Map[9]
        columnPossibleValueOccurrences[0] = [1:3, 2:3, 6:5, 7:2, 8:4, 9:6]
        columnPossibleValueOccurrences[1] = [1:4, 2:3, 3:1, 4:1, 5:5, 6:6, 7:4, 9:8]
        columnPossibleValueOccurrences[2] = [5:1, 6:3, 8:2, 9:3]
        columnPossibleValueOccurrences[3] = [1:2, 2:1, 3:4, 6:4, 7:3, 9:5]
        columnPossibleValueOccurrences[4] = [3:4, 4:1, 5:4, 6:6, 7:4, 8:5, 9:7]
        columnPossibleValueOccurrences[5] = [1:4, 3:4, 5:3, 6:4, 8:3, 9:5]
        columnPossibleValueOccurrences[6] = [1:1, 5:3, 7:2, 8:4]
        columnPossibleValueOccurrences[7] = [1:4, 2:3, 3:3, 4:2, 5:6, 6:3, 8:6, 9:5]
        columnPossibleValueOccurrences[8] = [2:2, 3:2, 5:4, 7:3, 8:4, 9:3]

        Map[][] blockPossibleValueOccurrences = new Map[3][3]
        blockPossibleValueOccurrences[0][0] = [2:4, 5:3, 6:4, 7:2, 8:3, 9:6]
        blockPossibleValueOccurrences[0][1] = [3:4, 4:1, 5:3, 6:4, 8:3, 9:6]
        blockPossibleValueOccurrences[0][2] = [1:2, 2:2, 3:2, 5:6, 8:6, 9:4]
        blockPossibleValueOccurrences[1][0] = [1:3, 5:3, 6:4, 7:4, 8:3, 9:6]
        blockPossibleValueOccurrences[1][1] = [1:2, 2:1, 3:4, 6:4, 7:5, 8:5, 9:7]
        blockPossibleValueOccurrences[1][2] = [2:3, 3:3, 5:3, 7:3, 8:6, 9:4]
        blockPossibleValueOccurrences[2][0] = [1:4, 2:2, 3:1, 4:1, 6:6, 9:5]
        blockPossibleValueOccurrences[2][1] = [1:4, 3:4, 5:4, 6:6, 7:2, 9:4]
        blockPossibleValueOccurrences[2][2] = [1:3, 4:2, 5:4, 6:3, 7:2, 8:2]

        testBoard1.calculatePossibleValuesByRowsAndColumns()
        testBoard1.calculatePossibleValuesByBlocks()
        testBoard1.calculatePossibleValueOccurrences()

        SudokuBlock[][] boardBlocks = testBoard1.getBoardBlocks()
        SudokuSlotArray[] boardColumns = testBoard1.getBoardColumns()
        SudokuSlotArray[] boardRows = testBoard1.getBoardRows()

        boardRows.eachWithIndex{ row, rowIndex ->
            assert row.getPossibleValueOccurrences() == rowPossibleValueOccurrences[rowIndex]
        }

        boardColumns.eachWithIndex{ column, columnIndex ->
            assert column.getPossibleValueOccurrences() == columnPossibleValueOccurrences[columnIndex]
        }

        boardBlocks.eachWithIndex{ blockRow, rowIndex ->
            blockRow.eachWithIndex { block, columnIndex ->
                assert block.getPossibleValueOccurrences() == blockPossibleValueOccurrences[rowIndex][columnIndex]
            }
        }
    }
}
