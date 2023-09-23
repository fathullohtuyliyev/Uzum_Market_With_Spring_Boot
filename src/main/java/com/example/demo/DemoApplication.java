package com.example.demo;

import com.example.demo.entity.Status;
import com.example.demo.repository.ActivateCodesRepository;
import com.example.demo.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.TimeUnit;

@Slf4j
@EnableScheduling
@EnableAsync
@EnableCaching
@SpringBootApplication
@RequiredArgsConstructor
public class DemoApplication {

	private final ActivateCodesRepository activateCodesRepository;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	@Bean
	public MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
		return new MongoTransactionManager(dbFactory);
	}
	@Bean
	public CommandLineRunner runner(StatusRepository statusRepository){
		return args -> {
			statusRepository.save(Status.builder().name("ORDER IS PREPARING").build());
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

