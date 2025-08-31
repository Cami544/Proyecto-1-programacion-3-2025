package hospital.data;

import hospital.logic.Medico;
import hospital.logic.Paciente;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GestorDatosPaciente {
    private File archivo = new File("medicos.xml");

    public void guardar(List<Paciente> lista) {
        try {
            JAXBContext ctx = JAXBContext.newInstance(ListaMedicos.class);
            Marshaller m = ctx.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.marshal(new ListaPacientes(lista), archivo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Paciente> cargar() {
        if (!archivo.exists()) return new ArrayList<>();
        try {
            JAXBContext ctx = JAXBContext.newInstance(ListaMedicos.class);
            Unmarshaller um = ctx.createUnmarshaller();
            return ((ListaPacientes) um.unmarshal(archivo)).getPacicentes();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
