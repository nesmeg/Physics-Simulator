package simulator.factories;

import org.json.JSONObject;

import simulator.control.EpsilonEqualStates;
import simulator.control.StateComparator;

public class EpsilonEqualStatesBuilder extends Builder<StateComparator> {
    
    public EpsilonEqualStatesBuilder() {
        _type = "epseq";
		_desc = "Epsilon equal state";
    }

    protected StateComparator createTheInstance(JSONObject data) {
		double eps = 0.1;

		if (data.has("eps"))
			eps = data.getDouble("eps");

		return new EpsilonEqualStates(eps);
	}

    protected JSONObject createData() {
		JSONObject data = new JSONObject();
		data.put("eps", "epsilon");
		return data;
	}
}
