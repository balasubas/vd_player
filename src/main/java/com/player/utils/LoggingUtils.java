package com.player.utils;

import com.player.entity.LogSpecification;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.*;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

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
    // TODO: Add this to the MainPlayerWindow class
    public static void reapOldLogs(int thresholdDays, String logDirectory){
        File logDir = new File(logDirectory);
        if(logDir.exists() && logDir.isDirectory()){
            Arrays.stream(Objects.requireNonNull(logDir.listFiles()))
                  .filter((file)-> file.isFile() && file.getName().endsWith(".log")).filter((file)->{
                      FileTime creationTime = null;
                      try {
                         creationTime = (FileTime) Files.getAttribute(file.toPath(), "creationTime");
                      } catch (IOException e) {
                         e.printStackTrace();
                      }

                      assert Objects.nonNull(creationTime);
                      Calendar calendar = Calendar.getInstance();
                      calendar.setTimeInMillis(creationTime.toMillis());
                      Date actualFileDate = calendar.getTime();
                      Date today = new Date();
                      Date thresholdDate = DateUtils.addDays(today,(thresholdDays * -1));
                      int result = Long.compare(actualFileDate.getTime(), thresholdDate.getTime());

                      return result < 1;
                  }).forEach(FileUtils::deleteQuietly);

        }
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
