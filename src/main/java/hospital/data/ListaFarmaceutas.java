package hospital.data;

import hospital.logic.Farmaceuta;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "farmaceutas")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListaFarmaceutas {
    @XmlElement(name = "farmaceuta")
    private List<Farmaceuta> farmaceutas;

    public ListaFarmaceutas() {
        this.farmaceutas = new ArrayList<>();
    }

    public ListaFarmaceutas(List<Farmaceuta> farmaceutas) {
        this.farmaceutas = farmaceutas;
    }

    public List<Farmaceuta> getFarmaceutas() {
        return farmaceutas;
    }

    public void setFarmaceutas(List<Farmaceuta> farmaceutas) {
        this.farmaceutas = farmaceutas;
    }
}