package simulator.model;

import java.util.List;

public interface SimulatorObserver {
    public void onRegister(List<Body> bodies, double time, double dt, String fLawsDesc);
    public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc);
    public void onBodyAdded(List<Body> bodies, Body b);
    public void onBodyDeleted(List<Body> bodies);
    public void onAdvance(List<Body> bodies, double time);
    public void onDeltaTimeChanged(double dt);
    public void onForceLawsChanged(String fLawsDesc);
    public void onStart();
    public void onStop();
}
