package hospital.presentation.Login;

import hospital.Application;
import hospital.logic.Sesion;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class View implements PropertyChangeListener {
    private JPanel panel;
    private JTextField idText;
    private JPasswordField claveText;
    private JButton loginButton;
    private JButton cancelarButton;
    private JButton cambiarClaveButton;
    private JLabel IDLabel;
    private JLabel CLAVELabel;
    private JDialog dialog;

    private Model model;
    private Controller controller;

    public View() {
        setupEventHandlers();
        setupDialog();
    }

    private void setupDialog() {
        dialog = new JDialog(Application.window, "Login", true);
        dialog.setContentPane(panel);
        dialog.setSize(435, 220);
        dialog.setLocationRelativeTo(Application.window);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setResizable(false);

        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                System.exit(0);
            }
        });
    }

    private void setupEventHandlers() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doLogin();
            }
        });

        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        cambiarClaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarCambiarClave();
            }
        });

        idText.addActionListener(e -> doLogin());
        claveText.addActionListener(e -> doLogin());
    }


    private void doLogin() {
        try {
            String id = idText.getText().trim();
            String clave = new String(claveText.getPassword());

            controller.login(id, clave);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialog,
                    ex.getMessage(),
                    "Error de Login",
                    JOptionPane.ERROR_MESSAGE);
            claveText.setText("");
            idText.requestFocus();
        }
    }

    private void mostrarCambiarClave() {
        String idUsuario = idText.getText().trim();

        if (idUsuario.isEmpty()) {
            JOptionPane.showMessageDialog(dialog,
                    "Debe ingresar su ID de usuario primero",
                    "ID Requerido",
                    JOptionPane.WARNING_MESSAGE);
            idText.requestFocus();
            return;
        }

        hospital.presentation.Login.Contrasena.View contrasenaView =
                new hospital.presentation.Login.Contrasena.View();

        contrasenaView.setModel(model);
        contrasenaView.setController(controller);
        contrasenaView.setUsuarioId(idUsuario);

        dialog.setVisible(false);

        contrasenaView.showDialog();

        dialog.setVisible(true);
    }

    public void setModel(Model model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void showDialog() {
        clear();
        dialog.setVisible(true);
    }

    private void clear() {
        idText.setText("");
        claveText.setText("");
        idText.requestFocus();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case Model.CURRENT:
                if (model.getCurrent() != null) {
                    dialog.dispose();
                    Application.doRun();
                }
                break;
        }
    }
}