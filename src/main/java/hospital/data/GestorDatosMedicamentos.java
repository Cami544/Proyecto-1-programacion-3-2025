package hospital.data;

import hospital.logic.Medicamento;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GestorDatosMedicamentos {
    private File archivo = new File("medicamentos.xml");

    public void guardar(List<Medicamento> lista) {
        try {
            JAXBContext ctx = JAXBContext.newInstance(ListaMedicamentos.class);
            Marshaller m = ctx.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.marshal(new ListaMedicamentos(lista), archivo);
        } catch (Exception e) { e.printStackTrace(); }
    }

    public List<Medicamento> cargar() {
        if (!archivo.exists()) return new ArrayList<>();
        try {
            JAXBContext ctx = JAXBContext.newInstance(ListaMedicamentos.class);
            Unmarshaller um = ctx.createUnmarshaller();
            return ((ListaMedicamentos) um.unmarshal(archivo)).getMedicamentos();
        } catch (Exception e) { return new ArrayList<>(); }
    }

}
