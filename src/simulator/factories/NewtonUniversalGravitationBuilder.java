package simulator.factories;

import org.json.JSONObject;

import simulator.model.ForceLaws;
import simulator.model.NewtonUniversalGravitation;

public class NewtonUniversalGravitationBuilder extends Builder<ForceLaws> {
    
    public NewtonUniversalGravitationBuilder() {
        _type = "nlug";
		_desc = "Newton's law of universal gravitation";
	}

    protected ForceLaws createTheInstance(JSONObject data) {
		double g = 6.67E-11;

		if (data.has("G"))
			g = data.getDouble("G");

		return new NewtonUniversalGravitation(g);
	}

    protected JSONObject createData() {
		JSONObject data = new JSONObject();
		data.put("G", "the gravitational constant (a number)");
		return data;
	}

	public JSONObject getBuilderInfo() {
		JSONObject info = new JSONObject();
		info.put("type", _type);
		info.put("data", createData());
		info.put("desc", _desc);
		return info;
	}
}
