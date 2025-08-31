package hospital.presentation.Paciente;

import hospital.logic.Paciente;
import hospital.presentation.AbstractTableModel;
import java.util.List;

public class TableModel extends AbstractTableModel<Paciente> implements javax.swing.table.TableModel {

    public static final int ID = 0;
    public static final int NOMBRE = 1;
    public static final int NACIMIENTO = 2;
    public static final int TELEFONO = 3;

    public TableModel(int[] cols, List<Paciente> rows) {
        super(cols, rows);
    }

    @Override
    protected Object getPropertyAt(Paciente paciente, int col) {
        switch (cols[col]) {
            case ID:
                return paciente.getId();
            case NOMBRE:
                return paciente.getNombre();
            case NACIMIENTO:
                return paciente.getFechaNacimiento();
            case TELEFONO:
               return paciente.getNumeroTelefono();
            default:
                return "";
        }
    }

    @Override
    protected void initColNames() {
        colNames = new String[4];
        colNames[ID] = "Id";
        colNames[NOMBRE] = "Nombre";
        colNames[NACIMIENTO] = "Nacimiento";
        colNames[TELEFONO] = "Telefono";
    }
}

