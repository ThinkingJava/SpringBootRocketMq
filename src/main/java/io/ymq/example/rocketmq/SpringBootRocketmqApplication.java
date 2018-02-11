package io.ymq.example.rocketmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = {"io.ymq.example"})
public class SpringBootRocketmqApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootRocketmqApplication.class, args);
	}
}
