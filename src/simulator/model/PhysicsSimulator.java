package simulator.model;

import java.util.List;
import org.json.JSONObject;

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
            _bodies = new List<Bodies>();
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
        
    }
}
