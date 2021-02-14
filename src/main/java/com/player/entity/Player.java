package com.player.entity;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.IOException;

public class Player {

    //////////////////////////////  DECLARATIONS  /////////////////////////////

    private MediaPlayer mediaPlayer;
    private Media media;
    private VideoFileWrapper videoFileWrapper;

    //////////////////////////////////////////////////////////////////////////
    public Player( VideoFileWrapper videoFileWrapper ) throws IOException {
        this.videoFileWrapper = videoFileWrapper;

        if(fileIsValid(videoFileWrapper)){
            media = new Media(videoFileWrapper.getVideoFile().toURI().toURL().toExternalForm());
            mediaPlayer = new MediaPlayer(media);
        }else{
            throw new IOException("Media file is not valid.");
        }
    }

    //////////////////////////////////////////////////////////////////////////
    public Media getMedia(){
        return media;
    }

    //////////////////////////////////////////////////////////////////////////
    public  MediaPlayer getMediaPlayer(){
        return mediaPlayer;
    }

    //////////////////////////////////////////////////////////////////////////
    private boolean fileIsValid( VideoFileWrapper videoFileWrapper ){
        // TODO: Implement
        return true;
    }


}
