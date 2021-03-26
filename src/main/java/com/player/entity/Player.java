package com.player.entity;

import com.player.service.ConsumerService;
import com.player.service.FrameService;
import javafx.scene.layout.GridPane;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;

public class Player {

    //////////////////////////////  DECLARATIONS  /////////////////////////////

    private VideoFileWrapper videoFileWrapper;
    private MediaPlayer.Status currentStatus = MediaPlayer.Status.UNKNOWN;
    private ConsumerService consumerService;
    private FrameService frameService;
    private VideoProxy proxy;
    private String imagePath;
    private GridPane gridPane;

    //////////////////////////////////////////////////////////////////////////
    public Player( VideoFileWrapper videoFileWrapper, ConsumerService consumerService,
                   FrameService frameService, String imagePath, GridPane gridPane ) throws IOException {

        this.videoFileWrapper = videoFileWrapper;
        this.consumerService = consumerService;
        this.frameService = frameService;
        this.imagePath = imagePath;
        this.gridPane = gridPane;

        if(fileIsValid(videoFileWrapper)){
            proxy = new VideoProxyImpl(this.gridPane);
            setListeners();
            proxy.setMediaPlayer(videoFileWrapper.getMediaPlayer());
        }else{
            throw new IOException("Media file is not valid.");
        }
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
        return videoFileWrapper.getMediaPlayer();
    }

    //////////////////////////////////////////////////////////////////////////
    public void setPlayerRate(double rate){
        double currentRate = videoFileWrapper.getMediaPlayer().getRate() + rate;
        videoFileWrapper.getMediaPlayer().setRate(currentRate);
    }

    //////////////////////////////////////////////////////////////////////////
    public Duration getPauseTime(){
        return videoFileWrapper.getMediaPlayer().getCurrentTime();
    }

    //////////////////////////////////////////////////////////////////////////
    public void play(){
        proxy.play();
    }

    //////////////////////////////////////////////////////////////////////////
    public void stop(){
        proxy.stop();
    }

    //////////////////////////////////////////////////////////////////////////
    public void pause(){
        if(videoFileWrapper.getMediaPlayer().getStatus().equals(MediaPlayer.Status.PLAYING)){
            proxy.pause();
        }
    }

    //////////////////////////////////////////////////////////////////////////
    public void resume(){
        if(videoFileWrapper.getMediaPlayer().getStatus().equals(MediaPlayer.Status.PAUSED) ){
            videoFileWrapper.getMediaPlayer().seek(getPauseTime());
            proxy.play();
        }
    }

    //////////////////////////////////////////////////////////////////////////
    private boolean fileIsValid( VideoFileWrapper videoFileWrapper ){
        // TODO: Implement
        return true;
    }

    //////////////////////////////////////////////////////////////////////////
    private void setListeners(){

        videoFileWrapper.getMediaPlayer().setOnEndOfMedia(()->{
            videoFileWrapper.getMediaPlayer().stop();
        });

        videoFileWrapper.getMediaPlayer().statusProperty().addListener((status,oldVal, newVal)->{
            currentStatus = newVal;

            if(Objects.nonNull(proxy)){
                proxy.fire(oldVal, newVal);
            }

            if(Objects.nonNull(oldVal) && Objects.nonNull(newVal)) {
                consumerService.fire(oldVal.toString(), newVal.toString());
            }
        });

        videoFileWrapper.getMediaPlayer().setOnStopped(()->{
            videoFileWrapper.getMediaPlayer().setStartTime(Duration.ZERO);
        });

        videoFileWrapper.getMediaPlayer().currentTimeProperty().addListener((durs)->{
            if(!isPaused()) {
                frameService.addPlaybackPoint(videoFileWrapper.getMediaPlayer().getCurrentTime().toMillis());
            }
        });

    }
}
