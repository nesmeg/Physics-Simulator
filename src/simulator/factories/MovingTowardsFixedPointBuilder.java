package simulator.factories;

import org.json.JSONObject;

import simulator.model.ForceLaws;
import simulator.model.MovingTowardsFixedPoint;

public class MovingTowardsFixedPointBuilder extends Builder<ForceLaws> {

    public MovingTowardsFixedPointBuilder() {
        _type = "mtcp";
		_desc = "Moving towards fixed point";
    }

    public ForceLaws createTheInstance(JSONObject object) {
		return new MovingTowardsFixedPoint(); // missing to pass c and g as parameters
	}
    
    protected JSONObject createData() {
		JSONObject data = new JSONObject();
		// FILL THE OBJECT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		return data;
	}
}
