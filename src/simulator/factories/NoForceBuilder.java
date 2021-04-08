package simulator.factories;

import org.json.JSONObject;

    import simulator.model.ForceLaws;
    import simulator.model.NoForce;
    
    public class NoForceBuilder extends Builder<ForceLaws> {
    
  public NoForceBuilder() {
    _type = "ng";
    _desc = "No force";
  }

  protected ForceLaws createTheInstance(JSONObject object) {
    return new NoForce();
  }

  protected JSONObject createData() {
		JSONObject data = new JSONObject();
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
