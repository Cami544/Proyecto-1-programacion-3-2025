package hospital.presentation.Farmaceuta;

import hospital.logic.Farmaceuta;
import hospital.presentation.AbstractModel;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class Model extends AbstractModel {
    private Farmaceuta current;
    private List<Farmaceuta> list;
    private List<Farmaceuta> filtered;

    public static final String CURRENT = "current";
    public static final String LIST = "list";
    public static final String FILTERED = "filtered";

    public Model() {
        current = new Farmaceuta();
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

    public Farmaceuta getCurrent() {
        return current;
    }

    public void setCurrent(Farmaceuta current) {
        this.current = current;
        firePropertyChange(CURRENT);
    }

    public List<Farmaceuta> getList() {
        return list;
    }

    public void setList(List<Farmaceuta> list) {
        this.list = list;
        firePropertyChange(LIST);
    }

    public List<Farmaceuta> getFiltered() {
        return filtered;
    }

    public void setFiltered(List<Farmaceuta> filtered) {
        this.filtered = filtered;
        firePropertyChange(FILTERED);
    }
}