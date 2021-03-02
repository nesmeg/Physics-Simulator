package simulator.model;

import simulator.misc.Vector2D;

public class MassLosingBody extends Body {
    protected double _lossFactor; // Between 0 and 1
    protected double _lossFrequency; // Positive number
    protected double c;

    public MassLosingBody(String id, Vector2D v_vel, Vector2D v_force, Vector2D v_pos, double mass, double lossFactor, double lossFrequency) {
        super(id, v_vel, v_force, v_pos, mass);
        _lossFactor = lossFactor;
        _lossFrequency = lossFrequency;
        c = 0.0;
    }

    public void move(double t) {
        super.move(t);

        c = c + t;
        if (c >= _lossFrequency) {
            _mass = _mass * (1 - _lossFactor);
            c = 0.0;
        }
    }
}
