package simulator.factories;

import org.json.JSONObject;

import simulator.model.Body;

public class MassLosingBodyBuilder extends Builder<Body> {
    
  public MassLosingBodyBuilder() {
    _type = "mlb";
    _desc = "Mass losing body";
  }

  protected Body createTheInstance(JSONObject object) {

  }

  protected JSONObject createData() {
		JSONObject data = new JSONObject();
		// FILL THE OBJECT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		return data;
	}
}