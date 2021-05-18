package simulator.factories;

import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.Body;
import simulator.model.MassLosingBody;

public class MassLosingBodyBuilder extends Builder<Body> {
    
  public MassLosingBodyBuilder() {
    _type = "mlb";
    _desc = "Mass losing body";
  }

  protected Body createTheInstance(JSONObject data) {
    MassLosingBody mlb = null;

    // get all the data needed to create a body:
    String id = data.getString("id");
    Vector2D v_pos = new Vector2D(data.getJSONArray("p").getDouble(0), data.getJSONArray("p").getDouble(1));
    Vector2D v_vel = new Vector2D(data.getJSONArray("v").getDouble(0), data.getJSONArray("v").getDouble(1));
    double mass = data.getDouble("m");
    double lossFrequency = data.getDouble("freq");
    double lossFactor = data.getDouble("factor");

    mlb = new MassLosingBody(id, v_vel, v_pos, mass, lossFactor, lossFrequency);

    return mlb;
  }

  protected JSONObject createData() {
		JSONObject data = new JSONObject();
    data.put("id", "body id");
    data.put("v", "body velocity");
    data.put("p", "body position");
    data.put("m", "body mass");
    data.put("factor", "mass loss factor");
    data.put("freq", "mass loss frequency");
		return data;
	}
}