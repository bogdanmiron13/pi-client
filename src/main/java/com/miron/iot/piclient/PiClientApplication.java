package com.miron.iot.piclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableFeignClients
public class PiClientApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(PiClientApplication.class);

	public static void main(String[] args) {
		StaticBridge.ARGS = args;
		StaticBridge.APP_CONTEXT = SpringApplication.run(PiClientApplication.class, args);

		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				return;
			}
		}
	}

	@PostConstruct
	public void startFxBootstrap() {
		FxBootstrap.startup();
	}

}