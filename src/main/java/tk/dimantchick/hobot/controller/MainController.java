package tk.dimantchick.hobot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.tinkoff.invest.openapi.model.rest.Currency;
import tk.dimantchick.hobot.api.Api;

import java.math.BigDecimal;

/**
 * Контроллер,отвечающий за информационные страниц.
 */
@Controller
@RequestMapping("/")
public class MainController {

    private final Api api;

    public MainController(Api api) {
        this.api = api;
    }

    @GetMapping("/")
    public String showHome(Model model) {
        return "redirect:dashboard";
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        return "dashboard";
    }

    @ModelAttribute("balance")
    public BigDecimal balance() {
        return api.getCurrency(Currency.USD).getBalance();
    }

}
