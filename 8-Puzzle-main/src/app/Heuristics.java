package app;

public class Heuristics {

	public enum HeuristicsType {
		NONE, MANHATTAN, EUCLIDEAN;
	}

    public int getHeuristics(State state, HeuristicsType heuristicsType) {
        if (heuristicsType == HeuristicsType.NONE)
            return 0;
        else if (heuristicsType == HeuristicsType.MANHATTAN)
            return calManhattan(state);
        else if (heuristicsType == HeuristicsType.EUCLIDEAN)
            return calEuclidean(state);
        else
            return 0;
    }


    private int calManhattan(State state) {
        int boardRowsNum = (int) Math.sqrt(state.getBoardSize());
        int emptySlotNum = state.getEmptySlotNum();
        int h = Math.abs(boardRowsNum - 1 - emptySlotNum/boardRowsNum) + Math.abs(boardRowsNum - 1 - emptySlotNum%boardRowsNum);

        for (int i = 0, actualRow, actualCol, slotNum, currRow, currCol; i < boardRowsNum; i++) {
            actualRow = i/boardRowsNum;
            actualCol = i%boardRowsNum;
            slotNum = state.getValSlot((byte) (i + 1));
            currRow = slotNum/boardRowsNum;
            currCol = slotNum%boardRowsNum;
            h += Math.abs(actualRow - currRow) + Math.abs(actualCol - currCol);
        }

        return h;
    }

    private int calEuclidean(State state) {
        int boardRowsNum = (int) Math.sqrt(state.getBoardSize());
        int emptySlotNum = state.getEmptySlotNum();
        int h = (int) Math.sqrt(Math.pow((boardRowsNum - 1 - emptySlotNum/boardRowsNum), 2) + Math.pow((boardRowsNum - 1 - emptySlotNum%boardRowsNum), 2));

        for (int i = 0, actualRow, actualCol, slotNum, currRow, currCol; i < state.getBoardSize(); i++) {
            actualRow = i/boardRowsNum;
            actualCol = i%boardRowsNum;
            slotNum = state.getValSlot((byte) (i + 1));
            currRow = slotNum/boardRowsNum;
            currCol = slotNum%boardRowsNum;

            h += Math.sqrt(Math.pow((actualRow - currRow), 2) + Math.pow((actualCol - currCol), 2));
        }

        return h;
    }

}
