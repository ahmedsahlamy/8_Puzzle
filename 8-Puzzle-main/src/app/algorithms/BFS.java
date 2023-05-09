package app.algorithms;

import app.IntState;
import java.util.*;


public class BFS {

    private int maxDepth;
    private Queue<Integer> frontier = new LinkedList<>();
    private HashSet<Integer> explored = new HashSet<>();
    private HashMap<Integer, Integer> parentMap = new HashMap<>();

    public List<Integer> BFS(int initialState) {
        frontier.clear();
        explored.clear();
        parentMap.clear();
        HashMap<Integer, Integer> depth_map = new HashMap<>();
        IntState intState = new IntState();
        frontier.add(initialState);
        parentMap.put(initialState, initialState);
        this.maxDepth = 0;
        depth_map.put(initialState, 0);
        boolean goalFound = false;
        int currState;
        while (!frontier.isEmpty()) {
            currState = frontier.poll();
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
