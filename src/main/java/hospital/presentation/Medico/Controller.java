package hospital.presentation.Medico;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import hospital.logic.Medico;
import hospital.logic.Paciente;
import hospital.logic.Service;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Controller {
    private View view;
    private Model model;

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);

        try {
            model.setList(Service.instance().findAllMedicos());
            model.setFiltered(Service.instance().findAllMedicos());
        } catch (Exception e) {
            System.err.println("Error cargando médicos iniciales: " + e.getMessage());
        }
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
            throw new Exception("Seleccione un medico para eliminar");
        }
    }

    public void clear() {
        model.setCurrent(new Medico());
    }

    public void filter(String criterio) {
        try {
            model.setFiltered(Service.instance().searchMedicos(criterio));
        } catch (Exception e) {
            System.err.println("Error filtrando medicos: " + e.getMessage());
        }
    }

    public void generarReporte() throws Exception {
        String pdfPath = "reporte_Médico.pdf";

        PdfWriter writer = new PdfWriter(pdfPath);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);
        document.setMargins(20, 25, 20, 25);


        // Titulo
        Paragraph titulo = new Paragraph("Reporte de médico")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(16)
                .setBold()
                .setMarginBottom(20);
        document.add(titulo);

        // Crear la tabla
        Table table = new Table(UnitValue.createPercentArray(new float[]{2, 4, 4})); // Proporciones
        table.setWidth(UnitValue.createPercentValue(100));

        // Encabezados con estilo
        String[] headers = {"ID", "Nombre", "Especialidad"};
        for (String h : headers) {
            Cell headerCell = new Cell().add(new Paragraph(h).setBold())
                    .setBackgroundColor(new DeviceRgb(230, 230, 230)) // gris claro
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBorder(new SolidBorder(ColorConstants.BLACK, 1));
            table.addHeaderCell(headerCell);
        }

        // Llenar tabla
        List<Medico> medicos = model.getList();

        for (Medico medico : medicos) {
            table.addCell(new Cell().add(new Paragraph(String.valueOf(medico.getId())))
                    .setTextAlignment(TextAlignment.CENTER));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(medico.getNombre())))
                    .setTextAlignment(TextAlignment.CENTER));
            table.addCell(new Cell().add(new Paragraph(medico.getEspecialidad()))
                    .setTextAlignment(TextAlignment.CENTER));
        }
        // Añadir tabla al doc.
        document.add(table);
        document.close();

        // Confirmación
        System.out.println("Reporte de médicos PDF generado en: " + new File(pdfPath).getAbsolutePath());
    }

}