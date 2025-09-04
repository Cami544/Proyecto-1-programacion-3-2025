package hospital.logic;

import hospital.data.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Service {
    private static Service theInstance;
    private GestorDatosMedicos gestorMedicos;
    private GestorDatosMedicamentos gestorMedicamentos;
    private GestorDatosPaciente gestorPaciente;
    private GestorDatosFarmaceutas gestorFarmaceutas;
    private List<Receta> recetasEnMemoria = new ArrayList<>();

    private Service() {
        gestorMedicos = new GestorDatosMedicos();
        gestorMedicamentos = new GestorDatosMedicamentos();
        gestorPaciente = new GestorDatosPaciente();
        gestorFarmaceutas = new GestorDatosFarmaceutas();

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

    public void create(Medico e) throws Exception {
        // Crear una instancia de ListaMedicos
        ListaMedicos listaMedicos = new ListaMedicos();

        Medico result = listaMedicos.getMedicos().stream()
                .filter(medico -> medico.getId().equals(e.getId()))
                .findFirst()
                .orElse(null);

        if (result == null) {
            listaMedicos.getMedicos().add(e);
            gestorMedicos.guardar(listaMedicos.getMedicos());
        } else {
            throw new Exception("Persona ya existe");
        }
    }

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

    // =============== PACIENTES ===============

    public List<Paciente> findAllPacientes() {return gestorPaciente.cargar();}


    public void createPaciente(Paciente paciente) throws Exception {
        List<Paciente> pacientes = gestorPaciente.cargar();

        Paciente existente = pacientes.stream()
                .filter(m -> m.getId().equals(paciente.getId()))
                .findFirst()
                .orElse(null);

        if (existente != null) {
            throw new Exception("Paciente ya existe con ese ID");
        }

        pacientes.add(paciente);
        gestorPaciente.guardar(pacientes);
    }

    public Paciente readPaciente(String id) throws Exception {
        List<Paciente> pacientes = gestorPaciente.cargar();

        Paciente result = pacientes.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (result != null) {
            return result;
        } else {
            throw new Exception("Paciente no existe");
        }
    }

    public void updatePaciente(Paciente paciente) throws Exception {
        List<Paciente> pacientes = gestorPaciente.cargar();

        for (int i = 0; i < pacientes.size(); i++) {
            if (pacientes.get(i).getId().equals(paciente.getId())) {
                pacientes.set(i, paciente);
                gestorPaciente.guardar(pacientes);
                return;
            }
        }
        throw new Exception("Paciente no encontrado para actualizar");
    }

    public void deletePaciente(String id) throws Exception {
        List<Paciente> pacientes = gestorPaciente.cargar();

        Paciente toRemove = pacientes.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (toRemove != null) {
            pacientes.remove(toRemove);
            gestorPaciente.guardar(pacientes);
        } else {
            throw new Exception("Paciente no encontrado para eliminar");
        }
    }

    public List<Paciente> searchPaciente(String criterio) { //general para id y nomPaciente
        List<Paciente> pacientes = gestorPaciente.cargar();

        if (criterio == null || criterio.trim().isEmpty()) {
            return pacientes;
        }
        String criterioBusqueda = criterio.toLowerCase().trim();

        return pacientes.stream()
                .filter(m ->
                        m.getId().toLowerCase().contains(criterioBusqueda) ||
                                m.getNombre().toLowerCase().contains(criterioBusqueda)
                )
                .collect(Collectors.toList());
    }

    // =============== FARMACEUTAS ===============
    public void createFarmaceuta(Farmaceuta farmaceuta) throws Exception {
        List<Farmaceuta> farmaceutas = gestorFarmaceutas.cargar();

        Farmaceuta existente = farmaceutas.stream()
                .filter(f -> f.getId().equals(farmaceuta.getId()))
                .findFirst()
                .orElse(null);

        if (existente == null) {
            farmaceuta.setClave(farmaceuta.getId());
            farmaceutas.add(farmaceuta);
            gestorFarmaceutas.guardar(farmaceutas);
        } else {
            throw new Exception("Farmaceuta ya existe");
        }
    }

    public Farmaceuta readFarmaceuta(String id) throws Exception {
        List<Farmaceuta> farmaceutas = gestorFarmaceutas.cargar();

        Farmaceuta result = farmaceutas.stream()
                .filter(f -> f.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (result != null) {
            return result;
        } else {
            throw new Exception("Farmaceuta no existe");
        }
    }

    public void updateFarmaceuta(Farmaceuta farmaceuta) throws Exception {
        List<Farmaceuta> farmaceutas = gestorFarmaceutas.cargar();

        for (int i = 0; i < farmaceutas.size(); i++) {
            if (farmaceutas.get(i).getId().equals(farmaceuta.getId())) {
                farmaceutas.set(i, farmaceuta);
                gestorFarmaceutas.guardar(farmaceutas);
                return;
            }
        }
        throw new Exception("Farmaceuta no encontrado para actualizar");
    }

    public void deleteFarmaceuta(String id) throws Exception {
        List<Farmaceuta> farmaceutas = gestorFarmaceutas.cargar();

        Farmaceuta toRemove = farmaceutas.stream()
                .filter(f -> f.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (toRemove != null) {
            farmaceutas.remove(toRemove);
            gestorFarmaceutas.guardar(farmaceutas);
        } else {
            throw new Exception("Farmaceuta no encontrado para eliminar");
        }
    }

    public List<Farmaceuta> findAllFarmaceutas() {
        return gestorFarmaceutas.cargar();
    }

    public List<Farmaceuta> searchFarmaceutas(String criterio) {
        List<Farmaceuta> farmaceutas = gestorFarmaceutas.cargar();

        if (criterio == null || criterio.trim().isEmpty()) {
            return farmaceutas;
        }

        String criterioBusqueda = criterio.toLowerCase().trim();

        return farmaceutas.stream()
                .filter(f ->
                        f.getId().toLowerCase().contains(criterioBusqueda) ||
                                f.getNombre().toLowerCase().contains(criterioBusqueda)
                )
                .collect(Collectors.toList());
    }

    // =============== MEDICAMENTOS ===============
    public void createMedicamento(Medicamento medicamento) throws Exception {
        List<Medicamento> medicamentos = gestorMedicamentos.cargar();

        Medicamento existente = medicamentos.stream()
                .filter(m -> m.getCodigo().equals(medicamento.getCodigo()))
                .findFirst()
                .orElse(null);

        if (existente == null) {
            medicamentos.add(medicamento);
            gestorMedicamentos.guardar(medicamentos);
        } else {
            throw new Exception("Medicamento ya existe con ese código");
        }
    }

    public Medicamento readMedicamento(String codigo) throws Exception {
        List<Medicamento> medicamentos = gestorMedicamentos.cargar();

        Medicamento result = medicamentos.stream()
                .filter(m -> m.getCodigo().equals(codigo))
                .findFirst()
                .orElse(null);

        if (result != null) {
            return result;
        } else {
            throw new Exception("Medicamento no existe");
        }
    }

    public void updateMedicamento(Medicamento medicamento) throws Exception {
        List<Medicamento> medicamentos = gestorMedicamentos.cargar();

        for (int i = 0; i < medicamentos.size(); i++) {
            if (medicamentos.get(i).getCodigo().equals(medicamento.getCodigo())) {
                medicamentos.set(i, medicamento);
                gestorMedicamentos.guardar(medicamentos);
                return;
            }
        }
        throw new Exception("Medicamento no encontrado para actualizar");
    }

    public void deleteMedicamento(String codigo) throws Exception {
        List<Medicamento> medicamentos = gestorMedicamentos.cargar();

        Medicamento toRemove = medicamentos.stream()
                .filter(m -> m.getCodigo().equals(codigo))
                .findFirst()
                .orElse(null);

        if (toRemove != null) {
            medicamentos.remove(toRemove);
            gestorMedicamentos.guardar(medicamentos);
        } else {
            throw new Exception("Medicamento no encontrado para eliminar");
        }
    }

    public List<Medicamento> findAllMedicamentos() {
        return gestorMedicamentos.cargar();
    }

    public List<Medicamento> searchMedicamentos(String criterio) {
        List<Medicamento> medicamentos = gestorMedicamentos.cargar();

        if (criterio == null || criterio.trim().isEmpty()) {
            return medicamentos;
        }

        String criterioBusqueda = criterio.toLowerCase().trim();

        return medicamentos.stream()
                .filter(m ->
                        m.getCodigo().toLowerCase().contains(criterioBusqueda) ||
                                m.getNombre().toLowerCase().contains(criterioBusqueda) ||
                                m.getPresentacion().toLowerCase().contains(criterioBusqueda)
                )
                .collect(Collectors.toList());
    }

    // =============== RECETAS ===============
    public void guardarReceta(Receta receta, LocalDate fechaRetiro) throws Exception {
        readPaciente(receta.getPacienteId());

        for (DetalleReceta detalle : receta.getDetalles()) {
            readMedicamento(detalle.getMedicamentoCodigo());
        }

        recetasEnMemoria.add(receta);

        System.out.println("Receta guardada: " + receta.getId() +
                " para paciente: " + receta.getPacienteId() +
                " fecha retiro: " + fechaRetiro);
    }

    public List<Receta> findRecetasByPaciente(String pacienteId) {
        return recetasEnMemoria.stream()
                .filter(r -> r.getPacienteId().equals(pacienteId))
                .collect(Collectors.toList());
    }


    public List<Receta> findAllRecetas() {
        return new ArrayList<>(recetasEnMemoria);
    }
}