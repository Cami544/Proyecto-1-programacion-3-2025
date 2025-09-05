package hospital.presentation.Preescribir;

import hospital.logic.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class Controller {
    private View view;
    private Model model;

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);

        cargarMedicamentos();
    }

    public void buscarPaciente(String criterio) throws Exception {
        List<Paciente> pacientes = Service.instance().searchPacientes(criterio);
        if (pacientes.isEmpty()) {
            throw new Exception("No se encontraron pacientes con el criterio: " + criterio);
        }

        Paciente pacienteSeleccionado = pacientes.get(0);
        model.setPacienteSeleccionado(pacienteSeleccionado);

        model.nuevaReceta();
    }

    public List<Paciente> obtenerTodosPacientes() throws Exception {
        return Service.instance().getPacientes();
    }

    public List<Medicamento> obtenerMedicamentos() {
        return model.getMedicamentosDisponibles();
    }

    public List<Medicamento> buscarMedicamentos(String criterio) {
        List<Medicamento> todosMedicamentos = model.getMedicamentosDisponibles();
        if (criterio == null || criterio.trim().isEmpty()) {
            return todosMedicamentos;
        }

        return todosMedicamentos.stream()
                .filter(m ->
                        m.getCodigo().toLowerCase().contains(criterio.toLowerCase()) ||
                                m.getNombre().toLowerCase().contains(criterio.toLowerCase()) ||
                                m.getPresentacion().toLowerCase().contains(criterio.toLowerCase())
                )
                .collect(java.util.stream.Collectors.toList());
    }

    public void agregarMedicamento(String codigoMedicamento, int cantidad, String indicaciones) throws Exception {
        if (model.getPacienteSeleccionado() == null) {
            throw new Exception("Debe seleccionar un paciente primero");
        }

        if (cantidad <= 0) {
            throw new Exception("La cantidad debe ser mayor a 0");
        }

        if (indicaciones == null || indicaciones.trim().isEmpty()) {
            throw new Exception("Debe especificar las indicaciones");
        }

        boolean medicamentoExiste = model.getMedicamentosDisponibles().stream()
                .anyMatch(m -> m.getCodigo().equals(codigoMedicamento));

        if (!medicamentoExiste) {
            throw new Exception("El medicamento seleccionado no existe");
        }

        DetalleReceta detalle = new DetalleReceta(codigoMedicamento, cantidad, indicaciones.trim());
        model.agregarDetalle(detalle);
    }

    public void editarMedicamento(int index, int nuevaCantidad, String nuevasIndicaciones) throws Exception {
        if (nuevaCantidad <= 0) {
            throw new Exception("La cantidad debe ser mayor a 0");
        }

        if (nuevasIndicaciones == null || nuevasIndicaciones.trim().isEmpty()) {
            throw new Exception("Debe especificar las indicaciones");
        }

        List<DetalleReceta> detalles = model.getDetallesReceta();
        if (index < 0 || index >= detalles.size()) {
            throw new Exception("indice de medicamento invalido");
        }

        DetalleReceta detalle = detalles.get(index);
        detalle.setCantidad(nuevaCantidad);
        detalle.setIndicaciones(nuevasIndicaciones.trim());

        model.actualizarDetalle(index, detalle);
    }

    public void eliminarMedicamento(int index) throws Exception {
        List<DetalleReceta> detalles = model.getDetallesReceta();
        if (index < 0 || index >= detalles.size()) {
            throw new Exception("indice de medicamento invalido");
        }

        model.removerDetalle(index);
    }

    public void setFechaRetiro(LocalDate fecha) throws Exception {
        if (fecha == null) {
            throw new Exception("Debe seleccionar una fecha de retiro");
        }

        if (fecha.isBefore(LocalDate.now())) {
            throw new Exception("La fecha de retiro no puede ser anterior a hoy");
        }

        model.setFechaRetiro(fecha);
    }



    public void guardarReceta() throws Exception {
        if (model.getPacienteSeleccionado() == null) {
            throw new Exception("Debe seleccionar un paciente");
        }

        if (model.getDetallesReceta().isEmpty()) {
            throw new Exception("Debe agregar al menos un medicamento");
        }

        if (model.getFechaRetiro() == null) {
            throw new Exception("Debe seleccionar una fecha de retiro");
        }

        Receta receta = model.getRecetaActual();
        if (receta == null) {
            model.nuevaReceta();
            receta = model.getRecetaActual();
        }

        receta.setDetalles(model.getDetallesReceta());

        Service.instance().createReceta(receta, model.getFechaRetiro());

        if (hospital.Application.historicoController != null) {
            try {
                hospital.Application.historicoController.refrescarRecetas();
                System.out.println("Historico actualizado despues de guardar receta");
            } catch (Exception e) {
                System.err.println("Error actualizando histórico: " + e.getMessage());
            }
        }

        model.limpiarReceta();
    }

    public void limpiarReceta() {
        model.limpiarReceta();
    }

    private void cargarMedicamentos() {
        try {
            List<Medicamento> medicamentos = Service.instance().getMedicamentos();
            model.setMedicamentosDisponibles(medicamentos);
        } catch (Exception e) {
            System.err.println("Error cargando medicamentos: " + e.getMessage());
            model.setMedicamentosDisponibles(new ArrayList<>());
        }
    }
}