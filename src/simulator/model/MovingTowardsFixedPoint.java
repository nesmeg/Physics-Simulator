package simulator.model;

import simulator.misc.Vector2D;

import java.util.List;

public class MovingTowardsFixedPoint implements ForceLaws {
    
    private Vector2D _c;
    private double _g;

    public MovingTowardsFixedPoint(Vector2D c, double g) {
        _c = c;
        _g = g;
    }

    public void apply(List<Body> bodies) {
        for (Body body : bodies) {
            body.addForce(calculateF(body));
        }
    }

    public Vector2D calculateF(Body body) {

        // 1. Calculate d(i) = c - p(i)
        Vector2D d_i = _c.minus(body.getPosition()).direction();

        // 2. Calculate F(i)
        Vector2D F_i = d_i.scale(body.getMass()/_g);

        return F_i;
    }
}
