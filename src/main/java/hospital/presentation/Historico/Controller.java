package hospital.presentation.Historico;

import hospital.logic.Paciente;
import hospital.logic.Receta;
import hospital.logic.Service;

import java.util.ArrayList;
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
        List<Receta> recetas = Service.instance().getRecetas();
        model.setRecetas(recetas);
        model.setRecetasFiltradas(recetas);
        model.setCriterioFiltro("");
    }

    public void refrescarRecetas() throws Exception {
        List<Receta> todasLasRecetas = Service.instance().getRecetas();
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
        List<Receta> todasLasRecetas = model.getRecetas();
        List<Receta> recetasFiltradas = new ArrayList<>();

        // Primero buscar por ID de receta directamente
        for (Receta receta : todasLasRecetas) {
            if (receta.getId().toLowerCase().contains(criterioBusqueda)) {
                recetasFiltradas.add(receta);
            }
        }

        // Si no encontró por ID de receta, buscar por paciente
        if (recetasFiltradas.isEmpty()) {
            // Buscar por ID exacto de paciente
            for (Receta receta : todasLasRecetas) {
                if (receta.getPacienteId().toLowerCase().equals(criterioBusqueda)) {
                    recetasFiltradas.add(receta);
                }
            }

            // Si aún no encuentra, buscar por nombre de paciente
            if (recetasFiltradas.isEmpty()) {
                try {
                    List<Paciente> pacientesEncontrados = Service.instance().searchPacientes(criterioBusqueda);

                    if (!pacientesEncontrados.isEmpty()) {
                        List<String> idsLPacientes = pacientesEncontrados.stream()
                                .map(Paciente::getId)
                                .collect(Collectors.toList());

                        recetasFiltradas = todasLasRecetas.stream()
                                .filter(r -> idsLPacientes.contains(r.getPacienteId()))
                                .collect(Collectors.toList());
                    }
                } catch (Exception e) {
                    System.err.println("Error buscando pacientes: " + e.getMessage());
                }
            }
        }

        model.setRecetasFiltradas(recetasFiltradas);

        // Debug: mostrar información sobre la búsqueda
        System.out.println("Criterio de búsqueda: " + criterio);
        System.out.println("Total recetas en sistema: " + todasLasRecetas.size());
        System.out.println("Recetas encontradas: " + recetasFiltradas.size());

        if (todasLasRecetas.size() > 0) {
            System.out.println("Ejemplos de recetas existentes:");
            for (int i = 0; i < Math.min(3, todasLasRecetas.size()); i++) {
                Receta r = todasLasRecetas.get(i);
                System.out.println("  - ID: " + r.getId() + ", Paciente: " + r.getPacienteId());
            }
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