package simulator.view;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.jar.JarFile;

import javax.swing.*;

import simulator.control.Controller;
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
        BorderLayout borderLayout = new BorderLayout();

        this.setLayout(borderLayout);
        this.add(left(), BorderLayout.WEST);

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

        // EXIT BUTTON
        _exitBtn = new JButton();
        _exitBtn.setIcon(new ImageIcon("resources/icons/exit.png"));
        _exitBtn.setToolTipText("Close the application");
        _exitBtn.addActionListener((e) -> exit());
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
                System.err.println("Error while opening the file");
            }

        } else {
            System.out.println("File load cancelled by user.");
        }
        
    }

    private void modifyData() {
        // TODO: ufffffffffff... this is very complicated
    }

    private void start() {
        // Disable all buttons except the stop one and set the value of stopped to false
        _loadFileBtn.setEnabled(false);
        _modifyBtn.setEnabled(false);
        _startBtn.setEnabled(false);
        _exitBtn.setEnabled(false);
        _stopped = false;

        // Set delta time to the one specified in the text field
        double new_dt = Double.parseDouble(_deltaTime.getText()); // get the text from the text field and parse it to double
        _ctrl.setDeltaTime(new_dt); // assign it to the controller

        // Call the method run_sim with the current value of steps
        int steps = (int)_steps.getValue();
        run_sim(steps);
        // When we have finished the execution, enable the buttons back
        _loadFileBtn.setEnabled(true);
        _modifyBtn.setEnabled(true);
        _startBtn.setEnabled(true);
        _exitBtn.setEnabled(true);
    }

    private void stop() {
        // Stop the execution
        _stopped = true;
        // Enable the buttons back
        _loadFileBtn.setEnabled(true);
        _modifyBtn.setEnabled(true);
        _startBtn.setEnabled(true);
        _exitBtn.setEnabled(true);
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
                // TODO show the error in a dialog box
                // TODO enable all buttons
                _stopped = true;
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
            // TODO enable all buttons
        }
    }
    // SimulatorObserver methods
    // ...
}
