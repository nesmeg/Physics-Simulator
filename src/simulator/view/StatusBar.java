package simulator.view;

import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class StatusBar extends JPanel implements SimulatorObserver {
    // ...
    private JLabel _currTime; // for current time
    private JLabel _currLaws; // for force laws
    private JLabel _numOfBodies; // for number of bodies

    StatusBar(Controller ctrl) {
        initGUI();
        ctrl.addObserver(this);
    }

    private void initGUI() {
        this.setLayout( new FlowLayout( FlowLayout.LEFT ));
        this.setBorder( BorderFactory.createBevelBorder( 1 ));
        // TODO complete the code to build the tool bar
    }

    // other private/protected methods
    // ...

    // SimulatorObserver methods
    // ...
    @Override
    public void onRegister(List<Body> bodies, double time, double dt, String fLawsDesc) {

    }

    @Override
    public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) {

    }
    
    @Override
    public void onBodyAdded(List<Body> bodies, Body b) {

    }
    
    @Override
    public void onAdvance(List<Body> bodies, double time) {

    }
    
    @Override
    public void onDeltaTimeChanged(double dt) {

    }
    
    @Override
    public void onForceLawsChanged(String fLawsDesc) {

    }
}
