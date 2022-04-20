package com.sodved.itesteg.app;

import com.sodved.itesteg.app.service.ApplicationTableService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableScheduling
@EnableAsync(proxyTargetClass=true)
@SpringBootApplication(scanBasePackages={"com.sodved.itesteg"})
public class ItestExampleApplication implements CommandLineRunner {

    @Autowired
    private ApplicationTableService applicationTableService;

    public static void main(String[] args) {
        log.info("STARTING THE APPLICATION");
        SpringApplication.run(ItestExampleApplication.class, args);
        log.info("APPLICATION FINISHED");
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Application running...");
        applicationTableService.listTables();
    }

}
