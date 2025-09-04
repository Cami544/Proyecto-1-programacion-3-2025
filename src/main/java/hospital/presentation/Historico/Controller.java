package hospital.presentation.Historico;

import hospital.logic.Paciente;
import hospital.logic.Receta;
import hospital.logic.Service;

import java.util.List;
import java.util.stream.Collectors;

public class Controller {
    private View view;
    private Model model;

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);

        try {
            cargarTodasLasRecetas();
        } catch (Exception e) {
            System.err.println("Error cargando recetas iniciales: " + e.getMessage());
        }
    }

    public void cargarTodasLasRecetas() throws Exception {
        List<Receta> recetas = Service.instance().findAllRecetas();
        model.setRecetas(recetas);
        model.setRecetasFiltradas(recetas);
        model.setCriterioFiltro("");
    }

    public void refrescarRecetas() throws Exception {
        List<Receta> todasLasRecetas = Service.instance().findAllRecetas();
        model.setRecetas(todasLasRecetas);
        model.setRecetasFiltradas(todasLasRecetas);
        model.setCriterioFiltro("");

        System.out.println("Historico actualizado. Total de recetas: " + todasLasRecetas.size());
    }

    public void buscarRecetas(String criterio) throws Exception {
        model.setCriterioFiltro(criterio);

        if (criterio == null || criterio.trim().isEmpty()) {
            model.setRecetasFiltradas(model.getRecetas());
            return;
        }

        String criterioBusqueda = criterio.toLowerCase().trim();

        List<Paciente> pacientesEncontrados = Service.instance().searchPaciente(criterioBusqueda);

        if (pacientesEncontrados.isEmpty()) {
            List<Receta> recetasFiltradas = model.getRecetas().stream()
                    .filter(r -> r.getId().toLowerCase().contains(criterioBusqueda))
                    .collect(Collectors.toList());

            model.setRecetasFiltradas(recetasFiltradas);
        } else {
            List<String> idsLPacientes = pacientesEncontrados.stream()
                    .map(Paciente::getId)
                    .collect(Collectors.toList());

            List<Receta> recetasFiltradas = model.getRecetas().stream()
                    .filter(r -> idsLPacientes.contains(r.getPacienteId()))
                    .collect(Collectors.toList());

            model.setRecetasFiltradas(recetasFiltradas);
        }
    }

    public void seleccionarReceta(int index) throws Exception {
        List<Receta> recetasMostradas = model.getRecetasFiltradas();

        if (index >= 0 && index < recetasMostradas.size()) {
            Receta recetaSeleccionada = recetasMostradas.get(index);
            model.setRecetaSeleccionada(recetaSeleccionada);
        } else {
            throw new Exception("indice de receta inválido");
        }
    }

    public Receta obtenerRecetaSeleccionada() {
        return model.getRecetaSeleccionada();
    }

    public String obtenerNombrePaciente(String pacienteId) {
        try {
            Paciente paciente = Service.instance().readPaciente(pacienteId);
            return paciente.getNombre();
        } catch (Exception e) {
            return pacienteId;
        }
    }

    public String obtenerDetallesMedicamentos(Receta receta) {
        if (receta.getDetalles() == null || receta.getDetalles().isEmpty()) {
            return "Sin medicamentos prescritos";
        }

        StringBuilder sb = new StringBuilder();
        receta.getDetalles().forEach(detalle -> {
            try {
                String nombreMedicamento = Service.instance().readMedicamento(detalle.getMedicamentoCodigo()).getNombre();
                sb.append(String.format("- %s (Cantidad: %d)\n  %s\n\n",
                        nombreMedicamento,
                        detalle.getCantidad(),
                        detalle.getIndicaciones()));
            } catch (Exception e) {
                sb.append(String.format("- %s (Cantidad: %d)\n  %s\n\n",
                        detalle.getMedicamentoCodigo(),
                        detalle.getCantidad(),
                        detalle.getIndicaciones()));
            }
        });

        return sb.toString();
    }

    public void limpiarFiltro() {
        try {
            model.setCriterioFiltro("");
            model.setRecetasFiltradas(model.getRecetas());
            model.setRecetaSeleccionada(null);
        } catch (Exception e) {
            System.err.println("Error limpiando filtro: " + e.getMessage());
        }
    }
}