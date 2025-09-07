package hospital.presentation.Dashboard;

import hospital.logic.Medicamento;
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
import java.util.ArrayList;
import java.util.Map;

public class View implements PropertyChangeListener {
    private JComboBox<String> desdeAnio;
    private JComboBox<String> desdeMes;
    private JComboBox<String> hastaAnio;
    private JComboBox<String> hastaMes;
    private JComboBox<String> medicamentoBox;
    private JButton seleccionaUnoButton;
    private JTable table1;
    private JPanel panel;
    private JPanel panelGraficoLineas;
    private JPanel panelGraficoBarras;
    private JButton seleccionaTodoButton;
    private JButton eliminarTodoButton;
    private JScrollPane eleminarTodoButton;
    private JButton eliminarUnoButton;
    private JPanel datosPanel;

    // MVC
    private Model model;
    private Controller controller;

    public View() {
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
    }

    private void setupEventHandlers() {
        seleccionaUnoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarDashboard();
            }
        });
        seleccionaTodoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        medicamentoBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seleccionarMedicamento();
            }
        });
        eliminarUnoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int col = table1.getSelectedColumn();
                if (col >= 0) {
                    TableColumnModel columnModel = table1.getColumnModel();
                    columnModel.removeColumn(columnModel.getColumn(col));
                    // refrescar gráficos con los datos actuales
                    actualizarGraficoLineas();
                    actualizarGraficoPastel();
                } else {
                    JOptionPane.showMessageDialog(panel, "Seleccione una columna para eliminar",
                            "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        eliminarTodoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    model.setDatosEstadisticas(new ArrayList<>()); // lista vacía
                    actualizarTablaEstadisticas();
                    actualizarGraficoLineas();
                    actualizarGraficoPastel();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "Error al eliminar todas las columnas: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
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
            LocalDate fechaDesde = obtenerFechaDesde();
            LocalDate fechaHasta = obtenerFechaHasta();

            controller.setFechaDesde(fechaDesde);
            controller.setFechaHasta(fechaHasta);

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

    private void seleccionarMedicamento() {
        try {
            String seleccion = (String) medicamentoBox.getSelectedItem();
            if (seleccion == null || seleccion.isEmpty()) {
                controller.setMedicamentoSeleccionado(null);
                return;
            }

            for (Medicamento med : controller.obtenerMedicamentos()) {
                String item = med.getCodigo() + " - " + med.getNombre() + " " + med.getPresentacion();
                if (item.equals(seleccion)) {
                    controller.setMedicamentoSeleccionado(med);
                    break;
                }
            }

        } catch (Exception ex) {
            System.err.println("Error seleccionando medicamento: " + ex.getMessage());
        }
        try {
            controller.actualizarEstadisticas();
        } catch (Exception e) {
            throw new RuntimeException(e);
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
                actualizarGraficoPastel();
                break;
        }
        if (panel != null) {
            this.panel.revalidate();
            panelGraficoLineas.repaint();
            panelGraficoBarras.repaint();
        }
    }


    private void actualizarComboMedicamentos() {
        medicamentoBox.removeAllItems();
        medicamentoBox.addItem("Todos los medicamentos");

        for (Medicamento med : model.getMedicamentosDisponibles()) {
            String item = med.getCodigo() + " - " + med.getNombre() + " " + med.getPresentacion();
            medicamentoBox.addItem(item);
        }
    }

    private void actualizarGraficoLineas() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Object[] fila : model.getDatosEstadisticas()) {
            String periodo = (String) fila[0];          // MM/yyyy
            String medicamento = (String) fila[1];      // nombre medicamento
            Integer cantidad = (Integer) fila[2];       // cantidad prescrita

            dataset.addValue(cantidad, medicamento, periodo);
        }

        JFreeChart chart = ChartFactory.createLineChart(
                "Medicamentos prescritos por mes",
                "Mes",
                "Cantidad",
                dataset
        );

        panelGraficoLineas.removeAll();
        panelGraficoLineas.add(new ChartPanel(chart));
        panelGraficoLineas.revalidate();

    }

    private void actualizarGraficoPastel() {
        DefaultPieDataset dataset = new DefaultPieDataset();

        for (Map.Entry<String, Integer> entry : model.getEstadisticasRecetas().entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "Estados de las Recetas",
                dataset,
                true,   // incluir leyenda
                true,
                false
        );

        panelGraficoBarras.removeAll();
        panelGraficoBarras.add(new ChartPanel(chart));
        panelGraficoBarras.revalidate();
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