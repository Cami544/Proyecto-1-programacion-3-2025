package hospital.presentation.Farmaceuta;

import hospital.logic.Farmaceuta;
import hospital.presentation.AbstractTableModel;
import java.util.List;

public class TableModel extends AbstractTableModel<Farmaceuta> implements javax.swing.table.TableModel {

    public static final int ID = 0;
    public static final int NOMBRE = 1;

    public TableModel(int[] cols, List<Farmaceuta> rows) {
        super(cols, rows);
    }

    @Override
    protected Object getPropertyAt(Farmaceuta farmaceuta, int col) {
        switch (cols[col]) {
            case ID:
                return farmaceuta.getId();
            case NOMBRE:
                return farmaceuta.getNombre();
            default:
                return "";
        }
    }

    @Override
    protected void initColNames() {
        colNames = new String[2];
        colNames[ID] = "Id";
        colNames[NOMBRE] = "Nombre";
    }
}