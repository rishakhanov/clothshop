package com.example.clothshop;

import com.example.clothshop.entity.Image;
import com.example.clothshop.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootApplication
public class ClothshopApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(ClothshopApplication.class, args);
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {

    }
}
