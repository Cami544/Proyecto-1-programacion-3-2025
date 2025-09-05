package hospital.presentation.Paciente;

import hospital.Application;
import hospital.logic.Paciente;
import hospital.presentation.AbstractModel;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class Model extends AbstractModel {
    List<Paciente> filtro;
    //Paciente filtro;
    List<Paciente> list;
    Paciente current;
    int mode;

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
       this.filtro = new ArrayList<>();
       // this.filtro = new Paciente();
        this.mode= Application.MODE_CREATE;

    }

    public Model(){
        init(hospital.logic.Service.instance().getPacientes());
    }

    public List<Paciente> getList() { return list; }

    public Paciente getCurrent() { return current; }

   public List<Paciente> getFiltered() { return filtro; }
   // public Paciente getFilter() { return filtro; }

    public int getModel() { return mode; }

    public void setList(List<Paciente> list) {
        this.list = list;
        firePropertyChange(LIST); //Actualiza pantalla con cambios de la lista
    }

    public void setFiltered(List<Paciente> filter) {
        this.filtro = filter;
        firePropertyChange(FILTER);
    }

   /* public void setFilter(Paciente paciente){
        this.filtro = paciente;
        firePropertyChange(FILTER);
    }
    */
    public void setCurrent(Paciente current) {
        this.current = current;
        firePropertyChange(CURRENT);
    }

    public void setModel(int mode) { this.mode = mode;}

    public static final String LIST = "list";
    public static final String CURRENT = "current";
    public static final String FILTER = "filter";
}
