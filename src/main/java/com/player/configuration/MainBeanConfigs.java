package com.player.configuration;

import com.player.entity.SampleEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Component
public class MainBeanConfigs {

//    @Bean
//    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
//        return args -> {
//
//            System.out.println("Let's inspect the beans provided by Spring Boot:");
//
//            String[] beanNames = ctx.getBeanDefinitionNames();
//            Arrays.sort(beanNames);
//            for (String beanName : beanNames) {
//                System.out.println(beanName);
//            }
//
//        };
//    }

    @Bean
    public SampleEntity getSampleEntity(){
        return new SampleEntity();
    }

}
