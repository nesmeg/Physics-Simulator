package simulator.model;

import simulator.misc.Vector2D;

public class MassLosingBody extends Body {
    protected double _lossFactor; // Between 0 and 1
    protected double _lossFrequency; // Positive number
    protected double c;

    // TODO: Hay que cambiar el builder del basic body, porque el vector fuerza no se le pasa al constructor, sino que inicialmente está a 0. Hay que cambiarlo en el Body.java, en sus clases hijas y en todas las clases que les afecta, como por ejemplo esta. Al super no hay que pasarle v_force, ni al constructor de MassLosingBody le tiene que venir ese v_force
    public MassLosingBody(String id, Vector2D v_vel, Vector2D v_pos, double mass, double lossFactor, double lossFrequency) {
        super(id, v_vel, v_pos, mass);
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
