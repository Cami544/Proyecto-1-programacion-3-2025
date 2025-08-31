package hospital.presentation.Paciente;

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
    private JTextField fechNacimientoTtext;

    public JPanel getPanel() { return panel; }

    public View() {

        pacienteList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row=pacienteList.getSelectedRow();
                controller.edit(row);
                super.mouseClicked(e);
            }
        });
        generarPdfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            if(validate()){
                Paciente paciente = take();
                try{
                    controller.save(paciente);
                }catch (Exception ex){
                    JOptionPane.showMessageDialog(panel,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                }
            }
            }
        });
        filtrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    private boolean validate(){
        boolean valid = true;
// Resetear labels
        idLabel.setBorder(null);
        nombreLabel.setBorder(null);
        fechaNacimiLabel.setBorder(null);
        telefonoLabel.setBorder(null);

        // Validar ID
        if (idText.getText().trim().isEmpty()) {
            idText.setBorder(BorderFactory.createLineBorder(Color.RED));
            idText.setText("El ID no puede estar vacío");
            //  idLabel.setToolTipText("El ID no puede estar vacío");
            valid = false;
        }

        // Validar Nombre
        if (nombreText.getText().trim().isEmpty()) {
            nombreLabel.setBorder(BorderFactory.createLineBorder(Color.RED));
            nombreLabel.setToolTipText("El nombre no puede estar vacío");
            valid = false;
        }

        // Validar Fecha
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate.parse(fechNacimientoTtext.getText().trim(), formatter);
        } catch (Exception e) {
            fechaNacimiLabel.setBorder(BorderFactory.createLineBorder(Color.RED));
            fechaNacimiLabel.setToolTipText("Formato de fecha inválido (dd/MM/yyyy)");
            valid = false;
        }

        // Validar Teléfono
        if (telefonoText.getText().trim().isEmpty()) {
            telefonoLabel.setBorder(BorderFactory.createLineBorder(Color.RED));
            telefonoLabel.setToolTipText("El teléfono no puede estar vacío");
            valid = false;
        }

        return valid;
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

                    if (model.getModel() == Application.MODE_EDIT) {
                        idText.setEnabled(false);
                        nombreText.setEnabled(true);
                    } else {
                        idText.setEnabled(true);
                        nombreText.setEnabled(true);
                    }
                    idLabel.setBorder(null);
                    idLabel.setToolTipText(null);
                    nombreLabel.setBorder(null);
                    nombreLabel.setToolTipText(null);
                }
                    break;
            case Model.FILTER:
                //revisar
                  //  buscarNomText.setText(model.getFiltro().getNombre());
               break;
                }
                    this.panel.revalidate();
        }

    }

