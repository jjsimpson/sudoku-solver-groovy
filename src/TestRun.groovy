import core.SudokuBoard

/**
 * Created by jerransimpson on 7/11/15.
 */

def puzzleFile

//puzzleFile = new File('test_puzzle1(easy)')

//puzzleFile = new File('test_puzzle2(medium)')

//puzzleFile = new File('test_puzzle3(easy)')

puzzleFile = new File('test_puzzle4(hard)')

//puzzleFile = new File('test_puzzle5(medium)')

//puzzleFile = new File('test_puzzle6(medium)')


int rowIndex = 0
int[][] initialValues = new int[9][9]
puzzleFile.eachLine { line ->
    String[] row = line.split('\t')
    row.eachWithIndex { numberText, columnIndex ->
        initialValues[rowIndex][columnIndex] = Integer.parseInt(numberText)
    }
    rowIndex++
}

SudokuBoard board = new SudokuBoard(3,3,initialValues)

board.printBoard()
board.solve()
board.printBoard()
board.printPossibleValues()
board.printPossibleValueOccurrences()

