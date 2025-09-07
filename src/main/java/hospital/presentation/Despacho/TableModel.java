package hospital.presentation.Despacho;

import hospital.logic.Farmaceuta;
import hospital.logic.Paciente;
import hospital.logic.Receta;
import hospital.logic.Service;
import hospital.presentation.AbstractTableModel;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TableModel extends AbstractTableModel<Receta> implements javax.swing.table.TableModel {

    public static final int FARNACEUTA=0;
    public static final int ID_RECETA = 1;
    public static final int PACIENTE = 2;
    public static final int FECHA_RETIRO = 3;
    public static final int ESTADO = 4;


    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public TableModel(int[] cols, List<Receta> rows) {
        super(cols, rows);
    }

    @Override
    protected Object getPropertyAt(Receta receta, int col) {
        switch (cols[col]) {
            case FARNACEUTA:
                return  obtenerNombreFarmaceuta( receta.getFarmaceutaId() );
            case ID_RECETA:
                return   receta.getId();
            case PACIENTE:
                return obtenerNombrePaciente(receta.getPacienteId());
            case FECHA_RETIRO:
                if (receta.getFechaRetiro() != null) {
                    return receta.getFechaRetiro().format(formatter);
                } else {
                    return receta.getFecha() != null ?
                            receta.getFecha().plusDays(1).format(formatter) : "Sin fecha";
                }
            case ESTADO:
                return receta.getEstadoReceta();
            default:
                return "";
        }
    }

    @Override
    protected void initColNames() {
        colNames = new String[5];
        colNames[FARNACEUTA] = "Farmaceuta Encargado";
        colNames[ID_RECETA] = "ID Receta";
        colNames[PACIENTE] = "Paciente";
        colNames[FECHA_RETIRO] = "Fecha Retiro";
        colNames[ESTADO] = "Estado";
    }

    private String obtenerNombrePaciente(String pacienteId) {
        try {
            Paciente paciente = Service.instance().readPaciente(pacienteId);
            return paciente.getNombre();
        } catch (Exception e) {
            return pacienteId;
        }
    }
    private String obtenerNombreFarmaceuta(String farmaceutaId) {
        try {
            Farmaceuta farmaceuta= Service.instance().readFarmaceuta(farmaceutaId);
            return farmaceuta.getNombre();
        } catch (Exception e) {
            return farmaceutaId;
        }
    }
}