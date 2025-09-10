package hospital.presentation.Login.Contrasena;

import hospital.Application;
import hospital.logic.Sesion;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class View {
    private JPanel panel;
    private JPasswordField claveActualText;
    private JPasswordField claveNuevaText;
    private JPasswordField confirmarText;
    private JButton aceptarButton;
    private JButton cancelarButton;
    private JLabel claveActualLabel;
    private JLabel claveNuevaLabel;
    private JLabel confirmarLabel;
    private JDialog dialog;

    private hospital.presentation.Login.Model model;
    private hospital.presentation.Login.Controller controller;
    private String usuarioId; // ID del usuario para cambiar contraseña

    public View() {
        setupEventHandlers();
        setupDialog();
    }

    private void setupDialog() {
        dialog = new JDialog(Application.window, "Cambiar Contraseña", true);
        dialog.setContentPane(panel);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(Application.window);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setResizable(false);
    }

    private void setupEventHandlers() {
        aceptarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cambiarClave();
            }
        });

        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        claveActualText.addActionListener(e -> claveNuevaText.requestFocus());
        claveNuevaText.addActionListener(e -> confirmarText.requestFocus());
        confirmarText.addActionListener(e -> cambiarClave());
    }

    private void cambiarClave() {
        try {
            String id = usuarioId;
            String claveActual = new String(claveActualText.getPassword());
            String claveNueva = new String(claveNuevaText.getPassword());
            String confirmar = new String(confirmarText.getPassword());

            if (id == null || id.isEmpty()) {
                throw new Exception("No se ha configurado el usuario para cambiar contraseña");
            }

            if (claveActual.isEmpty()) {
                throw new Exception("Debe ingresar la contraseña actual");
            }

            if (claveNueva.isEmpty()) {
                throw new Exception("Debe ingresar la nueva contraseña");
            }

            if (!claveNueva.equals(confirmar)) {
                throw new Exception("Las contraseñas nuevas no coinciden");
            }

            if (claveActual.equals(claveNueva)) {
                throw new Exception("La nueva contraseña debe ser diferente a la actual");
            }

            controller.cambiarClave(id, claveActual, claveNueva);

            JOptionPane.showMessageDialog(dialog,
                    "Contraseña cambiada exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialog,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            claveActualText.setText("");
            claveNuevaText.setText("");
            confirmarText.setText("");
            claveActualText.requestFocus();
        }
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public void setModel(hospital.presentation.Login.Model model) {
        this.model = model;
    }

    public void setController(hospital.presentation.Login.Controller controller) {
        this.controller = controller;
    }

    public void showDialog() {
        clear();
        dialog.setVisible(true);

        claveActualText.requestFocus();
    }

    private void clear() {
        claveActualText.setText("");
        claveNuevaText.setText("");
        confirmarText.setText("");
    }
}