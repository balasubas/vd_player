package com.player.entity;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;

public class VideoProxyImpl implements VideoProxy {

    //////////////////////////////  DECLARATIONS  /////////////////////////////

    private GridPane pane;
    private MediaPlayer mediaPlayer;
    private PropertyChangeSupport pcs;

    //////////////////////////////////////////////////////////////////////////
    public VideoProxyImpl(GridPane pane, MediaPlayer mediaPlayer){
        this.pane = pane;
        this.mediaPlayer = mediaPlayer;
        pcs = new PropertyChangeSupport(this);
        pcs.addPropertyChangeListener(this);
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void displayTemp() {

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
        displayActual();
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void fire(Object oldVal, Object newVal) {
        pcs.firePropertyChange("Player", oldVal, newVal);
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // TODO: need to detect a loading phase to display
        // a temp videowrapper:
        // https://docs.oracle.com/javase/8/javafx/api/javafx/scene/media/MediaPlayer.Status.html#READY
        if(evt.getOldValue() == null){
            System.out.println("NOTHING HERE");
        }
    }

}
