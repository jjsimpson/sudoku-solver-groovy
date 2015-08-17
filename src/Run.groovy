import core.SudokuBoard
import groovy.time.TimeCategory
import groovy.time.TimeDuration

/**
 * Created by jerransimpson on 7/11/15.
 */

def puzzleFile

//puzzleFile = new File('../test/testPuzzles/test_puzzle1(easy)')
//puzzleFile = new File('../test/testPuzzles/test_puzzle2(medium)')
//puzzleFile = new File('../test/testPuzzles/test_puzzle3(easy)')
//puzzleFile = new File('../test/testPuzzles/test_puzzle4(hard)')
//puzzleFile = new File('../test/testPuzzles/test_puzzle5(medium)')
//puzzleFile = new File('../test/testPuzzles/test_puzzle6(medium)')
println file.absolutePath
puzzleFile = new File('../test/testPuzzles/test_puzzle7(medium)')


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
def duration = elapsedTime {
    board.solve()
}
board.printBoard()
println "Solve Time: $duration"
board.printPossibleValues()
board.printPossibleValueOccurrences()






static TimeDuration elapsedTime(Closure closure) {

    // Create a Date to represent just before running code
    def timeStart = new Date()

    // Run the specified code
    closure()

    // Create a date to represent just after running the code
    def timeStop = new Date()

    // Get the difference
    TimeCategory.minus(timeStop, timeStart)
}
