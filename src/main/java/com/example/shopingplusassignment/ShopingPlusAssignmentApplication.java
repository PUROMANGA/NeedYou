package com.example.shopingplusassignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication

public class ShopingPlusAssignmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopingPlusAssignmentApplication.class, args);
	}

}
