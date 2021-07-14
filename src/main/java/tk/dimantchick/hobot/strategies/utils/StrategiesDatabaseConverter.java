package tk.dimantchick.hobot.strategies.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import tk.dimantchick.hobot.service.StrategiesService;
import tk.dimantchick.hobot.strategies.Strategy;

import javax.persistence.AttributeConverter;
import java.util.HashSet;
import java.util.Set;

@Component
public class StrategiesDatabaseConverter implements AttributeConverter<Set<String>, String> {

    private final ApplicationContext context;
    private StrategiesService strategiesService;

    public StrategiesDatabaseConverter(ApplicationContext context, StrategiesService strategiesService) {
        this.context = context;
        this.strategiesService = strategiesService;
    }

    @Override
    public String convertToDatabaseColumn(Set<String> strategies) {
        StringBuilder sb = new StringBuilder();
        for (String strategy : strategies) {
            sb.append(strategy);
            sb.append("&");
        }
        return sb.toString();
    }

    @Override
    public Set<String> convertToEntityAttribute(String s) {
        String[] parts = s.split("&");
        Set<String> strategySet = new HashSet<>();
        for (String part : parts) {
            Strategy strategy = strategiesService.getStrategyByName(part);
            if (strategy != null) {
                strategySet.add(part);
            }
        }
        return strategySet;
    }
}