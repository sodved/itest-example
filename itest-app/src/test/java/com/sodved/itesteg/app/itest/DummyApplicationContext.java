package com.sodved.itesteg.app.itest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication(scanBasePackages={"com.sodved.itesteg"})
public class DummyApplicationContext {

    public static void main(String[] args) {
        log.info("Dummy Context (Not sure if this should ever run)");
    }

}
