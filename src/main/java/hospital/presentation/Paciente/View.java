package hospital.presentation.Paciente;

import com.github.lgooddatepicker.components.DatePicker;
import hospital.Application;
import hospital.logic.Paciente;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class View implements PropertyChangeListener {
    private JPanel panel;
    private JLabel idLabel;
    private JTextField idText;
    private JLabel nombreLabel;
    private JTextField nombreText;
    private JTable pacienteList;
    private JLabel fechaNacimiLabel;
    private JLabel telefonoLabel;
    private JTextField telefonoText;
    private JButton guardarButton;
    private JButton eliminarButton;
    private JButton generarPdfButton;
    private JLabel buscarNomLabel;
    private JTextField buscarNomText;
    private JButton filtrarButton;
    private JPanel Busqueda;
    private DatePicker DatePicker;

    private Model model;
    private Controller controller;

    public JPanel getPanel() {
        return panel;
    }

    public View() {
        setupEventHandlers();
        ajustarTamanosCampos();
    }

    private void setupEventHandlers() {
        pacienteList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = pacienteList.getSelectedRow();
                    if (row >= 0) {
                        TableModel tableModel = (TableModel) pacienteList.getModel();
                        Paciente paciente = tableModel.getRowAt(row);
                        model.setCurrent(paciente);
                    }
                }
            }
        });

        generarPdfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.generarReporte();
                    JOptionPane.showMessageDialog(panel,
                            "Reporte PDF generado con éxito.",
                            "Información",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel,
                            "Error al generar el reporte: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.getCurrent() == null || model.getCurrent().getId() == null ||
                        model.getCurrent().getId().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(panel,
                            "Seleccione un paciente de la lista para eliminar",
                            "Advertencia",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(panel,
                        "Esta seguro de eliminar a este paciente " + model.getCurrent().getNombre() + "?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        controller.delete();
                        JOptionPane.showMessageDialog(panel,
                                "Paciente eliminado exitosamente",
                                "exito",
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

        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(validate()){
                    Paciente paciente = take();
                    try{
                        controller.save(paciente);
                        JOptionPane.showMessageDialog(panel,
                                "Paciente guardado exitosamente",
                                "exito",
                                JOptionPane.INFORMATION_MESSAGE);
                        clear();
                    }catch (Exception ex){
                        JOptionPane.showMessageDialog(panel,
                                ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        filtrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String criterio = buscarNomText.getText().trim();
                controller.filter(criterio);
            }
        });
    }

    private void ajustarTamanosCampos() {
        java.awt.Dimension tamanoPreferido = new java.awt.Dimension(150, 25);
        java.awt.Dimension tamanoMinimo = new java.awt.Dimension(100, 25);

        if (idText != null) {
            idText.setPreferredSize(tamanoPreferido);
            idText.setMinimumSize(tamanoMinimo);
        }

        if (nombreText != null) {
            nombreText.setPreferredSize(tamanoPreferido);
            nombreText.setMinimumSize(tamanoMinimo);
        }

        if (telefonoText != null) {
            telefonoText.setPreferredSize(tamanoPreferido);
            telefonoText.setMinimumSize(tamanoMinimo);
        }

        if (panel != null) {
            panel.revalidate();
            panel.repaint();
        }
    }

    private boolean validate(){
        boolean valid = true;
        clearValidationErrors();

        if (idText.getText().trim().isEmpty()) {
            setFieldError(idText, "El ID no puede estar vacio");
            valid = false;
        }
        if (nombreText.getText().trim().isEmpty()) {
            setFieldError(nombreText, "El nombre no puede estar vacio");
            valid = false;
        }

        if (DatePicker.getDate() == null) {
            DatePicker.setBackground(Application.BACKGROUND_ERROR);
            DatePicker.setToolTipText("Debe seleccionar una fecha de nacimiento");
            valid = false;
        }

        if (telefonoText.getText().trim().isEmpty()) {
            setFieldError(telefonoText, "El telefono no puede estar vacio");
            valid = false;
        }

        return valid;
    }

    private void setFieldError(JTextField field, String message) {
        field.setBackground(Application.BACKGROUND_ERROR);
        field.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        field.setToolTipText(message);
    }

    private void clearValidationErrors() {
        JTextField[] fields = {idText, nombreText, telefonoText};
        for (JTextField field : fields) {
            if (field != null) {
                field.setBackground(Color.WHITE);
                field.setBorder(UIManager.getBorder("TextField.border"));
                field.setToolTipText(null);
            }
        }

        if (DatePicker != null) {
            DatePicker.setBackground(Color.WHITE);
            DatePicker.setToolTipText(null);
        }
    }

    public void clear(){
        idText.setText("");
        nombreText.setText("");
        telefonoText.setText("");

        DatePicker.setDate(null);

        clearValidationErrors();
    }

    public Paciente take(){
        Paciente p = new Paciente();
        p.setId(idText.getText().trim());
        p.setNombre(nombreText.getText().trim());

        LocalDate fechaNacimiento = DatePicker.getDate();
        p.setFechaNacimiento(fechaNacimiento);

        p.setNumeroTelefono(telefonoText.getText().trim());
        return p;
    }

    public void setModel(Model model){
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    public void setController(Controller controller){
        this.controller = controller;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch(evt.getPropertyName()){
            case Model.LIST:
                updateTableWithList();
                break;
            case Model.FILTER:
                updateTableWithFiltered();
                break;
            case Model.CURRENT:
                updateCurrentFields();
                break;
        }
        this.panel.revalidate();
    }

    private void updateTableWithList() {
        int[] cols = {TableModel.ID, TableModel.NOMBRE, TableModel.NACIMIENTO, TableModel.TELEFONO};
        pacienteList.setModel(new TableModel(cols, model.getList()));
        pacienteList.setRowHeight(30);
        adjustColumnWidths();
    }

    private void updateTableWithFiltered() {
        int[] cols = {TableModel.ID, TableModel.NOMBRE, TableModel.NACIMIENTO, TableModel.TELEFONO};
        pacienteList.setModel(new TableModel(cols, model.getFiltered()));
        pacienteList.setRowHeight(30);
        adjustColumnWidths();
    }

    private void adjustColumnWidths() {
        if (pacienteList.getColumnModel().getColumnCount() > 0) {
            TableColumnModel columnModel = pacienteList.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(100);
            columnModel.getColumn(1).setPreferredWidth(200);
            columnModel.getColumn(2).setPreferredWidth(120);
            columnModel.getColumn(3).setPreferredWidth(120);
        }
    }

    private void updateCurrentFields() {
        if (model.getCurrent() != null) {
            idText.setText(model.getCurrent().getId() != null ? model.getCurrent().getId() : "");
            nombreText.setText(model.getCurrent().getNombre() != null ? model.getCurrent().getNombre() : "");

            if (model.getCurrent().getFechaNacimiento() != null) {
                DatePicker.setDate(model.getCurrent().getFechaNacimiento());
            } else {
                DatePicker.setDate(null);
            }

            telefonoText.setText(model.getCurrent().getNumeroTelefono() != null ? model.getCurrent().getNumeroTelefono() : "");

            clearValidationErrors();
        }
    }
}