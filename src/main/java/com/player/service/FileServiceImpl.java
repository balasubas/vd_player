package com.player.service;

import com.google.gson.Gson;
import com.player.entity.PlayList;
import com.player.entity.PlayListItem;
import com.player.entity.VideoFileWrapper;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
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
    public void savePlaylist(List<VideoFileWrapper> videoFiles,String name, String location) {
        save( videoFiles, name, location);
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public List<File> loadPlaylist(String fileName, String location) {
        return load(fileName, location);
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public boolean deletePlayList(String fileName, String location) {
        return false;
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void saveDefaultPlaylist(List<VideoFileWrapper> videoFiles, String location) {
        save( videoFiles, DEFAULT_PLAYLIST_NAME, location);
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public File loadDefaultPlaylist(String location) {
        List<File> files = load(DEFAULT_PLAYLIST_NAME, location);

        if(Objects.nonNull(files)){
            if(!files.isEmpty()){
                return files.get(0);
            }
        }

        return null;
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public String getDefaultPlayListName() {
        return DEFAULT_PLAYLIST_NAME;
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public PlayList parseToPlayList(List<File> files, String playListName){
        PlayList playList = new PlayList( (playListName == null ) ? DEFAULT_PLAYLIST_NAME : playListName );

        Set<PlayListItem> playListItemSet = files.stream()
                                                 .map(PlayListItem::new)
                                                 .collect(Collectors.toSet());

        playList.setPlayListItems(playListItemSet);
        return playList;
    }

    //////////////////////////////////////////////////////////////////////////
    private void save(List<VideoFileWrapper> videoFiles,String name, String location){
        File file = new File(location + "/" + name);
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
    private List<File> load(String fileName, String location){
        List<File> videoFiles = null;
        File playlistFile = Paths.get(location, fileName).toFile();

        if(playlistFile.exists() && playlistFile.isFile()){
            try {
                String jsonString = FileUtils.readLines(playlistFile,"UTF-8")
                        .stream()
                        .reduce("",(s1,s2)->{ return s1 + "" + s2;});

                PlayList playList = gson.fromJson(jsonString,PlayList.class);
                videoFiles = playList.getPlayListItems()
                        .stream()
                        .map(PlayListItem::getLocation)
                        .collect(Collectors.toList());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return videoFiles;
    }

}
