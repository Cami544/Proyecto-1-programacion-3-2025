package hospital;

import hospital.data.GestorDatosMedicamentos;
import hospital.data.GestorDatosPaciente;
import hospital.logic.Medicamento;
import hospital.logic.Paciente;
import hospital.logic.Service;
import hospital.presentation.Medico.Controller;
import hospital.presentation.Medico.Model;
import hospital.presentation.Medico.View;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

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
                super.windowClosing(e);
             //   Service.instance().stop();
            }
        });


        hospital.presentation.Paciente.Model pacienteModel = new hospital.presentation.Paciente.Model();
        hospital.presentation.Paciente.View pacienteView = new hospital.presentation.Paciente.View();
        pacientesControllers = new hospital.presentation.Paciente.Controller(pacienteView, pacienteModel);
        //Aqui luego se pone el icono


       tabbedPane.addTab("Pacientes",pacienteView.getPanel());


       Model medicoModel = new Model();
       View medicoView = new View();
       Controller medicoController = new Controller(medicoView, medicoModel);

       try{
           medicoModel.setList(Service.instance().findAllMedicos());
           medicoModel.setFiltered(Service.instance().findAllMedicos());
       }catch(Exception e){
           System.err.println("Error en medicos:"+e.getMessage());
       }
       tabbedPane.addTab("Medicos",medicoView.getPanel());

        window.setSize(800,600);
        window.setResizable(false);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setTitle("HOSPITAL");
        window.setVisible(true);

    }

    //Controller de todas las clases
    public static hospital.presentation.Paciente.Controller pacientesControllers;


    public static JFrame window;
    public final static int MODE_CREATE=1;
    public final static int MODE_EDIT=2;

    public static final Color BACKGROUND_ERROR = new Color(255, 102, 102);
}
