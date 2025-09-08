package hospital.presentation.Dashboard;

import hospital.logic.Medicamento;
import hospital.logic.Receta;
import hospital.presentation.AbstractModel;

import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Model extends AbstractModel {
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;
    private Medicamento medicamentoSeleccionado;
    private List<Medicamento> medicamentosDisponibles;
    private List<Object[]> datosEstadisticas;
    private List<Receta> recetasDashboard;
    private Map<String, Integer> estadisticasRecetas;

    public static final String FECHA_DESDE = "fechaDesde";
    public static final String FECHA_HASTA = "fechaHasta";
    public static final String MEDICAMENTO_SELECCIONADO = "medicamentoSeleccionado";
    public static final String MEDICAMENTOS_DISPONIBLES = "medicamentosDisponibles";
    public static final String DATOS_ESTADISTICAS = "datosEstadisticas";
    public static final String ESTADISTICAS_RECETAS = "estadisticasRecetas";
    public static final String RECETAS_DASHBOARD = "recetasDashboard";

    public Model() {
        this.fechaDesde = LocalDate.now().minusMonths(6);
        this.fechaHasta = LocalDate.now();
        this.medicamentoSeleccionado = null;
        this.medicamentosDisponibles = new ArrayList<>();
        this.datosEstadisticas = new ArrayList<>();
        this.estadisticasRecetas = new java.util.HashMap<>();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(FECHA_DESDE);
        firePropertyChange(FECHA_HASTA);
        firePropertyChange(MEDICAMENTO_SELECCIONADO);
        firePropertyChange(MEDICAMENTOS_DISPONIBLES);
        firePropertyChange(DATOS_ESTADISTICAS);
        firePropertyChange(ESTADISTICAS_RECETAS);
    }

    public LocalDate getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(LocalDate fechaDesde) {
        this.fechaDesde = fechaDesde;
        firePropertyChange(FECHA_DESDE);
    }

    public LocalDate getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(LocalDate fechaHasta) {
        this.fechaHasta = fechaHasta;
        firePropertyChange(FECHA_HASTA);
    }

    public Medicamento getMedicamentoSeleccionado() {
        return medicamentoSeleccionado;
    }

    public void setMedicamentoSeleccionado(Medicamento medicamentoSeleccionado) {
        this.medicamentoSeleccionado = medicamentoSeleccionado;
        firePropertyChange(MEDICAMENTO_SELECCIONADO);
    }

    public List<Medicamento> getMedicamentosDisponibles() {
        return medicamentosDisponibles;
    }

    public void setMedicamentosDisponibles(List<Medicamento> medicamentosDisponibles) {
        this.medicamentosDisponibles = medicamentosDisponibles;
        firePropertyChange(MEDICAMENTOS_DISPONIBLES);
    }

    public List<Object[]> getDatosEstadisticas() {
        return datosEstadisticas;
    }

    public void setDatosEstadisticas(List<Object[]> datosEstadisticas) {
        this.datosEstadisticas = datosEstadisticas;
        firePropertyChange(DATOS_ESTADISTICAS);
    }

    public Map<String, Integer> getEstadisticasRecetas() {
        return estadisticasRecetas;
    }

    public void setEstadisticasRecetas(Map<String, Integer> estadisticasRecetas) { //consultar
        this.estadisticasRecetas = estadisticasRecetas;
        firePropertyChange(ESTADISTICAS_RECETAS);
    }
    public List<Receta> getRecetasDashboard() {
        return recetasDashboard;
    }

    public void setRecetasDashboard(List<Receta> recetasDashboard) {
        this.recetasDashboard = recetasDashboard;
        firePropertyChange(RECETAS_DASHBOARD);
    }
}