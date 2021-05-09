package extra.graphviewer;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import org.json.JSONObject;
import org.json.JSONTokener;

public class ControlPanel extends JPanel {

	private JFileChooser _fc;
	private GraphViewer _gv;

	public ControlPanel(GraphViewer gv) {
		_gv = gv;
		initGUI();
	}

	private void initGUI() {
		setLayout(new BorderLayout());
		JToolBar toolaBar = new JToolBar();
		add(toolaBar, BorderLayout.PAGE_START);

		// Load Button
		
		_fc = new JFileChooser();
		_fc.setCurrentDirectory(new File(ControlPanel.class.getResource("./").getPath()));
		JButton loadButton = new JButton();
		loadButton.setToolTipText("Load bodies file into the editor");
		loadButton.setIcon(loadImage("open.png"));
		loadButton.addActionListener((e) -> loadFile());
		toolaBar.add(loadButton);

		// Exit
		JButton quitButton = new JButton();
		quitButton.setToolTipText("Exit");
		quitButton.setIcon(loadImage("exit.png"));
		quitButton.addActionListener((e) -> quit());
		toolaBar.add(quitButton);
	}

	protected void loadFile() {
		int returnVal = _fc.showOpenDialog(this.getParent());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = _fc.getSelectedFile();
			try {
				Graph g = new Graph(new JSONObject(new JSONTokener(new FileInputStream(file))));
				System.out.println(g);
				_gv.load(g);
				
			} catch (Exception e) {
				showError("Something went wrog while loading the file: " + e.getMessage());
			}
		}
	}

	protected void quit() {
		int n = JOptionPane.showOptionDialog((Frame) SwingUtilities.getWindowAncestor(this),
				"Are sure you want to quit?", "Quit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				null, null);

		if (n == 0) {
			System.exit(0);
		}
	}

	private void showError(String err) {
		SwingUtilities.invokeLater(() -> {
			JOptionPane.showMessageDialog((Frame) SwingUtilities.getWindowAncestor(this), err, "ERROR",
					JOptionPane.ERROR_MESSAGE);
		});
	}

	private ImageIcon loadImage(String file) {
		return new ImageIcon(Toolkit.getDefaultToolkit().createImage(ControlPanel.class.getResource(file)));
	}

}
