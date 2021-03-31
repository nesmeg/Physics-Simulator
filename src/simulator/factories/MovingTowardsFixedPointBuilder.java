package simulator.factories;

import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.ForceLaws;
import simulator.model.MovingTowardsFixedPoint;

public class MovingTowardsFixedPointBuilder extends Builder<ForceLaws> {

    public MovingTowardsFixedPointBuilder() {
        _type = "mtcp";
		_desc = "Moving towards fixed point";
    }

    protected ForceLaws createTheInstance(JSONObject data) {
		MovingTowardsFixedPoint mtfp = null;

		if (!data.isEmpty()) {
			Vector2D v_c = new Vector2D(data.getJSONArray("c").getDouble(0), data.getJSONArray("c").getDouble(1));
			double g = data.getDouble("g");

			mtfp = new MovingTowardsFixedPoint(v_c, g);
		}

		return mtfp;
	}
    
    protected JSONObject createData() {
		JSONObject data = new JSONObject();
		data.put("c", " ");
		data.put("g", "gravitational force");
		return data;
	}
}
