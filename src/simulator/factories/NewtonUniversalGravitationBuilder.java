package simulator.factories;

import org.json.JSONObject;

import simulator.model.ForceLaws;
import simulator.model.NewtonUniversalGravitation;

public class NewtonUniversalGravitationBuilder extends Builder<ForceLaws> {
    
    public NewtonUniversalGravitationBuilder() {
        _type = "nlug";
		_desc = "Newton's law of universal gravitation";
	}

    protected ForceLaws createTheInstance(JSONObject object) {
		NewtonUniversalGravitation newton = null;
		JSONObject data = new JSONObject();
		data = object.getJSONObject("data"); // data of the object in the variable data

		if (!data.isEmpty()) {
			double g = data.getDouble("G");

			newton = new NewtonUniversalGravitation(g);
		}

		return newton;
	}

    protected JSONObject createData() {
		JSONObject data = new JSONObject();
		// FILL THE OBJECT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		return data;
	}
}
