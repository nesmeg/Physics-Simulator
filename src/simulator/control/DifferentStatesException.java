package simulator.control;

import org.json.JSONObject;

public class DifferentStatesException extends Exception {
    private String message = "";

    public DifferentStatesException(JSONObject expState, JSONObject currState, int step) {
        super();
    }
}
