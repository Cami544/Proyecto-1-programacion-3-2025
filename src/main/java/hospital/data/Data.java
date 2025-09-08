package hospital.data;

import hospital.logic.*;
import jakarta.xml.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Data {

    @XmlElementWrapper(name = "pacientes")
    @XmlElement(name = "paciente")
    private List<Paciente> pacientes;

    @XmlElementWrapper(name = "medicos")
    @XmlElement(name = "medico")
    private List<Medico> medicos;

    @XmlElementWrapper(name = "farmaceutas")
    @XmlElement(name = "farmaceuta")
    private List<Farmaceuta> farmaceutas;

    @XmlElementWrapper(name = "medicamentos")
    @XmlElement(name = "medicamento")
    private List<Medicamento> medicamentos;

    @XmlElementWrapper(name = "administradores")
    @XmlElement(name = "administrador")
    private List<Administrador> administradores;

    @XmlElementWrapper(name = "recetas")
    @XmlElement(name = "receta")
    private List<Receta> recetas;


    public Data() {
        this.pacientes = new ArrayList<>();
        this.medicos = new ArrayList<>();
        this.farmaceutas = new ArrayList<>();
        this.medicamentos = new ArrayList<>();
        this.recetas = new ArrayList<>();
        this.administradores = new ArrayList<>();
        if (administradores.isEmpty()) {
            administradores.add(new Administrador("admin", "Administrador", "admin"));
        }
    }


    public List<Paciente> getPacientes() {
        return pacientes;
    }
    public List<Medico> getMedicos() { return medicos; }
    public List<Farmaceuta> getFarmaceutas() { return farmaceutas; }
    public List<Medicamento> getMedicamentos() { return medicamentos; }
    public List<Receta> getRecetas() { return recetas; }
    public List<Administrador> getAdministradores() { return administradores; }

}
