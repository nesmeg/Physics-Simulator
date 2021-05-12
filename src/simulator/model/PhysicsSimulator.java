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
    private List<SimulatorObserver> _observers;

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
            _observers = new ArrayList<SimulatorObserver>();
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

        // Send notification
        for (SimulatorObserver observer : _observers) {
            observer.onAdvance(_bodies, _current_time);
        }
    }

    public void addBody(Body b) {
        if (_bodies.contains(b)){
            throw new IllegalArgumentException("A body with the same identifier already exists");
        }
        else{
            _bodies.add(b);
        }

        // Send notification
        for (SimulatorObserver observer : _observers) {
            observer.onBodyAdded(_bodies, b);
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
       
        return getState().toString();
    }

    public void reset() {
        _current_time = 0.0;
        _bodies.clear();

        // Send notification
        for (SimulatorObserver observer : _observers) {
            observer.onReset(_bodies, _current_time, _dt, _forceLaws.toString());
        }
    }

    public void setDeltaTime(double dt) throws IllegalArgumentException {
        if (dt > 0) {
            _dt = dt;
        }
        else {
            throw new IllegalArgumentException();
        }
        
        // Send notification
        for (SimulatorObserver observer : _observers) {
            observer.onDeltaTimeChanged(dt);   
        }
    }

    public void setForceLaws(ForceLaws forceLaws) {
        if (forceLaws != null) {
            _forceLaws = forceLaws;
        }
        else {
            throw new IllegalArgumentException();
        }

        // Send notification
        for (SimulatorObserver observer : _observers) {
           observer.onForceLawsChanged(_forceLaws.toString());
        }
    }

    public void addObserver(SimulatorObserver o) {
        if (!_observers.contains(o)) {
            _observers.add(o);
            // Send notification
            o.onRegister(_bodies, _current_time, _dt, _forceLaws.toString());
        }
    }

    public String[] getBodyIds() {
        String[] bodyIds = new String[_bodies.size()];
        
        for (int i = 0; i < bodyIds.length; i++) {
            bodyIds[i] = _bodies.get(i).getId();
        }
        return bodyIds;
    }

    public void delBody(String bodyId) {
        for (Body body : _bodies) {
            if (bodyId.equals(body.getId())) {
                _bodies.remove(body);
                for (SimulatorObserver observer : _observers) {
                    observer.onBodyDeleted(_bodies);
                }
                break;
            }
        }
    }
}
