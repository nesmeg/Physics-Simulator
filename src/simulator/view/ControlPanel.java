package simulator.view;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import javax.swing.*;

import org.json.JSONObject;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class ControlPanel extends JPanel implements SimulatorObserver {
    // ...
    private Controller _ctrl;
    private boolean _stopped;
    private JButton _loadFileBtn;
    private JButton _modifyBtn;
    private JButton _startBtn;
    private JButton _stopBtn;
    private JButton _exitBtn;
    private JSpinner _steps;
    private JTextField _deltaTime;

    ControlPanel(Controller ctrl) {
        _ctrl = ctrl;
        _stopped = true;
        initGUI();
        _ctrl.addObserver(this);
    }

    private void initGUI() {
        // TODO build the tool bar by adding buttons, etc.

        // CREATION, IMAGE, TEXT AND FUNCTION ASSIGNMENT TO THE BUTTONS
        // LOAD FILE BUTTON
        _loadFileBtn = new JButton();
        _loadFileBtn.setIcon(new ImageIcon("resources/icons/open.png"));
        _loadFileBtn.setToolTipText("Open a file");
        _loadFileBtn.addActionListener((e) -> loadFile());

        // MODIFY DATA BUTTON
        _modifyBtn = new JButton();
        _modifyBtn.setIcon(new ImageIcon("resources/icons/physics.png"));
        _modifyBtn.setToolTipText("Modify the data of magnitudes and bodies");
        _modifyBtn.addActionListener((e) -> modifyData());

        // START BUTTON
        _startBtn = new JButton();
        _startBtn.setIcon(new ImageIcon("resources/icons/run.png"));
        _startBtn.setToolTipText("Start the execution");
        _startBtn.addActionListener((e) -> start());

        // STOP BUTTON
        _stopBtn = new JButton();
        _stopBtn.setIcon(new ImageIcon("resources/icons/stop.png"));
        _stopBtn.setToolTipText("Stop the execution");
        _stopBtn.addActionListener((e) -> stop());

        // STEPS
        _steps = new JSpinner();
        _steps.setToolTipText("Simulation steps to execute");

        // DELTA TIME
        _deltaTime = new JTextField();
        _deltaTime.setText("2500");
        _deltaTime.setToolTipText("Delta time");

        // EXIT BUTTON
        _exitBtn = new JButton();
        _exitBtn.setIcon(new ImageIcon("resources/icons/exit.png"));
        _exitBtn.setToolTipText("Close the application");
        _exitBtn.addActionListener((e) -> exit());

        // Collocation of buttons
        JPanel leftButtons = new JPanel();
        leftButtons.add(_loadFileBtn);
        leftButtons.add(_modifyBtn);
        leftButtons.add(_startBtn);
        leftButtons.add(_stopBtn);
        leftButtons.add(_steps);
        leftButtons.add(_deltaTime);
        
        JPanel rightButtons = new JPanel();
        rightButtons.add(_stopBtn);

        this.add(leftButtons, BorderLayout.WEST);
        this.add(rightButtons, BorderLayout.EAST);
    }
    
    // other private/protected methods
    // ...

    // IMPLEMENTATION OF THE BUTTONS FUNCTIONALITY
    private void loadFile() {
        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir") + "resources/examples");
        // Im not sure that the previous parameters work, but it should directly open the user folder where the json examples are saved
        
        int ret = fileChooser.showOpenDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            System.out.println("Loading: " + file.getName());
            try {
                _ctrl.reset();
                _ctrl.loadBodies(new FileInputStream(file));                
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(getParent(), "Error while opening the file", "Uh-oh...", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            System.out.println("File load cancelled by user.");
        }
        
    }

    private void modifyData() {

        // 1. Open dialog box to select the physic law
        JFrame frame = new JFrame("Force Laws Selection");
        List<JSONObject> laws = _ctrl.getForceLawsInfo();
        String[] forces = new String[laws.size()];
        JSONObject chosenLaw = null;
        String headerMesssage = "Select a force law and provide values for the parameters in the Value column (default values are used for parameters with no value).";
        
        //--------------------JCOMBOBOX----------------------------------\\
        for (int i = 0; i < laws.size(); i++) {
            forces[i] = laws.get(i).getString("desc");
        }

        String chosen = (String) JOptionPane.showInputDialog(this, headerMesssage, "Force Laws Selection",
                                        JOptionPane.PLAIN_MESSAGE, null, forces, forces[0]);
        
                                        
        JComboBox<String> selector = new JComboBox<String>(forces);
        

        selector.setName("Force Law: "); // TODO REVISAR ESTO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        selector.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent e) {

                String name = (String)selector.getSelectedItem();
                // 2. Once selected, change the force laws
                for (int i = 0; i < laws.size(); i++) {
                    if (name.equals(laws.get(i).getString("desc"))) {
                        chosenLaw = laws.get(i);
                        break; // stop searching for force laws
                    }
                }
                
            }
        });

        //-------------------------

        frame.add(selector);
    }

    private void start() {
        // Disable all buttons except the stop one and set the value of stopped to false
        enableButtonsTF(false);
        _stopped = false;

        try {
            // Set delta time to the one specified in the text field
            double new_dt = Double.parseDouble(_deltaTime.getText()); // get the text from the text field and parse it to double
            _ctrl.setDeltaTime(new_dt); // assign it to the controller

            // Call the method run_sim with the current value of steps
            int steps = (int)_steps.getValue();
            run_sim(steps);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.toString(), "Exception thrown", JOptionPane.WARNING_MESSAGE);
        }
        
        // When we have finished the execution, enable the buttons back
        enableButtonsTF(true);
    }

    private void stop() {
        // Stop the execution
        _stopped = true;
        // Enable the buttons back
        enableButtonsTF(true);
    }

    private void exit() {
        JFrame exit = new JFrame("Exit confirmation");

        int n = JOptionPane.showOptionDialog(exit, "Would you to exit?", "Exit confirmation", 
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

        if (n == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void run_sim(int n) {
        if ( n>0 && !_stopped ) {
            try {
                _ctrl.run(1);
            } catch (Exception e) {
                // show the error in a dialog box
                JOptionPane.showMessageDialog(this, "Simulation error.", "Uh-oh...", JOptionPane.WARNING_MESSAGE);
                // enable all buttons
                _stopped = true;
                enableButtonsTF(true);
                return;
            }
            SwingUtilities.invokeLater( new Runnable() {
                @Override
                public void run() {
                    run_sim(n-1);
                }
            });
        } else {
            _stopped = true;
            // enable all buttons
            enableButtonsTF(true);
        }
    }
    
    private void enableButtonsTF(boolean enableTF) {
        // enableTF == true => Enable all buttons
        // enableTF == false => Disable all buttons (doesn't affect the stop button)

        _loadFileBtn.setEnabled(enableTF);
        _modifyBtn.setEnabled(enableTF);
        _startBtn.setEnabled(enableTF);
        _exitBtn.setEnabled(enableTF);
        _deltaTime.setEnabled(enableTF);
        _steps.setEnabled(enableTF);
    }

    // SimulatorObserver methods
    // ...
    @Override
    public void onRegister(List<Body> bodies, double time, double dt, String fLawsDesc) {
        this._deltaTime.setText(String.valueOf(dt));
    }

    @Override
    public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) {
        this._deltaTime.setText(String.valueOf(dt));
    }
    
    @Override
    public void onBodyAdded(List<Body> bodies, Body b) {}
    
    @Override
    public void onAdvance(List<Body> bodies, double time) {}
    
    @Override
    public void onDeltaTimeChanged(double dt) {
        this._deltaTime.setText(String.valueOf(dt));
    }
    
    @Override
    public void onForceLawsChanged(String fLawsDesc) {}
}
