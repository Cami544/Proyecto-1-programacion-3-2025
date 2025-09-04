package hospital.presentation.Dashboard;

import hospital.presentation.AbstractTableModel;
import java.util.List;

public class TableModel extends AbstractTableModel<Object[]> implements javax.swing.table.TableModel {

    public static final int PERIODO = 0;
    public static final int MEDICAMENTO = 1;
    public static final int CANTIDAD_PRESCRITA = 2;
    public static final int RECETAS_GENERADAS = 3;

    public TableModel(int[] cols, List<Object[]> rows) {
        super(cols, rows);
    }

    @Override
    protected Object getPropertyAt(Object[] fila, int col) {
        if (fila == null || col >= fila.length) {
            return "";
        }

        switch (cols[col]) {
            case PERIODO:
                return fila[0];
            case MEDICAMENTO:
                return fila[1];
            case CANTIDAD_PRESCRITA:
                return fila[2];
            case RECETAS_GENERADAS:
                return fila.length > 3 ? fila[3] : 0;
            default:
                return "";
        }
    }

    @Override
    protected void initColNames() {
        colNames = new String[4];
        colNames[PERIODO] = "Período";
        colNames[MEDICAMENTO] = "Medicamento";
        colNames[CANTIDAD_PRESCRITA] = "Cantidad Prescrita";
        colNames[RECETAS_GENERADAS] = "Recetas Generadas";
    }
}