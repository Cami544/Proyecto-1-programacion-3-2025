package hospital.data;

import hospital.logic.Medicamento;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;


@XmlRootElement(name="listaMedicamentos")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListaMedicamentos {

    @XmlElement(name="medicamento")
    private List<Medicamento> medicamentos;

    public ListaMedicamentos() {}
    public ListaMedicamentos(List<Medicamento> medicamentos) {
        this.medicamentos = medicamentos;
    }

    public List<Medicamento> getMedicamentos() {
        return medicamentos;
    }
}