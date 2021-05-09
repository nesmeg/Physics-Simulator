package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

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
        
        _currTime = new JLabel();
        _currLaws = new JLabel();
        _numOfBodies = new JLabel();
    }

    private void updateStatusBar() {
        this.removeAll(); // clear previous data and separators to repaint new ones

        this.add(_numOfBodies);
        this.add(createVerticalSeparator());
        this.add(createSpaceSeparator());
		this.add(_currLaws);
        this.add(createVerticalSeparator());
        this.add(createSpaceSeparator());
		this.add(_currTime);
    }

    private static JComponent createVerticalSeparator() {
        JSeparator x = new JSeparator(SwingConstants.VERTICAL);
        x.setPreferredSize(new Dimension(100,0));
        return x;
    }

    private static JComponent createSpaceSeparator() {
        JSeparator x = new JSeparator(SwingConstants.VERTICAL);
        x.setPreferredSize(new Dimension(3,20));
        return x;
    }

    // SimulatorObserver methods
    // ...
    @Override
    public void onRegister(List<Body> bodies, double time, double dt, String fLawsDesc) {
        _currLaws.setText("Laws: " + fLawsDesc);
        _currTime.setText("Time: " + Double.toString(time));
        _numOfBodies.setText("Bodies: " + Integer.toString(bodies.size()));
        
        updateStatusBar();
    }

    @Override
    public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) {
        _currLaws.setText("Laws: " + fLawsDesc);
        _currTime.setText("Time: " + Double.toString(time));
        _numOfBodies.setText("Bodies: " + Integer.toString(bodies.size()));

        updateStatusBar();
    }
    
    @Override
    public void onBodyAdded(List<Body> bodies, Body b) {
        _numOfBodies.setText("Bodies: " + Integer.toString(bodies.size()));

        updateStatusBar();
    }
    
    @Override
    public void onAdvance(List<Body> bodies, double time) {
        _currTime.setText("Time: " + Double.toString(time));

        updateStatusBar();
    }
    
    @Override
    public void onDeltaTimeChanged(double dt) {
        updateStatusBar();
    }
    
    @Override
    public void onForceLawsChanged(String fLawsDesc) {
        _currLaws.setText("Laws: " + fLawsDesc);

        updateStatusBar();
    }
}
