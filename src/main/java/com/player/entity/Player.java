package com.player.entity;

import com.player.service.ConsumerService;
import com.player.service.FrameService;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
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
    private FrameService frameService;
    private VideoProxy proxy;

    //////////////////////////////////////////////////////////////////////////
    public Player( VideoFileWrapper videoFileWrapper, ConsumerService consumerService,
                   FrameService frameService ) throws IOException {

        this.videoFileWrapper = videoFileWrapper;
        this.consumerService = consumerService;
        this.frameService = frameService;

        if(fileIsValid(videoFileWrapper)){
            media = new Media(videoFileWrapper.getVideoFile().toURI().toURL().toExternalForm());
            mediaPlayer = new MediaPlayer(media);
            setListeners();
        }else{
            throw new IOException("Media file is not valid.");
        }
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
    public void play(GridPane gridPane){
        proxy = new VideoProxyImpl(gridPane, getMediaPlayer());
        proxy.play();
    }

    //////////////////////////////////////////////////////////////////////////
    private boolean fileIsValid( VideoFileWrapper videoFileWrapper ){
        // TODO: Implement
        return true;
    }

    //////////////////////////////////////////////////////////////////////////
    private void setListeners(){
        mediaPlayer.setOnEndOfMedia(()->{
            mediaPlayer.stop();
        });

        mediaPlayer.statusProperty().addListener((status,oldVal, newVal)->{
            currentStatus = newVal;

            if(Objects.nonNull(proxy)){
                proxy.fire(oldVal, newVal);
            }

            if(Objects.nonNull(oldVal) && Objects.nonNull(newVal)) {
                consumerService.fire(oldVal.toString(), newVal.toString());
            }
        });

        mediaPlayer.setOnStopped(()->{
            mediaPlayer.setStartTime(Duration.ZERO);
        });

        mediaPlayer.currentTimeProperty().addListener((durs)->{
            if(!isPaused()) {
                frameService.addPlaybackPoint(mediaPlayer.getCurrentTime().toMillis());
            }
        });
    }
}
