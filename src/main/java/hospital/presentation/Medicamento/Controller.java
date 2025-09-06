package hospital.presentation.Medicamento;

import hospital.logic.Medicamento;
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
            model.setList(Service.instance().getMedicamentos());
            model.setFiltered(Service.instance().getMedicamentos());
        } catch (Exception e) {
            System.err.println("Error cargando medicamentos iniciales: " + e.getMessage());
        }
    }

    public void save(Medicamento medicamento) throws Exception {
        try {
            Medicamento existing = Service.instance().readMedicamento(medicamento.getCodigo());
            Service.instance().updateMedicamento(medicamento);
        } catch (Exception e) {
            Service.instance().createMedicamento(medicamento);
        }

        model.setCurrent(new Medicamento());
        model.setList(Service.instance().getMedicamentos());
        model.setFiltered(Service.instance().getMedicamentos());
    }

    public void search(String codigo) throws Exception {
        try {
            Medicamento medicamento = Service.instance().readMedicamento(codigo);
            model.setCurrent(medicamento);
        } catch (Exception ex) {
            Medicamento newMedicamento = new Medicamento();
            newMedicamento.setCodigo(codigo);
            model.setCurrent(newMedicamento);
            throw ex;
        }
    }

    public void delete() throws Exception {
        if (model.getCurrent().getCodigo() != null && !model.getCurrent().getCodigo().trim().isEmpty()) {
            Service.instance().deleteMedicamento(model.getCurrent().getCodigo());
            model.setCurrent(new Medicamento());
            model.setList(Service.instance().getMedicamentos());
            model.setFiltered(Service.instance().getMedicamentos());
        } else {
            throw new Exception("Seleccione un medicamento para eliminar");
        }
    }

    public void clear() {
        model.setCurrent(new Medicamento());
    }

    public void filter(String criterio) {
        try {
            if (criterio == null || criterio.trim().isEmpty()) {
                model.setFiltered(Service.instance().getMedicamentos());
            } else {
                model.setFiltered(Service.instance().searchMedicamentos(criterio));
            }
        } catch (Exception e) {
            System.err.println("Error filtrando medicamentos: " + e.getMessage());
        }
    }
}