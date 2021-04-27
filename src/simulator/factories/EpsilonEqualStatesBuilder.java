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
		EpsilonEqualStates epseq = null;

		if (!data.isEmpty()) {
			double eps = data.getDouble("eps");

			epseq = new EpsilonEqualStates(eps);
		}
		else {
			// If data is empty, put default values in the constructor
			epseq = new EpsilonEqualStates(0.1);
		}

		return epseq;
	}

    protected JSONObject createData() {
		JSONObject data = new JSONObject();
		data.put("eps", "epsilon");
		return data;
	}
}
