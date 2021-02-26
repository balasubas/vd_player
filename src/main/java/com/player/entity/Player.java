package com.player.entity;

import com.player.service.ConsumerService;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;

public class Player {

    //////////////////////////////  DECLARATIONS  /////////////////////////////

    private MediaPlayer mediaPlayer;
    private Media media;
    private VideoFileWrapper videoFileWrapper;
    private MediaPlayer.Status currentStatus = MediaPlayer.Status.UNKNOWN;
    private ConsumerService consumerService;

    //////////////////////////////////////////////////////////////////////////
    public Player( VideoFileWrapper videoFileWrapper ) throws IOException {
        this.videoFileWrapper = videoFileWrapper;

        if(fileIsValid(videoFileWrapper)){
            media = new Media(videoFileWrapper.getVideoFile().toURI().toURL().toExternalForm());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setOnEndOfMedia(()->{
                mediaPlayer.stop();
            });

            mediaPlayer.statusProperty().addListener((status,oldVal, newVal)->{
                currentStatus = newVal;
                if(Objects.nonNull(oldVal) && Objects.nonNull(newVal)) {
                    consumerService.fire(oldVal.toString(), newVal.toString());
                }
            });
            
        }else{
            throw new IOException("Media file is not valid.");
        }

    }

    //////////////////////////////////////////////////////////////////////////
    public void register( ConsumerService consumerService){
        this.consumerService = consumerService;
    }

    //////////////////////////////////////////////////////////////////////////
    public Media getMedia(){
        return media;
    }

    //////////////////////////////////////////////////////////////////////////
    public boolean isPlaying(){
        return currentStatus.equals(MediaPlayer.Status.READY) ||
                currentStatus.equals(MediaPlayer.Status.PLAYING);
    }

    //////////////////////////////////////////////////////////////////////////
    public boolean isPaused(){
        return currentStatus.equals(MediaPlayer.Status.PAUSED);
    }

    //////////////////////////////////////////////////////////////////////////
    public boolean isDonePlaying(){
        return currentStatus.equals(MediaPlayer.Status.STOPPED);
    }

    //////////////////////////////////////////////////////////////////////////
    public  MediaPlayer getMediaPlayer(){
        return mediaPlayer;
    }

    //////////////////////////////////////////////////////////////////////////
    public void setPlayerRate(double rate){
        double currentRate = mediaPlayer.getRate() + rate;
        mediaPlayer.setRate(currentRate);
    }

    //////////////////////////////////////////////////////////////////////////
    public Duration getPauseTime(){
        return mediaPlayer.getCurrentTime();
    }

    //////////////////////////////////////////////////////////////////////////
    private boolean fileIsValid( VideoFileWrapper videoFileWrapper ){
        // TODO: Implement
        return true;
    }
}
