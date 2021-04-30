package com.player.test.misc;

import com.player.test.util.PropHelper;
import com.player.utils.LoggingUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;


public class LogReaperTest {

    //////////////////////////////  DECLARATIONS  /////////////////////////////

    private static PropHelper propHelper;

    //////////////////////////////////////////////////////////////////////////
    @BeforeClass
    public static void setUp(){
        propHelper = new PropHelper();
        buildFiles();
    }

    //////////////////////////////////////////////////////////////////////////
    @AfterClass
    public static void tearDown(){
        try {
            Files.list(Paths.get(propHelper.getTestLogDirectory()))
                    .map(Path::toFile)
                    .forEach((file)->{
                        try {
                            FileUtils.forceDelete(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //////////////////////////////////////////////////////////////////////////
    @Test
    public void reapOldFileTest(){
        LoggingUtils.reapOldLogs(propHelper.getLogMaxDays(), propHelper.getTestLogDirectory());
        File expiredFile = new File(propHelper.getTestLogDirectory() + "/expired_file.log");
        Assert.assertFalse(expiredFile.exists());
    }

    //////////////////////////////////////////////////////////////////////////
    private static void buildFiles(){
        File fileNotExpired = new File(propHelper.getTestLogDirectory() + "/good_file.log");
        File expiredFile = new File(propHelper.getTestLogDirectory() + "/expired_file.log");
        try {
            fileNotExpired.createNewFile();
            expiredFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Date futureDate = DateUtils.addDays(new Date(),propHelper.getLogMaxDays() * -1);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String formatted = StringUtils.join(format.format(futureDate),"1200");
        String command = "touch -t " + formatted + " " + expiredFile.getAbsolutePath();

        Runtime run  = Runtime.getRuntime();
        try {
            Process proc = run.exec(command);
            proc.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

}
