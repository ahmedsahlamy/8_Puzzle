package app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class State {

	private byte[] state;
	private int boardSize;

	/**
	 * constructor.
	 * @param state you need to send an optimized state array not the normal one.
	 */
	public State(byte[] state, int boardSize) {
		this.state = state;
		this.boardSize= boardSize;
	}

	public static byte[] convertNormalStateBytesToOptimizedBytes(byte[] state, int boardSize){
		byte[] optimizedState = new byte[(int) Math.ceil(boardSize/2.0)];
		for (int i = 0; i < state.length; i++) {
			optimizedState[i/2] =  (byte) (i % 2 == 0
					? (optimizedState[i/2] & 0b00001111 | (state[i] << 4))
					: (optimizedState[i/2] & 0b11110000 | state[i]));

			if (state[i] == 0)
				optimizedState[4] = (byte) (optimizedState[4] & 0b11110000 | i);

		}

		return optimizedState;
	}

	public byte getSlotVal(byte slotNum) {
		return (byte) (slotNum % 2 == 0 ? (Byte.toUnsignedInt(state[(int)slotNum/2]) >>> 4) : (state[(int)slotNum/2] & 0b00001111));
	}

	public byte getValSlot(byte value) {
		if (value < 0 || value > 8)
			throw new IllegalArgumentException();

		for (byte i = 0; i < state.length; i++) {
			if (getSlotVal(i) == value)
				return i;

		}

		return -1;
	}

	public void setSlotVal(byte slotNum, byte slotVal) {
		if (slotVal < 0 || slotVal > boardSize - 1)
			throw new IllegalArgumentException();

		state[slotNum/2] =  (byte) (slotNum % 2 == 0
				? (state[slotNum/2] & 0b00001111 | (slotVal << 4))
				: (state[slotNum/2] & 0b11110000 | slotVal));
	}



	public byte getEmptySlotNum() {
		return (byte) (state[4] & 0b00001111);
	}


	private void swapTwoSlots(byte fSlotNum, byte sSlotNum) {
		byte tempVal = getSlotVal(fSlotNum);
		setSlotVal(fSlotNum, getSlotVal(sSlotNum));
		setSlotVal(sSlotNum, tempVal);
	}

	public List<State> getNeighborStates() {
		byte emptySlotNum = getEmptySlotNum();
		ArrayList<State> neighborsList = new ArrayList<>();
		byte[] neighborSlots = new byte[]{(byte) (emptySlotNum + 1), (byte) (emptySlotNum - 1),
				(byte) (emptySlotNum + Math.sqrt(boardSize)), (byte) (emptySlotNum - Math.sqrt(boardSize))};

		for (byte currNeighborSlot : neighborSlots) {

			if (
					currNeighborSlot < 0
					|| currNeighborSlot > boardSize - 1
					|| emptySlotNum%Math.sqrt(boardSize) == 0 && currNeighborSlot == emptySlotNum - 1
					|| (emptySlotNum - Math.sqrt(boardSize) + 1)%Math.sqrt(boardSize) == 0 && currNeighborSlot == emptySlotNum + 1
			) {continue;}

			State neighbor = new State(Arrays.copyOf(this.state, this.state.length), this.boardSize);
			neighbor.swapTwoSlots(emptySlotNum, currNeighborSlot);
			neighborsList.add(neighbor);
		}

		return neighborsList;
	}

	public int getBoardSize() {
		return boardSize;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("[ ");

		for (byte i = 0; i < boardSize; i += 2) {
			byte mostSig = (byte) (Byte.toUnsignedInt(state[i/2]) >>> 4);
			stringBuilder.append(mostSig == 0 ? "[]" : mostSig);
			stringBuilder.append(" ");
			byte leastSig = (byte) (Byte.toUnsignedInt(state[i/2]) & 0b00001111);
			if (i != boardSize - 1) {
				stringBuilder.append(leastSig == 0 ? "[]" : leastSig);
				stringBuilder.append(" ");
			}

		}
		stringBuilder.append("]");
		return stringBuilder.toString();
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(state);
	}

	@Override
	public boolean equals(Object state2) {
		byte[] b = ((State) state2).state;
		if (b.length != this.state.length)
			return false;

		for (int i = 0; i < b.length; i++) {
			if (this.state[i] != b[i])
				return false;
		}

		return true;
	}
   
}

