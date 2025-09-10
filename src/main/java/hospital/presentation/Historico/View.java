package hospital.presentation.Historico;

import hospital.logic.Receta;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.format.DateTimeFormatter;

public class View implements PropertyChangeListener {
    private JPanel panel1;
    private JLabel buscarHist;
    private JTextField buscarHistField;
    private JTable table1;
    private JButton buscarButton;

    private Model model;
    private Controller controller;

    public View() {
        setupEventHandlers();
        inicializarTabla();
    }

    public JPanel getPanel() {
        return panel1;
    }

    public void setModel(Model model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    private void setupEventHandlers() {
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarRecetas();
            }
        });

        buscarHistField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarRecetas();
            }
        });

        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table1.getSelectedRow();
                    if (row >= 0) {
                        try {
                            controller.seleccionarReceta(row);
                            mostrarDetallesReceta();
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(panel1,
                                    "Error al seleccionar receta: " + ex.getMessage(),
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
    }

    private void inicializarTabla() {
        table1.setRowHeight(25);
        table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void buscarRecetas() {
        if (controller == null) {
            JOptionPane.showMessageDialog(panel1,
                    "Error: Controller no inicializado",
                    "Error del sistema",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String criterio = buscarHistField.getText().trim();
            controller.buscarRecetas(criterio);

            if (model.getRecetasFiltradas().isEmpty() && !criterio.isEmpty()) {
                JOptionPane.showMessageDialog(panel1,
                        "No se encontraron recetas con el criterio: " + criterio,
                        "Sin resultados",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panel1,
                    "Error al buscar recetas: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDetallesReceta() {
        Receta receta = controller.obtenerRecetaSeleccionada();
        if (receta == null) {
            return;
        }

        try {
            String nombrePaciente = controller.obtenerNombrePaciente(receta.getPacienteId());
            String detallesMedicamentos = controller.obtenerDetallesMedicamentos(receta);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String fechaConfeccion = receta.getFecha() != null ?
                    receta.getFecha().format(formatter) : "Sin fecha";
            String fechaRetiro = receta.getFechaRetiro() != null ?
                    receta.getFechaRetiro().format(formatter) :
                    (receta.getFecha() != null ? receta.getFecha().plusDays(1).format(formatter) : "Sin fecha");

            String mensaje = String.format(
                    "═══════════════ DETALLES DE RECETA ═══════════════\n\n" +
                            "ID Receta: %s\n" +
                            "Paciente: %s (ID: %s)\n" +
                            "Fecha Confección: %s\n" +
                            "Fecha Retiro: %s\n" +
                            "Estado: Confeccionada\n\n" +
                            "═══ MEDICAMENTOS PRESCRITOS ═══\n\n%s",
                    receta.getId(),
                    nombrePaciente,
                    receta.getPacienteId(),
                    fechaConfeccion,
                    fechaRetiro,
                    detallesMedicamentos
            );

            JDialog dialog = new JDialog();
            dialog.setTitle("Detalles de Receta - " + receta.getId());
            dialog.setSize(500, 400);
            dialog.setLocationRelativeTo(panel1);
            dialog.setModal(true);

            JTextArea textArea = new JTextArea(mensaje);
            textArea.setFont(new java.awt.Font(java.awt.Font.MONOSPACED, java.awt.Font.PLAIN, 12));
            textArea.setEditable(false);
            textArea.setBackground(dialog.getBackground());
            textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());

            JButton cerrarButton = new JButton("Cerrar");
            cerrarButton.addActionListener(e -> dialog.dispose());

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(cerrarButton);

            dialog.setLayout(new java.awt.BorderLayout());
            dialog.add(scrollPane, java.awt.BorderLayout.CENTER);
            dialog.add(buttonPanel, java.awt.BorderLayout.SOUTH);

            dialog.setVisible(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panel1,
                    "Error al mostrar detalles: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case Model.RECETAS:
            case Model.RECETAS_FILTRADAS:
                actualizarTabla();
                break;
            case Model.CRITERIO_FILTRO:
                break;
        }
        this.panel1.revalidate();
    }

    public void refrescarDatos() {
        if (controller != null) {
            try {
                controller.refrescarRecetas();
            } catch (Exception e) {
                System.err.println("Error refrescando histórico: " + e.getMessage());
            }
        }
    }

    private void actualizarTabla() {
        int[] cols = {
                TableModel.ID_RECETA,
                TableModel.PACIENTE,
                TableModel.FECHA_CONFECCION,
                TableModel.FECHA_RETIRO,
                TableModel.ESTADO,
                TableModel.NUM_MEDICAMENTOS
        };

        table1.setModel(new TableModel(cols, model.getRecetasFiltradas()));
        table1.setRowHeight(25);

        if (table1.getColumnModel().getColumnCount() > 0) {
            TableColumnModel columnModel = table1.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(120);
            columnModel.getColumn(1).setPreferredWidth(200);
            columnModel.getColumn(2).setPreferredWidth(100);
            columnModel.getColumn(3).setPreferredWidth(100);
            columnModel.getColumn(4).setPreferredWidth(100);
            columnModel.getColumn(5).setPreferredWidth(80);
        }
    }

    public void limpiarBusqueda() {
        buscarHistField.setText("");
        if (controller != null) {
            controller.limpiarFiltro();
        }
    }
}