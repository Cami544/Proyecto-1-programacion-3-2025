package hospital.presentation.Login;

import hospital.logic.Usuario;
import hospital.presentation.AbstractModel;

import java.beans.PropertyChangeListener;

public class Model extends AbstractModel {
    private Usuario current;

    public static final String CURRENT = "current";

    public Model() {
        current = null;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(CURRENT);
    }

    public Usuario getCurrent() {
        return current;
    }

    public void setCurrent(Usuario current) {
        this.current = current;
        firePropertyChange(CURRENT);
    }
}