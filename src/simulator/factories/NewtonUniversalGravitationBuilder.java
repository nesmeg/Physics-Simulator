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
		NewtonUniversalGravitation newton = null;

		if (!data.isEmpty()) {
			double g = data.getDouble("G");

			newton = new NewtonUniversalGravitation(g);
		}

		return newton;
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
