package hospital.presentation.Dashboard;

import hospital.logic.Medicamento;
import hospital.logic.Receta;
import hospital.logic.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Controller {
    private View view;
    private Model model;

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);

        cargarMedicamentos();

        try {
            // Copia de las recetas del Service
            List<Receta> recetasOriginales = Service.instance().getRecetas();
            model.setRecetasDashboard(new ArrayList<>(recetasOriginales));
        } catch (Exception e) {
            model.setRecetasDashboard(new ArrayList<>());
        }

    }

    public void setFechaDesde(LocalDate fecha) throws Exception {
        if (fecha == null) {
            throw new Exception("La fecha desde no puede ser nula");
        }

        if (model.getFechaHasta() != null && fecha.isAfter(model.getFechaHasta())) {
            throw new Exception("La fecha desde no puede ser posterior a la fecha hasta");
        }

        model.setFechaDesde(fecha);
    }

    public void setFechaHasta(LocalDate fecha) throws Exception {
        if (fecha == null) {
            throw new Exception("La fecha hasta no puede ser nula");
        }

        if (model.getFechaDesde() != null && fecha.isBefore(model.getFechaDesde())) {
            throw new Exception("La fecha hasta no puede ser anterior a la fecha desde");
        }

        model.setFechaHasta(fecha);
    }

    public void setMedicamentoSeleccionado(Medicamento medicamento) {
        model.setMedicamentoSeleccionado(medicamento);
    }

    public List<Medicamento> obtenerMedicamentos() {
        return model.getMedicamentosDisponibles();
    }

    public void actualizarEstadisticas() throws Exception {
        LocalDate fechaDesde = model.getFechaDesde();
        LocalDate fechaHasta = model.getFechaHasta();
        Medicamento medicamento = model.getMedicamentoSeleccionado();

        List<Object[]> estadisticas = generarEstadisticasMedicamentos(fechaDesde, fechaHasta, medicamento);
        model.setDatosEstadisticas(estadisticas);

        Map<String, Integer> estadisticasRecetas = generarEstadisticasRecetas();
        model.setEstadisticasRecetas(estadisticasRecetas);
    }

    private List<Object[]> generarEstadisticasMedicamentos(LocalDate desde, LocalDate hasta, Medicamento medicamento) {
        List<Object[]> estadisticas = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");

        try {
          //  model.setRecetasDashboard(Service.instance().getRecetas());
           List<Receta> recetas = model.getRecetasDashboard();
           // List<Receta> recetas = Service.instance().getRecetas();

            List<Receta> recetasFiltradas = recetas.stream()
                    .filter(r -> !r.getFecha().isBefore(desde) && !r.getFecha().isAfter(hasta))
                    .collect(Collectors.toList());

            Map<String, Map<String, Integer>> estadisticasPorMes = new LinkedHashMap<>();

            LocalDate fechaActual = desde.withDayOfMonth(1);
            while (!fechaActual.isAfter(hasta)) {
                String periodo = fechaActual.format(formatter);
                estadisticasPorMes.put(periodo, new HashMap<>());
                fechaActual = fechaActual.plusMonths(1);
            }

            for (Receta receta : recetasFiltradas) {
                String periodo = receta.getFecha().format(formatter);

                if (receta.getDetalles() != null) {
                    for (hospital.logic.DetalleReceta detalle : receta.getDetalles()) {
                        if (medicamento == null || medicamento.getCodigo().equals(detalle.getMedicamentoCodigo())) {
                            String nombreMed = obtenerNombreMedicamento(detalle.getMedicamentoCodigo());

                            estadisticasPorMes.get(periodo).merge(nombreMed, detalle.getCantidad(), Integer::sum);
                        }
                    }
                }
            }

            for (Map.Entry<String, Map<String, Integer>> entryMes : estadisticasPorMes.entrySet()) {
                String periodo = entryMes.getKey();
                Map<String, Integer> medicamentosPorMes = entryMes.getValue();

                if (medicamentosPorMes.isEmpty()) {
                    String nombreMed = medicamento != null ? medicamento.getNombre() : "Sin datos";
                    estadisticas.add(new Object[]{periodo, nombreMed, 0, 0});
                } else {
                    for (Map.Entry<String, Integer> entryMed : medicamentosPorMes.entrySet()) {
                        estadisticas.add(new Object[]{
                                periodo,
                                entryMed.getKey(),
                                entryMed.getValue(),
                                1
                        });
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Error generando estadísticas: " + e.getMessage());
            estadisticas = generarDatosSimulados(desde, hasta, medicamento);
        }

        return estadisticas;
    }

    private Map<String, Integer> generarEstadisticasRecetas() {

        Map<String, Integer> estadisticas = new HashMap<>();

        try {
           // model.setRecetasDashboard(Service.instance().getRecetas());
            List<Receta> todasRecetas = model.getRecetasDashboard();

            LocalDate desde = model.getFechaDesde();
            LocalDate hasta = model.getFechaHasta();

            // Filtrar por rango de fechas
            List<Receta> recetasFiltradas = todasRecetas.stream()
                    .filter(r -> !r.getFecha().isBefore(desde) && !r.getFecha().isAfter(hasta))
                    .collect(Collectors.toList());

            // Contadores por estado
            int confeccionadas = 0;
            int enProceso = 0;
            int listas = 0;
            int entregadas = 0;

            for (Receta r : recetasFiltradas) {
                switch (r.getEstadoReceta()) {
                    case "Confeccionada" -> confeccionadas++;
                    case "En proceso" -> enProceso++;
                    case "Lista" -> listas++;
                    case "Entregada" -> entregadas++;
                }
            }

            // Guardar en el Map
            estadisticas.put("Confeccionadas", confeccionadas);
            estadisticas.put("En Proceso", enProceso);
            estadisticas.put("Listas", listas);
            estadisticas.put("Entregadas", entregadas);

        } catch (Exception e) {
            // fallback si algo falla
            estadisticas.put("Confeccionadas", 0);
            estadisticas.put("En Proceso", 0);
            estadisticas.put("Listas", 0);
            estadisticas.put("Entregadas", 0);
            System.err.println("Error generando estadísticas de recetas: " + e.getMessage());
        }

        return estadisticas;

    }

    private List<Object[]> generarDatosSimulados(LocalDate desde, LocalDate hasta, Medicamento medicamento) {
        List<Object[]> datos = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");

        LocalDate fechaActual = desde.withDayOfMonth(1);
        while (!fechaActual.isAfter(hasta)) {
            String periodo = fechaActual.format(formatter);
            String nombreMed = medicamento != null ? medicamento.getNombre() : "Acetaminofén";
            int cantidad = (int) (Math.random() * 100) + 10;
            int recetas = (int) (Math.random() * 20) + 5;

            datos.add(new Object[]{periodo, nombreMed, cantidad, recetas});
            fechaActual = fechaActual.plusMonths(1);
        }

        return datos;
    }

    private String obtenerNombreMedicamento(String codigo) {
        try {
            return Service.instance().readMedicamento(codigo).getNombre();
        } catch (Exception e) {
            System.err.println("Error obteniendo medicamento con código: " + codigo);
            return codigo;
        }
    }

    private void cargarMedicamentos() {
        try {
            List<Medicamento> medicamentos = Service.instance().getMedicamentos();
            model.setMedicamentosDisponibles(medicamentos);
            System.out.println("Medicamentos cargados: " + medicamentos.size());
        } catch (Exception e) {
            System.err.println("Error cargando medicamentos: " + e.getMessage());
            model.setMedicamentosDisponibles(new ArrayList<>());
        }
    }

    public void exportarEstadisticas() throws Exception {
        throw new Exception("en desarrollo...");
    }
}