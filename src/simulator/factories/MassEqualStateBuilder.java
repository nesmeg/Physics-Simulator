package simulator.factories;

import org.json.JSONObject;

import simulator.control.MassEqualStates;
import simulator.control.StateComparator;

public class MassEqualStateBuilder extends Builder<StateComparator> {

    public MassEqualStateBuilder() {
		_type = "masseq";
		_desc = "Mass equal state";
	}

    public StateComparator createTheInstance(JSONObject object) {
		return new MassEqualStates();
	}
}
