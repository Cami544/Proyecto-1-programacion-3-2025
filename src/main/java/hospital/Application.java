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

        window.setSize(900,450);
        window.setResizable(false);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setTitle("HOSPITAL");
        window.setVisible(true);

        List<Paciente> list= new ArrayList<>();

        GestorDatosPaciente gestorDatosPaciente = new GestorDatosPaciente();
        list= gestorDatosPaciente.cargar();
        for(Paciente m: list){
            System.out.println(m.toString());
        }

        /*
        View view = new View();
        Model model = new Model();
        Controller controller = new Controller(view, model);

        //model.addPropertyChangeListener(view);

        List<Medicamento> lista = new ArrayList<>();
        lista.add(new Medicamento("001", "Paracetamol", "Tableta"));
        lista.add(new Medicamento("002", "Ibuprofeno", "Cápsula"));


        GestorDatosMedicamentos gestor = new GestorDatosMedicamentos();
        gestor.guardar(lista);
        System.out.println("Medicamentos guardados en medicamentos.xml");
        List<Medicamento> cargados = gestor.cargar();
        System.out.println("Medicamentos cargados:");
        for (Medicamento m : cargados) {
            System.out.println("Código: " + m.getCodigo() +
                    ", Nombre: " + m.getNombre() +
                    ", Presentación: " + m.getPresentacion());
        }

        JFrame window = new JFrame();
        window.setSize(800, 600);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("Hospital - Gestion de Medicos");
        window.setContentPane(view.getPanel());
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        try {
            model.setList(hospital.logic.Service.instance().findAllMedicos());
            model.setFiltered(hospital.logic.Service.instance().findAllMedicos());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(window,
                    "Error al cargar datos: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
*/

    }
    //Controller de todas las clases
    public static hospital.presentation.Paciente.Controller pacientesControllers;


    public static JFrame window;
    public final static int MODE_CREATE=1;
    public final static int MODE_EDIT=2;

    public static final Color BACKGROUND_ERROR = new Color(255, 102, 102);
}
