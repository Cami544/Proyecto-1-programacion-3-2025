package hospital.logic;


import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import hospital.data.Data;
import hospital.data.XmlPersister;

import java.util.Comparator;


    public class Service {   // Singleton
        private static Service theInstance;

        public static Service instance() {
            if (theInstance == null) theInstance = new Service();
            return theInstance;
        }

        private Data data;

        private Service() {
            try {
                data = XmlPersister.instance().load();
            } catch (Exception e) {
                data = new Data();
            }
        }

        public void stop() {
            try {
                XmlPersister.instance().store(data);
            } catch (Exception e) {
                System.out.println("Error guardando datos: " + e.getMessage());
            }
        }

        // ========================= PACIENTES ======================

        public void createPaciente(Paciente p) throws Exception {
            Paciente result = data.getPacientes().stream()
                    .filter(i -> i.getId().equals(p.getId()))
                    .findFirst()
                    .orElse(null);
            if (result == null) data.getPacientes().add(p);
            else throw new Exception("Paciente ya existe");
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
            }
            catch(Exception e){
                throw new Exception("Paciente no existe");
            }
        }

        public void deletePaciente(String id) throws Exception {
            Paciente result = this.readPaciente(id);
            data.getPacientes().remove(result);
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

        // ========================= MÉDICOS ========================

        public void createMedico(Medico m) throws Exception {
            Medico result = data.getMedicos().stream()
                    .filter(i -> i.getId().equals(m.getId()))
                    .findFirst()
                    .orElse(null);
            if (result == null) data.getMedicos().add(m);
            else throw new Exception("Médico ya existe");
        }


        public Medico readMedico(String id) throws Exception {
            Medico result = data.getMedicos().stream()
                    .filter(i -> i.getId().equals(id))
                    .findFirst()
                    .orElse(null);
            if (result != null) return result;
            else throw new Exception("Médico no existe");
        }


        public void updateMedico(Medico m) throws Exception {
            Medico result = this.readMedico(m.getId());
            data.getMedicos().remove(result);
            data.getMedicos().add(m);
        }

        public void deleteMedico(String id) throws Exception {
            Medico result = this.readMedico(id);
            data.getMedicos().remove(result);
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
            if (result == null) data.getFarmaceutas().add(f);
            else throw new Exception("Farmaceuta ya existe");
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
        }

        public void deleteFarmaceuta(String id) throws Exception {
            Farmaceuta result = this.readFarmaceuta(id);
            data.getFarmaceutas().remove(result);
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
            if (result == null) data.getMedicamentos().add(m);
            else throw new Exception("Medicamento ya existe");
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
        }

        public void deleteMedicamento (String codigo) throws Exception {
            Medicamento result = this.readMedicamento(codigo);
            data.getFarmaceutas().remove(result);
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

            readPaciente(receta.getPacienteId());      // Validar que el paciente exista
            for (DetalleReceta detalle : receta.getDetalles()) {  // Validar medicamentos
                readMedicamento(detalle.getMedicamentoCodigo());
            }
            Receta result = data.getRecetas().stream()   // Verificar si existe
                    .filter(i -> i.getId().equals(receta.getId()))
                    .findFirst()
                    .orElse(null);

            if (result == null) {
                // Guardarda
                data.getRecetas().add(receta);

                System.out.println("Receta guardada en XML: " + receta.getId() +
                        " para paciente: " + receta.getPacienteId() +
                        " fecha retiro: " + fechaRetiro);
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

        public void updateReceta(Receta r) throws Exception {
            Receta result = this.readReceta(r);
            data.getRecetas().remove(result);
            data.getRecetas().add(r);
        }

        public void deleteReceta(Receta r) throws Exception {
            Receta result = this.readReceta(r);
            data.getRecetas().remove(result);
        }

        public List<Receta> searchRecetasByPaciente(String id) {
            return data.getRecetas().stream()
                    .filter(r -> r.getPacienteId().equals(id))
                    .collect(Collectors.toList());
        }

        public List<Receta> getRecetas() {
            return data.getRecetas();
        }

    }
