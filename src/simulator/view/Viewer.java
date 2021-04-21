package simulator.view;

import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JComponent;

import simulator.control.Controller;
import simulator.misc.Vector2D;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class Viewer extends JComponent implements SimulatorObserver {
    // ...
    private int _centerX;
    private int _centerY;
    private double _scale;
    private List<Body> _bodies;
    private boolean _showHelp;
    private boolean _showVectors;
    
    Viewer(Controller ctrl) {
        initGUI();
        ctrl.addObserver(this);
    }

    private void initGUI() {
        // TODO add border with title
        _bodies = new ArrayList<>();
        _scale = 1.0;
        _showHelp = true;
        _showVectors = true;
        addKeyListener(new KeyListener() {
            // ...
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyChar()) {
                    case '-':
                        _scale = _scale * 1.1;
                        repaint();
                        break;
                    case ’+’:
                        _scale = Math.max(1000.0, _scale / 1.1);
                        repaint();
                        break;
                    case ’=’:
                        autoScale();
                        repaint();
                        break;
                    case ’h’:
                        _showHelp = !_showHelp;
                        repaint();
                        break;
                    case ’v’:
                        _showVectors = !_showVectors;
                        repaint();
                        break;
                    default:
                }
            }
        });

        addMouseListener(new MouseListener() {
            // ...
            @Override
            public void mouseEntered(MouseEvent e) {
                requestFocus();
            }
        });
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // use ’gr’ to draw not ’g’ --- it gives nicer results
        Graphics2D gr = (Graphics2D) g;
        gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gr.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // calculate the center
        _centerX = getWidth() / 2;
        _centerY = getHeight() / 2;
        // TODO draw a cross at center
        // TODO draw bodies (with vectors if _showVectors is true)
        // TODO draw help if _showHelp is true
    }

    // other private/protected methods
    // ...

    private void autoScale() {
        double max = 1.0;

        for (Body b : _bodies) {
            Vector2D p = b.getPosition();
            max = Math.max(max, Math.abs(p.getX()));
            max = Math.max(max, Math.abs(p.getY()));
        }
        
        double size = Math.max(1.0, Math.min(getWidth(), getHeight()));
        _scale = max > size ? 4.0 * max / size : 1.0;
    }

    // This method draws a line from (x1,y1) to (x2,y2) with an arrow.
    // The arrow is of height h and width w.
    // The last two arguments are the colors of the arrow and the line

    private void drawLineWithArrow(//
        Graphics g, //
        int x1, int y1, //
        int x2, int y2, //
        int w, int h, //
        Color lineColor, Color arrowColor) { // NOTE: Start of the function here

        int dx = x2 - x1, dy = y2 - y1;
        double D = Math.sqrt(dx * dx + dy * dy);
        double xm = D - w, xn = xm, ym = h, yn = -h, x;
        double sin = dy / D, cos = dx / D;
        
        x = xm * cos - ym * sin + x1;
        ym = xm * sin + ym * cos + y1;
        xm = x;
        
        x = xn * cos - yn * sin + x1;
        yn = xn * sin + yn * cos + y1;
        xn = x;
        
        int[] xpoints = { x2, (int) xm, (int) xn };
        int[] ypoints = { y2, (int) ym, (int) yn };
        
        g.setColor(lineColor);

        g.drawLine(x1, y1, x2, y2);
        g.setColor(arrowColor);
        g.fillPolygon(xpoints, ypoints, 3);
    }
    // SimulatorObserver methods
    // ...

}
