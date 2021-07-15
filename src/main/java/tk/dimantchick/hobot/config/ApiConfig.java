package tk.dimantchick.hobot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.tinkoff.invest.openapi.OpenApi;
import ru.tinkoff.invest.openapi.okhttp.OkHttpOpenApi;

/**
 * Конфигурация апи.
 */
@Configuration
@ComponentScan("tk.dimantchick.hobot")
@PropertySource("file:./config.properties")
@PropertySource("file:./application.properties")
public class ApiConfig {

    @Value("${tk.dimantchick.hobot.token}")
    private String token;

    @Bean
    public OpenApi openApi() {
        return new OkHttpOpenApi(token, false);
    }
}
