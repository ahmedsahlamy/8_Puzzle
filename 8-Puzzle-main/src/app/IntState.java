package app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IntState {

    private int level = 0;
    public enum HeuristicsType {
        MANHATTAN, EUCLIDEAN
    }

    private int goalState = 12345678;

    public int byteArrStateToInt(byte[] state) {
        if (state.length != 9)
            throw new IllegalArgumentException();

        int intState = 0;

        for (byte i = 8; i >= 0; i--)
            intState += state[8 - i] * Math.pow(10, i);

        return intState;

    }

    public int stringToIntState(String state) {
        if (state.length() != 9)
            throw new IllegalArgumentException();

        int intState = 0;

        for (byte i = 8; i >= 0; i--)
            intState += (state.charAt(8 - i) - '0') * Math.pow(10, i);

        return intState;
    }

    public byte[] intStateToByteArr(int intState) {
        byte[] state = new byte[9];
        for (byte i = 8; i >= 0; i--) {
            state[i] = (byte) (intState % 10);
            intState /= 10;
        }

        return state;
    }

    public String intStateToString(int intState) {
        StringBuilder stringBuilder = new StringBuilder();

        if (intState < 100000000)
            stringBuilder.append(0);

        stringBuilder.append(intState);
        return stringBuilder.toString();

    }

    public byte getValAtSlot(int intState, byte slotNum) {
        if (slotNum < 0 || slotNum > 8)
            throw new IllegalArgumentException();

        for (slotNum = (byte) (8 - slotNum); slotNum > 0; slotNum--)
            intState /= 10;

        return (byte) (intState%10);
    }

    private int clearSlot(int intState, byte slotNum) {
        byte slotVal = getValAtSlot(intState, slotNum);
        return (int) (intState - slotVal * Math.pow(10, 8 - slotNum));
    }

    public int setValueAtSlot(int intState, byte slotNum, byte val) {
        intState = clearSlot(intState, slotNum);
        return (int) (intState + val * Math.pow(10, 8 - slotNum));
    }

    public byte getValSlotNum(int intState, byte val) {
        for (byte i = 8; i >= 0; i--) {
            if (intState % 10 == val)
                return i;

            intState /= 10;
        }

        return -1;
    }

    public int swapTwoSlots(int intState, byte slot1, byte slot2) {
        byte temp = getValAtSlot(intState, slot1);
        intState = setValueAtSlot(intState, slot1, getValAtSlot(intState, slot2));
        intState = setValueAtSlot(intState, slot2, temp);

        return intState;
    }

    public List<Integer> getNeighborIntStates(int intState) {
        ArrayList<Integer> neighbors = new ArrayList(4);
        byte emptySlotNum = getValSlotNum(intState, (byte) 0);
        byte[] neighborSlots = new byte[]{(byte) (emptySlotNum + 1), (byte) (emptySlotNum - 1),
                (byte) (emptySlotNum + 3), (byte) (emptySlotNum - 3)};

        for (byte currNeighborSlot : neighborSlots) {
            if (
                    currNeighborSlot < 0
                            || currNeighborSlot > 8
                            || emptySlotNum % 3 == 0 && currNeighborSlot == emptySlotNum - 1
                            || (emptySlotNum - 2) % 3 == 0 && currNeighborSlot == emptySlotNum + 1
            ) {continue;}

            neighbors.add(swapTwoSlots(intState, emptySlotNum, currNeighborSlot));
        }

        return neighbors;

    }

    public boolean isGoalState(int intState) {
        return intState == goalState;
    }

    public int getHeuristics(int intState, IntState.HeuristicsType heuristicsType) {
        switch (heuristicsType) {
            case MANHATTAN: return calManhattan(intState);
            case EUCLIDEAN: return calEuclidean(intState);
            default: return 0;
        }
    }


    private int calManhattan(int intState) {
        int h = 0;

        for (int i = 0, actualRow, actualCol, slotNum, currRow, currCol; i < 9; i++) {
            actualRow = i/3;
            actualCol = i%3;
            slotNum = getValSlotNum(intState, (byte) i);
            currRow = slotNum/3;
            currCol = slotNum%3;
            h += Math.abs(actualRow - currRow) + Math.abs(actualCol - currCol);
        }

        return h;
    }

    private int calEuclidean(int intState) {
        int h = 0;

        for (int i = 0, actualRow, actualCol, slotNum, currRow, currCol; i < 9; i++) {
            actualRow = i/3;
            actualCol = i%3;
            slotNum = getValSlotNum(intState, (byte) i);
            currRow = slotNum/3;
            currCol = slotNum%3;
            h += Math.sqrt(Math.pow((actualRow - currRow), 2) + Math.pow((actualCol - currCol), 2));
        }

        return h;
    }

    public int getGoalState() {
        return goalState;
    }
}
