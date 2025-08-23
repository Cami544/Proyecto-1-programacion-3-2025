package hospital.logic;

public class Farmaceuta extends Usuario {
    private String clave;

    public Farmaceuta() {this("","","");}

    public Farmaceuta(String id, String nombre, String clave) {
        this.id = id;
        this.nombre = nombre;
        this.clave = clave;
    }

    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }

}
