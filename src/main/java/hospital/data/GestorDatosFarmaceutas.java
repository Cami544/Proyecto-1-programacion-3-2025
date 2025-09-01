package hospital.data;

import hospital.logic.Farmaceuta;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GestorDatosFarmaceutas {
    private File archivo = new File("farmaceutas.xml");

    public void guardar(List<Farmaceuta> lista) {
        try {
            JAXBContext ctx = JAXBContext.newInstance(ListaFarmaceutas.class);
            Marshaller m = ctx.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.marshal(new ListaFarmaceutas(lista), archivo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Farmaceuta> cargar() {
        if (!archivo.exists()) return new ArrayList<>();
        try {
            JAXBContext ctx = JAXBContext.newInstance(ListaFarmaceutas.class);
            Unmarshaller um = ctx.createUnmarshaller();
            return ((ListaFarmaceutas) um.unmarshal(archivo)).getFarmaceutas();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}