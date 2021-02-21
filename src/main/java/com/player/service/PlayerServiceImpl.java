package com.player.service;

import com.player.entity.Player;
import com.player.entity.VideoFileWrapper;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.Optional;


public class PlayerServiceImpl implements ConsumerService, PropertyChangeListener {

    //////////////////////////////  DECLARATIONS  /////////////////////////////

    private Player currentPlayer;
    private ChangeListener<? super Player> listener;
    private PropertyChangeSupport pcs;

    //////////////////////////////////////////////////////////////////////////
    public PlayerServiceImpl(){
        pcs = new PropertyChangeSupport(this);
        pcs.addPropertyChangeListener(this);
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void consume(VideoFileWrapper file, Pane pane) {
        GridPane gridPane = (GridPane) pane;

        Optional<Node> found =
                gridPane.getChildren().stream()
                                      .filter((node)-> node.getClass().equals(MediaView.class))
                                      .findFirst();

        found.ifPresent(node -> pane.getChildren().remove(node));

        try {
            currentPlayer = new Player(file);
            //Register this service to the player so it
            //can update the events
            currentPlayer.register(this);
            if(currentPlayer.getMediaPlayer() != null){
                MediaView mediaView = new MediaView(currentPlayer.getMediaPlayer());
                mediaView.setFitWidth(450);
                mediaView.setFitHeight(350);
                gridPane.setAlignment(Pos.CENTER);
                gridPane.add(mediaView,1,1);
                currentPlayer.getMediaPlayer().play();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void playFromQueue(ProducerService producerService, Pane pane) {
        //TODO: Using a loop here is not the way to go
        // once the status changes to READY or PLAYING
        // this loop breaks and the play from queue is stopped
        // Maybe a better way to do this to to set a Listener on the Player
        // 1. If there is no player then just play.
        // 2. If the player is DONE, then grab the next queue entry.
        //

        while(!isPlaying()){
            //if(!isPlaying()) {
            if(producerService.isEmpty()){ break;}
                VideoFileWrapper videoFileWrapper = producerService.get();
                System.out.println("Now playing: " + videoFileWrapper.getVideoFile().getName() + " <<<<< ");
                consume(videoFileWrapper, pane);
            //}
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
            if(currentPlayer.isPlaying()){
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
                !currentPlayer.isPlaying();
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void fire(String oldVal, String newVal){
        pcs.firePropertyChange("Player", oldVal, newVal);
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("Name      = " + evt.getPropertyName());
        System.out.println("Old Value = " + evt.getOldValue());
        System.out.println("New Value = " + evt.getNewValue());
    }
}
