package hospital.data;

import hospital.logic.Medico;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GestorDatosMedicos {
    private File archivo = new File("medicos.xml");

    public void guardar(List<Medico> lista) {
        try {
            JAXBContext ctx = JAXBContext.newInstance(ListaMedicos.class);
            Marshaller m = ctx.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.marshal(new ListaMedicos(lista), archivo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Medico> cargar() {
        if (!archivo.exists()) return new ArrayList<>();
        try {
            JAXBContext ctx = JAXBContext.newInstance(ListaMedicos.class);
            Unmarshaller um = ctx.createUnmarshaller();
            return ((ListaMedicos) um.unmarshal(archivo)).getMedicos();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}