package io.github.dafnipapado.careerstart_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CareerstartBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CareerstartBackendApplication.class, args);
	}

}
