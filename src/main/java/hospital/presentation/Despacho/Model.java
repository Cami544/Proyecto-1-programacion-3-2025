package hospital.presentation.Despacho;

import hospital.Application;

import hospital.logic.Farmaceuta;
import hospital.logic.Paciente;
import hospital.logic.Receta;
import hospital.logic.Service;
import hospital.presentation.AbstractModel;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class Model  extends AbstractModel {
    private List<Receta> recetas;
    private List<Receta> recetasFiltradasPaciente;
    private List<Farmaceuta> listFarmaceutas;
    private Receta recetaSeleccionada;
    private Farmaceuta farmaceutaSeleecionado;
    private String criterioFiltro;


    public static final String LIST_FARMACIA = "listFarmacia";
    public static final String LIST_RECETA = "listRecetas";
    public static final String RECETA_SELECCIONADO = "pacienteSeleccionado";
    public static final String RECETA_FILTRADO = "recetaFiltradaPaciente";
    public static final String FARMACEUTA_SELECCIONADO = "farmaceutaSeleccionado";
    public static final String CRITERIO_Filtro_RECETA = "criterioFiltro";


    public Model() {
       this.recetas= Service.instance().getRecetas();
       this.recetasFiltradasPaciente = new ArrayList<>();
       this.listFarmaceutas = Service.instance().getFarmaceutas();
       this.farmaceutaSeleecionado = null;
       this.recetaSeleccionada = null;
       this.criterioFiltro = "";

    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(LIST_FARMACIA);
        firePropertyChange(RECETA_FILTRADO);
        firePropertyChange(LIST_RECETA);


    }

    public List<Receta> getListRecetas() {
        return recetas;
    }


    void setListReceta(List<Receta> listaRecetas) {
        this.recetas = listaRecetas;
        firePropertyChange(LIST_RECETA);
    }

    public List<Farmaceuta> getListFarmaceutas() { return listFarmaceutas; }

    void setListFarmaceutas(List<Farmaceuta> list) {
        this.listFarmaceutas = list;
        firePropertyChange(LIST_FARMACIA);
    }

    public Receta getRecetaSeleccionada() { return recetaSeleccionada;}

    public void setRecetaPacienteSeleccionado(Receta receta) {
        this.recetaSeleccionada = receta;
        firePropertyChange(RECETA_SELECCIONADO);
    }

    public Farmaceuta getFarmaceuta() { return farmaceutaSeleecionado; }

    public void setFarmaceuta(Farmaceuta farmaceuta) {
        this.farmaceutaSeleecionado = farmaceuta;
        firePropertyChange(FARMACEUTA_SELECCIONADO);
    }
    public String getRecetaFiltro() { return criterioFiltro;}

    public void setCriterioFiltro(String criterio) {
        this.criterioFiltro = criterio;
        firePropertyChange(CRITERIO_Filtro_RECETA);
    }

    public void actualizarRecetaEnListas(Receta receta) {
        boolean changed = false;
        if (recetas != null) {
            for (int i = 0; i < recetas.size(); i++) {
                if (recetas.get(i).getId().equals(receta.getId())) {
                    recetas.set(i, receta);
                    changed = true;
                    break;
                }
            }
        }

        if (recetasFiltradasPaciente != null) {
            for (int i = 0; i < recetasFiltradasPaciente.size(); i++) {
                if (recetasFiltradasPaciente.get(i).getId().equals(receta.getId())) {
                    recetasFiltradasPaciente.set(i, receta);
                    changed = true;
                    break;
                }
            }
        }
        this.recetaSeleccionada = receta;
        firePropertyChange(LIST_RECETA);
        firePropertyChange(RECETA_FILTRADO);
        firePropertyChange(RECETA_SELECCIONADO);
    }


    public List<Receta> getRecetasFiltradasPaciente() { return recetasFiltradasPaciente;}

    void setListRecetaPacienteFiltrado(List<Receta> list) {
        this.recetasFiltradasPaciente = list;
        firePropertyChange(RECETA_FILTRADO);
    }
    }


