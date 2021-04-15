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

        // LOAD FILE BUTTON
        JButton loadFileBtn = new JButton();
        loadFileBtn.setIcon(new ImageIcon("resources/icons/open.png"));
        loadFileBtn.addActionListener((e) -> loadFile());

        // MODIFY DATA BUTTON

        // START BUTTON

        // STOP BUTTON

        // EXIT BUTTON
        JButton ExitBtn = new JButton();
        loadFileBtn.setIcon(new ImageIcon("resources/icons/exit.png"));
        loadFileBtn.addActionListener((e) -> exit());
    }
    
    // other private/protected methods
    // ...

    private void loadFile() {
        JFileChooser fileChooser = new JFileChooser();
        
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

    }

    private void start() {
        
    }

    private void stop() {
        
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
