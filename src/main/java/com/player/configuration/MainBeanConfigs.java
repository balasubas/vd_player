package com.player.configuration;

import com.player.entity.SampleEntity;
import com.player.ui.MainScreen;
import com.player.utils.ApplicationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Component
public class MainBeanConfigs {

    //////////////////////////////////////////////////////////////////////////
    @Bean
    public SampleEntity getSampleEntity(){
        return new SampleEntity();
    }

    //////////////////////////////////////////////////////////////////////////
    @Bean("mainScreen")
    public MainScreen getMainScreen(){ return new MainScreen(); }

    //////////////////////////////////////////////////////////////////////////
    @Bean("appProperties")
    public ApplicationProperties getApplicationProperties(){ return new ApplicationProperties(); }

}
