package com.example.demo;

import com.example.demo.entity.AuthUser;
import com.example.demo.entity.Role;
import com.example.demo.entity.Status;
import com.example.demo.enums.Gender;
import com.example.demo.repository.ActivateCodesRepository;
import com.example.demo.repository.AuthUserRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.StatusRepository;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashSet;
import java.util.List;
import java.time.LocalDate;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@EnableScheduling
@EnableAsync
@EnableCaching
@SpringBootApplication
@RequiredArgsConstructor
public class DemoApplication {

	private final ActivateCodesRepository activateCodesRepository;
	private final RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	public static void stop(){
		System.out.println("Application Failed");
	}
	@Bean
	public CommandLineRunner runner1(){
		return args -> {
			try {
			Role customer = Role.builder()
					.name("CUSTOMER")
					.build();
			roleRepository.save(customer);

			Role seller = Role.builder()
					.name("SELLER")
					.build();
			roleRepository.save(seller);

			Role admin = Role.builder()
					.name("ADMIN")
					.build();
			roleRepository.save(admin);

			Role superAdmin = Role.builder()
					.name("SUPER_ADMIN")
					.build();
			roleRepository.save(superAdmin);
			}catch (Exception ignore){}
		};
	}
	@Bean
	public CommandLineRunner runner(StatusRepository statusRepository,
									AuthUserRepository authUserRepository){
		return args -> {

			//status creating process
                try {
					Status status = Status.builder().name("ORDER IS PREPARING").build();
					statusRepository.save(status);
					HashSet<Role> roles = new HashSet<>(roleRepository.findAll());
					AuthUser adminUser = AuthUser.builder()
							.email("admin123@mail.com")
							.active(true)
							.birthdate(LocalDate.of(
									2000, 5, 6
							))
							.gender(Gender.MALE)
							.firstName("Admin")
							.lastName("Root")
							.phone("+998999067760")
							.roles(roles)
							.build();
					authUserRepository.save(adminUser);
				}catch (Exception ignore){}
		};
	}
	@Scheduled(fixedDelay = 1, initialDelay = 1,timeUnit = TimeUnit.DAYS)
	public void dailyCycle(){
		try {
			activateCodesRepository.deleteOldCodes();
			log.info("deleted old activation codes");
		}catch (Exception e){
			String message = "Error processing the while deleting old activation codes method";
			log.error(message, e);
			throw new RuntimeException(message, e);
		}
	}
	@Bean
	public SecurityScheme createAPIKeyScheme() {
		return new SecurityScheme().type(SecurityScheme.Type.HTTP)
				.bearerFormat("JWT")
				.scheme("bearer");
	}

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI().addSecurityItem(new SecurityRequirement()
						.addList("Bearer Authentication"))
				.components(new Components().addSecuritySchemes(
						"Bearer Authentication", createAPIKeyScheme()))
				.info(new Info().title("My REST API")
						.description("Some custom description of API.")
						.version("1.0").contact(new Contact().name("Soliyev Boburjon")
								.email("http://localhost:8080").url("soliyevboburjon95@@gmail.com"))
						.license(new License().name("License of API")
								.url("API license URL")));
	}


	@Bean
	public GroupedOpenApi admin() {
		return GroupedOpenApi.builder()
				.group("admin")
				.pathsToMatch("/**")
				.build();
	}
	@Bean
	public GroupedOpenApi seller() {
		return GroupedOpenApi.builder()
				.group("seller")
				.pathsToMatch("/api.auth/**",
						"/api.good/**",
						"/api.delivery/get/**",
						"/api.comment/**",
						"/api.color/**",
						"/api.basket/**",
						"/api.basket/**",
						"/api.favourites/**",
						"/api.basket/**",
						"/api.order/**",
						"/api.payment.type/get/**",
						"/api.promo-code/**",
						"/api.role/get/**",
						"/api.status/get/**",
						"/api.type/get/**")
				.build();
	}
	@Bean
	public GroupedOpenApi customer() {
		return GroupedOpenApi.builder()
				.group("customer")
				.pathsToMatch("/api.auth/**",
						"/api.good/get/**",
						"/api.delivery/get/**",
						"/api.comment/**",
						"/api.color/get/**",
						"/api.basket/**",
						"/api.favourites/**",
						"/api.order/save/**",
						"/api.order/get/**",
						"/api.payment.type/get/**",
						"/api.promo-code/get/**",
						"/api.role/get/**",
						"/api.status/get/**",
						"/api.type/get/**")
				.build();
	}
}

