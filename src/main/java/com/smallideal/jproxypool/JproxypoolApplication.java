package com.smallideal.jproxypool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class JproxypoolApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(JproxypoolApplication.class, args);
        System.in.read();
    }

}
