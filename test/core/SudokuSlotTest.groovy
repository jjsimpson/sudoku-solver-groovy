package core

/**
 * Created by Jerran on 7/28/2015.
 */
class SudokuSlotTest extends GroovyTestCase {

    SudokuSlot testBlankSlot
    int boardSize = 9


    void setUp() {
        super.setUp()
        testBlankSlot = new SudokuSlot(4,7, boardSize)
    }

    void tearDown() {
        testBlankSlot = null
    }

    void testSetValue() {
        //set new blank slot to 5
        testBlankSlot.setValue(5)
        assert testBlankSlot.getValue() == 5

        //assign a new value to the slot and confirm the new value gets assigned
        testBlankSlot.setValue(8)
        assert testBlankSlot.getValue() == 8
    }

    void testGetRowIndex() {
        assert testBlankSlot.getRowIndex() == 4
    }

    void testGetColumnIndex() {
        assert testBlankSlot.getColumnIndex() == 7
    }

    void testRemovePossibleValue() {
        assert testBlankSlot.getPossibleValues() == 1..boardSize as Set<Integer>

        testBlankSlot.removePossibleValue(9)
        assert testBlankSlot.getPossibleValues() == 1..8 as Set<Integer>

        testBlankSlot.removePossibleValue(5)
        assert testBlankSlot.getPossibleValues() == [1,2,3,4,6,7,8] as Set<Integer>
    }

    void testGetPossibleValues() {
        assert testBlankSlot.getPossibleValues() == 1..boardSize as Set<Integer>
    }

    void testGetValue() {
        assert testBlankSlot.getValue() == 0

        testBlankSlot.setValue(5)
        assert testBlankSlot.getValue() == 5

        testBlankSlot.setValue(8)
        assert testBlankSlot.getValue() == 8
    }

    void testHasValue() {
        assert testBlankSlot.getValue() == 0
        assert !testBlankSlot.hasValue()

        testBlankSlot.setValue(7)
        assert testBlankSlot.hasValue()
    }

    void testToString() {
        assert testBlankSlot.toString() == '0'
        testBlankSlot.setValue(3)
        assert testBlankSlot.toString() == '3'
    }
}
