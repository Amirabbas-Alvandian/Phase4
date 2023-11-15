package com.example.phase4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.time.LocalTime;

@SpringBootApplication
public class Phase4Application {

    public static void main(String[] args) {
        SpringApplication.run(Phase4Application.class, args);
        System.out.println("hi");
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();
        System.out.println(localTime);
    }


}
