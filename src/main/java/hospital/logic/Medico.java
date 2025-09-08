package hospital.logic;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "medico")
@XmlAccessorType(XmlAccessType.FIELD)
public class Medico extends Usuario {
    private String clave;
    private String especialidad;

    public Medico() {
        this("", "", "", "", "MED");
    }

    public Medico(String id, String nombre, String clave, String especialidad) {
        super(id, nombre, "MED");
        this.clave = clave;
        this.especialidad = especialidad;
    }

    public Medico(String id, String nombre, String clave, String especialidad, String rol) {
        super(id, nombre, rol);
        this.clave = clave;
        this.especialidad = especialidad;
    }

    public String getClave() {
        return clave;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
}