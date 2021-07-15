package tk.dimantchick.hobot.controller;

import org.apache.commons.collections4.list.TreeList;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.tinkoff.invest.openapi.model.rest.Currency;
import ru.tinkoff.invest.openapi.model.rest.MarketInstrument;
import tk.dimantchick.hobot.api.Api;
import tk.dimantchick.hobot.domain.instrument.Instrument;
import tk.dimantchick.hobot.domain.instrument.InstrumentStatus;
import tk.dimantchick.hobot.domain.instrument.InstrumentsFilter;
import tk.dimantchick.hobot.service.InstrumentsService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Контроллер,отвечающий за страницы с инструментами.
 */
@Controller
@RequestMapping("/instruments")
public class InstrumentsController {

    private final InstrumentsService instrumentsService;

    private final Api api;

    public InstrumentsController(InstrumentsService instrumentsService, Api api) {
        this.instrumentsService = instrumentsService;
        this.api = api;
    }

    @GetMapping("")
    public String showAll(@ModelAttribute("filter")InstrumentsFilter filter, Model model/*, @RequestParam(name = "page", defaultValue = "0") int page*/) {
        final int onPage = 25;
        int pages = instrumentsService.countByFilter(filter);
        int maxPage = pages % onPage > 0 ? pages / onPage + 1 : pages / onPage;
        if (filter.getPage() > maxPage) {
            filter.setPage(maxPage);
        }
        if (filter.getPage() < 1) {
            filter.setPage(1);
        }
        Pageable pageable = PageRequest.of(filter.getPage() - 1, onPage, Sort.Direction.ASC, "ticker");
        List<Instrument> all = instrumentsService.getByFilter(filter, pageable);
        model.addAttribute("instruments", all);
        model.addAttribute("enabled", InstrumentStatus.ENABLED);
        model.addAttribute("disabled", InstrumentStatus.DISABLED);
        model.addAttribute("pages", maxPage);
        model.addAttribute("currencies", Currency.values());
        model.addAttribute("statuses", InstrumentStatus.values());
        return "instruments";
    }

    @GetMapping("/{ticker}/{status}")
    public String enable(@PathVariable("ticker") String ticker, @PathVariable("status") String status) {

        Optional<Instrument> instrument = instrumentsService.getByTicker(ticker);
        if (instrument.isPresent()) {
            switch (status) {
                case "ENABLE":
                    instrumentsService.enable(instrument.get());
                    break;
                case "DISABLE":
                    instrumentsService.disable(instrument.get());
            }
        }
        return "redirect:/instruments";
    }

    @GetMapping("/grab")
    public String enable() {
        List<MarketInstrument> stocks = api.getStocks();
        List<Instrument> instruments = new TreeList<>();
        for (MarketInstrument stock : stocks) {
            Instrument instrument = new Instrument();
            instrument.setTicker(stock.getTicker());
            instrument.setFigi(stock.getFigi());
            instrument.setCurrency(stock.getCurrency());
            instrument.setStatus(InstrumentStatus.DISABLED);
            instrument.setName(stock.getName());
            instruments.add(instrument);

        }
        instrumentsService.saveAll(instruments);
        return "redirect:/instruments";
    }

    @ModelAttribute("balance")
    public BigDecimal balance() {
        return api.getCurrency(Currency.USD).getBalance();
    }

}
