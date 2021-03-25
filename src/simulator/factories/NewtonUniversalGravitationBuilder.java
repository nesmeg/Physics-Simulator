package simulator.factories;

import org.json.JSONObject;

import simulator.model.ForceLaws;
import simulator.model.NewtonUniversalGravitation;

public class NewtonUniversalGravitationBuilder extends Builder<ForceLaws> {
    
    public NewtonUniversalGravitationBuilder() {
        _type = "nlug";
		_desc = "Newton's law of universal gravitation";
	}

    public ForceLaws createTheInstance(JSONObject object) {
		return new NewtonUniversalGravitation(); // missing to pass the G as parameter
	}

    protected JSONObject createData() {
		/*
		JSONObject data = new JSONObject();
		// FILL THE OBJECT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		return data;
		*/
	}
}
