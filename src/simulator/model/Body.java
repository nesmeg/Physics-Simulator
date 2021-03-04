package simulator.model;

import org.json.JSONObject;

import simulator.misc.Vector2D;

public class Body {
    protected String _id;
    protected Vector2D _v_vel;
    protected Vector2D _v_force;
    protected Vector2D _v_pos;
    protected double _mass;

    public Body (String id, Vector2D v_vel, Vector2D v_force, Vector2D v_pos, double mass) {
        _id = id;
        _v_vel = v_vel;
        _v_force = v_force;
        _v_pos = v_pos;
        _mass = mass;
    }

    public String getId() {
        return _id;
    }

    public Vector2D getVelocity() {
        return _v_vel;
    }

    public Vector2D getPosition() {
        return _v_pos;
    }

    public Vector2D getForce() {
        return _v_force;
    }

    public double getMass() {
        return _mass;
    }

    void addForce(Vector2D f) {
        _v_force = _v_force.plus(f);
    }

    void resetForce() {
        //_v_force = _v_force.minus(_v_force);
        _v_force = _v_force.scale(0);
    }

    void move(double t) {
        // 1. Compute acceleration
        Vector2D v_acc = _v_force.scale(1/_mass);   // a = f / m
        // 2. Change position
        _v_pos = _v_pos.plus(_v_vel.scale(t)).plus(v_acc.scale(0.5 * t * t));   // p = p0 + v*t + (1/2)*a*(t^2)
        // 3. Change velocity
        _v_vel = _v_vel.plus(v_acc.scale(t));   // v = v0 + a*t
    }

    public JSONObject getState() {
        JSONObject state = new JSONObject();
        state.put("id", _id);
        state.put("m", _mass);
        state.put("p", _v_pos);
        state.put("v", _v_vel);
        state.put("f", _v_force);
        return state;
    }

    public String toString() {
        //return getState().toString();
        // Output example of this method:
        // {"p":"[3.5,5.0]","v":"[4.0,7.0]","f":"[5.0,6.0]","id":"1","m":3.45}

        // I THINK WE NEED THIS SECOND METHOD!!!
        String toStr;
        toStr = " {" +  " \"id\": \"" + _id + "\", \"m\": "  + _mass + ", \"p\": " + _v_pos +  ", \"v\": " + _v_vel + ", \"f\": " + _v_force + " }" ;
        return toStr;
        // Output example of this method:
        //  { "id": "1", "m": 3.45, "p": [3.5,5.0], "v": [4.0,7.0], "f": [5.0,6.0] }
    }

    public boolean equals(Body b){
        return b.getId().equals(this._id); 
    }
}
