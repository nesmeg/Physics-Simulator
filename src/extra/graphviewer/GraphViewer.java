package extra.graphviewer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;

public class GraphViewer extends JComponent {

	static final int _NODE_RADIUS = 10;

	Graph _g;
	Node _selectedDragNode;
	Node _selectedSrcNode;
	Node _selectedTrgtNode;

	GraphViewer() {
		initGUI();
	}

	private void initGUI() {
		_g = new Graph();
		_selectedDragNode = null;
		_selectedSrcNode = null;

		addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					_selectedSrcNode = null;
					repaint();
				}
			}
		});

		addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				if (_selectedDragNode != null) {
					int x = e.getX();
					int y = e.getY();
					if (x >= 0 && x <= getWidth() && y >= 0 && y <= getHeight()) {
						_selectedDragNode.setX(e.getX());
						_selectedDragNode.setY(e.getY());
						repaint();
					}
				}
			}
		});

		addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				_selectedDragNode = null;
			}

			@Override
			public void mousePressed(MouseEvent e) {
				_selectedDragNode = getSeletedNode(e.getX(), e.getY());
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				requestFocus();
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() >0 ) {
					Node n = getSeletedNode(e.getX(), e.getY());
					if (n == null) {
						_g.addNode(new Node("$n" + _g.getNodes().size(), e.getX(), e.getY()));
					} else if (_selectedSrcNode == null) {
						_selectedSrcNode = n;
					} else if ( n != _selectedSrcNode) {
						_g.addEdge(new Edge(_selectedSrcNode, n));
						_selectedSrcNode = n;
					} else {
						_selectedSrcNode = null;
					}
					repaint();
				}
			}
		});
	}

	protected Node getSeletedNode(int x, int y) {
		for (Node n : _g.getNodes()) {
			if (Math.sqrt(Math.pow(x - n.getX(), 2) + Math.pow(y - n.getY(), 2)) <= _NODE_RADIUS) {
				return n;
			}
		}
		return null;
	}

	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		if (_g == null) {
			g.drawString("No Graph Yet!", getWidth() / 2 - 30, getHeight() / 2 - 10);
			return;
		}

		g.setColor(Color.RED);
		for (Edge e : _g.getEdges()) {
			int x1 = e.getSrc().getX();
			int y1 = e.getSrc().getY();
			int x2 = e.getTrgt().getX();
			int y2 = e.getTrgt().getY();

			g.drawLine(x1, y1, x2, y2);
		}

		for (Node n : _g.getNodes()) {
			int x = n.getX() - _NODE_RADIUS;
			int y = n.getY() - _NODE_RADIUS;
			g.setColor(Color.BLUE);
			g.fillOval(x, y, 2 * _NODE_RADIUS, 2 * _NODE_RADIUS);
			g.setColor(Color.GREEN);
			g.drawString(n.getId(), x, y);

		}

		if (_selectedSrcNode != null) {
			g.setColor(Color.YELLOW);
			int x = _selectedSrcNode.getX() - _NODE_RADIUS;
			int y = _selectedSrcNode.getY() - _NODE_RADIUS;
			g.fillOval(x, y, 2 * _NODE_RADIUS, 2 * _NODE_RADIUS);
		}

	}

	public void load(Graph g) {
		_g = g;
		repaint();
	}
}
