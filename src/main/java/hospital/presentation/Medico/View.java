package hospital.presentation.Medico;

import hospital.Application;
import hospital.logic.Medico;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class View implements PropertyChangeListener {
    private JPanel panel;
    private JLabel idLabel;
    private JTextField idText;
    private JLabel nombreLabel;
    private JTextField nombreText;
    private JLabel especialidadLabel;
    private JTextField especialidadText;
    private JLabel busquedaLabel;
    private JTextField busquedaText;
    private JTable table;
    private JButton guardarButton;
    private JButton limpiarButton;
    private JButton borrarButton;
    private JButton buscarButton;
    private JButton reporteButton;

    private Controller controller;
    private Model model;

    public View() {
        setupEventHandlers();
    }

    public JPanel getPanel() {
        return panel;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setModel(Model model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    private void setupEventHandlers() {
        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validate()) {
                    Medico medico = take();
                    try {
                        controller.save(medico);
                        JOptionPane.showMessageDialog(panel,
                                "Médico guardado exitosamente",
                                "Éxito",
                                JOptionPane.INFORMATION_MESSAGE);
                        clear();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(panel,
                                ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        limpiarButton.addActionListener(e -> {
            clear();
            controller.clear();
        });

        borrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.getCurrent() == null || model.getCurrent().getId() == null ||
                        model.getCurrent().getId().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(panel,
                            "Seleccione un médico de la lista para eliminar",
                            "Advertencia",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(panel,
                        "¿Está seguro de eliminar al médico " + model.getCurrent().getNombre() + "?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        controller.delete();
                        JOptionPane.showMessageDialog(panel,
                                "Médico eliminado exitosamente",
                                "Éxito",
                                JOptionPane.INFORMATION_MESSAGE);
                        clear();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(panel,
                                ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        buscarButton.addActionListener(e -> {
            String criterio = busquedaText.getText().trim();
            controller.filter(criterio);
        });

        reporteButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(panel,
                    "Función de reporte en desarrollo",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        // Doble clic en tabla para editar
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row >= 0) {
                        TableModel tableModel = (TableModel) table.getModel();
                        Medico medico = tableModel.getRowAt(row);
                        model.setCurrent(medico);
                    }
                }
            }
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case Model.LIST:
                updateTable();
                break;
            case Model.FILTERED:
                updateFilteredTable();
                break;
            case Model.CURRENT:
                updateCurrentFields();
                break;
        }
        this.panel.revalidate();
    }

    private void updateTable() {
        int[] cols = {TableModel.ID, TableModel.NOMBRE, TableModel.ESPECIALIDAD};
        table.setModel(new TableModel(cols, model.getList()));
        table.setRowHeight(25);
        adjustColumnWidths();
    }

    private void updateFilteredTable() {
        int[] cols = {TableModel.ID, TableModel.NOMBRE, TableModel.ESPECIALIDAD};
        table.setModel(new TableModel(cols, model.getFiltered()));
        table.setRowHeight(25);
        adjustColumnWidths();
    }

    private void adjustColumnWidths() {
        if (table.getColumnModel().getColumnCount() > 0) {
            TableColumnModel columnModel = table.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(80);   // ID
            columnModel.getColumn(1).setPreferredWidth(200);  // Nombre
            columnModel.getColumn(2).setPreferredWidth(150);  // Especialidad
        }
    }

    private void updateCurrentFields() {
        if (model.getCurrent() != null) {
            idText.setText(model.getCurrent().getId() != null ? model.getCurrent().getId() : "");
            nombreText.setText(model.getCurrent().getNombre() != null ? model.getCurrent().getNombre() : "");
            especialidadText.setText(model.getCurrent().getEspecialidad() != null ? model.getCurrent().getEspecialidad() : "");
        }
        clearValidationErrors();
    }

    private boolean validate() {
        boolean valid = true;
        clearValidationErrors();

        if (idText.getText().trim().isEmpty()) {
            setFieldError(idText, "ID requerido");
            valid = false;
        }

        if (nombreText.getText().trim().isEmpty()) {
            setFieldError(nombreText, "Nombre requerido");
            valid = false;
        }

        if (especialidadText.getText().trim().isEmpty()) {
            setFieldError(especialidadText, "Especialidad requerida");
            valid = false;
        }

        return valid;
    }

    private void setFieldError(JTextField field, String message) {
        field.setBackground(Application.BACKGROUND_ERROR);
        field.setToolTipText(message);
        field.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
    }

    private void clearValidationErrors() {
        JTextField[] fields = {idText, nombreText, especialidadText};
        for (JTextField field : fields) {
            field.setBackground(Color.WHITE);
            field.setToolTipText(null);
            field.setBorder(UIManager.getBorder("TextField.border"));
        }
    }

    private Medico take() {
        Medico medico = new Medico();
        medico.setId(idText.getText().trim());
        medico.setNombre(nombreText.getText().trim());
        medico.setEspecialidad(especialidadText.getText().trim());
        return medico;
    }

    private void clear() {
        idText.setText("");
        nombreText.setText("");
        especialidadText.setText("");
        busquedaText.setText("");
        clearValidationErrors();
    }
}