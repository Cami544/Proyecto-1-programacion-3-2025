package hospital.presentation.Medico;

import hospital.Application;
import hospital.logic.Medico;
import hospital.presentation.Highlighter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class View  {

    private JLabel idLabel;
    private JLabel nombreLabel;
    private JTable table1;
    private JLabel listadoLabel;
    private JLabel busquedaLabel;
    private JLabel especialidadLabel;
    private JTextField textField1;
    private JTextField textField2;
    private JButton guardarButton;

    public View(){

//        guardarButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if (validate()) {
//                    Medico n = take();
//                    try {
//                        controller.create(n);
//                        JOptionPane.showMessageDialog(panel, "REGISTRO APLICADO", "", JOptionPane.INFORMATION_MESSAGE);
//                    } catch (Exception ex) {
//                        JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//                    }
//
//                }
//            }
//        });

        Highlighter highlighter = new Highlighter(Color.green);










    }


    public Medico take() {
        Medico e = new Medico();
        e.setId(idLabel.getText());
        e.setNombre(nombreLabel.getText());
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
