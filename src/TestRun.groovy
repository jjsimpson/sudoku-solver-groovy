import core.SudokuBoard

/**
 * Created by jerransimpson on 7/11/15.
 */

SudokuBoard board = new SudokuBoard(3,3)

def puzzleFile

//puzzleFile = new File('test_puzzle1(easy)')

puzzleFile = new File('test_puzzle2(medium)')

//puzzleFile = new File('test_puzzle3(easy)')


int rowIndex = 0
puzzleFile.eachLine { line ->
    String[] row = line.split('\t')
    row.eachWithIndex { numberText, index ->
        board.assignValue(rowIndex, index, Integer.parseInt(numberText))
    }
    rowIndex++
}



board.printBoard()
def count = 0;
while(!board.isComplete() && count < 11) {
    board.calculatePossibleValuesByBlocks()
    board.calculatePossibleValuesByRowsAndColumns()
    board.assignCorrectValues()
    count++
}
board.printBoard()
//board.printPossibleValues()

