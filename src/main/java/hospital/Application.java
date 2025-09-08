package hospital;

import hospital.logic.Sesion;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import java.awt.*;

public class Application {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ex) {}

        window = new JFrame();
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                hospital.logic.Service.instance().stop();
                System.exit(0);
            }
        });

        initializeControllers();

        window.setSize(1200, 600);
        window.setResizable(true);
        window.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        window.setTitle("HOSPITAL - Sistema de Prescripcion y Despacho");
        window.setLocationRelativeTo(null);

        doLogin();
    }

    private static void doLogin() {
        hospital.presentation.Login.Model loginModel = new hospital.presentation.Login.Model();
        hospital.presentation.Login.View loginView = new hospital.presentation.Login.View();
        hospital.presentation.Login.Controller loginController =
                new hospital.presentation.Login.Controller(loginView, loginModel);

        loginView.showDialog();
    }

    public static void doRun() {
        JTabbedPane tabbedPane = new JTabbedPane();
        window.setContentPane(tabbedPane);

        createMenuBar();

        String rol = Sesion.getUsuario() != null ? Sesion.getUsuario().getRol() : "ADM";

        window.setTitle("Recetas - " + Sesion.getUsuario().getId() + " (" + Sesion.getUsuario().getRol() + ")");

        switch (rol) {
            case "ADM":
                tabbedPane.addTab("Medicos", medicosIcon, medicoView.getPanel());
                tabbedPane.addTab("Farmaceutas", farmaceutasIcon, farmaceutaView.getPanel());
                tabbedPane.addTab("Pacientes", pacientesIcon, pacienteView.getPanel());
                tabbedPane.addTab("Medicamentos", medicamentosIcon, medicamentoView.getPanel());
                tabbedPane.addTab("Preescribir", preescribirIcon, preescribirView.getPanel());
                tabbedPane.addTab("Despachos", despachosIcon, despachoView.getPanel());
                tabbedPane.addTab("Dashboard", estadisticasIcon, dashboardView.getPanel());
                tabbedPane.addTab("Historico", historicoIcon, historicoView.getPanel());
                break;

            case "MED":
                tabbedPane.addTab("Preescribir", preescribirIcon, preescribirView.getPanel());
                tabbedPane.addTab("Dashboard", estadisticasIcon, dashboardView.getPanel());
                tabbedPane.addTab("Historico", historicoIcon, historicoView.getPanel());
                break;

            case "FAR":
                tabbedPane.addTab("Despachos", despachosIcon, despachoView.getPanel());
                tabbedPane.addTab("Dashboard", estadisticasIcon, dashboardView.getPanel());
                tabbedPane.addTab("Historico", historicoIcon, historicoView.getPanel());
                break;

            default:
                tabbedPane.addTab("Médicos", medicoView.getPanel());
                tabbedPane.addTab("Pacientes", pacienteView.getPanel());
                tabbedPane.addTab("Farmaceutas", farmaceutaView.getPanel());
                tabbedPane.addTab("Medicamentos", medicamentoView.getPanel());
                tabbedPane.addTab("Preescribir", preescribirView.getPanel());
                tabbedPane.addTab("Despachos", despachoView.getPanel());
                tabbedPane.addTab("Dashboard", dashboardView.getPanel());
                tabbedPane.addTab("Historico", historicoView.getPanel());
                break;
        }

        window.setVisible(true);
    }

    private static void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu usuarioMenu = new JMenu("Usuario");

        JMenuItem cambiarClaveItem = new JMenuItem("Cambiar Clave");
        cambiarClaveItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(window,
                    "Funcion de cambiar clave no implementada aun",
                    "En desarrollo",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        JMenuItem logoutItem = new JMenuItem("Cerrar Sesion");
        logoutItem.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(window,
                    "Esta seguro de cerrar sesion?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                Sesion.logout();
                window.dispose();

                window = new JFrame();
                window.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        hospital.logic.Service.instance().stop();
                        System.exit(0);
                    }
                });
                window.setSize(1200, 800);
                window.setResizable(true);
                window.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                window.setTitle("HOSPITAL - Sistema de Prescripcion y Despacho");
                window.setLocationRelativeTo(null);

                doLogin();
            }
        });

        usuarioMenu.add(cambiarClaveItem);
        usuarioMenu.addSeparator();
        usuarioMenu.add(logoutItem);

        JMenu ayudaMenu = new JMenu("Ayuda");

        JMenuItem acercaDeItem = new JMenuItem("Acerca de...");
        acercaDeItem.addActionListener(e -> {
            String rolDescripcion = getRolDescripcion(Sesion.getUsuario().getRol());
            JOptionPane.showMessageDialog(window,
                    "Sistema de Prescripcion y Despacho de Recetas\n" +
                            "Hospital Nacional\n" +
                            "Version 1.0\n\n" +
                            "Usuario: " + Sesion.getUsuario().getNombre() + "\n" +
                            "ID: " + Sesion.getUsuario().getId() + "\n" +
                            "Rol: " + rolDescripcion,
                    "Acerca de",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        ayudaMenu.add(acercaDeItem);

        menuBar.add(usuarioMenu);
        menuBar.add(ayudaMenu);

        window.setJMenuBar(menuBar);
    }

    private static String getRolDescripcion(String rol) {
        switch (rol) {
            case "ADM":
                return "Administrador";
            case "MED":
                return "Medico";
            case "FAR":
                return "Farmaceuta";
            case "PAC":
                return "Paciente";
            default:
                return rol;
        }
    }

    private static void initializeControllers() {
        hospital.presentation.Paciente.Model pacienteModel = new hospital.presentation.Paciente.Model();
        pacienteView = new hospital.presentation.Paciente.View();
        pacientesControllers = new hospital.presentation.Paciente.Controller(pacienteView, pacienteModel);

        hospital.presentation.Medico.Model medicoModel = new hospital.presentation.Medico.Model();
        medicoView = new hospital.presentation.Medico.View();
        medicoControllers = new hospital.presentation.Medico.Controller(medicoView, medicoModel);

        hospital.presentation.Farmaceuta.Model farmaceutaModel = new hospital.presentation.Farmaceuta.Model();
        farmaceutaView = new hospital.presentation.Farmaceuta.View();
        farmaceutaControllers = new hospital.presentation.Farmaceuta.Controller(farmaceutaView, farmaceutaModel);

        hospital.presentation.Medicamento.Model medicamentoModel = new hospital.presentation.Medicamento.Model();
        medicamentoView = new hospital.presentation.Medicamento.View();
        medicamentoController = new hospital.presentation.Medicamento.Controller(medicamentoView, medicamentoModel);

        hospital.presentation.Dashboard.Model dashboardModel = new hospital.presentation.Dashboard.Model();
        dashboardView = new hospital.presentation.Dashboard.View();
        dashboardController = new hospital.presentation.Dashboard.Controller(dashboardView, dashboardModel);

        hospital.presentation.Historico.Model historicoModel = new hospital.presentation.Historico.Model();
        historicoView = new hospital.presentation.Historico.View();
        historicoController = new hospital.presentation.Historico.Controller(historicoView, historicoModel);

        hospital.presentation.Preescribir.Model preescribirModel = new hospital.presentation.Preescribir.Model();
        preescribirView = new hospital.presentation.Preescribir.View();
        preescribirController = new hospital.presentation.Preescribir.Controller(preescribirView, preescribirModel);

        hospital.presentation.Despacho.Model despachoModel = new hospital.presentation.Despacho.Model();
        despachoView = new hospital.presentation.Despacho.View();
        despachoController = new hospital.presentation.Despacho.Controller(despachoView, despachoModel);
    }

    public static hospital.presentation.Paciente.Controller pacientesControllers;
    public static hospital.presentation.Medico.Controller medicoControllers;
    public static hospital.presentation.Farmaceuta.Controller farmaceutaControllers;
    public static hospital.presentation.Medicamento.Controller medicamentoController;
    public static hospital.presentation.Dashboard.Controller dashboardController;
    public static hospital.presentation.Historico.Controller historicoController;
    public static hospital.presentation.Preescribir.Controller preescribirController;
    public static hospital.presentation.Despacho.Controller despachoController;

    private static hospital.presentation.Paciente.View pacienteView;
    private static hospital.presentation.Medico.View medicoView;
    private static hospital.presentation.Farmaceuta.View farmaceutaView;
    private static hospital.presentation.Medicamento.View medicamentoView;
    private static hospital.presentation.Dashboard.View dashboardView;
    private static hospital.presentation.Historico.View historicoView;
    private static hospital.presentation.Preescribir.View preescribirView;
    private static hospital.presentation.Despacho.View despachoView;

    private static ImageIcon medicosIcon = null;
    private static ImageIcon farmaceutasIcon = null;
    private static ImageIcon pacientesIcon = null;
    private static ImageIcon medicamentosIcon = null;
    private static ImageIcon estadisticasIcon = null;
    private static ImageIcon historicoIcon = null;
    private static ImageIcon preescribirIcon = null;
    private static ImageIcon despachosIcon = null;

    public static JFrame window;
    public final static int MODE_CREATE = 1;
    public final static int MODE_EDIT = 2;

    public static final Color BACKGROUND_ERROR = new Color(255, 102, 102);
}