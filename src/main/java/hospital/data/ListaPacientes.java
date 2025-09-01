package hospital.data;

import hospital.logic.Medico;
import hospital.logic.Paciente;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "pacientes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListaPacientes {
    @XmlElement(name = "paciente")
    private List<Paciente> pacientes;

    public ListaPacientes() {
        this.pacientes = new ArrayList<>();
    }

    public ListaPacientes(List<Paciente> pacientes) {
        this.pacientes= pacientes;
    }

    public List<Paciente> getPacicentes() {
        return pacientes;
    }

    public void setPacientes(List<Paciente> pacientes) {
        this.pacientes = pacientes;
    }
}
