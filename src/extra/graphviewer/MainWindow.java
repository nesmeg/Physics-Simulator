package extra.graphviewer;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class MainWindow extends JFrame {

	public MainWindow() {
		super("Graph Viewer");
		initGUI();
	}

	private void initGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		setContentPane(mainPanel);

		GraphViewer gv = new GraphViewer();
		gv.setPreferredSize(new Dimension(400, 400));
		ControlPanel cp = new ControlPanel(gv);

		mainPanel.add(cp, BorderLayout.PAGE_START);
		mainPanel.add(new JScrollPane(gv), BorderLayout.CENTER);

		pack();
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new MainWindow();
		});
	}

}
