package simulator.factories;

import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.ForceLaws;
import simulator.model.MovingTowardsFixedPoint;

public class MovingTowardsFixedPointBuilder extends Builder<ForceLaws> {

    public MovingTowardsFixedPointBuilder() {
        _type = "mtfp";
		_desc = "Moving towards a fixed point";
    }

    protected ForceLaws createTheInstance(JSONObject data) {

		Double g = 9.81;
    	Vector2D v_c = new Vector2D();

		if (data.has("g"))
			g = data.getDouble("g");

		if (data.has("c"))
			v_c = new Vector2D(data.getJSONArray("c").getDouble(0), data.getJSONArray("c").getDouble(1));

		return new MovingTowardsFixedPoint(v_c, g);

	}
    
    protected JSONObject createData() {
		JSONObject data = new JSONObject();
		data.put("c", "the point towards which bodies move (a json list of 2 numbers)");
		data.put("g", "the length of the acceleration vector (a number)");
		return data;
	}

	public JSONObject getBuilderInfo(){
		JSONObject info = new JSONObject();
		info.put("type", _type);
		info.put("data", createData());
		info.put("desc", _desc);
		return info;
	}
}
