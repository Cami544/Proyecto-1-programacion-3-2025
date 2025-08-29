package hospital.presentation.Medico;

import hospital.logic.Medico;
import hospital.presentation.AbstractModel;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class Model extends AbstractModel {
    private Medico current;
    private List<Medico> list;
    private List<Medico> filtered;

    public static final String CURRENT = "current";
    public static final String LIST = "list";
    public static final String FILTERED = "filtered";

    public Model() {
        current = new Medico();
        list = new ArrayList<>();
        filtered = new ArrayList<>();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(CURRENT);
        firePropertyChange(LIST);
        firePropertyChange(FILTERED);
    }

    public Medico getCurrent() {
        return current;
    }

    public void setCurrent(Medico current) {
        this.current = current;
        firePropertyChange(CURRENT);
    }

    public List<Medico> getList() {
        return list;
    }

    public void setList(List<Medico> list) {
        this.list = list;
        firePropertyChange(LIST);
    }

    public List<Medico> getFiltered() {
        return filtered;
    }

    public void setFiltered(List<Medico> filtered) {
        this.filtered = filtered;
        firePropertyChange(FILTERED);
    }
}