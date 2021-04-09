package com.player.test.service;

import com.player.entity.VideoFileWrapper;
import com.player.service.FileService;
import com.player.service.FileServiceImpl;
import com.player.test.util.PropHelper;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FileServiceTest {

    //////////////////////////////  DECLARATIONS  /////////////////////////////

    private static FileService fileService;
    private static PropHelper propHelper;
    private static List<VideoFileWrapper> wrappers;

    //////////////////////////////////////////////////////////////////////////
    @BeforeClass
    public static void setUp(){
        fileService = new FileServiceImpl();
        propHelper = new PropHelper();
        List<File> files = loadVidFiles();
        Assert.assertNotNull(files);
        Assert.assertFalse(files.isEmpty());

//        wrappers = parseToVidWrapper(files);
//        Assert.assertNotNull(wrappers);
//        Assert.assertFalse(wrappers.isEmpty());
    }

    //////////////////////////////////////////////////////////////////////////
    @Test
    public void parsePlaylistToJsonTest(){

    }

    //////////////////////////////////////////////////////////////////////////
    private static List<VideoFileWrapper> parseToVidWrapper(List<File> files){
        //TODO: This is erroring out in an NPE.
        return files.stream()
                    .map((file)-> new VideoFileWrapper(null,file, null))
                    .collect(Collectors.toList());
    }

    //////////////////////////////////////////////////////////////////////////
    private static List<File> loadVidFiles(){
        List<File> files = null;
        try {
            files = Files.list(Paths.get(propHelper.getVideoFileDirectory()))
                         .map(Path::toFile)
                         .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return files;
    }

}
