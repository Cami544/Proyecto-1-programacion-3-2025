package hospital.logic;
import java.util.Objects;

public class Medico {
    String id;
    String clave;
    String nombre;
    String especialidad;

    public Medico() {this("","","","");}

    public Medico(String id, String clave, String nombre, String especialidad) {
        this.id = id;
        this.clave = clave;
        this.nombre = nombre;
        this.especialidad = especialidad;
    }
    public String getId() { return id; }
    public String getClave() { return clave; }
    public String getNombre() { return nombre; }
    public String getEspecialidad() { return especialidad; }


    public void setId(String id) { this.id = id; }
    public void setClave(String clave) { this.clave = clave; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

}
