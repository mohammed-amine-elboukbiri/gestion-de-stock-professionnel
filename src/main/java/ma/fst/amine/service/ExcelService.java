package ma.fst.amine.service;

import ma.fst.amine.entities.Article;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class ExcelService {

    public ByteArrayInputStream exportArticles(List<Article> articles) {
        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Articles");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Code");
            header.createCell(1).setCellValue("Désignation");
            header.createCell(2).setCellValue("Catégorie");
            header.createCell(3).setCellValue("Matière");
            header.createCell(4).setCellValue("Epaisseur");
            header.createCell(5).setCellValue("Stock");
            header.createCell(6).setCellValue("Seuil Alerte");
            header.createCell(7).setCellValue("Prix Achat");
            header.createCell(8).setCellValue("Prix Vente");

            int rowIdx = 1;
            for (Article article : articles) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(article.getCode());
                row.createCell(1).setCellValue(article.getDesignation());
                row.createCell(2).setCellValue(article.getCategorie());
                row.createCell(3).setCellValue(article.getMatiere());
                row.createCell(4).setCellValue(article.getEpaisseur());
                row.createCell(5).setCellValue(article.getStock());
                row.createCell(6).setCellValue(article.getSeuilAlerte());
                row.createCell(7).setCellValue(article.getPrixAchat().doubleValue());
                row.createCell(8).setCellValue(article.getPrixVente().doubleValue());
            }

            for (int i = 0; i < 9; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception e) {
            throw new RuntimeException("Erreur export Excel", e);
        }
    }
}