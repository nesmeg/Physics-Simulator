package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;


import simulator.control.Controller;

public class BodiesTable extends JPanel {
    
    BodiesTable(Controller ctrl) {
        
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.black, 2),
                    "Bodies",
                    TitledBorder.LEFT, TitledBorder.TOP));

        BodiesTableModel btm = new BodiesTableModel(ctrl);
        JTable table = new JTable(btm);
        JScrollPane scroll = new JScrollPane(table);
        this.add(scroll);
    }
}
