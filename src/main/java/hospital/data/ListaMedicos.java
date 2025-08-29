package hospital.data;

import hospital.logic.Medico;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "medicos")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListaMedicos {
    @XmlElement(name = "medico")
    private List<Medico> medicos;

    public ListaMedicos() {
        this.medicos = new ArrayList<>();
    }

    public ListaMedicos(List<Medico> medicos) {
        this.medicos = medicos;
    }

    public List<Medico> getMedicos() {
        return medicos;
    }

    public void setMedicos(List<Medico> medicos) {
        this.medicos = medicos;
    }
}