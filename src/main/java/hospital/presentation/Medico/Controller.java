package hospital.presentation.Medico;

import hospital.logic.Medico;
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
            model.setList(Service.instance().getMedicos());
            model.setFiltered(Service.instance().getMedicos());
        } catch (Exception e) {
            System.err.println("Error cargando medicos iniciales: " + e.getMessage());
        }
    }

    public void save(Medico medico) throws Exception {
        boolean esNuevo = false;

        try {
            Medico existing = Service.instance().readMedico(medico.getId());
            medico.setClave(existing.getClave());
            Service.instance().updateMedico(medico);
        } catch (Exception e) {
            medico.setClave(medico.getId());
            Service.instance().createMedico(medico);
            esNuevo = true;
        }

        model.setCurrent(new Medico());
        model.setList(Service.instance().getMedicos());
        model.setFiltered(Service.instance().getMedicos());

        if (esNuevo) {
            view.mostrarClaveAsignada(medico);
        }
    }

    public void search(String id) throws Exception {
        try {
            Medico medico = Service.instance().readMedico(id);
            model.setCurrent(medico);
        } catch (Exception ex) {
            Medico newMedico = new Medico();
            newMedico.setId(id);
            model.setCurrent(newMedico);
            throw ex;
        }
    }

    public void delete() throws Exception {
        if (model.getCurrent().getId() != null && !model.getCurrent().getId().trim().isEmpty()) {
            Service.instance().deleteMedico(model.getCurrent().getId());
            model.setCurrent(new Medico());
            model.setList(Service.instance().getMedicos());
            model.setFiltered(Service.instance().getMedicos());
        } else {
            throw new Exception("Seleccione un medico para eliminar");
        }
    }

    public void clear() {
        model.setCurrent(new Medico());
    }

    public void filter(String criterio) {
        try {
            if (criterio == null || criterio.trim().isEmpty()) {
                model.setFiltered(Service.instance().getMedicos());
            } else {
                model.setFiltered(Service.instance().searchMedicos(criterio));
            }
        } catch (Exception e) {
            System.err.println("Error filtrando medicos: " + e.getMessage());
        }
    }
}