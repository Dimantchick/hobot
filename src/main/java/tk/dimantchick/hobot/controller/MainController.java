package tk.dimantchick.hobot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.tinkoff.invest.openapi.model.rest.Currency;
import tk.dimantchick.hobot.api.Api;
import tk.dimantchick.hobot.service.StrategiesService;

import java.math.BigDecimal;

@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    private Api api;

    @Autowired
    private StrategiesService strategiesService;

    @GetMapping("/")
    public String showHome(Model model) {
        return "redirect:dashboard";
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
//        model.addAttribute("balance", api.getCurrency(Currency.USD).getBalance());
//        model.addAttribute("buyStrategies", strategiesService.getBuyStrategies().keySet());
        return "dashboard";
    }

    @GetMapping("/hello")
    public String showHello(Model model) {
        return "hello";
    }

    @ModelAttribute("balance")
    public BigDecimal balance() {
        return api.getCurrency(Currency.USD).getBalance();
    }

}
