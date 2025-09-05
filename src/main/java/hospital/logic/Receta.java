package hospital.logic;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="receta")
// nombre del objeto deltro del archivo
@XmlAccessorType(XmlAccessType.FIELD)
public class Receta {
    private String id;
    private String pacienteId; //revisar si es mejor tipo Paciente
    @XmlJavaTypeAdapter(LocalDateAdapter.class) //para que lo type de manera correcta
    private LocalDate fecha;
    //Revisar anteriores
    private List<DetalleReceta> detalles = new ArrayList<>();

    public Receta() {}
    public Receta(String id, String pacienteId, LocalDate fecha) {
        this.id = id;
        this.pacienteId = pacienteId;
        this.fecha = fecha;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(String pacienteId) {
        this.pacienteId = pacienteId;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public List<DetalleReceta> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleReceta> detalles) {
        this.detalles = detalles;
    }

}