package hospital.logic;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="detalleReceta")
@XmlAccessorType(XmlAccessType.FIELD)
public class DetalleReceta {
    private String medicamentoCodigo;
    private int cantidad;
    private String indicaciones;

    public DetalleReceta() {}
    public DetalleReceta(String medicamentoCodigo, int cantidad, String indicaciones) {
        this.medicamentoCodigo = medicamentoCodigo;
        this.cantidad = cantidad;
        this.indicaciones = indicaciones;
    }

    public String getMedicamentoCodigo() {
        return medicamentoCodigo;
    }

    public void setMedicamentoCodigo(String medicamentoCodigo) {
        this.medicamentoCodigo = medicamentoCodigo;
    }

    public String getIndicaciones() {
        return indicaciones;
    }

    public void setIndicaciones(String indicaciones) {
        this.indicaciones = indicaciones;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}