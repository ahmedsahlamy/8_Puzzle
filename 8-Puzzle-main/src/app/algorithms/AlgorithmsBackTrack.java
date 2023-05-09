package app.algorithms;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class AlgorithmsBackTrack {

    public static List<Integer> backTrackPath(HashMap<Integer, Integer> parentMap, int goalState) {
        int currState = goalState, parent;
        Vector<Integer> states = new Vector<>();
        while (true) {
            states.add(currState);
            parent = parentMap.get(currState);
            if (currState == parent)
                break;

            currState = parent;
        }

        Collections.reverse(states);
        return states;
    }

}
