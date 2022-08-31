package io.makai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class ForumApiApplication {

    @Value("${server.port}")
    private int port;

    public static void main(String[] args) {
        SpringApplication.run(ForumApiApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationStart() {
        System.out.printf("\nhttp://localhost:%d", this.port, "\n");
    }
}
