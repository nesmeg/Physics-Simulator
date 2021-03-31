package simulator.model;

import java.util.List;

import simulator.misc.Vector2D;

public class NewtonUniversalGravitation implements ForceLaws {
    
    private double _G;

    public NewtonUniversalGravitation(double G) {
        _G = G;
    }

    public void apply(List<Body> bodies) {
        for (Body body : bodies) {
            for (Body body2 : bodies) {
                body.addForce(calculateF(body, body2));
            }
        }
    }

    public Vector2D calculateF(Body body_i, Body body_j) {

        // 1. Calculate d(i,j)
        Vector2D d_ij = body_j.getPosition().minus(body_i.getPosition());
        
        // 2. Calculate f(i,j)
        double f_ij;
        if (d_ij.magnitude() > 0) {
            f_ij = _G * ((body_i.getMass() * body_j.getMass()) / (Math.pow(d_ij.magnitude(), 2)));
        }
        else
            f_ij = 0;

        // 3. Calculate F(i,j)
        d_ij = d_ij.direction();
        Vector2D F_ij = d_ij.scale(f_ij);
        
        return F_ij;
    }
}
