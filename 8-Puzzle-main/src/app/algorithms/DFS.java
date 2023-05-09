package app.algorithms;

import app.IntState;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

public class DFS {

    private int maxDepth;
    private Stack<Integer> frontier = new Stack<>();
    private HashSet<Integer> explored = new HashSet<>();
    private HashMap<Integer, Integer> parentMap = new HashMap<>();

    public List<Integer> DFS(int initialState) {
        frontier.clear();
        explored.clear();
        parentMap.clear();
        HashMap<Integer, Integer> depth_map = new HashMap<>();
        IntState intState = new IntState();
        frontier.add(initialState);
        parentMap.put(initialState, initialState);
        depth_map.put(initialState, 0);
        boolean goalFound = false;
        int currState;
        this.maxDepth = 0;
        while (!frontier.isEmpty()) {
            currState = frontier.pop();
            if (explored.contains(currState))
                continue;
            else if (intState.isGoalState(currState)) {
                goalFound = true;
                break;
            }

            explored.add(currState);
            List<Integer> neighbors = intState.getNeighborIntStates(currState);
            int dep = depth_map.get(currState);
            for (int n : neighbors) {
                if (explored.contains(n))
                    continue;
                depth_map.put(n, dep+1);
                if (dep + 1 > this.maxDepth)
                    this.maxDepth = dep+1;
                frontier.add(n);
                parentMap.put(n, currState);
            }

        }

        return goalFound ? AlgorithmsBackTrack.backTrackPath(parentMap, intState.getGoalState()) : null;
    }

    public int getNumberOfExpanded(){
        return this.parentMap.size();
    }

    public int getMaxDepth() {
        return maxDepth;
    }
}