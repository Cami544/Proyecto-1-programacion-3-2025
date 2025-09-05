package hospital.presentation.Preescribir;

import hospital.logic.DetalleReceta;
import hospital.logic.Medicamento;
import hospital.logic.Service;
import hospital.presentation.AbstractTableModel;
import java.util.List;

public class TableModel extends AbstractTableModel<DetalleReceta> implements javax.swing.table.TableModel {

    public static final int MEDICAMENTO = 0;
    public static final int PRESENTACION = 1;
    public static final int CANTIDAD = 2;
    public static final int INDICACIONES = 3;

    public TableModel(int[] cols, List<DetalleReceta> rows) {
        super(cols, rows);
    }

    @Override
    protected Object getPropertyAt(DetalleReceta detalle, int col) {
        switch (cols[col]) {
            case MEDICAMENTO:
                return obtenerNombreMedicamento(detalle.getMedicamentoCodigo());
            case PRESENTACION:
                return obtenerPresentacionMedicamento(detalle.getMedicamentoCodigo());
            case CANTIDAD:
                return detalle.getCantidad();
            case INDICACIONES:
                return detalle.getIndicaciones();
            default:
                return "";
        }
    }

    @Override
    protected void initColNames() {
        colNames = new String[4];
        colNames[MEDICAMENTO] = "Medicamento";
        colNames[PRESENTACION] = "Presentación";
        colNames[CANTIDAD] = "Cantidad";
        colNames[INDICACIONES] = "Indicaciones";
    }

    private String obtenerNombreMedicamento(String codigo) {
        try {
            List<Medicamento> medicamentos = Service.instance().getMedicamentos();
            for (Medicamento med : medicamentos) {
                if (med.getCodigo().equals(codigo)) {
                    return med.getNombre();
                }
            }
        } catch (Exception e) {
            System.err.println("Error obteniendo medicamento: " + e.getMessage());
        }
        return codigo;
    }

    private String obtenerPresentacionMedicamento(String codigo) {
        try {
            List<Medicamento> medicamentos = Service.instance().getMedicamentos();
            for (Medicamento med : medicamentos) {
                if (med.getCodigo().equals(codigo)) {
                    return med.getPresentacion();
                }
            }
        } catch (Exception e) {
            System.err.println("Error obteniendo presentación: " + e.getMessage());
        }
        return "";
    }
}