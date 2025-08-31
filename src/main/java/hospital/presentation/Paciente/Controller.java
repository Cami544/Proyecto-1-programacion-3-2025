package hospital.presentation.Paciente;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import hospital.Application;
import hospital.logic.Medico;
import hospital.logic.Paciente;
import hospital.logic.Service;
import com.itextpdf.layout.Document;

import java.io.File;
import java.util.List;

public class Controller {
    View view;
    Model model;

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
    }


    public void search(String id) throws Exception {
        try {
            Paciente paciente = Service.instance().readPaciente(id);
            model.setCurrent(paciente);
        } catch (Exception ex) {
            Paciente newPaciente = new Paciente();
            newPaciente.setId(id);
            model.setCurrent(newPaciente);
            throw ex;
        }
    }


    public void save(Paciente paciente) throws Exception {
        try {
            Paciente existing = Service.instance().readPaciente(paciente.getId());
            Service.instance().updatePaciente(paciente);
        } catch (Exception e) {
            Service.instance().createPaciente(paciente);
        }

        model.setCurrent(new Paciente());
        model.setList(Service.instance().findAllPacientes());
        model.setFiltered(Service.instance().findAllPacientes());
    }

    public void edit(int row){
        Paciente e = model.getList().get(row);
        try {
       //   model.setMode(Application.MODE_EDIT);
            model.setCurrent(Service.instance().readPaciente(String.valueOf(e)));
        } catch (Exception ex) {}
    }

    public void delete() throws Exception {
        if (model.getCurrent().getId() != null && !model.getCurrent().getId().trim().isEmpty()) {
            Service.instance().deletePaciente(model.getCurrent().getId());
            model.setCurrent(new Paciente());
            model.setList(Service.instance().findAllPacientes());
            model.setFiltered(Service.instance().findAllPacientes());
        } else {
            throw new Exception("Seleccione un paciente para eliminar");
        }
    }


    public void clear() {
      //  model.setMode(Application.MODE_CREATE);
        model.setCurrent(new Paciente());
    }
    /*
    public void generarReporte() throws Exception {
        String pdfPath = "reporte_Paciente.pdf";

        PdfWriter writer = new PdfWriter(pdfPath);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);
        document.setMargins(20, 25, 20, 25);
        //PdfFont font= PdfFontFactory.createFont();
        document.add(new Paragraph("Reporte de Pacientes"));

        // Crear la tabla
        float[] columnWidths = {100,100,200,200,200}; // Ancho de columnas
        Table table = new Table(columnWidths);
        table.addCell("ID");
        table.addCell("Nombre");
        table.addCell("Telefono");
        table.addCell("Email");
        table.addCell("Descuento");

        List<Paciente> pacientes = model.getList();
        // Llena la tabla con cajeros
        for (Paciente paciente :pacientes) {
            table.addCell(pacientes.getFirst().getId());
            table.addCell(pacientes.getFirst().getNombre());

        }
        document.add(table);  // Añade la tabla al documento
        document.close(); // Cerrar

        // Imprimir ruta donde se guardó
        System.out.println("Reporte de cajeros PDF generado en: " + new File(pdfPath).getAbsolutePath());
    }
*/
}

