package hospital.presentation.Medico;

import hospital.Application;
import hospital.logic.Medico;
import hospital.presentation.Highlighter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class View implements PropertyChangeListener {

    private JLabel idLabel;
    private JLabel nombreLabel;
    private JTable table1;
    private JLabel listadoLabel;
    private JLabel busquedaLabel;
    private JLabel especialidadLabel;
    private JTextField textField1;
    private JTextField textField2;
    private JButton guardarButton;
    private JPanel panel;

    public View(){

        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validate()) {
                    Medico n = take();
                    try {
                        controller.create(n);
                        JOptionPane.showMessageDialog(panel, "REGISTRO APLICADO", "", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }

                }
            }
        });

        Highlighter highlighter = new Highlighter(Color.green);
        idLabel.addMouseListener(highlighter);
        nombreLabel.addMouseListener(highlighter);
        busquedaLabel.addMouseListener(highlighter);
        especialidadLabel.addMouseListener(highlighter);
        listadoLabel.addMouseListener(highlighter);
        guardarButton.addMouseListener(highlighter);


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



    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case Model.LIST:
                int[] cols = {TableModel.ID, TableModel.NOMBRE, TableModel.ESPECIALIDAD};
                table1.setModel(new TableModel(cols, model.getList()));
                break;
            case Model.CURRENT:
                idLabel.setText(model.getCurrent().getId());
                nombreLabel.setText(model.getCurrent().getNombre());
                especialidadLabel.setText(model.getCurrent().getEspecialidad());

                idLabel.setBackground(null);
                idLabel.setToolTipText(null);
                nombreLabel.setBackground(null);
                nombreLabel.setToolTipText(null);
                especialidadLabel.setBackground(null);
                especialidadLabel.setToolTipText(null);
                break;
        }
        this.panel.revalidate();
    }

    public Medico take() {
        Medico e = new Medico();
        e.setId(idLabel.getText());
        e.setNombre(nombreLabel.getText());
        e.setEspecialidad(especialidadLabel.getText());

        return e;
    }

    Controller controller;
    Model model;
    private boolean validate() {
        boolean valid = true;
        if (idLabel.getText().isEmpty()) {
            valid = false;
            idLabel.setBackground(Application.BACKGROUND_ERROR);
            idLabel.setToolTipText("id requerido");
        } else {
            idLabel.setBackground(null);
            idLabel.setToolTipText(null);
        }

        if (nombreLabel.getText().isEmpty()) {
            valid = false;
            nombreLabel.setBackground(Application.BACKGROUND_ERROR);
            nombreLabel.setToolTipText("Nombre requerido");
        } else {
            nombreLabel.setBackground(null);
            nombreLabel.setToolTipText(null);
        }
        return valid;
    }


}
