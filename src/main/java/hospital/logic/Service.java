
package hospital.logic;

import hospital.data.GestorDatosMedicos;
import hospital.data.GestorDatosMedicamentos;
import hospital.data.ListaMedicamentos;
import hospital.data.ListaMedicos;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Service {
    private static Service theInstance;
    private GestorDatosMedicos gestorMedicos;
    private GestorDatosMedicamentos gestorMedicamentos;

    private Service() {
        gestorMedicos = new GestorDatosMedicos();
        gestorMedicamentos = new GestorDatosMedicamentos();
    }

    public static Service instance() {
        if (theInstance == null) theInstance = new Service();
        return theInstance;
    }

    // =============== MÉDICOS ===============
    public void createMedico(Medico medico) throws Exception {
        List<Medico> medicos = gestorMedicos.cargar();

        Medico existente = medicos.stream()
                .filter(m -> m.getId().equals(medico.getId()))
                .findFirst()
                .orElse(null);

        if (existente == null) {
            // Al crear un médico, la clave inicial es igual al ID
            medico.setClave(medico.getId());
            medicos.add(medico);
            gestorMedicos.guardar(medicos);
        } else {
            throw new Exception("Médico ya existe");
        }
    }

    public Medico readMedico(String id) throws Exception {
        List<Medico> medicos = gestorMedicos.cargar();

        Medico result = medicos.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (result != null) {
            return result;
        } else {
            throw new Exception("Médico no existe");
        }
    }

    public void updateMedico(Medico medico) throws Exception {
        List<Medico> medicos = gestorMedicos.cargar();

        for (int i = 0; i < medicos.size(); i++) {
            if (medicos.get(i).getId().equals(medico.getId())) {
                medicos.set(i, medico);
                gestorMedicos.guardar(medicos);
                return;
            }
        }
        throw new Exception("Médico no encontrado para actualizar");
    }

    public void deleteMedico(String id) throws Exception {
        List<Medico> medicos = gestorMedicos.cargar();

        Medico toRemove = medicos.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (toRemove != null) {
            medicos.remove(toRemove);
            gestorMedicos.guardar(medicos);
        } else {
            throw new Exception("Médico no encontrado para eliminar");
        }
    }

//    public void create(Medico e) throws Exception {
//        Medico result = ListaMedicos.getMedicos().stream()
//                .filter(i -> i.getId().equals(e.getId()))
//                .findFirst()
//                .orElse(null);
//        if (result == null) {
//            ListaMedicos.getMedicos().add(e);
//        } else {
//            throw new Exception("Persona ya existe");
//        }
//    }

    public List<Medico> findAllMedicos() {
        return gestorMedicos.cargar();
    }

    public List<Medico> searchMedicos(String criterio) {
        List<Medico> medicos = gestorMedicos.cargar();

        if (criterio == null || criterio.trim().isEmpty()) {
            return medicos;
        }

        String criterioBusqueda = criterio.toLowerCase().trim();

        return medicos.stream()
                .filter(m ->
                        m.getId().toLowerCase().contains(criterioBusqueda) ||
                                m.getNombre().toLowerCase().contains(criterioBusqueda) ||
                                m.getEspecialidad().toLowerCase().contains(criterioBusqueda)
                )
                .collect(Collectors.toList());
    }

    // =============== MEDICAMENTOS ===============
    public List<Medicamento> findAllMedicamentos() {
        return gestorMedicamentos.cargar();
    }

    public void saveMedicamentos(List<Medicamento> medicamentos) throws IOException {
        gestorMedicamentos.guardar(medicamentos);
    }


//    public List<Medico> findAll() {
//        return ListaMedicos.getMedicos();
//    }


}