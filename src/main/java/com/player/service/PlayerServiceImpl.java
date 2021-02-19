package com.player.service;

import com.player.entity.Player;
import com.player.entity.VideoFileWrapper;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.IOException;


public class PlayerServiceImpl implements ConsumerService {

    //////////////////////////////  DECLARATIONS  /////////////////////////////

    private Player currentPlayer;

    //////////////////////////////////////////////////////////////////////////
    public PlayerServiceImpl(){

    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void consume(VideoFileWrapper file, Pane pane) {

        //TODO:
        //     1. Determine if there is something current
        try {
            currentPlayer = new Player(file);
            if(currentPlayer.getMediaPlayer() != null){
                MediaView mediaView = new MediaView(currentPlayer.getMediaPlayer());
                mediaView.setFitWidth(450);
                mediaView.setFitHeight(350);
                ((GridPane) pane).setAlignment(Pos.CENTER);
                ((GridPane) pane).add(mediaView,1,1);
                currentPlayer.getMediaPlayer().play();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void playFromQueue(ProducerService producerService, Pane pane) {
        while(!producerService.isEmpty()){
            consume(producerService.get(),pane);
        }
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void stop() {
        if(currentPlayer != null){
            if(currentPlayer.getMediaPlayer().getStatus().equals(MediaPlayer.Status.PLAYING)){
                currentPlayer.getMediaPlayer().stop();
            }
        }
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void pause() {
        if(currentPlayer != null){
            if(currentPlayer.getMediaPlayer().getStatus().equals(MediaPlayer.Status.PLAYING)){
                currentPlayer.getMediaPlayer().pause();
            }
        }
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void play(VideoFileWrapper file, Pane pane, ProducerService producerService) {
        if(currentPlayer != null){
            if(currentPlayer.getMediaPlayer().getStatus().equals(MediaPlayer.Status.PLAYING)){
                currentPlayer.getMediaPlayer().stop();
                producerService.clear();
                consume(file,pane);
            }
        }
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void resume() {
        if(currentPlayer != null){
            if(currentPlayer.getMediaPlayer().getStatus().equals(MediaPlayer.Status.STOPPED) ||
                    currentPlayer.getMediaPlayer().getStatus().equals(MediaPlayer.Status.PAUSED) ){
                currentPlayer.getMediaPlayer().play();
            }
        }
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void forward(double rate) {
        if(currentPlayer.getMediaPlayer().getRate() < 1.90) {
            currentPlayer.setPlayerRate(rate);
        }
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public boolean isPlaying() {
        return currentPlayer != null &&
                currentPlayer.getMediaPlayer().getStatus().equals(MediaPlayer.Status.PLAYING);
    }

}
