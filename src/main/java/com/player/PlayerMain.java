package com.player;

import com.player.controller.VideoController;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@ComponentScan
public class PlayerMain {

    private static ApplicationContext applicationContext;

    public static void main(String ... args ){
        applicationContext =
                new AnnotationConfigApplicationContext(PlayerMain.class);

        for (String beanName : applicationContext.getBeanDefinitionNames()) {
            if(beanName.equals("videoController")){
                VideoController videoController = (VideoController) applicationContext.getBean(beanName);
                videoController.index();
            }
        }

    }

}
