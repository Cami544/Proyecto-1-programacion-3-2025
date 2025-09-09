package hospital.presentation.Despacho;

import hospital.logic.Farmaceuta;
import hospital.logic.Paciente;
import hospital.logic.Receta;
import hospital.logic.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Controller {

        private View view;
        private Model model;

        public Controller(View view, Model model) {
            this.view = view;
            this.model = model;
            view.setController(this);
            view.setModel(model);
            try{
                List< Receta> recetas = Service.instance().getRecetas();
                List<Farmaceuta> farmaceutas = Service.instance().getFarmaceutas();
                model.setListFarmaceutas( farmaceutas);
                model.setListReceta(recetas);
                model.setListRecetaPacienteFiltrado( recetas);
                model.setCriterioFiltro("");
            }
            catch(Exception e){ System.out.println("Error al cargar los datos"+ e.getMessage()); }
        }

       public void refrecarDatos()throws Exception{
           List< Receta> recetas = Service.instance().getRecetas();
           List<Farmaceuta> farmaceutas = Service.instance().getFarmaceutas();
           model.setListFarmaceutas( farmaceutas);
           model.setListReceta( recetas);
           model.setListRecetaPacienteFiltrado( recetas);
           model.setCriterioFiltro("");
        }

    public void buscarRecetasPorPaciente(String criterio) {
        model.setCriterioFiltro(criterio);
        // Si no hay criterio → mostrar todas
        if (criterio == null || criterio.trim().isEmpty()) {
            model.setListRecetaPacienteFiltrado(model.getListRecetas());
            return;
        }
        String criterioBusqueda = criterio.toLowerCase().trim();
        List<Receta> todasLasRecetas = model.getListRecetas();
        List<Receta> recetasFiltradas = new ArrayList<>();
        // Busca ID de paciente
        for (Receta receta : todasLasRecetas) {
            if (receta.getPacienteId() != null && receta.getPacienteId().equalsIgnoreCase(criterioBusqueda)) {
                recetasFiltradas.add(receta);
            }
        }
        // actualizar modelo
        model.setListRecetaPacienteFiltrado(recetasFiltradas);
        // Debug para asegurarse
        System.out.println("Criterio de búsqueda: " + criterio);
        System.out.println("Recetas encontradas: " + recetasFiltradas.size());
    }

    public void guardarCambiosReceta(Farmaceuta farmaceutaSeleccionado, String estado) throws Exception {
        Receta receta = model.getRecetaSeleccionada();
        if (receta == null) {
            throw new IllegalStateException("No hay receta seleccionada.");
        }
        if (farmaceutaSeleccionado != null) {
            receta.setFarmaceutaId(farmaceutaSeleccionado.getId());
        }
        if (estado != null && !estado.isEmpty()) {    // Actualiza estado
            receta.setEstadoReceta(estado);
        }
        Service.instance().updateReceta(receta);  // Persistir cambios
        model.actualizarRecetaEnListas(receta); // Refrescar
    }



    public void seleccionarRecetaPaciente(int index) throws Exception {
        List<Receta> recetasMostradas = model.getRecetasFiltradasPaciente();

        if (recetasMostradas == null || recetasMostradas.isEmpty()) {   // si la lista filtrada está vacía, usar la lista completa
            recetasMostradas = model.getListRecetas();
        }
        if (index >= 0 && index < recetasMostradas.size()) {
            Receta recetaSeleccionada = recetasMostradas.get(index);
            model.setRecetaPacienteSeleccionado(recetaSeleccionada);
        } else {
            throw new Exception("Índice de receta inválido");
        }
    }

}
