package com.player.service;

import com.google.gson.Gson;
import com.player.entity.PlayList;
import com.player.entity.PlayListItem;
import com.player.entity.VideoFileWrapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FileServiceImpl implements FileService {

    //////////////////////////////  DECLARATIONS  /////////////////////////////

    private final String DEFAULT_PLAYLIST_NAME = "auto_save.json";
    private Gson gson;

    //////////////////////////////////////////////////////////////////////////
    public FileServiceImpl(){
        gson = new Gson();
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void savePlaylist(List<VideoFileWrapper> videoFiles, String location) {
        File file = new File(location);
        if(file.exists()){
            try {
                FileUtils.deleteQuietly(file);
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<File> files = videoFiles.stream()
                                     .map(VideoFileWrapper::getVideoFile)
                                     .collect(Collectors.toList());

        PlayList playList = parseToPlayList(files, file.getName());
        String jsonString = gson.toJson(playList);
        try {
            FileUtils.writeStringToFile(file,jsonString, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public List<VideoFileWrapper> loadPlaylist(String fileName, String location) {
        return null;
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public boolean deletePlayList(String fileName, String location) {
        return false;
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void saveDefaultPlaylist(List<File> files, String location) {

    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public VideoFileWrapper loadDefaultPlaylist(String location) {
        return null;
    }

    //////////////////////////////////////////////////////////////////////////
    private PlayList parseToPlayList(List<File> files, String playListName){
        PlayList playList = new PlayList( (playListName == null ) ? DEFAULT_PLAYLIST_NAME : playListName );

        Set<PlayListItem> playListItemSet = files.stream()
                                                 .map(PlayListItem::new)
                                                 .collect(Collectors.toSet());

        playList.setPlayListItems(playListItemSet);
        return playList;
    }

}
