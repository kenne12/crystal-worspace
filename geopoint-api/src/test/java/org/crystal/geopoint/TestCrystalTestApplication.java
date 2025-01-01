package org.crystal.geopoint;

import org.springframework.boot.SpringApplication;

public class TestCrystalTestApplication {

    public static void main(String[] args) {
        SpringApplication.from(CrystalTestApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
