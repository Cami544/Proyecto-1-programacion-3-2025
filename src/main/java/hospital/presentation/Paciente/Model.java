package hospital.presentation.Paciente;

import hospital.Application;
import hospital.logic.Paciente;
import hospital.presentation.AbstractModel;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class Model extends AbstractModel {
    private List<Paciente> filtro;
    private List<Paciente> list;
    private Paciente current;
    private int mode;

    public static final String LIST = "list";
    public static final String CURRENT = "current";
    public static final String FILTER = "filter";

    public Model() {
        init(new ArrayList<>());

        try {
            List<Paciente> pacientes = hospital.logic.Service.instance().getPacientes();
            this.list = pacientes;
            this.filtro = new ArrayList<>(pacientes);
        } catch (Exception e) {
            System.err.println("Error cargando pacientes iniciales: " + e.getMessage());
        }
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(LIST);
        firePropertyChange(CURRENT);
        firePropertyChange(FILTER);
    }

    public void init(List<Paciente> list) {
        this.list = list;
        this.current = new Paciente();
        this.filtro = new ArrayList<>(list);
        this.mode = Application.MODE_CREATE;
    }

    public List<Paciente> getList() {
        return list;
    }

    public Paciente getCurrent() {
        return current;
    }

    public List<Paciente> getFiltered() {
        return filtro;
    }

    public int getModel() {
        return mode;
    }

    public void setList(List<Paciente> list) {
        this.list = list;
        firePropertyChange(LIST);
    }

    public void setFiltered(List<Paciente> filter) {
        this.filtro = filter;
        firePropertyChange(FILTER);
    }

    public void setCurrent(Paciente current) {
        this.current = current;
        firePropertyChange(CURRENT);
    }

    public void setModel(int mode) {
        this.mode = mode;
    }
}