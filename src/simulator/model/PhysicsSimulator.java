package simulator.model;

public class PhysicsSimulator {
    private double _time;
    private ForceLaws _forceLaws;

    public PhysicsSimulator(double time, ForceLaws forceLaws) {
        _time = time;
        _forceLaws = forceLaws;
    }
}
