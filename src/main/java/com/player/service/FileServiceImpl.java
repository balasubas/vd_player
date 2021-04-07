package com.player.service;

import com.player.entity.PlayList;
import com.player.entity.PlayListItem;
import com.player.entity.VideoFileWrapper;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FileServiceImpl implements FileService {

    //////////////////////////////  DECLARATIONS  /////////////////////////////

    private final String DEFAULT_PLAYLIST_NAME = "auto_save.json";
    // TODO: This is not importing.
    //private Gson gson;

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void savePlaylist(List<VideoFileWrapper> videoFiles) {

    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public List<VideoFileWrapper> loadPlaylist(String fileName) {
        return null;
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public boolean deletePlayList(String fileName) {
        return false;
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void saveDefaultPlaylist(List<File> files) {

    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public VideoFileWrapper loadDefaultPlaylist() {
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
