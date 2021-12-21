package com.kpparekh.InvestmentPortfolio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class InvestmentPortfolioApplication {

	public static void main(String[] args) {
		SpringApplication.run(InvestmentPortfolioApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
//				registry.addMapping("/investments").allowedOrigins("http://localhost:3000");
//				registry.addMapping("/investment/{quote}").allowedOrigins("http://localhost:3000");
//				registry.addMapping("/price/{quote}").allowedOrigins("http://localhost:3000");
//				registry.addMapping("/addinvestment").allowedOrigins("http://localhost:3000");
//				registry.addMapping("/updateinvestment").allowedOrigins("http://localhost:3000");
//				registry.addMapping("/delete/{quote}").allowedOrigins("http://localhost:3000");
//				registry.addMapping("/delete/").allowedOrigins("http://localhost:3000");
				registry.addMapping("/**").allowedMethods("PUT", "GET", "DELETE", "OPTIONS", "PATCH", "POST");
			}
		};
	}

}
