package hospital;

import hospital.data.GestorDatosMedicamentos;
import hospital.logic.Medicamento;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Application {
    public static void main(String[] args) throws IOException {

        List<Medicamento> lista = new ArrayList<>();
        lista.add(new Medicamento("001", "Paracetamol", "Tableta"));
        lista.add(new Medicamento("002", "Ibuprofeno", "Cápsula"));
//

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
    }
}
