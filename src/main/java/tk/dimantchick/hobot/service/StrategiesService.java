package tk.dimantchick.hobot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import tk.dimantchick.hobot.strategies.BuyStrategy;
import tk.dimantchick.hobot.strategies.SellStrategy;
import tk.dimantchick.hobot.strategies.Strategy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Сервис, отвечающий за стратегии.
 * Дает возможность получать стратегии по именам, имени по стратегии,
 * список имен стратегий для создания/редактирования позиций.
 */
@Service
public class StrategiesService {

    private Logger logger = LoggerFactory.getLogger(StrategiesService.class);

    private final ApplicationContext context;
    private Map<String, BuyStrategy> buyStrategies;
    private Map<String, SellStrategy> sellStrategies;
    private Map<Strategy, String> strategyNames;

    public StrategiesService(ApplicationContext context) {
        this.context = context;
        loadStrategies();
    }

    private void loadStrategies() {
        logger.debug("fill buyStrategies");
        buyStrategies = context.getBeansOfType(BuyStrategy.class);
        logger.debug("fill sellStrategies");
        sellStrategies = context.getBeansOfType(SellStrategy.class);
        logger.debug("fill strategyNames");
        strategyNames = new HashMap<>();
        for (Map.Entry<String, BuyStrategy> entry : buyStrategies.entrySet()) {
            strategyNames.put(entry.getValue(), entry.getKey());
        }
        for (Map.Entry<String, SellStrategy> entry : sellStrategies.entrySet()) {
            strategyNames.put(entry.getValue(), entry.getKey());
        }
    }

    public Map<String, BuyStrategy> getBuyStrategies() {
        return buyStrategies;
    }

    public Map<String, SellStrategy> getSellStrategies() {
        return sellStrategies;
    }

    public String getStrategyName(Strategy strategy) {
        String[] names = context.getBeanNamesForType(strategy.getClass());
        if (names.length > 0) {
            return names[0];
        }
        return null;
    }

    public Strategy getStrategyByName(String name) {
        if (buyStrategies.containsKey(name)) {
            return buyStrategies.get(name);
        } else if (sellStrategies.containsKey(name)) {
            return sellStrategies.get(name);
        }
        return null;
    }

    public <T extends Strategy> Set<String> getStrategiesNames(Set<T> strategies) {
        Set<String> names = new HashSet<>();
        for (T strategy : strategies) {
            names.add(getStrategyName(strategy));
        }
        return names;
    }

}
