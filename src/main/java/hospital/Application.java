package hospital;

import hospital.data.GestorDatosMedicamentos;
import hospital.logic.Medicamento;
import hospital.presentation.Medico.Controller;
import hospital.presentation.Medico.Model;
import hospital.presentation.Medico.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import java.awt.*;

public class Application {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");}
        catch (Exception ex) {};

        View view = new View();
        Model model = new Model();
        Controller controller = new Controller(view, model);

        List<Medicamento> lista = new ArrayList<>();
        lista.add(new Medicamento("001", "Paracetamol", "Tableta"));
        lista.add(new Medicamento("002", "Ibuprofeno", "Cápsula"));


        GestorDatosMedicamentos gestor = new GestorDatosMedicamentos();
        gestor.guardar(lista);
        System.out.println("Medicamentos guardados en medicamentos.xml");
        System.out.println("sdf");
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
        //window.setContentPane(view.getPanel());
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

    public static final Color BACKGROUND_ERROR = new Color(255, 102, 102);
}
