package hospital.logic;
import java.util.Objects;

public class Medico extends Usuario {
    private String clave;
    private String especialidad;

    public Medico() {this("","","","");}

    public Medico(String id, String clave, String nombre, String especialidad) {
        this.id = id;
        this.clave = clave;
        this.nombre = nombre;
        this.especialidad = especialidad;
    }
    public String getClave() { return clave; }
    public String getEspecialidad() { return especialidad; }


    public void setClave(String clave) { this.clave = clave; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

}
