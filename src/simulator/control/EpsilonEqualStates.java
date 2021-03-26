package simulator.control;

import org.json.JSONObject;

import simulator.misc.Vector2D;

import org.json.JSONArray;

public class EpsilonEqualStates implements StateComparator {
    
    private double _eps;

    public EpsilonEqualStates(double eps) {
        _eps = eps;
    }

    // SHOULD WE IMPLEMENT 3 DIFFERENT FUNCTIONS, ONE FOR EACH TYPE OF PARAMETERS????????????????????????????
    public boolean equal(JSONObject s1, JSONObject s2) {
        JSONArray bodies1 = s1.getJSONArray("bodies");
        JSONArray bodies2 = s2.getJSONArray("bodies");

        String id1, id2;
        JSONArray p1, p2;
        JSONArray v1, v2;
        JSONArray f1, f2;
        double m1, m2;
        
        if (s1.get("time") == s2.get("time")) {
            
            for (int i = 0; i < bodies1.length(); i++) { // traverse the JSON list
                id1 = bodies1.getJSONObject(i).getJSONObject("data").getString("id");
                id2 = bodies2.getJSONObject(i).getJSONObject("data").getString("id");
                m1 = bodies1.getJSONObject(i).getJSONObject("data").getDouble("m");
                m2 = bodies2.getJSONObject(i).getJSONObject("data").getDouble("m");
                p1 = bodies1.getJSONObject(i).getJSONObject("data").getJSONArray("p");
                p2 = bodies2.getJSONObject(i).getJSONObject("data").getJSONArray("p");
                v1 = bodies1.getJSONObject(i).getJSONObject("data").getJSONArray("v");
                v2 = bodies2.getJSONObject(i).getJSONObject("data").getJSONArray("v");
                f1 = bodies1.getJSONObject(i).getJSONObject("data").getJSONArray("f");
                f2 = bodies2.getJSONObject(i).getJSONObject("data").getJSONArray("f");

                // check equality of: id, mass, position, velocity and force
                if (!((epsEqualId(id1, id2)) && epsEqualNumbers(m1, m2)
                        && epsEqualVectors(p1.getDouble(0), p1.getDouble(1), p2.getDouble(0), p2.getDouble(1))
                        && epsEqualVectors(v1.getDouble(0), v1.getDouble(1), v2.getDouble(0), v2.getDouble(1))
                        && epsEqualVectors(f1.getDouble(0), f1.getDouble(1), f2.getDouble(0), f2.getDouble(1)))) {
                    return false;
                }
            }
        }
        else // if key "time" is not equal, return false
            return false;
            
        // if none of the two conditions for it to be false are met, then it is true
        return true;
    }

    public boolean epsEqualVectors(double x1, double y1, double x2, double y2){
        Vector2D v1 = new Vector2D(x1, y1);
        Vector2D v2 = new Vector2D(x2, y2);
        
        return (v1.distanceTo(v2) <= _eps);
    }

    public boolean epsEqualNumbers(double num1, double num2) {
        return (Math.abs(num1-num2) <= _eps);
    }

    public boolean epsEqualId(String id1, String id2) {
        return (id1.equals(id2));
    }
}
