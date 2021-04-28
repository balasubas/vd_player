package com.player.entity;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogSpecification {

    //////////////////////////////  DECLARATIONS  /////////////////////////////

    private String loggerName;
    private String logFileName;
    private String directory;
    private Level logLevelMin;
    private Level logLevelMax;
    private boolean isFileLog;

    //////////////////////////////////////////////////////////////////////////
    public LogSpecification(String logFileName, String directory, Level logLevelMin, Level logLevelMax,
                     boolean isFileLog, String loggerName){
        String pattern = "yyyyMMdd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String dateString = simpleDateFormat.format(new Date());

        this.logFileName = StringUtils.join(logFileName,"-",dateString,".log");
        this.directory = directory;
        this.logLevelMin = logLevelMin;
        this.logLevelMax = logLevelMax;
        this.isFileLog = isFileLog;
        this.loggerName = loggerName;
    }

    //////////////////////////////////////////////////////////////////////////
    public String getLogFileName() {
        return logFileName;
    }

    //////////////////////////////////////////////////////////////////////////
    public String getDirectory() {
        return directory;
    }

    //////////////////////////////////////////////////////////////////////////
    public Level getLogLevelMin() {
        return logLevelMin;
    }

    //////////////////////////////////////////////////////////////////////////
    public Level getLogLevelMax() {
        return logLevelMax;
    }

    //////////////////////////////////////////////////////////////////////////
    public boolean isFileLog() {
        return isFileLog;
    }

    //////////////////////////////////////////////////////////////////////////
    public String getLoggerName() {
        return loggerName;
    }
}
