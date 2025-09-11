package hospital.presentation.Despacho;

import hospital.logic.Farmaceuta;
import hospital.logic.Receta;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class View implements PropertyChangeListener {
    private JTable list;
    private JTextField buscarIdText;
    private JLabel idPacienteLabel;
    private JButton buscarButton;
    private JComboBox<Farmaceuta> farmaceutaComboBox;
    private JLabel estadoRecetaLabel;
    private JComboBox<String> recetaComboBox;
    private JButton guardarButton;
    private JButton limpiarButton;
    private JPanel panel;

    private Model model;
    private Controller controller;

    public View() {
        setupEventHandlers();
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
        buscarButton.addActionListener(e -> {
            try {
                String idPaciente = buscarIdText.getText().trim();
                controller.buscarRecetasPorPaciente(idPaciente);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Error en busqueda: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Farmaceuta farmaceuta = (Farmaceuta) farmaceutaComboBox.getSelectedItem();
                    String estado = (String) recetaComboBox.getSelectedItem();
                    String farmaceutaId = farmaceuta != null ? farmaceuta.getId() : null;
                    controller.guardarCambiosReceta(farmaceutaId, estado);

                    JOptionPane.showMessageDialog(panel,
                            "La receta se actualizo correctamente.",
                            "Información",
                            JOptionPane.INFORMATION_MESSAGE);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel,
                            "Error al guardar cambios: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = list.getSelectedRow();
                    if (row >= 0) {
                        try {
                            controller.seleccionarRecetaPaciente(row);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(panel, "Error al seleccionar receta: " + ex.getMessage(),
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
        limpiarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarIdText.setText("");
                try {
                    controller.refrecarDatos();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "Error al refrescar datos: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void actualizarCombos() {
        farmaceutaComboBox.removeAllItems();
        for (Farmaceuta f : model.getListFarmaceutas()) {
            farmaceutaComboBox.addItem(f);
        }

        recetaComboBox.removeAllItems();
        recetaComboBox.addItem("Confeccionada");
        recetaComboBox.addItem("En proceso");
        recetaComboBox.addItem("Lista");
        recetaComboBox.addItem("Entregada");
    }

    private void actualizarDetalleReceta() {
        Receta receta = model.getRecetaSeleccionada();
        if (receta != null) {
            buscarIdText.setText(receta.getPacienteId());

            if (receta.getFarmaceutaId() != null) {
                for (int i = 0; i < farmaceutaComboBox.getItemCount(); i++) {
                    Farmaceuta f = (Farmaceuta) farmaceutaComboBox.getItemAt(i);
                    if (f != null && receta.getFarmaceutaId().equals(f.getId())) {
                        farmaceutaComboBox.setSelectedItem(f);
                        break;
                    }
                }
            } else {
                farmaceutaComboBox.setSelectedIndex(-1);
            }

            if (receta.getEstadoReceta() != null) {
                recetaComboBox.setSelectedItem(receta.getEstadoReceta());
            } else {
                recetaComboBox.setSelectedIndex(-1);
            }
        } else {
            buscarIdText.setText("");
            farmaceutaComboBox.setSelectedIndex(-1);
            recetaComboBox.setSelectedIndex(-1);
        }
    }

    private void actualizarTabla() {
        int[] cols = {TableModel.FARNACEUTA, TableModel.ID_RECETA, TableModel.PACIENTE,
                TableModel.FECHA_RETIRO, TableModel.ESTADO};

        if (model.getRecetasFiltradasPaciente() != null && !model.getRecetasFiltradasPaciente().isEmpty()) {
            list.setModel(new TableModel(cols, model.getRecetasFiltradasPaciente()));
        } else {
            list.setModel(new TableModel(cols, model.getListRecetas()));
        }

        list.setRowHeight(30);
        if (list.getColumnModel().getColumnCount() > 0) {
            TableColumnModel columnModel = list.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(150);
            columnModel.getColumn(1).setPreferredWidth(80);
            columnModel.getColumn(2).setPreferredWidth(150);
            columnModel.getColumn(3).setPreferredWidth(80);
            columnModel.getColumn(4).setPreferredWidth(150);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case Model.LIST_RECETA, Model.RECETA_FILTRADO:
                actualizarTabla();
                break;
            case Model.LIST_FARMACIA:
                actualizarCombos();
                break;
            case Model.RECETA_SELECCIONADO:
                actualizarDetalleReceta();
                break;
        }
        this.panel.revalidate();
    }
}