package app.algorithms;

import app.IntState;

import java.util.*;

public class A_STAR {

	private int maxDepth;
	private HashMap<Integer, Integer> parentMap = new HashMap<>();
	private class StateHeuristicHolder {
		private int state;
		private byte g;
		private byte h;

		private StateHeuristicHolder(int state, byte g, byte h) {
			this.state = state;
			this.g = g;
			this.h = h;
		}

		public int getState() {
			return state;
		}

		public byte getG() {
			return g;
		}

		public byte getH() {
			return h;
		}

		public byte getHeuristic() {
			return (byte) (h + g);
		}
	}

	private class HeuristicComparator implements Comparator<StateHeuristicHolder> {
		@Override
		public int compare(StateHeuristicHolder o1, StateHeuristicHolder o2) {
			return o1.getHeuristic() - o2.getHeuristic();
		}
	}


	public List<Integer> AStar(int initialState, IntState.HeuristicsType heuristicsType) {
		HeuristicComparator comparator = new HeuristicComparator();
		PriorityQueue<StateHeuristicHolder> frontier = new PriorityQueue<StateHeuristicHolder>(comparator);
		HashSet<Integer> explored = new HashSet<>();
		HashMap<Integer, Integer> depth_map = new HashMap<>();
		IntState intState = new IntState();
		frontier.add(new StateHeuristicHolder(initialState, (byte) 0, (byte) 0));
		parentMap.put(initialState, initialState);
		depth_map.put(initialState, 0);
		boolean goalFound = false;
		StateHeuristicHolder currStateHeuristicHolder;
		int currState;
		this.maxDepth = 0;
		while (!frontier.isEmpty()) {
			currStateHeuristicHolder = frontier.poll();
			currState = currStateHeuristicHolder.getState();
			if (explored.contains(currState))
				continue;
			else if (intState.isGoalState(currState)) {
				goalFound = true;
				break;
			}
			int depth = depth_map.get(currStateHeuristicHolder.state);
			if (this.maxDepth < depth){
				this.maxDepth = depth;
			}
			explored.add(currState);
			List<Integer> neighbors = intState.getNeighborIntStates(currState);
			for (int n : neighbors) {
				if (explored.contains(n))
					continue;

				frontier.add(new StateHeuristicHolder(n, (byte) (currStateHeuristicHolder.getG() + 1), (byte) (intState.getHeuristics(n, heuristicsType))));
				if (depth_map.containsKey(n)){
					if (depth_map.get(n) > (currStateHeuristicHolder.getG() + 1)){
						depth_map.put(n,(currStateHeuristicHolder.getG() + 1));
						parentMap.put(n, currState);
					}
				}else{
					depth_map.put(n,(currStateHeuristicHolder.getG() + 1));
					parentMap.put(n, currState);
				}

			}
		}

		return goalFound ? AlgorithmsBackTrack.backTrackPath(parentMap, intState.getGoalState()) : null;
	}

	public int getNumberOfExpanded(){
		return this.parentMap.size();
	}

	public int getMaxDepth(){
		return this.maxDepth;
	}


}
