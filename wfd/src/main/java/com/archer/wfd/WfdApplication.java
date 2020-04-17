package com.archer.wfd;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Hooks;

@Log4j2
@SpringBootApplication
public class WfdApplication {

    public static void main(String[] args) {
        Hooks.onOperatorDebug();
        SpringApplication.run(WfdApplication.class, args);
    }
}


