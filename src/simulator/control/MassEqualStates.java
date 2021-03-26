package simulator.control;

import org.json.JSONObject;

import org.json.JSONArray;

public class MassEqualStates implements StateComparator {
    
    public MassEqualStates() {}

    public boolean equal(JSONObject s1, JSONObject s2) {
        JSONArray bodies1 = s1.getJSONArray("bodies");
        JSONArray bodies2 = s2.getJSONArray("bodies");

        String id1, id2;
        double m1, m2;
        
        if (s1.get("time") == s2.get("time")) {
            
            for (int i = 0; i < bodies1.length(); i++) { // traverse the JSON list
                id1 = bodies1.getJSONObject(i).getJSONObject("data").getString("id");
                id2 = bodies2.getJSONObject(i).getJSONObject("data").getString("id");
                m1 = bodies1.getJSONObject(i).getJSONObject("data").getDouble("m");
                m2 = bodies2.getJSONObject(i).getJSONObject("data").getDouble("m");
                if (!((id1.equals(id2)) && (m1 == m2))) {
                    return false;
                }
            }
        }
        else
            return false;
            
        // if none of the two conditions for it to be false are met, then it is true
        return true;
    }


}


