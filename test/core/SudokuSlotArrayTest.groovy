package core

/**
 * Created by Jerran on 7/28/2015.
 */
class SudokuSlotArrayTest extends GroovyTestCase {

    SudokuSlotArray testSlotArray
    int arrayLength = 9

    void setUp() {
        super.setUp()
        testSlotArray = new SudokuSlotArray(arrayLength)
    }

    void tearDown() {
        testSlotArray = null
    }

    void testGetSlots() {
        assert testSlotArray.getSlots() != null
        testSlotArray.getSlots().each{slot ->
            assert slot == null
        }

        testSlotArray.addSlot(new SudokuSlot(5,0,0,9),0)
        testSlotArray.getSlots().eachWithIndex{ slot, i ->
            if(i == 0) {
                assert slot.getValue() == 5
            }
        }

    }

    void testAddSlot() {
        testSlotArray.addSlot(new SudokuSlot(5,0,0,9),0)
        testSlotArray.addSlot(new SudokuSlot(2,0,1,9),1)
        testSlotArray.addSlot(new SudokuSlot(7,0,2,9),2)
        testSlotArray.getSlots().eachWithIndex{ slot, i ->
            if(i == 0) {
                assert slot.getValue() == 5
            } else if(i == 1) {
                assert slot.getValue() == 2
            } else if(i == 2) {
                assert slot.getValue() == 7
            }
        }
    }

    void testCalculatePossibleValues() {
        testSlotArray.addSlot(new SudokuSlot(5,0,0,9),0)
        testSlotArray.addSlot(new SudokuSlot(2,0,1,9),1)
        testSlotArray.addSlot(new SudokuSlot(7,0,2,9),2)
        testSlotArray.addSlot(new SudokuSlot(0,0,3,9),3)
        testSlotArray.addSlot(new SudokuSlot(0,0,4,9),4)
        testSlotArray.addSlot(new SudokuSlot(0,0,5,9),5)
        testSlotArray.addSlot(new SudokuSlot(1,0,6,9),6)
        testSlotArray.addSlot(new SudokuSlot(0,0,7,9),7)
        testSlotArray.addSlot(new SudokuSlot(0,0,8,9),8)


        testSlotArray.calculatePossibleValues()


        testSlotArray.getSlots().eachWithIndex{ slot, i ->
            if(i == 0) {
                assert slot.getPossibleValues().size() == 0
            } else if(i == 1) {
                assert slot.getPossibleValues().size() == 0
            } else if(i == 2) {
                assert slot.getPossibleValues().size() == 0
            } else if(i == 3) {
                assert slot.getPossibleValues().size() == 5
                assert slot.getPossibleValues() == [3,4,6,8,9] as Set
            } else if(i == 4) {
                assert slot.getPossibleValues().size() == 5
                assert slot.getPossibleValues() == [3,4,6,8,9] as Set
            } else if(i == 5) {
                assert slot.getPossibleValues().size() == 5
                assert slot.getPossibleValues() == [3,4,6,8,9] as Set
            } else if(i == 6) {
                assert slot.getPossibleValues().size() == 0
            } else if(i == 7) {
                assert slot.getPossibleValues().size() == 5
                assert slot.getPossibleValues() == [3,4,6,8,9] as Set
            } else if(i == 2) {
                assert slot.getPossibleValues().size() == 5
                assert slot.getPossibleValues() == [3,4,6,8,9] as Set
            }
        }
    }

    void testRecalculatePossibleValues() {

    }

    void testCalculatePossibleValueOccurrences() {
        testSlotArray.addSlot(new SudokuSlot(5,0,0,9),0)
        testSlotArray.addSlot(new SudokuSlot(2,0,1,9),1)
        testSlotArray.addSlot(new SudokuSlot(7,0,2,9),2)
        testSlotArray.addSlot(new SudokuSlot(0,0,3,9),3)
        testSlotArray.addSlot(new SudokuSlot(0,0,4,9),4)
        testSlotArray.addSlot(new SudokuSlot(0,0,5,9),5)
        testSlotArray.addSlot(new SudokuSlot(1,0,6,9),6)
        testSlotArray.addSlot(new SudokuSlot(0,0,7,9),7)
        testSlotArray.addSlot(new SudokuSlot(0,0,8,9),8)

        testSlotArray.calculatePossibleValues()
        testSlotArray.calculatePossibleValueOccurrences()

        assert testSlotArray.possibleValueOccurrences.keySet().size() == 5
        assert testSlotArray.possibleValueOccurrences.get(1) == null
        assert testSlotArray.possibleValueOccurrences.get(2) == null
        assert testSlotArray.possibleValueOccurrences.get(3) == 5
        assert testSlotArray.possibleValueOccurrences.get(4) == 5
        assert testSlotArray.possibleValueOccurrences.get(5) == null
        assert testSlotArray.possibleValueOccurrences.get(6) == 5
        assert testSlotArray.possibleValueOccurrences.get(7) == null
        assert testSlotArray.possibleValueOccurrences.get(8) == 5
        assert testSlotArray.possibleValueOccurrences.get(9) == 5
    }

    void testGetPossibleValueOccurrences() {
        assert testSlotArray.getPossibleValueOccurrences() == null

        testSlotArray.addSlot(new SudokuSlot(5,0,0,9),0)
        testSlotArray.addSlot(new SudokuSlot(2,0,1,9),1)
        testSlotArray.addSlot(new SudokuSlot(7,0,2,9),2)
        testSlotArray.addSlot(new SudokuSlot(0,0,3,9),3)
        testSlotArray.addSlot(new SudokuSlot(0,0,4,9),4)
        testSlotArray.addSlot(new SudokuSlot(0,0,5,9),5)
        testSlotArray.addSlot(new SudokuSlot(1,0,6,9),6)
        testSlotArray.addSlot(new SudokuSlot(0,0,7,9),7)
        testSlotArray.addSlot(new SudokuSlot(0,0,8,9),8)

        testSlotArray.calculatePossibleValues()
        testSlotArray.calculatePossibleValueOccurrences()

        assert testSlotArray.getPossibleValueOccurrences() != null

        assert testSlotArray.getPossibleValueOccurrences().keySet() == 5

    }

    void testAssignCorrectValues() {

    }

    void testIsComplete() {
        assert !testSlotArray.isComplete()
        testSlotArray.addSlot(new SudokuSlot(5,0,0,9),0)
        assert !testSlotArray.isComplete()
        testSlotArray.addSlot(new SudokuSlot(2,0,1,9),1)
        assert !testSlotArray.isComplete()
        testSlotArray.addSlot(new SudokuSlot(7,0,2,9),2)
        assert !testSlotArray.isComplete()
        testSlotArray.addSlot(new SudokuSlot(1,0,3,9),3)
        assert !testSlotArray.isComplete()
        testSlotArray.addSlot(new SudokuSlot(9,0,4,9),4)
        assert !testSlotArray.isComplete()
        testSlotArray.addSlot(new SudokuSlot(3,0,5,9),5)
        assert !testSlotArray.isComplete()
        testSlotArray.addSlot(new SudokuSlot(6,0,6,9),6)
        assert !testSlotArray.isComplete()
        testSlotArray.addSlot(new SudokuSlot(8,0,7,9),7)
        assert !testSlotArray.isComplete()
        testSlotArray.addSlot(new SudokuSlot(4,0,8,9),8)
        assert testSlotArray.isComplete()
    }

    void testDecrementPossibleValueCount() {

    }

}
