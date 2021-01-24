package com.player.controller;

import com.player.configuration.MainBeanConfigs;
import com.player.entity.SampleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  This is just a regular controller not for REST. This is going to be used for a standalone service.
 *
 * **/

@Controller
public class VideoController {

    @Autowired
    private SampleEntity sampleEntity;

    //@RequestMapping("/")
    public String index() {
        sampleEntity.printHello();
        return "Greetings from Spring Boot!";
    }

}
