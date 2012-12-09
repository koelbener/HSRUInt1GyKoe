package application.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import application.core.Texts;

import com.google.common.base.Stopwatch;
import com.google.common.io.Resources;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import domain.Customer;
import domain.Loan;

public class OverdueNoticeGenerator {

    private static final int PARAGRAPH_SPACING_LARGE = 70;
    private static final int PARAGRAPH_SPACING = 20;
    private static Font bold = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    private static Font normal = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
    private final Logger logger = LoggerFactory.getLogger(OverdueNoticeGenerator.class);

    public void generate(File selectedFile, List<Loan> overdueLoans) throws ReportException {

        Stopwatch sw = new Stopwatch();
        sw.start();

        Document document = new Document();
        try {
            String outfile = selectedFile.getCanonicalPath();
            logger.info("Starting to generate report to file \"{}\".", outfile);
            PdfWriter.getInstance(document, new FileOutputStream(outfile));
            document.open();
            addContent(document, overdueLoans);
        } catch (Exception e) {
            throw new ReportException(e);
        } finally {
            if (document != null) {
                document.close();
            }
        }

        sw.stop();
        long ms = sw.elapsedMillis();
        logger.info("Finished report. Generated {} notices in {} ms.", overdueLoans.size(), ms);

    }

    private void addContent(Document document, List<Loan> overdueLoans) throws DocumentException, MalformedURLException, IOException {

        for (Loan loan : overdueLoans) {

            Customer customer = loan.getCustomer();

            // customer address

            Paragraph paragraph = new Paragraph("", normal);
            paragraph.setIndentationLeft(300);
            // paragraph.setSpacingBefore(400);
            paragraph.add(customer.getFullName());
            paragraph.add("\n");
            paragraph.add(customer.getStreet());
            paragraph.add("\n");
            paragraph.add(customer.getZip() + " " + customer.getCity());
            document.add(paragraph);

            // library address

            StringBuilder sb = new StringBuilder();
            sb.append("Swinging Library");
            sb.append("\n");
            sb.append("Oberseestrasse 10");
            sb.append("\n");
            sb.append("8640 Rapperswil");
            addParagraph(document, PARAGRAPH_SPACING_LARGE, sb.toString(), normal);

            // title and content

            addParagraph(document, PARAGRAPH_SPACING_LARGE, Texts.get("report.notice.content.title"), bold);

            addParagraph(document, PARAGRAPH_SPACING, Texts.get("report.notice.content.intro"), normal);

            paragraph = new Paragraph("", normal);
            paragraph.setSpacingBefore(PARAGRAPH_SPACING);
            createTable(paragraph, loan);
            document.add(paragraph);

            addParagraph(document, PARAGRAPH_SPACING, Texts.get("report.notice.content.request"), normal);

            addParagraph(document, PARAGRAPH_SPACING_LARGE, Texts.get("report.notice.content.greetings"), normal);

            Image image = Image.getInstance(Resources.getResource("signature-olga.png").toExternalForm());
            document.add(image);

            addParagraph(document, 0, "Olga Ordentlich", normal);

            document.newPage();
        }

    }

    private void addParagraph(Document document, int spacing, String text, Font font) throws DocumentException {
        Paragraph paragraph = new Paragraph(text, font);
        paragraph.setSpacingBefore(spacing);
        document.add(paragraph);

    }

    private void createTable(Paragraph paragraph, Loan loan2) throws DocumentException {
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);

        createCell(table, Texts.get("report.notice.content.table.title"), bold);
        createCell(table, Texts.get("report.notice.content.table.author"), bold);
        createCell(table, Texts.get("report.notice.content.table.due"), bold);

        DateFormat f = SimpleDateFormat.getDateInstance();
        createCell(table, loan2.getCopy().getTitle().getName(), normal);
        createCell(table, loan2.getCopy().getTitle().getAuthor(), normal);
        createCell(table, f.format(loan2.getDueDate().getTime()), normal);

        paragraph.add(table);

    }

    private void createCell(PdfPTable table, String text, Font font) {
        PdfPCell c1 = new PdfPCell(new Phrase(text, font));
        c1.setBorderColor(BaseColor.GRAY);
        c1.setPadding(6);
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c1);
    }

    public class ReportException extends Exception {
        public ReportException(Exception e) {
            super(e);
        }

        private static final long serialVersionUID = 1L;

    }

}
