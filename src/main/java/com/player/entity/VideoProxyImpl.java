package com.player.entity;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private String imageLogo;

    //////////////////////////////////////////////////////////////////////////
    public VideoProxyImpl(GridPane pane, MediaPlayer mediaPlayer, String imageLogo){
        this.pane = pane;
        this.mediaPlayer = mediaPlayer;
        this.imageLogo = imageLogo;
        pcs = new PropertyChangeSupport(this);
        pcs.addPropertyChangeListener(this);
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void displayTemp() {
        Image img = new Image(imageLogo);
        ImageView imageView = new ImageView(img);
        pane.setAlignment(Pos.CENTER);
        pane.add(imageView,1,1);
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
    public void propertyChange(PropertyChangeEvent evt) {
        // TODO: need to detect a loading phase to display
        // a temp videowrapper:
        // https://docs.oracle.com/javase/8/javafx/api/javafx/scene/media/MediaPlayer.Status.html#READY
        if(evt.getOldValue() == null){
            System.out.println("NOTHING HERE");
        }
    }

}
