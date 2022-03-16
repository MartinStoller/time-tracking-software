package de.example.haegertime;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.time.LocalTime;
import java.util.Collections;

@SpringBootApplication
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
public class HaegertimeApplication {

	public static void main(String[] args) {
		SpringApplication.run(HaegertimeApplication.class, args);
	}

	@Bean
	public Docket swaggerConfig(){
		return new Docket(DocumentationType.SWAGGER_2).select()
				//between select and build all the configurations are written:

				//the next line tell swagger to ignore the spring related endpoints, which we didnt create personally because all our controllers are behind an "/api"
				.paths(PathSelectors.ant("/api/**"))
				.apis(RequestHandlerSelectors.basePackage("de.example.haegertime")) //for me this line didnt change anything it seems

				.build()

				.ignoredParameterTypes(LocalTime.class) //the use of LocalTime caused some weird models to appear in the ui, so i excluded it here, which solved the problem

				.apiInfo(apiDetails());//include APIInfo below
	}

	private ApiInfo apiDetails(){
		return new ApiInfo(
				"Haegertime API",
				"API for tracking working hours of Employees and automatic billing export to customers",
				"2.0",
				"Free to use",
				new Contact("josalongmartin", "http://haeger-consulting.de", "josalongmartin@gmail.com"),
				"API License",
				"http://haeger-consulting2.de",
				Collections.emptyList()
		);
	}

}
