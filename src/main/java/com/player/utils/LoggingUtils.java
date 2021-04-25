package com.player.utils;

import com.player.entity.LogSpecification;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.*;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

import java.util.Objects;

// TODO: Add console appender method. See how to use multiple loggers
public class LoggingUtils {

    //////////////////////////////  DECLARATIONS  /////////////////////////////

    private static final String DEFAULT_FILE_LOGGER_NAME = "MainFileLogger";
    private static final String DEFAULT_CONSOLE_LOGGER_NAME = "ConsoleOut";

    //////////////////////////////////////////////////////////////////////////
    public static void initializeLogger(LogSpecification logSpecification){
        ConfigurationBuilder<BuiltConfiguration> loggingConfig =
                ConfigurationBuilderFactory.newConfigurationBuilder();

        AppenderComponentBuilder mainAppender =
                createFileAppender(logSpecification.getDirectory() + "/" + logSpecification.getLogFileName(),
                        logSpecification.getLoggerName(),
                        logSpecification.getLogLevelMin(),
                        logSpecification.getLogLevelMax(),
                        loggingConfig);

        loggingConfig.add(mainAppender);
        loggingConfig.setStatusLevel(logSpecification.getLogLevelMin());

        RootLoggerComponentBuilder rootLoggerMin = loggingConfig.newRootLogger(logSpecification.getLogLevelMin());
        rootLoggerMin.add(loggingConfig.newAppenderRef(logSpecification.getLoggerName()));
        loggingConfig.add(rootLoggerMin);

        Configurator.reconfigure(loggingConfig.build());
    }

    //////////////////////////////////////////////////////////////////////////
    // TODO: Depreacate this
    private static AppenderComponentBuilder createAppender(String logName,
                                                           ConfigurationBuilder<BuiltConfiguration> logConfig){
        AppenderComponentBuilder appenderComponentBuilder = null;
        LayoutComponentBuilder layoutBuilder = logConfig.newLayout("PatternLayout")
                .addAttribute("pattern", "%d %p %c [%t] %m%n");

        if(Objects.nonNull(logName)){
            appenderComponentBuilder = logConfig.newAppender(DEFAULT_FILE_LOGGER_NAME, "File");
            appenderComponentBuilder.addAttribute("fileName",logName);
        }else{
            appenderComponentBuilder = logConfig.newAppender(DEFAULT_CONSOLE_LOGGER_NAME, "Console");
        }

        appenderComponentBuilder.add(layoutBuilder);
        return appenderComponentBuilder;
    }

    //////////////////////////////////////////////////////////////////////////
    private static AppenderComponentBuilder createFileAppender(String logName, String fileAppenderName,
                                                               Level minLevel, Level maxLevel,
                                                               ConfigurationBuilder<BuiltConfiguration> logConfig){

        LayoutComponentBuilder layoutBuilder = logConfig.newLayout("PatternLayout")
                .addAttribute("pattern", "%d %p %c [%t] %m%n");

        AppenderComponentBuilder appenderComponentBuilder =
                logConfig.newAppender(fileAppenderName, "File");
        appenderComponentBuilder.addAttribute("fileName",logName);

        FilterComponentBuilder filterComponentBuilder =
                logConfig.newFilter("LevelRangeFilter",Filter.Result.ACCEPT, Filter.Result.DENY);
        filterComponentBuilder.addAttribute("minLevel", minLevel);
        filterComponentBuilder.addAttribute("maxLevel", maxLevel);
        appenderComponentBuilder.add(filterComponentBuilder);
        appenderComponentBuilder.add(layoutBuilder);

        return appenderComponentBuilder;
    }

}
