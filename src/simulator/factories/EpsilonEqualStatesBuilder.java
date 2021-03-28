package simulator.factories;

import org.json.JSONObject;

import simulator.control.EpsilonEqualStates;
import simulator.control.StateComparator;

public class EpsilonEqualStatesBuilder extends Builder<StateComparator> {
    
    public EpsilonEqualStatesBuilder() {
        _type = "epseq";
		_desc = "Epsilon equal state";
    }

    protected StateComparator createTheInstance(JSONObject object) {
		EpsilonEqualStates epseq = null;
		JSONObject data = new JSONObject();
		data = object.getJSONObject("data"); // data of the object in the variable data

		if (!data.isEmpty()) {
			double eps = data.getDouble("eps");

			epseq = new EpsilonEqualStates(eps);
		}

		return epseq;
	}

    protected JSONObject createData() {
		JSONObject data = new JSONObject();
		data.put("eps", "epsilon");
		return data;
	}
}
