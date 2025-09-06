package hospital.presentation.Medicamento;

import hospital.Application;
import hospital.logic.Medicamento;

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
    private JLabel codigoLabel;
    private JTextField codigoText;
    private JLabel nombreLabel;
    private JTextField nombreText;
    private JLabel presentacionLabel;
    private JTextField presentacionText;
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
                    Medicamento medicamento = take();
                    try {
                        controller.save(medicamento);
                        JOptionPane.showMessageDialog(panel,
                                "Medicamento guardado exitosamente",
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
                if (model.getCurrent() == null || model.getCurrent().getCodigo() == null ||
                        model.getCurrent().getCodigo().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(panel,
                            "Seleccione un medicamento de la lista para eliminar",
                            "Advertencia",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(panel,
                        "¿Está seguro de eliminar el medicamento " + model.getCurrent().getNombre() + "?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        controller.delete();
                        JOptionPane.showMessageDialog(panel,
                                "Medicamento eliminado exitosamente",
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

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row >= 0) {
                        TableModel tableModel = (TableModel) table.getModel();
                        Medicamento medicamento = tableModel.getRowAt(row);
                        model.setCurrent(medicamento);
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
        int[] cols = {TableModel.CODIGO, TableModel.NOMBRE, TableModel.PRESENTACION};
        table.setModel(new TableModel(cols, model.getList()));
        table.setRowHeight(25);
        adjustColumnWidths();
    }

    private void updateFilteredTable() {
        int[] cols = {TableModel.CODIGO, TableModel.NOMBRE, TableModel.PRESENTACION};
        table.setModel(new TableModel(cols, model.getFiltered()));
        table.setRowHeight(25);
        adjustColumnWidths();
    }

    private void adjustColumnWidths() {
        if (table.getColumnModel().getColumnCount() > 0) {
            TableColumnModel columnModel = table.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(100);
            columnModel.getColumn(1).setPreferredWidth(200);
            columnModel.getColumn(2).setPreferredWidth(150);
        }
    }

    private void updateCurrentFields() {
        if (model.getCurrent() != null) {
            codigoText.setText(model.getCurrent().getCodigo() != null ? model.getCurrent().getCodigo() : "");
            nombreText.setText(model.getCurrent().getNombre() != null ? model.getCurrent().getNombre() : "");
            presentacionText.setText(model.getCurrent().getPresentacion() != null ? model.getCurrent().getPresentacion() : "");
        }
        clearValidationErrors();
    }

    private boolean validate() {
        boolean valid = true;
        clearValidationErrors();

        if (codigoText.getText().trim().isEmpty()) {
            setFieldError(codigoText, "Código requerido");
            valid = false;
        }

        if (nombreText.getText().trim().isEmpty()) {
            setFieldError(nombreText, "Nombre requerido");
            valid = false;
        }

        if (presentacionText.getText().trim().isEmpty()) {
            setFieldError(presentacionText, "Presentación requerida");
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
        JTextField[] fields = {codigoText, nombreText, presentacionText};
        for (JTextField field : fields) {
            field.setBackground(Color.WHITE);
            field.setToolTipText(null);
            field.setBorder(UIManager.getBorder("TextField.border"));
        }
    }

    private Medicamento take() {
        Medicamento medicamento = new Medicamento();
        medicamento.setCodigo(codigoText.getText().trim());
        medicamento.setNombre(nombreText.getText().trim());
        medicamento.setPresentacion(presentacionText.getText().trim());
        return medicamento;
    }

    private void clear() {
        codigoText.setText("");
        nombreText.setText("");
        presentacionText.setText("");
        busquedaText.setText("");
        clearValidationErrors();
    }
}