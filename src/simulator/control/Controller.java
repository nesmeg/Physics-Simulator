package simulator.control;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import java.io.InputStream;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.ForceLaws;
import simulator.model.PhysicsSimulator;
import simulator.model.SimulatorObserver;
import simulator.model.Body;

public class Controller {
    private PhysicsSimulator _sim;
    private Factory<ForceLaws> _forceLawsFactory;
    private Factory<Body> _bodiesFactory;

    public Controller(PhysicsSimulator sim, Factory<ForceLaws> forceLawsFactory, Factory<Body> bodiesFactory) {
        this._sim = sim;
        _forceLawsFactory = forceLawsFactory;
        _bodiesFactory = bodiesFactory;
    }

    public void loadBodies(InputStream in) {
        JSONObject jsonInput = new JSONObject(new JSONTokener(in)); // parse the whole InputSream into a JSONObject
        
        JSONArray bodies = jsonInput.getJSONArray("bodies");

        for (int i = 0; i < bodies.length(); i++) { // traverse the JSON list
            _sim.addBody(_bodiesFactory.createInstance(bodies.getJSONObject(i))); // create the bodies and add them to the simulator
        }
    }

    public void run(int steps, OutputStream out, InputStream expOut, StateComparator cmp) throws DifferentStatesException {
        JSONObject expOutJO = null;
        if (expOut != null) {
            // To convert the inputStream into a JSONObject we need to encapsulate it in a JSONTokener object
            expOutJO = new JSONObject(new JSONTokener(expOut));
        }

        if (out == null) {
            out = new OutputStream() {
                public void write(int b) throws IOException {}
            };
        }

        PrintStream p = new PrintStream(out);
        p.println("{");
        p.println("\"states\": [");

        JSONObject currState = null;
        JSONObject expState = null;

        // State 0
        currState = _sim.getState();
        p.println(currState);
        if (expOutJO != null) {
            expState = expOutJO.getJSONArray("states").getJSONObject(0);
            if (!cmp.equal(expState, currState)) {
                throw new DifferentStatesException(expState, currState, 0);
            }
        }

        // Rest of states
        for (int i = 1; i <= steps; i++) {
            _sim.advance();
            currState = _sim.getState();
            p.print(",");
            p.println(currState);

            if (expOutJO != null) {
                expState = expOutJO.getJSONArray("states").getJSONObject(i);
                if (!cmp.equal(expState, currState)) {
                    throw new DifferentStatesException(expState, currState, i);
                }
            }
        }
        p.println("]");
        p.println("}");
    }

    public void reset(){
        _sim.reset();
    }

    public void setDeltaTime(double dt) {
        _sim.setDeltaTime(dt);
    }

    public void addObserver(SimulatorObserver o) {
        _sim.addObserver(o);
    }

    public void run(int n) {
        for (int i = 0; i < n; i++) {
            _sim.advance();
        }
    }

    public List<JSONObject> getForceLawsInfo() {
        return _forceLawsFactory.getInfo();
    }

    public List<JSONObject> getBodiesInfo() {
        return _bodiesFactory.getInfo();
    }

    public void setForceLaws(JSONObject info) {
        ForceLaws forceLaws;
        forceLaws = _forceLawsFactory.createInstance(info);
        _sim.setForceLaws(forceLaws);
    }

    public String[] getBodyIds(){
        return _sim.getBodyIds();
    }

    public void delBody(String bodyId) {
        _sim.delBody(bodyId);
    }

    public void onStart(){
        _sim.onStart();
    }

    public void onStop(){
        _sim.onStop();
    }

    public void addBody(JSONObject body) {
        _sim.addBody(_bodiesFactory.createInstance(body));
    }
}