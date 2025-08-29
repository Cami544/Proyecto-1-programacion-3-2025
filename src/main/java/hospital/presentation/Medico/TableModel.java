package hospital.presentation.Medico;

import hospital.logic.Medico;
import hospital.presentation.AbstractTableModel;
import java.util.List;

public class TableModel extends AbstractTableModel<Medico> implements javax.swing.table.TableModel {

    public static final int ID = 0;
    public static final int NOMBRE = 1;
    public static final int ESPECIALIDAD = 2;

    public TableModel(int[] cols, List<Medico> rows) {
        super(cols, rows);
    }

    @Override
    protected Object getPropertyAt(Medico medico, int col) {
        switch (cols[col]) {
            case ID:
                return medico.getId();
            case NOMBRE:
                return medico.getNombre();
            case ESPECIALIDAD:
                return medico.getEspecialidad();
            default:
                return "";
        }
    }

    @Override
    protected void initColNames() {
        colNames = new String[3];
        colNames[ID] = "Id";
        colNames[NOMBRE] = "Nombre";
        colNames[ESPECIALIDAD] = "Especialidad";
    }

    @Override
    protected Object getPropetyAt(Medico medico, int col) {
        switch (cols[col]) {
            case ID:
                return medico.getId();
            case NOMBRE:
                return medico.getNombre();
            case ESPECIALIDAD:
                return medico.getEspecialidad();
            default:
                return "";
        }
    }
}