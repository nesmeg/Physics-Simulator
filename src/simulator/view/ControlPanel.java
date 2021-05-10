package simulator.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.json.JSONObject;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;
import simulator.model.ForceLaws;

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

    private JsonTableModel _dataTableModel;
    private JTable _dataTable;

    private JComboBox<String> _selector;
    private DefaultComboBoxModel<ForceLaws> _selectorModel;

    // Nested class for the model of the table
    private class JsonTableModel extends AbstractTableModel {
        private static final long serialVersionUID = 1L;

		private String[] _header = { "Key", "Value", "Description" };
		String[][] _data;
        private JSONObject _selectedLaw;

        JsonTableModel(JSONObject selectedLaw) {
            _selectedLaw = selectedLaw;
            if (_selectedLaw.getString("type").equals("nlug"))
                _data = new String[1][3]; // 1 parameter (G)
            else if (_selectedLaw.getString("type").equals("mtfp"))
                _data = new String[2][3]; // 2 parameters (c, g)
            else if (_selectedLaw.getString("type").equals("ng"))
                _data = new String[0][3]; // 0 parameters

            initialize();
		}

        public void initialize() {
            int i = 0;
            JSONObject data = _selectedLaw.getJSONObject("data");
            Iterator<String> keys = data.keys(); // keys of the force law to be iterated

            while (keys.hasNext()) {
                String key = keys.next();
                _data[i][0] = key; // Key column
                _data[i][1] = ""; // Value column (initially empty)
                _data[i][2] = data.getString(key); // Description column
                i++;
            }
        }

        @Override
		public String getColumnName(int column) {
			return _header[column];
		}

		@Override
		public int getRowCount() {
			return _data.length;
		}

		@Override
		public int getColumnCount() {
			return _header.length;
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
            if (getColumnName(columnIndex).equals("Value")) 
                return true;
			return false;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return _data[rowIndex][columnIndex];
		}

		@Override
		public void setValueAt(Object o, int rowIndex, int columnIndex) {
			_data[rowIndex][columnIndex] = o.toString();
		}

		// Method getData() returns a String corresponding to a JSON structure
		// with column 1 as keys and column 2 as values.

		// This method return the coIt is important to build it as a string, if
		// we create a corresponding JSONObject and use put(key,value), all values
		// will be added as string. This also means that if users want to add a
		// string value they should add the quotes as well as part of the
		// value (2nd column).
		//
		public String getData() {
			StringBuilder s = new StringBuilder();
			s.append('{');
			for (int i = 0; i < _data.length; i++) {
				if (!_data[i][0].isEmpty() && !_data[i][1].isEmpty()) {
					s.append('"');
					s.append(_data[i][0]);
					s.append('"');
					s.append(':');
					s.append(_data[i][1]);
					s.append(',');
				}
			}

			if (s.length() > 1)
				s.deleteCharAt(s.length() - 1);
			s.append('}');

			return s.toString();
		}
    }


    ControlPanel(Controller ctrl) {
        _ctrl = ctrl;
        _stopped = true;
        initGUI();
        _ctrl.addObserver(this);
    }

    private void initGUI() {
        setLayout(new BorderLayout());
        JToolBar toolBar = new JToolBar();
        this.add(toolBar, BorderLayout.PAGE_START);

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
        _stopBtn.setEnabled(false);
        _stopBtn.addActionListener((e) -> stop());

        // STEPS
        _steps = new JSpinner(new SpinnerNumberModel(150, 1, 10000, 1));
        _steps.setToolTipText("Simulation steps to execute");
        _steps.setMaximumSize(new Dimension(80, 40));
		_steps.setMinimumSize(new Dimension(80, 40));
		_steps.setPreferredSize(new Dimension(80, 40));

        // DELTA TIME
        _deltaTime = new JTextField();
        _deltaTime.setText("2500");
        _deltaTime.setToolTipText("Delta time");
        _deltaTime.setMaximumSize(new Dimension(80, 40));
		_deltaTime.setMinimumSize(new Dimension(80, 40));
		_deltaTime.setPreferredSize(new Dimension(80, 40));

        // EXIT BUTTON
        _exitBtn = new JButton();
        _exitBtn.setIcon(new ImageIcon("resources/icons/exit.png"));
        _exitBtn.setToolTipText("Close the application");
        _exitBtn.addActionListener((e) -> exit());

        // Placement of buttons
        JPanel leftButtons = new JPanel();
        leftButtons.add(_loadFileBtn);
        leftButtons.add(createVerticalSeparator());
        leftButtons.add(_modifyBtn);
        leftButtons.add(createVerticalSeparator());
        leftButtons.add(_startBtn);
        leftButtons.add(_stopBtn);
        leftButtons.add(new JLabel("Steps:"));
        leftButtons.add(_steps);
        leftButtons.add(new JLabel("Delta-time:"));
        leftButtons.add(_deltaTime);

        JPanel rightButtons = new JPanel();
        rightButtons.add(createVerticalSeparator());
        rightButtons.add(_exitBtn);

        BorderLayout layout = new BorderLayout();
        toolBar.setLayout(layout);
        toolBar.add(leftButtons, BorderLayout.WEST);
        toolBar.add(rightButtons, BorderLayout.EAST);
    }
    
    // other private/protected methods
    // ...

    static JComponent createVerticalSeparator() {
        JSeparator x = new JSeparator(SwingConstants.VERTICAL);
        x.setPreferredSize(new Dimension(3,40));
        return x;
    }

    // IMPLEMENTATION OF THE BUTTONS FUNCTIONALITY 
    // LOAD FILE BUTTON
    private void loadFile() {
        JFileChooser fileChooser = new JFileChooser("resources/examples/");
        
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

    //      ////// MODIFY FORCE LAW BUTTON \\\\\\
    private void modifyData() {

        // 1. Open dialog box to select the physic law

        Window parentWindow = SwingUtilities.windowForComponent(this); 
        // or pass 'this' if you are inside the panel
        JFrame parentFrame = null;
        if (parentWindow instanceof JFrame) {
            parentFrame = (JFrame)parentWindow;
        }
        JDialog dialog = new JDialog(parentFrame, "Force Laws Selection");
        dialog.setSize(1000, 500);

        List<JSONObject> lawsInfo = _ctrl.getForceLawsInfo();
        String[] forces = new String[lawsInfo.size()];

        for (int i = 0; i < lawsInfo.size(); i++) {
            forces[i] = lawsInfo.get(i).getString("desc");
        }

        // initial text
        JLabel text = new JLabel("<html><p>Select a force law and provide values for the parameters in the <b>Value column</b> (default values are used for parameters with no value).</p></html>");

		text.setAlignmentX(CENTER_ALIGNMENT);
		dialog.add(text);

		dialog.add(Box.createRigidArea(new Dimension(0, 20)));

        // Table (by default we create a table including the force law with index 0)
        JPanel tablePanel = new JPanel();
        tablePanel.add(createTable(lawsInfo.get(0)));
        dialog.add(tablePanel);
        

        // OK and Cancel buttons
        JPanel buttonsPanel = new JPanel();
		buttonsPanel.setAlignmentX(CENTER_ALIGNMENT);

        JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// do nothing on the controller
				dialog.setVisible(false);// close the window (dialog) of modifyData
			}
		});
		buttonsPanel.add(cancelButton);

        JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (_selector.getSelectedItem() != null) {

					// if we press OK, we have to change the controller:
                    int i = 0;
                    boolean found = false;
                    JSONObject selectedLaw = new JSONObject();

                    // know which law we have selected from the array of strings that the comboBox has:
                    while (i < lawsInfo.size() && !found ) {
                        if (lawsInfo.get(i).getString("desc").equalsIgnoreCase(_selector.getSelectedItem().toString())) {
                            selectedLaw = lawsInfo.get(i);
                            found = true;
                        }
                        i++;
                    }

                    Iterator<String> keys = selectedLaw.keys(); // keys of the force law to be iterated
                    JSONObject newForceLaw = new JSONObject();
                    i = 0;

                    while (keys.hasNext()) {
                        newForceLaw.put(keys.next(), _dataTable.getValueAt(i, 1));
                        // we take the value of the cell (i,1) because row i is the key we are looking
                        // for and column 1 always contains the value of that key
                        i++;
                    }

                    _ctrl.setForceLaws(newForceLaw); // change the force law in the controller

					dialog.setVisible(false); // close the window (dialog) of modifyData
				}
			}
		});
		buttonsPanel.add(okButton);

		dialog.add(buttonsPanel, BorderLayout.PAGE_END);


        // comboBox
        JPanel comboBoxPanel = new JPanel();
        _selectorModel = new DefaultComboBoxModel<>();
        _selector = new JComboBox<String>(forces);
        // identify when the force law is changed
        _selector.addActionListener((e) -> optionChanged(_selector.getSelectedItem().toString(), lawsInfo));
        comboBoxPanel.add(_selector);

        dialog.add(comboBoxPanel);
        dialog.setVisible(true);
    }

    // Method called when there is a change in the option chosen in the
    // JComboBox to trigger the change of table
    private void optionChanged(String selectedLaw, List<JSONObject> lawsInfo) {
        int i = 0;
        boolean found = false;

        while (i < lawsInfo.size() && !found ) {
            if (lawsInfo.get(i).getString("desc").equalsIgnoreCase(selectedLaw)) {
                _dataTable = createTable(lawsInfo.get(i)); // change the _dataTable with new data
                found = true;
            }
            i++;
        }
    }

    private JTable createTable(JSONObject selectedLaw) {

        _dataTableModel = new JsonTableModel(selectedLaw);

        JTable dataTable = new JTable(_dataTableModel) {
			private static final long serialVersionUID = 1L;

			// we override prepareRenderer to resized rows to fit to content
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component component = super.prepareRenderer(renderer, row, column);
				int rendererWidth = component.getPreferredSize().width;
				TableColumn tableColumn = getColumnModel().getColumn(column);
				tableColumn.setPreferredWidth(
						Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
				return component;
			}
		};

        return dataTable;
    }

    //      ////// END OF MODIFY FORCE LAW BUTTON \\\\\\

    // START BUTTON
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
    }

    // STOP BUTTON
    private void stop() {
        // Stop the execution
        _stopped = true;
        // Enable the buttons back
        enableButtonsTF(true);
    }

    // EXIT BUTTON
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
        _stopBtn.setEnabled(!enableTF);
        _exitBtn.setEnabled(enableTF);
        _deltaTime.setEnabled(enableTF);
        _steps.setEnabled(enableTF);
        this.repaint();
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
