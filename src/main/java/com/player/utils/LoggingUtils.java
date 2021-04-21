package com.player.utils;

import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

import java.util.Objects;

public class LoggingUtils {

    //////////////////////////////  DECLARATIONS  /////////////////////////////


    //////////////////////////////////////////////////////////////////////////
    // TODO: Continue implementing
    public static void initializeLogger(String logName, String directory, String outputType){
        ConfigurationBuilder<BuiltConfiguration> loggingConfig =
                                            ConfigurationBuilderFactory.newConfigurationBuilder();

        loggingConfig.add(createAppender(logName, loggingConfig));
    }

    //////////////////////////////////////////////////////////////////////////
    private static AppenderComponentBuilder createAppender(String logName,
                                                           ConfigurationBuilder<BuiltConfiguration> logConfig){
        AppenderComponentBuilder appenderComponentBuilder = null;
        if(Objects.nonNull(logName)){
            appenderComponentBuilder = logConfig.newAppender("log", "File");
            appenderComponentBuilder.addAttribute("fileName",logName);
        }else{
            appenderComponentBuilder = logConfig.newAppender("stdout", "Console");
        }

        return appenderComponentBuilder;
    }

}
