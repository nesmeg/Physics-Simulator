package simulator.control;

import org.json.JSONObject;

public class DifferentStatesException extends Exception {

    public DifferentStatesException(JSONObject expState, JSONObject currState, int step) {
        super("Expected state: " + expState.toString() + "\n"
                + "Current state: " + expState.toString() + "\n"
                + "Execution step number: " + Integer.toString(step));
    }
}
