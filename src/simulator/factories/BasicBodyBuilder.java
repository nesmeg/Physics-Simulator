package simulator.factories;

import org.json.JSONObject;

import simulator.model.Body;

public class BasicBodyBuilder extends Builder<Body>{
    
    public BasicBodyBuilder() {
        _type = "basic";
		_desc = "Basic body";
    }

    protected Body createTheInstance(JSONObject object) {

    }

    protected JSONObject createData() {
		JSONObject data = new JSONObject();
		// FILL THE OBJECT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		return data;
	}
}
