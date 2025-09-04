package hospital.presentation.Preescribir;

import hospital.logic.DetalleReceta;
import hospital.logic.Medicamento;
import hospital.logic.Paciente;
import hospital.logic.Service;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.List;

public class View implements PropertyChangeListener {
    private JButton buscarPacienteButton;
    private JButton agregarMedicamentoButton;
    private JTable tableReceta;
    private JButton guardarButton;
    private JButton limpiarButton;
    private JPanel panel;

    // MVC
    private Model model;
    private Controller controller;

    public View() {
        setupEventHandlers();
        inicializarTabla();
        agregarMedicamentoButton.setEnabled(false);
        guardarButton.setEnabled(false);
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
        buscarPacienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarDialogoBuscarPaciente();
            }
        });

        agregarMedicamentoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarDialogoAgregarMedicamento();
            }
        });

        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarReceta();
            }
        });

        limpiarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarReceta();
            }
        });

        tableReceta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tableReceta.getSelectedRow();
                    if (row >= 0) {
                        editarMedicamento(row);
                    }
                }
            }
        });
    }

    private void inicializarTabla() {
        tableReceta.setRowHeight(30);
        tableReceta.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void mostrarDialogoBuscarPaciente() {
        try {
            String criterio = JOptionPane.showInputDialog(panel, "Ingrese nombre o ID del paciente:");
            if (criterio != null && !criterio.trim().isEmpty()) {
                controller.buscarPaciente(criterio.trim());
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panel,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDialogoAgregarMedicamento() {
        if (controller == null) {
            JOptionPane.showMessageDialog(panel,
                    "Error: Controller no inicializado",
                    "Error del sistema",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (model.getPacienteSeleccionado() == null) {
            JOptionPane.showMessageDialog(panel,
                    "Debe seleccionar un paciente primero",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            List<Medicamento> medicamentos = controller.obtenerMedicamentos();
            if (medicamentos.isEmpty()) {
                JOptionPane.showMessageDialog(panel,
                        "No hay medicamentos disponibles",
                        "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Crear array para JOptionPane
            String[] opciones = medicamentos.stream()
                    .map(m -> m.getCodigo() + " - " + m.getNombre() + " " + m.getPresentacion())
                    .toArray(String[]::new);

            String seleccion = (String) JOptionPane.showInputDialog(
                    panel,
                    "Seleccione un medicamento:",
                    "Medicamentos",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );

            if (seleccion != null) {
                String codigo = seleccion.split(" - ")[0];
                mostrarDialogoDetallesMedicamento(codigo);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panel,
                    "Error al cargar medicamentos: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDialogoDetallesMedicamento(String codigoMedicamento) {
        try {
            String cantidadStr = JOptionPane.showInputDialog(panel, "Cantidad:", "1");
            if (cantidadStr == null) return;

            String indicaciones = JOptionPane.showInputDialog(panel, "Indicaciones:", "Tomar cada 8 horas");
            if (indicaciones == null) return;

            int cantidad = Integer.parseInt(cantidadStr);
            controller.agregarMedicamento(codigoMedicamento, cantidad, indicaciones);

            JOptionPane.showMessageDialog(panel,
                    "Medicamento agregado exitosamente",
                    "exito",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(panel,
                    "La cantidad debe ser un numero valido",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panel,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarMedicamento(int row) {
        if (row < 0 || row >= model.getDetallesReceta().size()) return;

        try {
            DetalleReceta detalle = model.getDetallesReceta().get(row);

            String nuevaCantidadStr = JOptionPane.showInputDialog(panel,
                    "Nueva cantidad:",
                    detalle.getCantidad());
            if (nuevaCantidadStr == null) return;

            String nuevasIndicaciones = JOptionPane.showInputDialog(panel,
                    "Nuevas indicaciones:",
                    detalle.getIndicaciones());
            if (nuevasIndicaciones == null) return;

            int nuevaCantidad = Integer.parseInt(nuevaCantidadStr);
            controller.editarMedicamento(row, nuevaCantidad, nuevasIndicaciones);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(panel,
                    "La cantidad debe ser un número válido",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panel,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardarReceta() {
        try {
            controller.setFechaRetiro(LocalDate.now().plusDays(1));

            int confirm = JOptionPane.showConfirmDialog(panel,
                    "Confirma guardar la receta para " + model.getPacienteSeleccionado().getNombre() + "?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                controller.guardarReceta();
                JOptionPane.showMessageDialog(panel,
                        "Receta guardada exitosamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panel,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarReceta() {
        int confirm = JOptionPane.showConfirmDialog(panel,
                "¿Está seguro de limpiar la receta actual?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            controller.limpiarReceta();
            JOptionPane.showMessageDialog(panel,
                    "Receta limpiada",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case Model.PACIENTE_SELECCIONADO:
                actualizarPacienteSeleccionado();
                break;
            case Model.DETALLES_RECETA:
                actualizarTablaReceta();
                break;
            case Model.MEDICAMENTOS_DISPONIBLES:
                // Los medicamentos se actualizan cuando se necesitan
                break;
        }
        this.panel.revalidate();
    }

    private void actualizarPacienteSeleccionado() {
        Paciente paciente = model.getPacienteSeleccionado();
        boolean hayPaciente = paciente != null;

        agregarMedicamentoButton.setEnabled(hayPaciente);
        guardarButton.setEnabled(hayPaciente);

        if (hayPaciente) {
            JOptionPane.showMessageDialog(panel,
                    "Paciente seleccionado: " + paciente.getNombre() + " (ID: " + paciente.getId() + ")",
                    "Paciente",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void actualizarTablaReceta() {
        int[] cols = {TableModel.MEDICAMENTO, TableModel.PRESENTACION, TableModel.CANTIDAD, TableModel.INDICACIONES};
        tableReceta.setModel(new TableModel(cols, model.getDetallesReceta()));
        tableReceta.setRowHeight(30);

        if (tableReceta.getColumnModel().getColumnCount() > 0) {
            TableColumnModel columnModel = tableReceta.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(150);
            columnModel.getColumn(1).setPreferredWidth(120);
            columnModel.getColumn(2).setPreferredWidth(80);
            columnModel.getColumn(3).setPreferredWidth(250);
        }
    }
}