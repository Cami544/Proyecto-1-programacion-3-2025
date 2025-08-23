package hospital.logic;

public class Paciente extends Usuario {
    private String fechaNacimiento;
    private String numeroTelefono;

    public Paciente() {this("","","","");}

    public Paciente(String id, String nombre, String fechaNacimiento, String numeroTelefono) {
        this.id = id;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.numeroTelefono = numeroTelefono;
    }

    public String getFechaNacimiento() {return fechaNacimiento;}
    public void setFechaNacimiento(String fechaNacimiento) {this.fechaNacimiento = fechaNacimiento;}
    public String getNumeroTelefono() {return numeroTelefono;}
    public void setNumeroTelefono(String numeroTelefono) {this.numeroTelefono = numeroTelefono;}


}
