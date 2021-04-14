package com.player.test.service;

import com.player.entity.PlayList;
import com.player.entity.VideoFileWrapper;
import com.player.service.FileService;
import com.player.service.FileServiceImpl;
import com.player.service.Preloader;
import com.player.service.PreloaderServiceImpl;
import com.player.test.util.PropHelper;
import org.apache.commons.io.FileUtils;
import org.junit.*;
import org.junit.BeforeClass;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FileServiceTest {

    //////////////////////////////  DECLARATIONS  /////////////////////////////

    private static FileService fileService;
    private static PropHelper propHelper;
    private static List<VideoFileWrapper> wrappers;
    private static Preloader preloader;
    private static final String stdName = "play_one.json";

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

        fileService.savePlaylist(wrappers,stdName,propHelper.getTempFileDirectory());
        fileService.saveDefaultPlaylist(wrappers,propHelper.getTempFileDirectory());
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
        try {
            List<File> fileList =
                    Files.list(Paths.get(propHelper.getTempFileDirectory()))
                            .map(Path::toFile)
                            .collect(Collectors.toList());

            Assert.assertFalse(fileList.isEmpty());

            Optional<String> found = fileList.stream()
                                             .map(File::getName)
                                             .filter((fileName)-> fileName.equals(stdName))
                                             .findFirst();

            Assert.assertTrue(found.isPresent());

            found = fileList.stream()
                    .map(File::getName)
                    .filter((fileName)-> fileName.equals("auto_save.json"))
                    .findFirst();

            Assert.assertTrue(found.isPresent());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //////////////////////////////////////////////////////////////////////////

    @Test
    public void loadPlayListTest(){
        List<File> files =
                fileService.loadPlaylist(stdName,propHelper.getTempFileDirectory());

        Assert.assertNotNull(files);
        Assert.assertFalse(files.isEmpty());

        File defaultFile = fileService.loadDefaultPlaylist(propHelper.getTempFileDirectory());

        Assert.assertNotNull(defaultFile);

        PlayList defaultPlaylist =
                fileService.parseToPlayList(Collections.singletonList(defaultFile),"default");

        defaultPlaylist.getPlayListItems().forEach((item)->{
            Assert.assertTrue(item.getLocation().exists());
            Assert.assertTrue(item.getLocation().isFile());
        });

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
