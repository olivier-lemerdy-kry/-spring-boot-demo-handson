package se.kry.springboot.demo.handson.infra;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfraConfiguration {

  @Bean
  Clock clock() {
    return Clock.systemUTC();
  }
}
