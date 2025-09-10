package hospital.logic;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import hospital.data.Data;
import hospital.data.XmlPersister;

import java.util.Comparator;

public class Service {
    private static Service theInstance;

    public static Service instance() {
        if (theInstance == null) theInstance = new Service();
        return theInstance;
    }

    private Data data;

    private Service() {
        try {
            data = XmlPersister.instance().load();
            System.out.println("Datos cargados desde XML");
        } catch (Exception e) {
            System.out.println("No se encontro archivo XML, creando nueva estructura de datos");
            data = new Data();
        }
    }

    public void stop() {
        try {
            XmlPersister.instance().store(data);
            System.out.println("Datos guardados en XML exitosamente");
        } catch (Exception e) {
            System.err.println("Error guardando datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ========================= PACIENTES ======================

    public void createPaciente(Paciente p) throws Exception {
        Paciente result = data.getPacientes().stream()
                .filter(i -> i.getId().equals(p.getId()))
                .findFirst()
                .orElse(null);
        if (result == null) {
            data.getPacientes().add(p);
            stop();
        } else {
            throw new Exception("Paciente ya existe");
        }
    }

    public Paciente readPaciente(String id) throws Exception {
        Paciente result = data.getPacientes().stream()
                .filter(i -> i.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (result != null) {
            return result;
        } else {
            throw new Exception("Paciente no existe");
        }
    }

    public void updatePaciente(Paciente p) throws Exception {
        Paciente result;
        try{
            result = this.readPaciente(p.getId());
            data.getPacientes().remove(result);
            data.getPacientes().add(p);
            stop();
        }
        catch(Exception e){
            throw new Exception("Paciente no existe");
        }
    }

    public void deletePaciente(String id) throws Exception {
        Paciente result = this.readPaciente(id);
        data.getPacientes().remove(result);
        stop();
    }

    public List<Paciente> searchPacientes(String nombre) {
        return data.getPacientes().stream()
                .filter(i -> i.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .sorted(Comparator.comparing(Paciente::getNombre))
                .collect(Collectors.toList());
    }

    public List<Paciente> getPacientes() {
        return data.getPacientes();
    }

    // ========================= MEDICOS ========================

    public void createMedico(Medico m) throws Exception {
        Medico result = data.getMedicos().stream()
                .filter(i -> i.getId().equals(m.getId()))
                .findFirst()
                .orElse(null);
        if (result == null) {
            data.getMedicos().add(m);
            stop();
        } else {
            throw new Exception("Medico ya existe");
        }
    }

    public Medico readMedico(String id) throws Exception {
        Medico result = data.getMedicos().stream()
                .filter(i -> i.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (result != null) return result;
        else throw new Exception("Medico no existe");
    }

    public void updateMedico(Medico m) throws Exception {
        Medico result = this.readMedico(m.getId());
        data.getMedicos().remove(result);
        data.getMedicos().add(m);
        stop();
    }

    public void deleteMedico(String id) throws Exception {
        Medico result = this.readMedico(id);
        data.getMedicos().remove(result);
        stop();
    }

    public List<Medico> searchMedicos(String nombre) {
        return data.getMedicos().stream()
                .filter(i -> i.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .sorted(Comparator.comparing(Medico::getNombre))
                .collect(Collectors.toList());
    }

    public List<Medico> getMedicos() {
        return data.getMedicos();
    }

    // ====================== FARMACEUTAS =======================

    public void createFarmaceuta(Farmaceuta f) throws Exception {
        Farmaceuta result = data.getFarmaceutas().stream()
                .filter(i -> i.getId().equals(f.getId()))
                .findFirst()
                .orElse(null);
        if (result == null) {
            data.getFarmaceutas().add(f);
            stop();
        } else {
            throw new Exception("Farmaceuta ya existe");
        }
    }

    public Farmaceuta readFarmaceuta(String id) throws Exception {
        Farmaceuta result = data.getFarmaceutas().stream()
                .filter(i -> i.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (result != null) return result;
        else throw new Exception("Farmaceuta con id " + id + " no existe");
    }

    public void updateFarmaceuta(Farmaceuta f) throws Exception {
        Farmaceuta result = this.readFarmaceuta(f.getId());
        data.getFarmaceutas().remove(result);
        data.getFarmaceutas().add(f);
        stop();
    }

    public void deleteFarmaceuta(String id) throws Exception {
        Farmaceuta result = this.readFarmaceuta(id);
        data.getFarmaceutas().remove(result);
        stop();
    }

    public List<Farmaceuta> searchFarmaceutas(String nombre) {
        return data.getFarmaceutas().stream()
                .filter(i -> i.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .sorted(Comparator.comparing(Farmaceuta::getNombre))
                .collect(Collectors.toList());
    }

    public List<Farmaceuta> getFarmaceutas() {
        return data.getFarmaceutas();
    }

    // ====================== MEDICAMENTOS ======================

    public void createMedicamento(Medicamento m) throws Exception {
        Medicamento result = data.getMedicamentos().stream()
                .filter(i -> i.getCodigo().equals(m.getCodigo()))
                .findFirst()
                .orElse(null);
        if (result == null) {
            data.getMedicamentos().add(m);
            stop();
        } else {
            throw new Exception("Medicamento ya existe");
        }
    }

    public Medicamento readMedicamento(String codigo) throws Exception {
        Medicamento result = data.getMedicamentos().stream()
                .filter(i -> i.getCodigo().equals(codigo))
                .findFirst()
                .orElse(null);

        if (result != null) return result;
        else throw new Exception("Medicamento con código " + codigo + " no existe");
    }

    public void updateMedicamento(Medicamento m) throws Exception {
        Medicamento result = this.readMedicamento(m.getCodigo());
        data.getMedicamentos().remove(result);
        data.getMedicamentos().add(m);
        stop();
    }

    public void deleteMedicamento(String codigo) throws Exception {
        Medicamento result = this.readMedicamento(codigo);
        data.getMedicamentos().remove(result);
        stop();
    }

    public List<Medicamento> searchMedicamentos(String nombre) {
        return data.getMedicamentos().stream()
                .filter(i -> i.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .sorted(Comparator.comparing(Medicamento::getNombre))
                .collect(Collectors.toList());
    }

    public List<Medicamento> getMedicamentos() {
        return data.getMedicamentos();
    }

    // ========================= RECETAS ========================

    public void createReceta(Receta receta, LocalDate fechaRetiro) throws Exception {
        readPaciente(receta.getPacienteId());

        for (DetalleReceta detalle : receta.getDetalles()) {
            readMedicamento(detalle.getMedicamentoCodigo());
        }
        Receta result = data.getRecetas().stream()
                .filter(i -> i.getId().equals(receta.getId()))
                .findFirst()
                .orElse(null);

        if (result == null) {
            if (receta.getFecha() == null) {  // setea fecha si no viene
                receta.setFecha(LocalDate.now());
            }
            if (fechaRetiro != null) {  // fechaRetiro
                receta.setFechaRetiro(fechaRetiro);
            } else if (receta.getFechaRetiro() == null) {
                receta.setFechaRetiro(receta.getFecha().plusDays(1));
            }
            data.getRecetas().add(receta);
            stop();
        } else {
            throw new Exception("Receta con id " + receta.getId() + " ya existe");
        }
    }

    public Receta readReceta(Receta r) throws Exception {
        Receta result = data.getRecetas().stream()
                .filter(i -> i.getId().equals(r.getId()))
                .findFirst()
                .orElse(null);
        if (result != null) return result;
        else throw new Exception("Receta no existe");
    }

    public Receta updateReceta(Receta r) throws Exception {
        Receta result = this.readReceta(r);
        if (result == null) {
            throw new Exception("Receta no existe");
        }
        result.setFarmaceutaId(r.getFarmaceutaId());
        result.setEstadoReceta(r.getEstadoReceta());
        result.setFecha(r.getFecha());
        result.setFechaRetiro(r.getFechaRetiro());
        result.setPacienteId(r.getPacienteId());
        result.setDetalles(r.getDetalles());
        stop();
        return result;
    }

    public void deleteReceta(Receta r) throws Exception {
        Receta result = this.readReceta(r);
        data.getRecetas().remove(result);
        stop();
    }

    public List<Receta> searchRecetasByPaciente(String id) {
        return data.getRecetas().stream()
                .filter(r -> r.getPacienteId().equals(id))
                .collect(Collectors.toList());
    }

    public List<Receta> getRecetas() {
        return data.getRecetas();
    }

    public void clearAllData() {
        data.getPacientes().clear();
        data.getMedicos().clear();
        data.getFarmaceutas().clear();
        data.getMedicamentos().clear();
        data.getRecetas().clear();

        stop();

        System.out.println("Todos los datos han sido eliminados");
    }

    // ====================== ADMINISTRADORES ======================

    public void createAdministrador(Administrador a) throws Exception {
        boolean exists = false;

        try {
            readMedico(a.getId());
            exists = true;
        } catch (Exception e) {}

        if (!exists) {
            try {
                readFarmaceuta(a.getId());
                exists = true;
            } catch (Exception e) {}
        }

        if (!exists) {
            try {
                readAdministrador(a.getId());
                exists = true;
            } catch (Exception e) {}
        }

        if (exists) {
            throw new Exception("Ya existe un usuario con ese ID");
        }

        data.getAdministradores().add(a);
        stop();
    }

    public Administrador readAdministrador(String id) throws Exception {
        Administrador result = data.getAdministradores().stream()
                .filter(i -> i.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (result != null) return result;
        else throw new Exception("Administrador con id " + id + " no existe");
    }

    public void updateAdministrador(Administrador a) throws Exception {
        Administrador result = this.readAdministrador(a.getId());
        data.getAdministradores().remove(result);
        data.getAdministradores().add(a);
        stop();
    }

    public void deleteAdministrador(String id) throws Exception {
        Administrador result = this.readAdministrador(id);
        data.getAdministradores().remove(result);
        stop();
    }

    public List<Administrador> searchAdministradores(String nombre) {
        return data.getAdministradores().stream()
                .filter(i -> i.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .sorted(Comparator.comparing(Administrador::getNombre))
                .collect(Collectors.toList());
    }

    public List<Administrador> getAdministradores() {
        return data.getAdministradores();
    }

// ====================== MÉTODOS DE AUTENTICACIÓN ======================

    public Usuario authenticate(String id, String clave) throws Exception {
        if (id == null || id.trim().isEmpty()) {
            throw new Exception("ID requerido");
        }
        if (clave == null || clave.trim().isEmpty()) {
            throw new Exception("Clave requerida");
        }

        try {
            Medico medico = readMedico(id);
            if (medico.getClave() != null && medico.getClave().equals(clave)) {
                return medico;
            }
        } catch (Exception e) {
        }

        try {
            Farmaceuta farmaceuta = readFarmaceuta(id);
            if (farmaceuta.getClave() != null && farmaceuta.getClave().equals(clave)) {
                return farmaceuta;
            }
        } catch (Exception e) {
        }

        try {
            Administrador admin = readAdministrador(id);
            if (admin.getClave() != null && admin.getClave().equals(clave)) {
                return admin;
            }
        } catch (Exception e) {
        }

        throw new Exception("Usuario o clave incorrectos");
    }





    public void cambiarClave(String id, String claveActual, String claveNueva) throws Exception {
        if (id == null || id.trim().isEmpty()) {
            throw new Exception("ID requerido");
        }
        if (claveActual == null || claveActual.trim().isEmpty()) {
            throw new Exception("Contraseña actual requerida");
        }
        if (claveNueva == null || claveNueva.trim().isEmpty()) {
            throw new Exception("Nueva contraseña requerida");
        }

        Usuario usuario = authenticate(id, claveActual);

        if (usuario instanceof Medico) {
            Medico medico = (Medico) usuario;
            medico.setClave(claveNueva);
            updateMedico(medico);
        } else if (usuario instanceof Farmaceuta) {
            Farmaceuta farmaceuta = (Farmaceuta) usuario;
            farmaceuta.setClave(claveNueva);
            updateFarmaceuta(farmaceuta);
        } else if (usuario instanceof Administrador) {
            Administrador admin = (Administrador) usuario;
            admin.setClave(claveNueva);
            updateAdministrador(admin);
        } else {
            throw new Exception("Tipo de usuario no valido para cambio de contraseña");
        }
    }
}