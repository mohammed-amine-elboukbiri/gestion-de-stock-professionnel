package ma.fst.amine.controllers;

import ma.fst.amine.entities.Article;
import ma.fst.amine.repositories.ArticleRepository;
import ma.fst.amine.service.ExcelService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/exports")
public class ExportController {

    private final ExcelService excelService;
    private final ArticleRepository articleRepository;

    public ExportController(ExcelService excelService, ArticleRepository articleRepository) {
        this.excelService = excelService;
        this.articleRepository = articleRepository;
    }

    @GetMapping("/articles/excel")
    public ResponseEntity<InputStreamResource> exportArticlesExcel() {

        List<Article> articles = articleRepository.findAll();

        System.out.println("========== EXPORT EXCEL ==========");
        System.out.println("Nombre d'articles exportés : " + articles.size());

        for (Article a : articles) {
            System.out.println("Article -> id=" + a.getId()
                    + ", code=" + a.getCode()
                    + ", designation=" + a.getDesignation());
        }

        InputStreamResource file = new InputStreamResource(
                excelService.exportArticles(articles)
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=articles_inox.xlsx")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(file);
    }
}