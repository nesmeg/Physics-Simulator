package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class BodiesTableModel extends AbstractTableModel implements SimulatorObserver {
        public static final String[] columns = new String[]{"Id", "Mass", "Position", "Velocity", "Force"};
        private List<Body> _bodies;

        BodiesTableModel(Controller ctrl) {
            _bodies = new ArrayList<>();
            ctrl.addObserver(this);
        }

        @Override
        public int getRowCount() {
            return _bodies.size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public String getColumnName(int column) {
            return columns[column].toString();
        }
        
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            
            if (rowIndex < 0 || rowIndex >= _bodies.size())
                return null;
            Body b = _bodies.get(rowIndex);
            if (columnIndex == 0)
                return b.getId();
            else if (columnIndex == 1)
                return b.getMass();
            else if (columnIndex == 2)
                return b.getPosition();
            else if (columnIndex == 3)
                return b.getVelocity();
            else if (columnIndex == 4)
                return b.getForce();
            else
                return null;
        }
        // SimulatorObserver methods
        // ...

        @Override
        public void onRegister(List<Body> bodies, double time, double dt, String fLawsDesc) {
            _bodies = bodies;
            fireTableStructureChanged();
        }

        @Override
        public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) {
            _bodies = bodies;
            fireTableStructureChanged();
        }

        @Override
        public void onBodyAdded(List<Body> bodies, Body b) {
            _bodies = bodies;
            fireTableStructureChanged();
        }

        @Override
        public void onAdvance(List<Body> bodies, double time) {
            _bodies = bodies;
            fireTableStructureChanged();
        }

        @Override
        public void onDeltaTimeChanged(double dt) {}

        @Override
        public void onForceLawsChanged(String fLawsDesc) {}
}

