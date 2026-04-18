package ma.fst.amine.service;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import ma.fst.amine.entities.BonLivraison;
import ma.fst.amine.entities.LigneBonLivraison;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PdfService {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
    private static final DecimalFormat MONEY_FORMAT = new DecimalFormat("#,##0.00");

    public ByteArrayInputStream genererBonLivraisonPdf(BonLivraison bon) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            Document document = new Document(PageSize.A4, 12, 12, 12, 12);
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();

            Font fontTitle = new Font(Font.HELVETICA, 15, Font.BOLD, Color.BLACK);
            Font fontBold = new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK);
            Font fontNormal = new Font(Font.HELVETICA, 9, Font.NORMAL, Color.BLACK);
            Font fontSmall = new Font(Font.HELVETICA, 8, Font.NORMAL, Color.BLACK);
            Font fontSmallBold = new Font(Font.HELVETICA, 8, Font.BOLD, Color.BLACK);
            Font stampFont = new Font(Font.HELVETICA, 20, Font.BOLD, new Color(220, 0, 0));

            List<LigneBonLivraison> lignes = bon.getLignes();

            BigDecimal totalGeneral = BigDecimal.ZERO;
            int nombreArticles = 0;

            if (lignes != null) {
                for (LigneBonLivraison ligne : lignes) {
                    if (ligne != null && ligne.getTotal() != null) {
                        totalGeneral = totalGeneral.add(ligne.getTotal());
                    }
                    if (ligne != null && ligne.getQuantite() != null && ligne.getQuantite() > 0) {
                        nombreArticles++;
                    }
                }
            }

            BigDecimal totalBon = bon.getTotal() != null ? bon.getTotal() : totalGeneral;
            BigDecimal montantPaye = bon.getMontantPaye() != null ? bon.getMontantPaye() : BigDecimal.ZERO;
            BigDecimal resteAPayer = bon.getResteAPayer() != null ? bon.getResteAPayer() : totalBon.subtract(montantPaye);

            if (resteAPayer.compareTo(BigDecimal.ZERO) < 0) {
                resteAPayer = BigDecimal.ZERO;
            }

            BigDecimal soldeClient = BigDecimal.ZERO;
            if (bon.getClient() != null && bon.getClient().getSolde() != null) {
                soldeClient = bon.getClient().getSolde();
            }

            // =======================
            // 1) Logo + date impression
            // =======================
            PdfPTable topTable = new PdfPTable(2);
            topTable.setWidthPercentage(100);
            topTable.setWidths(new float[]{68f, 32f});
            topTable.setSpacingAfter(4f);

            PdfPCell logoCell = new PdfPCell();
            logoCell.setBorder(Rectangle.NO_BORDER);
            logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            logoCell.setPadding(0f);

            try {
                ClassPathResource resource = new ClassPathResource("static/css/images/logo-inox-salam.png");
                InputStream is = resource.getInputStream();
                byte[] imageBytes = is.readAllBytes();

                Image logo = Image.getInstance(imageBytes);
                logo.scaleToFit(240, 95);
                logo.setAlignment(Image.ALIGN_LEFT);
                logoCell.addElement(logo);
            } catch (Exception e) {
                Paragraph fallback = new Paragraph("INOX SALAM", new Font(Font.HELVETICA, 18, Font.BOLD, Color.BLACK));
                fallback.setAlignment(Element.ALIGN_LEFT);
                logoCell.addElement(fallback);
            }

            PdfPCell printCell = new PdfPCell();
            printCell.setBorder(Rectangle.NO_BORDER);
            printCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            printCell.setVerticalAlignment(Element.ALIGN_TOP);
            printCell.setPaddingTop(10f);

            String dateImpression = LocalDateTime.now().format(DATE_FORMAT);
            Paragraph printDate = new Paragraph("Imprimé Le : " + dateImpression, fontSmall);
            printDate.setAlignment(Element.ALIGN_RIGHT);
            printCell.addElement(printDate);

            topTable.addCell(logoCell);
            topTable.addCell(printCell);

            document.add(topTable);

            // =======================
            // 2) Référence document
            // =======================
            Paragraph refDoc = new Paragraph(
                    bon.getNumero() != null ? bon.getNumero() : "BL-0001",
                    new Font(Font.HELVETICA, 12, Font.BOLD, Color.BLACK)
            );
            refDoc.setSpacingAfter(8f);
            refDoc.setIndentationLeft(25f);
            document.add(refDoc);

            // =======================
            // 3) Titre + client
            // =======================
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new float[]{42f, 58f});
            headerTable.setSpacingAfter(8f);

            PdfPCell leftCell = new PdfPCell();
            leftCell.setBorder(Rectangle.NO_BORDER);
            leftCell.setPadding(0f);

            Paragraph title = new Paragraph("Bon de Livraison Client", fontTitle);
            title.setSpacingAfter(6f);
            leftCell.addElement(title);

            String numero = bon.getNumero() != null ? bon.getNumero() : "";
            String date = bon.getDateCreation() != null ? bon.getDateCreation().format(DATE_FORMAT) : "";
            String heure = bon.getDateCreation() != null ? bon.getDateCreation().format(TIME_FORMAT) : "";

            PdfPTable infoTable = new PdfPTable(3);
            infoTable.setWidthPercentage(100);
            infoTable.setWidths(new float[]{32f, 40f, 15f});

            infoTable.addCell(noBorderCell("N° : " + numero, fontBold, Element.ALIGN_LEFT));
            infoTable.addCell(noBorderCell("Date : " + date, fontBold, Element.ALIGN_LEFT));
            infoTable.addCell(noBorderCell(heure, fontBold, Element.ALIGN_LEFT));

            leftCell.addElement(infoTable);

            PdfPCell rightCell = new PdfPCell();
            rightCell.setBorder(Rectangle.BOX);
            rightCell.setBorderWidth(1f);
            rightCell.setPadding(8f);
            rightCell.setMinimumHeight(60f);

            String nomClient = bon.getClient() != null && bon.getClient().getNom() != null
                    ? bon.getClient().getNom().toUpperCase() : "";
            String telephone = bon.getClient() != null && bon.getClient().getTelephone() != null
                    ? bon.getClient().getTelephone() : "";
            String adresse = bon.getClient() != null && bon.getClient().getAdresse() != null
                    ? bon.getClient().getAdresse().toUpperCase() : "";

            Paragraph pNom = new Paragraph(nomClient, fontBold);
            pNom.setSpacingAfter(4f);

            Paragraph pTel = new Paragraph(telephone, fontNormal);
            pTel.setSpacingAfter(3f);

            Paragraph pAdr = new Paragraph(adresse, fontNormal);

            rightCell.addElement(pNom);
            rightCell.addElement(pTel);
            rightCell.addElement(pAdr);

            headerTable.addCell(leftCell);
            headerTable.addCell(rightCell);

            document.add(headerTable);

            // =======================
            // 4) Grand tableau (5 colonnes)
            // =======================
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{20f, 40f, 10f, 12f, 18f});

            table.addCell(headerCell("Référence", fontSmallBold));
            table.addCell(headerCell("Désignation", fontSmallBold));
            table.addCell(headerCell("Qte", fontSmallBold));
            table.addCell(headerCell("P.U", fontSmallBold));
            table.addCell(headerCell("Total", fontSmallBold));

            int lignesRemplies = 0;

            if (lignes != null) {
                for (LigneBonLivraison ligne : lignes) {
                    if (ligne == null || ligne.getArticle() == null) {
                        continue;
                    }

                    String code = ligne.getArticle().getCode() != null ? ligne.getArticle().getCode() : "";
                    String designation = ligne.getArticle().getDesignation() != null ? ligne.getArticle().getDesignation() : "";
                    String qte = ligne.getQuantite() != null ? String.valueOf(ligne.getQuantite()) : "";
                    String pu = formatMoney(ligne.getPrixUnitaire());
                    String total = formatMoney(ligne.getTotal());

                    table.addCell(bodyCell(code, fontSmall, Element.ALIGN_LEFT, 18f));
                    table.addCell(bodyCell(designation, fontSmall, Element.ALIGN_LEFT, 18f));
                    table.addCell(bodyCell(qte, fontSmall, Element.ALIGN_RIGHT, 18f));
                    table.addCell(bodyCell(pu, fontSmall, Element.ALIGN_RIGHT, 18f));
                    table.addCell(bodyCell(total, fontSmall, Element.ALIGN_RIGHT, 18f));

                    lignesRemplies++;
                }
            }

            int totalLignesVisuelles = 14;
            int lignesVides = Math.max(totalLignesVisuelles - lignesRemplies, 0);

            for (int i = 0; i < lignesVides; i++) {
                table.addCell(emptyCell(22f));
                table.addCell(emptyCell(22f));
                table.addCell(emptyCell(22f));
                table.addCell(emptyCell(22f));
                table.addCell(emptyCell(22f));
            }

            document.add(table);

            // =======================
            // 5) Tampon NON PAYE dynamique
            // =======================
            boolean nonPaye = resteAPayer.compareTo(BigDecimal.ZERO) > 0;
            if (nonPaye) {
                PdfContentByte canvas = writer.getDirectContentUnder();
                ColumnText.showTextAligned(
                        canvas,
                        Element.ALIGN_CENTER,
                        new Phrase("Non PAYE", stampFont),
                        170,
                        225,
                        12
                );
            }

            document.add(new Paragraph(" "));

            // =======================
            // 6) Bas de page
            // =======================
            PdfPTable bottomTable = new PdfPTable(2);
            bottomTable.setWidthPercentage(100);
            bottomTable.setWidths(new float[]{58f, 42f});

            PdfPCell leftBottom = new PdfPCell();
            leftBottom.setBorder(Rectangle.NO_BORDER);
            leftBottom.setPadding(0f);

            leftBottom.addElement(new Paragraph("Opérateur : SAR", fontSmall));
            leftBottom.addElement(new Paragraph("Nombre d'article : " + nombreArticles, fontSmall));

            PdfPCell rightBottom = new PdfPCell();
            rightBottom.setBorder(Rectangle.NO_BORDER);
            rightBottom.setPadding(0f);

            PdfPTable totalTable = new PdfPTable(2);
            totalTable.setWidthPercentage(100);
            totalTable.setWidths(new float[]{52f, 48f});

            totalTable.addCell(simpleBoxCell("Remise", fontSmallBold));
            totalTable.addCell(simpleBoxCell("0.00", fontSmall));

            totalTable.addCell(simpleBoxCell("Net à payer", fontSmallBold));
            totalTable.addCell(simpleBoxCell(formatMoney(totalBon), fontSmallBold));

            totalTable.addCell(simpleBoxCell("Montant payé", fontSmallBold));
            totalTable.addCell(simpleBoxCell(formatMoney(montantPaye), fontSmall));

            totalTable.addCell(simpleBoxCell("Reste", fontSmallBold));
            totalTable.addCell(simpleBoxCell(formatMoney(resteAPayer), fontSmallBold));

            totalTable.addCell(simpleBoxCell("Solde", fontSmallBold));
            totalTable.addCell(simpleBoxCell(formatMoney(soldeClient), fontSmall));

            rightBottom.addElement(totalTable);

            bottomTable.addCell(leftBottom);
            bottomTable.addCell(rightBottom);

            document.add(bottomTable);

            document.close();

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du PDF", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private PdfPCell headerCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5f);
        cell.setBackgroundColor(new Color(230, 230, 230));
        cell.setBorderWidth(0.8f);
        return cell;
    }

    private PdfPCell bodyCell(String text, Font font, int align, float height) {
        PdfPCell cell = new PdfPCell(new Phrase(text != null ? text : "", font));
        cell.setHorizontalAlignment(align);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(4f);
        cell.setFixedHeight(height);
        cell.setBorderWidth(0.8f);
        return cell;
    }

    private PdfPCell emptyCell(float height) {
        PdfPCell cell = new PdfPCell(new Phrase(""));
        cell.setFixedHeight(height);
        cell.setBorderWidth(0.8f);
        return cell;
    }

    private PdfPCell noBorderCell(String text, Font font, int align) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(align);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(2f);
        return cell;
    }

    private PdfPCell simpleBoxCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5f);
        cell.setBorderWidth(0.8f);
        return cell;
    }

    private String formatMoney(BigDecimal value) {
        if (value == null) {
            return "0.00";
        }
        return MONEY_FORMAT.format(value);
    }
}