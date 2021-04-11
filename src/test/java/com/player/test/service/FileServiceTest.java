package com.player.test.service;

import com.player.entity.VideoFileWrapper;
import com.player.service.FileService;
import com.player.service.FileServiceImpl;
import com.player.service.Preloader;
import com.player.service.PreloaderServiceImpl;
import com.player.test.util.PropHelper;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FileServiceTest {

    //////////////////////////////  DECLARATIONS  /////////////////////////////

    private static FileService fileService;
    private static PropHelper propHelper;
    private static List<VideoFileWrapper> wrappers;
    private static Preloader preloader;

    //////////////////////////////////////////////////////////////////////////
    @BeforeClass
    public static void setUp(){
        preloader = new PreloaderServiceImpl();
        fileService = new FileServiceImpl();
        propHelper = new PropHelper();

        List<File> files = loadVidFiles();
        Assert.assertNotNull(files);
        Assert.assertFalse(files.isEmpty());

        wrappers = parseToVidWrapper(files);
        Assert.assertNotNull(wrappers);
        Assert.assertFalse(wrappers.isEmpty());

        if(Files.notExists(Paths.get(propHelper.getTempFileDirectory()))){
            Paths.get(propHelper.getTempFileDirectory()).toFile().mkdir();
        }else if(!Paths.get(propHelper.getTempFileDirectory()).toFile().isDirectory()){
            Paths.get(propHelper.getTempFileDirectory()).toFile().delete();
            Paths.get(propHelper.getTempFileDirectory()).toFile().mkdir();
        }

    }

    //////////////////////////////////////////////////////////////////////////
    @AfterClass
    public static void tearDown(){
        try {
            FileUtils.deleteDirectory(Paths.get(propHelper.getTempFileDirectory()).toFile());
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    //////////////////////////////////////////////////////////////////////////
    @Test
    public void parsePlaylistToJsonTest(){
        final String stdName = "play_one.json";
        fileService.savePlaylist(wrappers,stdName,propHelper.getTempFileDirectory());
        try {
            List<File> fileList =
                    Files.list(Paths.get(propHelper.getTempFileDirectory()))
                            .map(Path::toFile)
                            .collect(Collectors.toList());

            Assert.assertFalse(fileList.isEmpty());

            Optional<String> found = fileList.stream()
                                             .map((file)->{
                                                 return file.getName();})
                                             .filter((fileName)->{
                                                return fileName.equals(stdName);
                                             }).findFirst();
            Assert.assertTrue(found.isPresent());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void loadPlayListTest(){
        //TODO: Implement
    }
    //////////////////////////////////////////////////////////////////////////
    private  static List<VideoFileWrapper> parseToVidWrapper(List<File> files){
        return files.stream()
                .map((file)->{
                    preloader.load(file);
                    return new VideoFileWrapper(null, file, preloader.getMediaPlayer());
                }).collect(Collectors.toList());
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
