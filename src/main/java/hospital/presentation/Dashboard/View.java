package hospital.presentation.Dashboard;

import hospital.logic.DetalleReceta;
import hospital.logic.Medicamento;
import hospital.logic.Receta;
import hospital.logic.Service;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class View implements PropertyChangeListener {
    private JComboBox<String> desdeAnio;
    private JComboBox<String> desdeMes;
    private JComboBox<String> hastaAnio;
    private JComboBox<String> hastaMes;
    private JComboBox<String> medicamentoBox;
    private JButton seleccionarUnoButton;
    private JTable table1;
    private JPanel panel;
    private JPanel panelGraficoLineas;
    private JPanel panelGraficoBarras;
    private JButton seleccionarTodoButton;
    private JButton borrarUnoButton;
    private JButton borrarTodoButton;

    // MVC
    private Model model;
    private Controller controller;

    public View() {
        //Aqui un actualizar, bueno mejor revisar
        setupEventHandlers();
        inicializarComponentes();
    }

    public JPanel getPanel() {
        return panel;
    }

    public void setModel(Model model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    public void setController(Controller controller) {
        this.controller = controller;
        this.controller.cargarMedicamentos();
    }

    private void setupEventHandlers() {
        seleccionarUnoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Validar que el usuario haya seleccionado un medicamento
                    if (medicamentoBox.getSelectedIndex() == -1 ||
                            medicamentoBox.getSelectedItem() == null ||
                            medicamentoBox.getSelectedItem().toString().trim().isEmpty()) {

                        JOptionPane.showMessageDialog(panel,
                                "Debe seleccionar un medicamento antes de continuar.",
                                "Advertencia",
                                JOptionPane.WARNING_MESSAGE);
                        return; // No ejecutar nada más si no hay selección
                    }
                    seleccionarMedicamento();
                    actualizarDashboard();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel,
                            "Error al seleccionar medicamento: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        seleccionarTodoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    LocalDate fechaDesde = obtenerFechaDesde();
                    LocalDate fechaHasta = obtenerFechaHasta();

                    controller.setMedicamentoSeleccionado(null);           // selección previa de medicamento
                    if (medicamentoBox.getItemCount() > 0) {
                        medicamentoBox.setSelectedIndex(0); // "Sin seleccionar"
                    }

                    List<Receta> todas = controller.obtenerRecetasEnRango(fechaDesde, fechaHasta);  // todas las recetas en rango de fechas

                    model.setRecetasDashboard(todas);      // Actualiza modelo con todas las recetas
                    controller.actualizarEstadisticas();

                    JOptionPane.showMessageDialog(panel,
                            "Se cargaron todas las recetas del período seleccionado.",
                            "Información",
                            JOptionPane.INFORMATION_MESSAGE);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel,
                            "Error al seleccionar todo: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        borrarUnoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int selectedRow = table1.getSelectedRow();
                    if (selectedRow == -1) {
                        JOptionPane.showMessageDialog(panel, "Seleccione una fila para borrar.");
                        return;
                    }

                    Object periodoObj = table1.getValueAt(selectedRow, 0);
                    Object medicamentoObj = table1.getValueAt(selectedRow, 1);
                    Object cantidadObj = table1.getValueAt(selectedRow, 2);

                    if (periodoObj == null || medicamentoObj == null || cantidadObj == null) {
                        JOptionPane.showMessageDialog(panel, "Fila inválida.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    String periodo = periodoObj.toString();
                    String medicamento = medicamentoObj.toString();
                    int cantidad;
                    if (cantidadObj instanceof Number) {
                        cantidad = ((Number) cantidadObj).intValue();
                    } else {
                        try { cantidad = Integer.parseInt(cantidadObj.toString()); }
                        catch (Exception ex) { cantidad = 0; }
                    }

                    int confirm = JOptionPane.showConfirmDialog(
                            panel,
                            "¿Está seguro de borrar la fila seleccionada?\nPeriodo: " + periodo + "\nMedicamento: " + medicamento,
                            "Confirmar borrado",
                            JOptionPane.YES_NO_OPTION
                    );

                    if (confirm != JOptionPane.YES_OPTION) return;

                    //  fila agregada con 0 recetas, elimina directamente de datosEstadisticas
                    if (cantidad == 0) {
                        List<Object[]> datos = model.getDatosEstadisticas() == null
                                ? new ArrayList<>()
                                : new ArrayList<>(model.getDatosEstadisticas());

                        boolean removed = datos.removeIf(arr -> periodo.equals(arr[0]) && medicamento.equals(arr[1]));
                        if (removed) {
                            model.setDatosEstadisticas(datos); // dispara actualización de tabla y gráfico de líneas
                            JOptionPane.showMessageDialog(panel, "Fila (sin datos) eliminada de la vista.");
                        } else {
                            JOptionPane.showMessageDialog(panel, "No se encontró la fila para eliminar.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                        }
                        return;
                    }

                    //  cantidad > 0  eliminar las Receta de recetasDashboard que coincidan con periodo + medicamento
                    List<Receta> actuales = model.getRecetasDashboard() == null
                            ? new ArrayList<>()
                            : new ArrayList<>(model.getRecetasDashboard());

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");

                    boolean removedAny = actuales.removeIf(r -> {
                        try {
                            // usar fechaRetiro
                            LocalDate fechaRef = (r.getFechaRetiro() != null) ? r.getFechaRetiro() : r.getFecha();
                            if (fechaRef == null) return false;
                            if (!fechaRef.format(formatter).equals(periodo)) return false;
                            if (r.getDetalles() == null) return false;

                            for (DetalleReceta d : r.getDetalles()) {
                                try {
                                    String nombreMed = Service.instance().readMedicamento(d.getMedicamentoCodigo()).getNombre();
                                    if (medicamento.equals(nombreMed)) return true;
                                } catch (Exception ex) {
                                    // fallback comparar por código
                                    if (medicamento.equals(d.getMedicamentoCodigo())) return true;
                                }
                            }
                        } catch (Exception ex) {
                            return false;
                        }
                        return false;
                    });

                    if (removedAny) { //Señor
                        // actualiza recetas temporales y recalcula estadísticas desde esas recetas
                        model.setRecetasDashboard(actuales);
                        controller.actualizarEstadisticas();
                        JOptionPane.showMessageDialog(panel, "Recetas eliminadas de la vista y estadísticas recalculadas.");
                    } else {
                        JOptionPane.showMessageDialog(panel, "No se encontraron recetas que coincidan con la fila seleccionada.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel,
                            "Error al borrar receta: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        borrarTodoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int confirm = JOptionPane.showConfirmDialog(
                            panel,
                            "¿Seguro que desea limpiar todas las recetas del Dashboard?\n(Esta acción no afecta al sistema).",
                            "Confirmación",
                            JOptionPane.YES_NO_OPTION
                    );

                    if (confirm == JOptionPane.YES_OPTION) {
                        model.setRecetasDashboard(new ArrayList<>());   // Vaciar la lista temporal
                        resetFiltrosPorDefecto();

                        controller.setMedicamentoSeleccionado(null); // Asegura que no haya medicamento seleccionado

                        controller.cargarMedicamentos();    // Recargar medicamentos en el combo

                        if (medicamentoBox != null && medicamentoBox.getItemCount() > 0) {
                            medicamentoBox.setSelectedIndex(0);
                        }

                        controller.actualizarEstadisticas();
                        JOptionPane.showMessageDialog(
                                panel,
                                "Se limpiaron todas las recetas del Dashboard.\n(No se eliminaron del sistema).",
                                "Información",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                            panel,
                            "Error al limpiar recetas: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        if (medicamentoBox != null) { // Listener selecciona medicamento desde el combo
            medicamentoBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    seleccionarMedicamento();
                }
            });
        }
    }

    private void inicializarComponentes() {
        LocalDate now = LocalDate.now();

        desdeAnio.setSelectedItem(String.valueOf(now.minusMonths(6).getYear()));
        desdeMes.setSelectedItem(now.minusMonths(6).getMonthValue() + "-" +
                obtenerNombreMes(now.minusMonths(6).getMonthValue()));

        hastaAnio.setSelectedItem(String.valueOf(now.getYear()));
        hastaMes.setSelectedItem(now.getMonthValue() + "-" + obtenerNombreMes(now.getMonthValue()));

        table1.setRowHeight(25);
        table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void actualizarDashboard() {
        try {
            seleccionarMedicamento(); // aseguramos leer el medicamento seleccionado en el combo

            LocalDate fechaDesde = obtenerFechaDesde();
            LocalDate fechaHasta = obtenerFechaHasta();

            controller.setFechaDesde(fechaDesde);
            controller.setFechaHasta(fechaHasta);

            // recargar recetas filtradas en el dashboard
            List<Receta> filtradas = controller.obtenerRecetasFiltradas(fechaDesde, fechaHasta, model.getMedicamentoSeleccionado());
            model.setRecetasDashboard(filtradas);

            controller.actualizarEstadisticas();

            JOptionPane.showMessageDialog(panel,
                    "Dashboard actualizado correctamente",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panel,
                    "Error al actualizar dashboard: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetFiltrosPorDefecto() {
        LocalDate now = LocalDate.now();
        // Poner los combos a los valores por defecto
        if (desdeAnio != null) desdeAnio.setSelectedItem(String.valueOf(now.minusMonths(6).getYear()));
        if (desdeMes != null) desdeMes.setSelectedItem(now.minusMonths(6).getMonthValue() + "-" + obtenerNombreMes(now.minusMonths(6).getMonthValue()));
        if (hastaAnio != null) hastaAnio.setSelectedItem(String.valueOf(now.getYear()));
        if (hastaMes != null) hastaMes.setSelectedItem(now.getMonthValue() + "-" + obtenerNombreMes(now.getMonthValue()));

        // limpiar combo medicamento
        if (medicamentoBox != null) medicamentoBox.setSelectedIndex(0);
        controller.setMedicamentoSeleccionado(null);
    }

    private void seleccionarMedicamento() {
        try {
            String seleccion = (String) medicamentoBox.getSelectedItem();
            if (seleccion == null || seleccion.isEmpty() || seleccion.equals("Sin seleccionar")) {
                controller.setMedicamentoSeleccionado(null);
                return;
            }

            String codigoSeleccion = seleccion.split(" - ")[0].trim();

            for (Medicamento med : controller.obtenerMedicamentos()) {
                if (med.getCodigo() != null && med.getCodigo().equalsIgnoreCase(codigoSeleccion)) {
                    controller.setMedicamentoSeleccionado(med);
                    return;
                }
            }
            //alternativa bsuqueda
            for (Medicamento med : controller.obtenerMedicamentos()) {
                String item = med.getCodigo() + " - " + med.getNombre() + " " + med.getPresentacion();
                if (item.equalsIgnoreCase(seleccion)) {
                    controller.setMedicamentoSeleccionado(med);
                    return;
                }
            }

            controller.setMedicamentoSeleccionado(null);
        } catch (Exception ex) {
            System.err.println("Error seleccionando medicamento: " + ex.getMessage());
        }
    }


    private LocalDate obtenerFechaDesde() throws Exception {
        try {
            int anio = Integer.parseInt((String) desdeAnio.getSelectedItem());
            int mes = extraerNumeroMes((String) desdeMes.getSelectedItem());
            return LocalDate.of(anio, mes, 1);
        } catch (Exception e) {
            throw new Exception("Fecha desde invalida");
        }
    }

    private LocalDate obtenerFechaHasta() throws Exception {
        try {
            int anio = Integer.parseInt((String) hastaAnio.getSelectedItem());
            int mes = extraerNumeroMes((String) hastaMes.getSelectedItem());
            return LocalDate.of(anio, mes, 1).plusMonths(1).minusDays(1);
        } catch (Exception e) {
            throw new Exception("Fecha hasta inválida");
        }
    }

    private int extraerNumeroMes(String mesTexto) {
        if (mesTexto != null && mesTexto.contains("-")) {
            return Integer.parseInt(mesTexto.split("-")[0]);
        }
        return 1;
    }

    private String obtenerNombreMes(int numeroMes) {
        String[] meses = {"", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        return numeroMes > 0 && numeroMes <= 12 ? meses[numeroMes] : "Enero";
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case Model.MEDICAMENTOS_DISPONIBLES:
                actualizarComboMedicamentos();
                break;
            case Model.DATOS_ESTADISTICAS:
                actualizarTablaEstadisticas();
                actualizarGraficoLineas();
                break;
            case Model.ESTADISTICAS_RECETAS:
                actualizarTablaEstadisticas();
                actualizarGraficoPastel();
                break;

            case Model.RECETAS_DASHBOARD:
                try {
                    controller.actualizarEstadisticas();
                    actualizarTablaEstadisticas(); // refrescar tabla con la lista actual
                } catch (Exception ex) {
                    System.err.println("Error recalculando estadísticas tras cambio en RecetasDashboard: " + ex.getMessage());
                }
                break;
        }
        if (panel != null) {
            this.panel.revalidate();
        }
    }

    private void actualizarComboMedicamentos() {
        medicamentoBox.removeAllItems();
        medicamentoBox.addItem("");

        for (Medicamento med : model.getMedicamentosDisponibles()) {
            String item = med.getCodigo() + " - " + med.getNombre() + " " + med.getPresentacion();
            medicamentoBox.addItem(item);
        }
    }

    private void actualizarGraficoLineas() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        //  agrupa datos
        Map<String, Map<String, Integer>> agrupado = new HashMap<>();

        for (Object[] fila : model.getDatosEstadisticas()) {
            String periodo = (String) fila[0]; // formato "MM/yyyy"
            String medicamento = (String) fila[1];
            Integer cantidad = (Integer) fila[2];

            agrupado.putIfAbsent(periodo, new HashMap<>());
            agrupado.get(periodo).merge(medicamento, cantidad, Integer::sum);
        }

        //  ordena por fecha
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
        List<String> periodosOrdenados = agrupado.keySet().stream()
                .sorted(Comparator.comparing(p -> java.time.YearMonth.parse(p, formatter)))
                .toList();

        //  carga dataset ya ordenado
        for (String periodo : periodosOrdenados) {
            for (Map.Entry<String, Integer> entryMed : agrupado.get(periodo).entrySet()) {
                dataset.addValue(entryMed.getValue(), entryMed.getKey(), periodo);
            }
        }

        // Crea gráfico
        JFreeChart chart = ChartFactory.createLineChart(
                "Medicamentos prescritos por mes",
                "Mes",
                "Cantidad",
                dataset
        );

        panelGraficoLineas.removeAll();
        panelGraficoLineas.add(new ChartPanel(chart));
        panelGraficoLineas.revalidate();
        panelGraficoLineas.repaint();
    }



    private void actualizarGraficoPastel() {
        DefaultPieDataset dataset = new DefaultPieDataset();

        for (Map.Entry<String, Integer> entry : model.getEstadisticasRecetas().entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "Estados de las Recetas",
                dataset,
                true,
                true,
                false
        );

        panelGraficoBarras.removeAll();
        panelGraficoBarras.add(new ChartPanel(chart));
        panelGraficoBarras.revalidate();
        panelGraficoBarras.repaint();
    }

    private void actualizarTablaEstadisticas() {
        int[] cols = {
                TableModel.PERIODO,
                TableModel.MEDICAMENTO,
                TableModel.CANTIDAD_PRESCRITA,
                TableModel.RECETAS_GENERADAS
        };

        table1.setModel(new TableModel(cols, model.getDatosEstadisticas()));
        table1.setRowHeight(25);

        if (table1.getColumnModel().getColumnCount() > 0) {
            TableColumnModel columnModel = table1.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(50);
            columnModel.getColumn(1).setPreferredWidth(80);
            columnModel.getColumn(2).setPreferredWidth(80);
            columnModel.getColumn(3).setPreferredWidth(80);
        }
    }
}