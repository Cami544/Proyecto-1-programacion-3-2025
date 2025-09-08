package hospital.presentation.Medicamento;

import hospital.logic.Medicamento;
import hospital.presentation.AbstractTableModel;
import java.util.List;

public class TableModel extends AbstractTableModel<Medicamento> implements javax.swing.table.TableModel {

    public static final int CODIGO = 0;
    public static final int NOMBRE = 1;
    public static final int PRESENTACION = 2;

    public TableModel(int[] cols, List<Medicamento> rows) {
        super(cols, rows);
    }

    @Override
    protected Object getPropertyAt(Medicamento medicamento, int col) {
        switch (cols[col]) {
            case CODIGO:
                return medicamento.getCodigo();
            case NOMBRE:
                return medicamento.getNombre();
            case PRESENTACION:
                return medicamento.getPresentacion();
            default:
                return "";
        }
    }

    @Override
    protected void initColNames() {
        colNames = new String[3];
        colNames[CODIGO] = "Codigo";
        colNames[NOMBRE] = "Nombre";
        colNames[PRESENTACION] = "Presentacion";
    }
}