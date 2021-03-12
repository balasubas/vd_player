package com.player.entity;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;

public class VideoProxyImpl implements VideoProxy {

    //////////////////////////////  DECLARATIONS  /////////////////////////////

    private GridPane pane;
    private MediaPlayer mediaPlayer;
    private PropertyChangeSupport pcs;
    private String imageLogo;
    private ImageView imageView;

    //////////////////////////////////////////////////////////////////////////
    public VideoProxyImpl(GridPane pane){
        this.pane = pane;
        pcs = new PropertyChangeSupport(this);
        pcs.addPropertyChangeListener(this);
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void displayActual() {
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setFitWidth(450);
        mediaView.setFitHeight(350);

        pane.setAlignment(Pos.CENTER);
        pane.add(mediaView,1,1);
        mediaPlayer.play();
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void play() {
        // TODO: this is where we might have to do the switch.
        displayActual();
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void stop() {
        mediaPlayer.stop();
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void pause() {
        mediaPlayer.pause();
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void fire(Object oldVal, Object newVal) {
        pcs.firePropertyChange("Player", oldVal, newVal);
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // TODO: need to detect a loading phase to display
        if(evt.getOldValue() == null){
            System.out.println("NOTHING HERE");
        }
    }

}
