package hospital;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import java.awt.*;

public class Application {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ex) {};

        window = new JFrame();
        JTabbedPane tabbedPane = new JTabbedPane();
        window.setContentPane(tabbedPane);

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //super.windowClosing(e);
                hospital.logic.Service.instance().stop();
                System.exit(0);
            }
        });

        hospital.presentation.Paciente.Model pacienteModel = new hospital.presentation.Paciente.Model();
        hospital.presentation.Paciente.View pacienteView = new hospital.presentation.Paciente.View();
        pacientesControllers = new hospital.presentation.Paciente.Controller(pacienteView, pacienteModel);

        hospital.presentation.Medico.Model medicoModel = new hospital.presentation.Medico.Model();
        hospital.presentation.Medico.View medicoView = new hospital.presentation.Medico.View();
        medicoControllers = new hospital.presentation.Medico.Controller(medicoView, medicoModel);

        hospital.presentation.Farmaceuta.Model farmaceutaModel = new hospital.presentation.Farmaceuta.Model();
        hospital.presentation.Farmaceuta.View farmaceutaView = new hospital.presentation.Farmaceuta.View();
        farmaceutaControllers = new hospital.presentation.Farmaceuta.Controller(farmaceutaView, farmaceutaModel);

        hospital.presentation.Medicamento.Model medicamentoModel = new hospital.presentation.Medicamento.Model();
        hospital.presentation.Medicamento.View medicamentoView = new hospital.presentation.Medicamento.View();
        medicamentoController = new hospital.presentation.Medicamento.Controller(medicamentoView, medicamentoModel);

        hospital.presentation.Dashboard.Model dashboardModel = new hospital.presentation.Dashboard.Model();
        hospital.presentation.Dashboard.View dashboardView = new hospital.presentation.Dashboard.View();
        dashboardController = new hospital.presentation.Dashboard.Controller(dashboardView, dashboardModel);

        hospital.presentation.Historico.Model historicoModel = new hospital.presentation.Historico.Model();
        hospital.presentation.Historico.View historicoView = new hospital.presentation.Historico.View();
        historicoController = new hospital.presentation.Historico.Controller(historicoView, historicoModel);

        hospital.presentation.Preescribir.Model preescribirModel = new hospital.presentation.Preescribir.Model();
        hospital.presentation.Preescribir.View preescribirView = new hospital.presentation.Preescribir.View();
        preescribirController = new hospital.presentation.Preescribir.Controller(preescribirView, preescribirModel);

        hospital.presentation.Despacho.Model despachoModel = new hospital.presentation.Despacho.Model();
        hospital.presentation.Despacho.View despachoView = new hospital.presentation.Despacho.View();
        despachoController = new  hospital.presentation.Despacho.Controller(despachoView, despachoModel);

        tabbedPane.addTab("Medicos", medicoView.getPanel());
        tabbedPane.addTab("Pacientes", pacienteView.getPanel());
        tabbedPane.addTab("Farmaceutas", farmaceutaView.getPanel());
        tabbedPane.addTab("Medicamentos", medicamentoView.getPanel());
        tabbedPane.addTab("Preescribir", preescribirView.getPanel());
        tabbedPane.addTab("Despachos", despachoView.getPanel());
        tabbedPane.addTab("Dashboard", dashboardView.getPanel());
        tabbedPane.addTab("Historico", historicoView.getPanel());

        window.setSize(1000, 600);
        window.setResizable(true);
        window.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        window.setTitle("HOSPITAL - Sistema de Prescripción y Despacho");
        window.setLocationRelativeTo(null); //para centrar
        window.setVisible(true);
    }

    public static hospital.presentation.Paciente.Controller pacientesControllers;
    public static hospital.presentation.Medico.Controller medicoControllers;
    public static hospital.presentation.Farmaceuta.Controller farmaceutaControllers;
    public static hospital.presentation.Medicamento.Controller medicamentoController;
    public static hospital.presentation.Dashboard.Controller dashboardController;
    public static hospital.presentation.Historico.Controller historicoController;
    public static hospital.presentation.Preescribir.Controller preescribirController;
    public static  hospital.presentation.Despacho.Controller despachoController;

    public static JFrame window;
    public final static int MODE_CREATE = 1;
    public final static int MODE_EDIT = 2;

    public static final Color BACKGROUND_ERROR = new Color(255, 102, 102);
}