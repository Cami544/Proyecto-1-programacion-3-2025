package hospital.presentation.Paciente;

import hospital.Application;
import hospital.logic.Medico;
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
    private JTextField fechNacimientoTtext;
    private JPanel Busqueda;

    public JPanel getPanel() { return panel; }

    public View() {

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
                /*int row=pacienteList.getSelectedRow(); es por la nueva structura del xml
                controller.edit(row);
                super.mouseClicked(e);

                 */
            }
        });
        generarPdfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.generarReporte();
                    JOptionPane.showMessageDialog(panel, "Reporte PDF generado con éxito.", "Información", JOptionPane.INFORMATION_MESSAGE);
                }
                catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "Error al generar el reporte: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
                        "¿Está seguro de eliminar a este paciente " + model.getCurrent().getNombre() + "?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        controller.delete();
                        JOptionPane.showMessageDialog(panel,
                                "Paciente eliminado exitosamente",
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
        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            if(validate()){
                Paciente paciente = take();
                try{
                    controller.save(paciente);
                    JOptionPane.showMessageDialog(panel,
                            "Paciente guardado exitosamente",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                    clear();
                }catch (Exception ex){
                    JOptionPane.showMessageDialog(panel,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
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
    private boolean validate(){
        boolean valid = true;
        clearValidationErrors();

        if (idText.getText().trim().isEmpty()) {
            setFieldError(idText, "El ID no puede estar vacío");
            valid = false;
        }
        if (nombreText.getText().trim().isEmpty()) {
            setFieldError(nombreText, "El nombre no puede estar vacío");
            valid = false;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate.parse(fechNacimientoTtext.getText().trim(), formatter);
        } catch (Exception e) {
            setFieldError(fechNacimientoTtext, "Formato de fecha inválido (dd/MM/yyyy)");
            valid = false;
        }
        if (telefonoText.getText().trim().isEmpty()) {
            setFieldError(telefonoText, "El teléfono no puede estar vacío");
            valid = false;
        }

        return valid;
    }

    private void setFieldError(JTextField field, String message) {
        field.setBackground(Color.PINK);
        field.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        field.setToolTipText(message);
    }

    private void clearValidationErrors() {
        JTextField[] fields = {idText, nombreText, fechNacimientoTtext, telefonoText};
        for (JTextField field : fields) {
            field.setBackground(Color.WHITE);
            field.setBorder(UIManager.getBorder("TextField.border"));
            field.setToolTipText(null);
        }
    }

public void clear(){
        idText.setText("");
        nombreText.setText("");
        fechNacimientoTtext.setText("");
        telefonoText.setText("");
}

    public Paciente take(){
        Paciente p = new Paciente();
        p.setId(idText.getText());
        p.setNombre(nombreText.getText());
        //fecha de nacimiento,con restricción
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        p.setFechaNacimiento(LocalDate.parse(fechNacimientoTtext.getText(), formatter));
        p.setNumeroTelefono( telefonoText.getText());
        return p;
    }

    //MVC
    Model model;
    Controller controller;

    public void setModel(Model model){
        this.model = model;
        model.addPropertyChangeListener(this);
    }
    public void setController(Controller controller){ this.controller = controller; }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch(evt.getPropertyName()){
            case Model.LIST:
                int[] cols= {TableModel.ID, TableModel.NOMBRE, TableModel.NACIMIENTO, TableModel.TELEFONO};
                pacienteList.setModel(new TableModel(cols,model.getList()));
                pacienteList.setRowHeight(30);
                TableColumnModel columnModel = pacienteList.getColumnModel();
                columnModel.getColumn(0).setPreferredWidth(150);
                columnModel.getColumn(1).setPreferredWidth(150);
                columnModel.getColumn(2).setPreferredWidth(150);
                columnModel.getColumn(3).setPreferredWidth(150);
                break;
            case Model.CURRENT:
                if (model.getCurrent() != null) {
                    idText.setText(model.getCurrent().getId());
                    nombreText.setText(model.getCurrent().getNombre());

                    //Ver que se hace con la fecha de nacimiento
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    if (model.getCurrent().getFechaNacimiento() != null) {
                        fechNacimientoTtext.setText(model.getCurrent().getFechaNacimiento().format(formatter));
                    } else {
                        fechNacimientoTtext.setText("");
                    }

                    telefonoText.setText(model.getCurrent().getNumeroTelefono());
//revisar
                    if (model.getModel() == Application.MODE_EDIT) {
                        idText.setEnabled(false);
                        eliminarButton.setEnabled(true);
                    } else {
                        idText.setEnabled(true);
                        eliminarButton.setEnabled(true);
                    }
                    idLabel.setBorder(null);
                    idLabel.setToolTipText(null);
                    nombreLabel.setBorder(null);
                    nombreLabel.setToolTipText(null);
                }
                    break;
            case Model.FILTER:
                //revisar
               //controller.filter(String.valueOf(buscarNomText));
                //buscarNomText.setText(model.getFiltered());
               break;
                }
                    this.panel.revalidate();
        }

    }

