package com.player.test.misc;

import com.player.test.util.PropHelper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.util.Date;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;

// TODO: Continue implementing
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
    @Test
    public void reapOldFileTest(){
        System.out.println("Implement me");
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

        Date futureDate = DateUtils.addDays(new Date(),16);

        FileTime fileTime = FileTime.fromMillis(futureDate.getTime());

        try {
            Files.setAttribute(expiredFile.toPath(), "basic:creationTime", fileTime, NOFOLLOW_LINKS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
