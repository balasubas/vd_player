package com.player.configuration;

import com.player.service.*;
import com.player.ui.MainScreen;
import com.player.ui.ProgressWindow;
import com.player.utils.ApplicationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@EnableScheduling
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

    //////////////////////////////////////////////////////////////////////////
    @Bean("queueService")
    public QueueService getQueueService(){ return new QueueServiceImpl(); }

    //////////////////////////////////////////////////////////////////////////
    @Bean("preloadService")
    public Preloader getPreloaderService(){ return new PreloaderServiceImpl(); }

    //////////////////////////////////////////////////////////////////////////
    @Bean("progressWindow")
    public ProgressWindow getProgressWindow(){ return new ProgressWindow(); }
}
