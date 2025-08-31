package hospital.presentation.Medico;

import hospital.logic.Medico;
import hospital.logic.Service;

public class Controller {
    private View view;
    private Model model;

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;
//        view.setController(this);
//        view.setModel(model);
    }



    public void save(Medico medico) throws Exception {
        try {
            Medico existing = Service.instance().readMedico(medico.getId());
            Service.instance().updateMedico(medico);
        } catch (Exception e) {
            Service.instance().createMedico(medico);
        }

        model.setCurrent(new Medico());
        model.setList(Service.instance().findAllMedicos());
        model.setFiltered(Service.instance().findAllMedicos());
    }

    public void create(Medico e) throws  Exception{
        Service.instance().create(e);
        model.setCurrent(new Medico());
        model.setList(Service.instance().findAllMedicos());
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
            model.setList(Service.instance().findAllMedicos());
            model.setFiltered(Service.instance().findAllMedicos());
        } else {
            throw new Exception("Seleccione un médico para eliminar");
        }
    }

    public void clear() {
        model.setCurrent(new Medico());
    }

    public void filter(String criterio) {
        model.setFiltered(Service.instance().searchMedicos(criterio));
    }
}