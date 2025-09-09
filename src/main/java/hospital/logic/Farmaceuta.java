package hospital.logic;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "farmaceuta")
@XmlAccessorType(XmlAccessType.FIELD)
public class Farmaceuta extends Usuario {
    private String clave;

    public Farmaceuta() {
        this("", "", "", "FAR");
    }

    public Farmaceuta(String id, String nombre, String clave) {
        super(id, nombre, "FAR");
        this.clave = clave;
    }

    public Farmaceuta(String id, String nombre, String clave, String rol) {
        super(id, nombre, rol);
        this.clave = clave;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String toString() {
        return id + "-" + nombre;
    }

}