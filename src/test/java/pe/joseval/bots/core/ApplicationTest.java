package pe.joseval.bots.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("pe.joseval.bots.core")
@SpringBootApplication
public class ApplicationTest {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(ApplicationTest.class, args);
	}

}
