package com.challengers;

import com.challengers.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class ChallengersApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChallengersApplication.class, args);
	}

}
