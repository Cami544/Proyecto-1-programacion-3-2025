package hospital.logic;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.time.LocalDate;


@XmlRootElement(name="paciente")
@XmlAccessorType(XmlAccessType.FIELD)
public class Paciente extends Usuario {

  @XmlJavaTypeAdapter(LocalDateAdapter.class) //Realmente es necesario?, pienso que seria mejor solo en DetalleReceta
    private LocalDate fechaNacimiento;
    private String numeroTelefono;

    public Paciente(){}

    public Paciente(String id, String nombre, LocalDate fechaNacimiento, String numeroTelefono) {
        this.id = id;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.numeroTelefono = numeroTelefono;
    }

    public LocalDate getFechaNacimiento() {return fechaNacimiento;}
    public void setFechaNacimiento(LocalDate fechaNacimiento) {this.fechaNacimiento = fechaNacimiento;}
    public String getNumeroTelefono() {return numeroTelefono;}
    public void setNumeroTelefono(String numeroTelefono) {this.numeroTelefono = numeroTelefono;}


}
