package simulator.view;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import simulator.control.Controller;

public class MainWindow extends JFrame {

    private Controller _ctrl;
    private ControlPanel _controlPanel;
    private StatusBar _statusBar;
    private BodiesTable _bodiesTable;
    private Viewer _viewer;
    
    public MainWindow(Controller ctrl) {
        super("Physics Simulator");
        _ctrl = ctrl;
        initGUI();

        this.setVisible(true);
    }

    private void initGUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        setContentPane(mainPanel);

        // TODO complete this method to build the GUI
        // 1. Place control panel at PAGE_START of mainPanel
        _controlPanel = new ControlPanel(_ctrl);
        mainPanel.add(_controlPanel, BorderLayout.PAGE_START);
        // 2. Place status bar at PAGE_END of mainPanel
        _statusBar = new StatusBar(_ctrl);
        mainPanel.add(_statusBar, BorderLayout.PAGE_END);
        // 3. Create a panel with BoxLayout and place it at CENTER and add bodiesTable and viewer
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.PAGE_AXIS));

        _bodiesTable = new BodiesTable(_ctrl);
        centerPanel.add(_bodiesTable);

        _viewer = new Viewer(_ctrl);
        centerPanel.add(_viewer);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
    }

    // other private/protected methods
    // ...
}
