package hospital.presentation.Farmaceuta;

import hospital.logic.Farmaceuta;
import hospital.logic.Service;

public class Controller {
    private View view;
    private Model model;

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);

        try {
            model.setList(Service.instance().getFarmaceutas());
            model.setFiltered(Service.instance().getFarmaceutas());
        } catch (Exception e) {
            System.err.println("Error cargando farmaceutas iniciales: " + e.getMessage());
        }
    }

    public void save(Farmaceuta farmaceuta) throws Exception {
        boolean esNuevo = false;

        try {
            Farmaceuta existing = Service.instance().readFarmaceuta(farmaceuta.getId());
            farmaceuta.setClave(existing.getClave());
            Service.instance().updateFarmaceuta(farmaceuta);
        } catch (Exception e) {
            farmaceuta.setClave(farmaceuta.getId());
            Service.instance().createFarmaceuta(farmaceuta);
            esNuevo = true;
        }

        model.setCurrent(new Farmaceuta());
        model.setList(Service.instance().getFarmaceutas());
        model.setFiltered(Service.instance().getFarmaceutas());

        if (esNuevo) {
            view.mostrarClaveAsignada(farmaceuta);
        }
    }

    public void search(String id) throws Exception {
        try {
            Farmaceuta farmaceuta = Service.instance().readFarmaceuta(id);
            model.setCurrent(farmaceuta);
        } catch (Exception ex) {
            Farmaceuta newFarmaceuta = new Farmaceuta();
            newFarmaceuta.setId(id);
            model.setCurrent(newFarmaceuta);
            throw ex;
        }
    }

    public void delete() throws Exception {
        if (model.getCurrent().getId() != null && !model.getCurrent().getId().trim().isEmpty()) {
            Service.instance().deleteFarmaceuta(model.getCurrent().getId());
            model.setCurrent(new Farmaceuta());
            model.setList(Service.instance().getFarmaceutas());
            model.setFiltered(Service.instance().getFarmaceutas());
        } else {
            throw new Exception("Seleccione un farmaceuta para eliminar");
        }
    }

    public void clear() {
        model.setCurrent(new Farmaceuta());
    }

    public void filter(String criterio) {
        try {
            if (criterio == null || criterio.trim().isEmpty()) {
                model.setFiltered(Service.instance().getFarmaceutas());
            } else {
                model.setFiltered(Service.instance().searchFarmaceutas(criterio));
            }
        } catch (Exception e) {
            System.err.println("Error filtrando farmaceutas: " + e.getMessage());
        }
    }
}