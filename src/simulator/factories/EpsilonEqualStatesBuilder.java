package simulator.factories;

import org.json.JSONObject;

import simulator.control.EpsilonEqualStates;
import simulator.control.StateComparator;

public class EpsilonEqualStatesBuilder extends Builder<StateComparator> {
    
    public EpsilonEqualStatesBuilder() {
        _type = "epseq";
		_desc = "Epsilon equal state";
    }

    public StateComparator createTheInstance(JSONObject object) {
		return new EpsilonEqualStates();
	}

    protected JSONObject createData() {
		/*
		JSONObject data = new JSONObject();
		data.put("eps", "epsilon");
		return data;
		*/
	}
}
