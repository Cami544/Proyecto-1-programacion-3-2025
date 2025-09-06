package hospital.presentation.Medicamento;

import hospital.logic.Medicamento;
import hospital.presentation.AbstractModel;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class Model extends AbstractModel {
    private Medicamento current;
    private List<Medicamento> list;
    private List<Medicamento> filtered;

    public static final String CURRENT = "current";
    public static final String LIST = "list";
    public static final String FILTERED = "filtered";

    public Model() {
        current = new Medicamento();
        list = new ArrayList<>();
        filtered = new ArrayList<>();

        try {
            this.list = hospital.logic.Service.instance().getMedicamentos();
            this.filtered = new ArrayList<>(this.list);
        } catch (Exception e) {
            System.err.println("Error cargando medicamentos iniciales: " + e.getMessage());
        }
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(CURRENT);
        firePropertyChange(LIST);
        firePropertyChange(FILTERED);
    }

    public Medicamento getCurrent() {
        return current;
    }

    public void setCurrent(Medicamento current) {
        this.current = current;
        firePropertyChange(CURRENT);
    }

    public List<Medicamento> getList() {
        return list;
    }

    public void setList(List<Medicamento> list) {
        this.list = list;
        firePropertyChange(LIST);
    }

    public List<Medicamento> getFiltered() {
        return filtered;
    }

    public void setFiltered(List<Medicamento> filtered) {
        this.filtered = filtered;
        firePropertyChange(FILTERED);
    }
}