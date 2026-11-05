package ar.edu.um.isa.oncall.service.oncall;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * El reloj se inyecta para que las reglas que dependen del tiempo no llamen a Instant.now()
 * por su cuenta (AGENTS.md). Un test puede reemplazarlo por un Clock.fixed(...).
 */
@Configuration
public class RelojConfiguration {

    @Bean
    public Clock reloj() {
        return Clock.systemUTC();
    }
}
