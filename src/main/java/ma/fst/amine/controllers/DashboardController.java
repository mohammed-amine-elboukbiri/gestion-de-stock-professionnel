package ma.fst.amine.controllers;

import ma.fst.amine.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("stats", dashboardService.getStats());
        model.addAttribute("articlesCritiques", dashboardService.getArticlesCritiques());
        model.addAttribute("topValeurStock", dashboardService.getTopValeurStock());
        return "index";
    }
}