package hospital.presentation.Paciente;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import hospital.Application;
import hospital.logic.Medico;
import hospital.logic.Paciente;
import hospital.logic.Service;
import com.itextpdf.layout.Document;

import java.io.File;
import java.time.format.DateTimeFormatter;
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
            model.setCurrent(Service.instance().readPaciente(e.getId()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

    public void filter(String criterio) {
        try {
            model.setFiltered(Service.instance().searchPaciente(criterio));
        } catch (Exception e) {
            System.err.println("Error filtrando pacientes: " + e.getMessage());
        }
    }


    public void generarReporte() throws Exception {
        String pdfPath = "reporte_Paciente.pdf";

        PdfWriter writer = new PdfWriter(pdfPath);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);
        document.setMargins(20, 25, 20, 25);


        // Titulo
        Paragraph titulo = new Paragraph("Reporte de Pacientes")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(16)
                .setBold()
                .setMarginBottom(20);
        document.add(titulo);

        // Crear la tabla
        Table table = new Table(UnitValue.createPercentArray(new float[]{2, 4, 4, 4})); // Proporciones
        table.setWidth(UnitValue.createPercentValue(100));

        // Encabezados con estilo
        String[] headers = {"ID", "Nombre", "Fecha de nacimiento", "Teléfono"};
        for (String h : headers) {
            Cell headerCell = new Cell().add(new Paragraph(h).setBold())
                    .setBackgroundColor(new DeviceRgb(230, 230, 230)) // gris claro
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBorder(new SolidBorder(ColorConstants.BLACK, 1));
            table.addHeaderCell(headerCell);
        }

        // Llenar tabla
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        List<Paciente> pacientes = model.getList();

        for (Paciente paciente : pacientes) {
            table.addCell(new Cell().add(new Paragraph(String.valueOf(paciente.getId())))
                    .setTextAlignment(TextAlignment.CENTER));
            table.addCell(new Cell().add(new Paragraph(paciente.getNombre())));
            table.addCell(new Cell().add(new Paragraph(
                            paciente.getFechaNacimiento().format(formatter)))
                    .setTextAlignment(TextAlignment.CENTER));
            table.addCell(new Cell().add(new Paragraph(paciente.getNumeroTelefono()))
                    .setTextAlignment(TextAlignment.CENTER));
        }
        // Añadir tabla al doc.
        document.add(table);
        document.close();

        // Confirmación
        System.out.println("Reporte de pacientes PDF generado en: " + new File(pdfPath).getAbsolutePath());
    }
}

