package hospital.presentation.Historico;

import hospital.logic.Paciente;
import hospital.logic.Receta;
import hospital.logic.Service;
import hospital.presentation.AbstractTableModel;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TableModel extends AbstractTableModel<Receta> implements javax.swing.table.TableModel {

    public static final int ID_RECETA = 0;
    public static final int PACIENTE = 1;
    public static final int FECHA_CONFECCION = 2;
    public static final int FECHA_RETIRO = 3;
    public static final int ESTADO = 4;
    public static final int NUM_MEDICAMENTOS = 5;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public TableModel(int[] cols, List<Receta> rows) {
        super(cols, rows);
    }

    @Override
    protected Object getPropertyAt(Receta receta, int col) {
        switch (cols[col]) {
            case ID_RECETA:
                return receta.getId();
            case PACIENTE:
                return obtenerNombrePaciente(receta.getPacienteId());
            case FECHA_CONFECCION:
                return receta.getFecha().format(formatter);
            case FECHA_RETIRO:
                if (receta.getFechaRetiro() != null) {
                    return receta.getFechaRetiro().format(formatter);
                }
                else {
                    return receta.getFecha() != null ?
                            receta.getFecha().plusDays(1).format(formatter) : "Sin fecha";
                }
            case ESTADO:
                return "Confeccionada";
            case NUM_MEDICAMENTOS:
                return receta.getDetalles() != null ? receta.getDetalles().size() : 0;
            default:
                return "";
        }
    }

    @Override
    protected void initColNames() {
        colNames = new String[6];
        colNames[ID_RECETA] = "ID Receta";
        colNames[PACIENTE] = "Paciente";
        colNames[FECHA_CONFECCION] = "Fecha Confección";
        colNames[FECHA_RETIRO] = "Fecha Retiro";
        colNames[ESTADO] = "Estado";
        colNames[NUM_MEDICAMENTOS] = "Medicamentos";
    }

    private String obtenerNombrePaciente(String pacienteId) {
        try {
            Paciente paciente = Service.instance().readPaciente(pacienteId);
            return paciente.getNombre();
        } catch (Exception e) {
            return pacienteId;
        }
    }
}