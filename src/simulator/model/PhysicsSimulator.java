package simulator.model;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import org.json.JSONArray;

public class PhysicsSimulator {
    private double _dt;
    private double _current_time;
    private ForceLaws _forceLaws;
    private List<Body> _bodies;

    public PhysicsSimulator(double deltaTime, ForceLaws forceLaws) throws IllegalArgumentException {
        if (deltaTime <= 0){
            throw new IllegalArgumentException("deltaTime must be a positive value");
        }
        else if (forceLaws == null){
            throw new IllegalArgumentException("forceLaws cannot be null");
        }
        else {
            _dt = deltaTime;
            _current_time = 0;
            _forceLaws = forceLaws;
            _bodies = new ArrayList<Body>();
        }

    }

    public void advance() {
        // 1. resetForce for each body
        for (Body body : _bodies) {
            body.resetForce();
        }
        // 2. Apply force
        _forceLaws.apply(_bodies);
        // 3. move each body
        for (Body body : _bodies) {
            body.move(_dt);
        }
        // 4. Increment current time
        _current_time += _dt;
    }

    public void addBody(Body b) {
        if (_bodies.contains(b)){
            throw new IllegalArgumentException("A body with the same identifier already exists");
        }
        else{
            _bodies.add(b);
        }
    }

    public JSONObject getState() {
        JSONObject state = new JSONObject();
        JSONArray jsonBodies = new JSONArray();
        
        state.put("time", _current_time);
        for (Body body : _bodies) {
            jsonBodies.put(body.getState());
        }
        state.put("bodies", jsonBodies);
        return state;
    }

    public String toString() {
        String bodiesStr = "";

        for (Body body : _bodies) {
            bodiesStr += body.toString();
        }
        
        String toStr;
        toStr = " {" +  " \"time\": \"" + _current_time + "\", \"bodies\": ["  + bodiesStr + "] }";
        return toStr;
    }
}
