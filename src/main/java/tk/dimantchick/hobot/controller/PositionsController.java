package tk.dimantchick.hobot.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.invest.openapi.model.rest.Currency;
import tk.dimantchick.hobot.api.Api;
import tk.dimantchick.hobot.domain.position.HobotPosition;
import tk.dimantchick.hobot.domain.position.PositionFilter;
import tk.dimantchick.hobot.domain.position.PositionStatus;
import tk.dimantchick.hobot.service.InstrumentsService;
import tk.dimantchick.hobot.service.PositionsService;
import tk.dimantchick.hobot.service.StrategiesService;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * Контроллер,отвечающий за работу с позициями.
 */
@Controller
@RequestMapping("/positions")
public class PositionsController {

    private final PositionsService positionsService;

    private final StrategiesService strategiesService;

    private final InstrumentsService instrumentsService;

    private final Api api;

    public PositionsController(PositionsService positionsService, StrategiesService strategiesService, InstrumentsService instrumentsService, Api api) {
        this.positionsService = positionsService;
        this.strategiesService = strategiesService;
        this.instrumentsService = instrumentsService;
        this.api = api;
    }

    @GetMapping("")
    public String showAll(@ModelAttribute("filter") PositionFilter filter, Model model) {
        long pages = positionsService.countByFilter(filter);
        long maxPage = pages % filter.getOnPage() > 0 ? pages / filter.getOnPage() + 1 : pages / filter.getOnPage();
        filter.fix((int) maxPage);
        // NEED sort by id for pages correct working with sort by ticker
        Sort sort = Sort.by(filter.getSortDirection(), filter.getSort()).and(Sort.by("id"));
        Pageable pageable = PageRequest.of(filter.getPage() - 1, filter.getOnPage(), sort);
        Iterable<HobotPosition> positions = positionsService.findByFilter(filter, pageable);
        model.addAttribute("positions", positions);
        model.addAttribute("statuses", PositionStatus.values());
        model.addAttribute("pages", maxPage);
        return "positions/showAll";
    }

    @GetMapping("/{id}")
    public String show(Model model, @PathVariable("id") Long id) {
        Optional<HobotPosition> position = positionsService.getByID(id);
        if (position.isPresent()) {
            model.addAttribute("position", position.get());
            model.addAttribute("strategiesService", strategiesService);
            return "positions/show";
        }
        return "redirect:/positions";
    }

    @GetMapping("/new")
    public String add(@ModelAttribute("position") HobotPosition position, Model model) {
        model.addAttribute("tickers", instrumentsService.getTickers());
        model.addAttribute("statuses", PositionStatus.values());
        model.addAttribute("buyStrats", strategiesService.getBuyStrategies().keySet());
        model.addAttribute("sellStrats", strategiesService.getSellStrategies().keySet());
        return "positions/new";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute("position") @Valid HobotPosition position, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("tickers", instrumentsService.getTickers());
            model.addAttribute("statuses", PositionStatus.values());
            model.addAttribute("buyStrats", strategiesService.getBuyStrategies().keySet());
            model.addAttribute("sellStrats", strategiesService.getSellStrategies().keySet());
            return "positions/new";
        }
        positionsService.newPosition(position);
        return "redirect:/positions";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id) {
        Optional<HobotPosition> position = positionsService.getByID(id);
        if (position.isPresent()) {
            model.addAttribute("position", position.get());
            model.addAttribute("tickers", instrumentsService.getTickers());
            model.addAttribute("statuses", PositionStatus.values());
            model.addAttribute("buyStrats", strategiesService.getBuyStrategies().keySet());
            model.addAttribute("sellStrats", strategiesService.getSellStrategies().keySet());
            return "positions/edit";
        }
        return "redirect:/positions";
    }

    @PostMapping("/edit")
    public String update(@ModelAttribute("position") @Valid HobotPosition position, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("tickers", instrumentsService.getTickers());
            model.addAttribute("statuses", PositionStatus.values());
            model.addAttribute("buyStrats", strategiesService.getBuyStrategies().keySet());
            model.addAttribute("sellStrats", strategiesService.getSellStrategies().keySet());
            return "positions/edit";
        }
        Optional<HobotPosition> fromDB = positionsService.getByID(position.getId());
        if (fromDB.isPresent()) {
            HobotPosition hobotPosition = fromDB.get();
            hobotPosition.setInstrument(position.getInstrument());
            hobotPosition.setStatus(position.getStatus());
            hobotPosition.setPriceToBuy(position.getPriceToBuy());
            hobotPosition.setPriceSL(position.getPriceSL());
            hobotPosition.setMaxPosition(position.getMaxPosition());
            hobotPosition.setBuyStrategy(position.getBuyStrategy());
            hobotPosition.setSellStrategy(position.getSellStrategy());
            hobotPosition.setRestart(position.isRestart());
            hobotPosition.setVirtual(position.isVirtual());
            positionsService.savePosition(hobotPosition);
            positionsService.updatePositions();
        }


        return "redirect:/positions/" + position.getId() + "/";
    }

    @GetMapping("/{id}/delete")
    public String delete(Model model, @PathVariable("id") Long id) {
        Optional<HobotPosition> position = positionsService.getByID(id);
        if (position.isPresent()) {
            positionsService.delete(position.get());
            return "redirect:/positions";
        }
        return "redirect:/positions";
    }


    @ModelAttribute("balance")
    public BigDecimal balance() {
        return api.getCurrency(Currency.USD).getBalance();
    }
}
