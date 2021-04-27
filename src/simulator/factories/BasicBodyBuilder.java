package simulator.factories;

import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.Body;

public class BasicBodyBuilder extends Builder<Body>{
    
  public BasicBodyBuilder() {
    _type = "basic";
    _desc = "Basic body";
  }

  protected Body createTheInstance(JSONObject data) {
    Body b = null;

    // get all the data needed to create a body:
    String id = data.getString("id");
    Vector2D v_pos = new Vector2D(data.getJSONArray("p").getDouble(0), data.getJSONArray("p").getDouble(1));
    Vector2D v_vel = new Vector2D(data.getJSONArray("v").getDouble(0), data.getJSONArray("v").getDouble(1)); 
    double mass = data.getDouble("m");

    b = new Body(id, v_vel, v_pos, mass);

    return b;
  }

  protected JSONObject createData() {
		JSONObject data = new JSONObject();
    data.put("id", "body id");
    data.put("vel", "body velocity");
    data.put("force", "body force");
    data.put("pos", "body position");
    data.put("mass", "body mass");
		return data;
	}
}
