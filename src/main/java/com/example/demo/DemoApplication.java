package com.example.demo;

import com.example.demo.entity.AuthUser;
import com.example.demo.entity.Role;
import com.example.demo.entity.Status;
import com.example.demo.enums.Gender;
import com.example.demo.repository.ActivateCodesRepository;
import com.example.demo.repository.AuthUserRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.concurrent.TimeUnit;

@Slf4j
@EnableScheduling
@EnableAsync
@EnableCaching
@SpringBootApplication
@RequiredArgsConstructor
public class DemoApplication {
	public static Integer statusId;
	public static Long customerId;
	public static Long sellerId;
	public static Long adminId;
	public static Long superAdminId;

	private final ActivateCodesRepository activateCodesRepository;
	private final RoleRepository roleRepository;
    private static ConfigurableApplicationContext context;
	public static void main(String[] args) {
		context = SpringApplication.run(DemoApplication.class, args);
	}
	public static void stop(){
		System.out.println("Application Failed");
        context.stop();
	}
	@Bean
	public MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
		return new MongoTransactionManager(dbFactory);
	}
	@Bean
	public CommandLineRunner runner(StatusRepository statusRepository,
									AuthUserRepository authUserRepository){
		return args -> {
				Role customer = Role.builder()
						.name("CUSTOMER")
						.build();
				try {
					customerId=roleRepository.save(customer).getId();
				}catch (Exception e){
					try {
						customerId=roleRepository.findByName(customer.getName())
								.orElseThrow(RuntimeException::new).getId();
					}catch (Exception ex){
						stop();
					}
				}
				Role seller = Role.builder()
						.name("SELLER")
						.build();
			    try {
                    sellerId=roleRepository.save(seller).getId();
		    	}catch (Exception e){
                    try {
						sellerId=roleRepository.findByName(seller.getName())
								.orElseThrow(RuntimeException::new).getId();
					}catch (Exception ex){
						stop();
					}
			    }
				Role admin = Role.builder()
						.name("ADMIN")
						.build();
		    	try {
					adminId=roleRepository.save(admin).getId();
			    }catch (Exception e){
                    try {
						adminId=roleRepository.findByName(admin.getName())
								.orElseThrow(RuntimeException::new).getId();
					}catch (Exception ex){
						stop();
					}
			    }
				Role superAdmin = Role.builder()
						.name("SUPER_ADMIN")
						.build();
			    try {
                    superAdminId=roleRepository.save(superAdmin).getId();
			    }catch (Exception e){
                    try {
						superAdminId=roleRepository.findByName(superAdmin.getName())
								.orElseThrow(RuntimeException::new).getId();
					}catch (Exception ex){
						stop();
					}
			    }
			//status creating process
			try {
				Status status = statusRepository.save
						(Status.builder().name("ORDER IS PREPARING").build());
				statusId = status.getId();
			}catch (Exception e){
				try {
					statusId = statusRepository.findByName("ORDER IS PREPARING")
							.orElseThrow(RuntimeException::new).getId();
				}catch (Exception ex){
					System.out.println("System error");
					stop();
				}
			}
			List<Long> idList = List.of(
					adminId,
					superAdminId,
					customerId
			);
			List<Role> roles = roleRepository.findAllById(idList);
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
					.roles(new HashSet<>(roles))
					.build();
			try {
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
}

