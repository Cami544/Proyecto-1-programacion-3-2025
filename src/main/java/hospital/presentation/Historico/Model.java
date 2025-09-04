package hospital.presentation.Historico;

import hospital.logic.Receta;
import hospital.presentation.AbstractModel;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class Model extends AbstractModel {
    private List<Receta> recetas;
    private List<Receta> recetasFiltradas;
    private Receta recetaSeleccionada;
    private String criterioFiltro;

    public static final String RECETAS = "recetas";
    public static final String RECETAS_FILTRADAS = "recetasFiltradas";
    public static final String RECETA_SELECCIONADA = "recetaSeleccionada";
    public static final String CRITERIO_FILTRO = "criterioFiltro";

    public Model() {
        this.recetas = new ArrayList<>();
        this.recetasFiltradas = new ArrayList<>();
        this.recetaSeleccionada = null;
        this.criterioFiltro = "";
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(RECETAS);
        firePropertyChange(RECETAS_FILTRADAS);
        firePropertyChange(RECETA_SELECCIONADA);
        firePropertyChange(CRITERIO_FILTRO);
    }

    public List<Receta> getRecetas() {
        return recetas;
    }

    public void setRecetas(List<Receta> recetas) {
        this.recetas = recetas;
        firePropertyChange(RECETAS);
    }

    public List<Receta> getRecetasFiltradas() {
        return recetasFiltradas;
    }

    public void setRecetasFiltradas(List<Receta> recetasFiltradas) {
        this.recetasFiltradas = recetasFiltradas;
        firePropertyChange(RECETAS_FILTRADAS);
    }

    public Receta getRecetaSeleccionada() {
        return recetaSeleccionada;
    }

    public void setRecetaSeleccionada(Receta recetaSeleccionada) {
        this.recetaSeleccionada = recetaSeleccionada;
        firePropertyChange(RECETA_SELECCIONADA);
    }

    public String getCriterioFiltro() {
        return criterioFiltro;
    }

    public void setCriterioFiltro(String criterioFiltro) {
        this.criterioFiltro = criterioFiltro;
        firePropertyChange(CRITERIO_FILTRO);
    }
}