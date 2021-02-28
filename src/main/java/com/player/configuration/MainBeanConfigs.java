package com.player.configuration;

import com.player.service.*;
import com.player.ui.MainScreen;
import com.player.utils.ApplicationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Component
public class MainBeanConfigs {

    //////////////////////////////////////////////////////////////////////////
    @Bean("mainScreen")
    public MainScreen getMainScreen(){ return new MainScreen(); }

    //////////////////////////////////////////////////////////////////////////
    @Bean("appProperties")
    public ApplicationProperties getApplicationProperties(){ return new ApplicationProperties(); }

    //////////////////////////////////////////////////////////////////////////
    @Bean("playerService")
    public ConsumerService getPlayerService(){ return new PlayerServiceImpl(); }

    //////////////////////////////////////////////////////////////////////////
    @Bean("producerService")
    public ProducerService getProducerService(){ return new ProducerServiceImpl(); }

    //////////////////////////////////////////////////////////////////////////
    @Bean("frameService")
    public FrameService getFrameService(){ return new FrameServiceImpl(); }

}
