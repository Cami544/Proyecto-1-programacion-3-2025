package hospital.presentation.Preescribir;

import hospital.logic.DetalleReceta;
import hospital.logic.Medicamento;
import hospital.logic.Paciente;
import hospital.logic.Receta;
import hospital.presentation.AbstractModel;

import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Model extends AbstractModel {
    private Paciente pacienteSeleccionado;
    private Receta recetaActual;
    private List<DetalleReceta> detallesReceta;
    private List<Medicamento> medicamentosDisponibles;
    private LocalDate fechaRetiro;

    public static final String PACIENTE_SELECCIONADO = "pacienteSeleccionado";
    public static final String RECETA_ACTUAL = "recetaActual";
    public static final String DETALLES_RECETA = "detallesReceta";
    public static final String MEDICAMENTOS_DISPONIBLES = "medicamentosDisponibles";
    public static final String FECHA_RETIRO = "fechaRetiro";

    public Model() {
        this.pacienteSeleccionado = null;
        this.recetaActual = null;
        this.detallesReceta = new ArrayList<>();
        this.medicamentosDisponibles = new ArrayList<>();
        this.fechaRetiro = LocalDate.now().plusDays(1); // Por defecto: mañana
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(PACIENTE_SELECCIONADO);
        firePropertyChange(RECETA_ACTUAL);
        firePropertyChange(DETALLES_RECETA);
        firePropertyChange(MEDICAMENTOS_DISPONIBLES);
        firePropertyChange(FECHA_RETIRO);
    }

    public Paciente getPacienteSeleccionado() {
        return pacienteSeleccionado;
    }

    public void setPacienteSeleccionado(Paciente pacienteSeleccionado) {
        this.pacienteSeleccionado = pacienteSeleccionado;
        firePropertyChange(PACIENTE_SELECCIONADO);
    }

    public Receta getRecetaActual() {
        return recetaActual;
    }

    public void setRecetaActual(Receta recetaActual) {
        this.recetaActual = recetaActual;
        firePropertyChange(RECETA_ACTUAL);
    }

    public List<DetalleReceta> getDetallesReceta() {
        return detallesReceta;
    }

    public void setDetallesReceta(List<DetalleReceta> detallesReceta) {
        this.detallesReceta = detallesReceta;
        firePropertyChange(DETALLES_RECETA);
    }

    public void agregarDetalle(DetalleReceta detalle) {
        for (DetalleReceta existente : this.detallesReceta) {
            if (existente.getMedicamentoCodigo().equals(detalle.getMedicamentoCodigo())) {
                int nuevaCantidad = existente.getCantidad() + detalle.getCantidad();
                existente.setCantidad(nuevaCantidad);
                String nuevasIndicaciones = existente.getIndicaciones() + "; " + detalle.getIndicaciones();
                existente.setIndicaciones(nuevasIndicaciones.trim());
                firePropertyChange(DETALLES_RECETA);
                return;
            }
        }

        this.detallesReceta.add(detalle);
        firePropertyChange(DETALLES_RECETA);
    }

    public void removerDetalle(int index) {
        if (index >= 0 && index < this.detallesReceta.size()) {
            this.detallesReceta.remove(index);
            firePropertyChange(DETALLES_RECETA);
        }
    }

    public void actualizarDetalle(int index, DetalleReceta detalle) {
        if (index >= 0 && index < this.detallesReceta.size()) {
            this.detallesReceta.set(index, detalle);
            firePropertyChange(DETALLES_RECETA);
        }
    }

    public List<Medicamento> getMedicamentosDisponibles() {
        return medicamentosDisponibles;
    }

    public void setMedicamentosDisponibles(List<Medicamento> medicamentosDisponibles) {
        this.medicamentosDisponibles = medicamentosDisponibles;
        firePropertyChange(MEDICAMENTOS_DISPONIBLES);
    }

    public LocalDate getFechaRetiro() {
        return fechaRetiro;
    }

    public void setFechaRetiro(LocalDate fechaRetiro) {
        this.fechaRetiro = fechaRetiro;
        firePropertyChange(FECHA_RETIRO);
    }

    public void limpiarReceta() {
        this.pacienteSeleccionado = null;
        this.recetaActual = null;
        this.detallesReceta.clear();
        this.fechaRetiro = LocalDate.now().plusDays(1); // Resetear a mañana
        firePropertyChange(PACIENTE_SELECCIONADO);
        firePropertyChange(RECETA_ACTUAL);
        firePropertyChange(DETALLES_RECETA);
        firePropertyChange(FECHA_RETIRO);
    }

    public void nuevaReceta() {
        if (pacienteSeleccionado != null) {
            String recetaId = generarIdReceta();
            this.recetaActual = new Receta(recetaId, pacienteSeleccionado.getId(), LocalDate.now());
            this.recetaActual.setFechaRetiro(this.fechaRetiro);
            this.recetaActual.setDetalles(new ArrayList<>(this.detallesReceta));
            firePropertyChange(RECETA_ACTUAL);
        }
    }

    private String generarIdReceta() {
        long timestamp = System.currentTimeMillis();
        return "REC-" + LocalDate.now().toString().replace("-", "") + "-" + (timestamp % 10000);
    }
}