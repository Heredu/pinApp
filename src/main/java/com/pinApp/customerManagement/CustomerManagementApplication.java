package com.pinApp.customerManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CustomerManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerManagementApplication.class, args);
		System.out.println("RabbitMQ Host: " + System.getenv("RABBITMQ_HOST"));
	}

}
