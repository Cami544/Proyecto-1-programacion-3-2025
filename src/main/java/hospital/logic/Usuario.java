package hospital.logic;

public abstract class Usuario {
    protected String id;
    protected String nombre;
    protected String rol;

    public Usuario() {}

    public Usuario(String id, String nombre, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.rol =rol;
    }
    public String getId() {return id;}
    public void setId(String id) {this.id = id;}
    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public String getRol() {return rol;}
    public void setRol(String rol) {this.rol = rol;}


}
