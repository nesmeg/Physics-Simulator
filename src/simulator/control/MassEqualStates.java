package simulator.control;

import org.json.JSONObject;

import simulator.model.Body;

import org.json.JSONArray;
import org.json.JSONArray;
import java.util.ArrayList;

public class MassEqualStates implements StateComparator {
    public MassEqualStates() {

    }

    public boolean equal(JSONObject s1, JSONObject s2) {
        boolean equals;
        JSONArray bodies1 = new JSONArray(s1.get("bodies"));
        JSONArray bodies2 = new JSONArray(s2.get("bodies"));
        
        ArrayList body1 = new ArrayList<Body>(s1.get("bodies"));
        if (s1.get("time") == s2.get("time")) {
            for (int i = 0; i < array.length; i++) {
                
            }
            for (JSONArray array : bodies1) {
                
            }
            if (bodies1.equals(bodies2)) {
                equals = true;
            }
        }
        else
            equals = false;
            
        return equals;
    }
}
