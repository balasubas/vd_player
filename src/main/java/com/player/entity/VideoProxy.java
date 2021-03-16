package com.player.entity;

import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.media.MediaPlayer;

import java.beans.PropertyChangeListener;

public interface VideoProxy extends PropertyChangeListener {

    //////////////////////////////////////////////////////////////////////////
    default void displayTemp(GridPane pane){
        ((ImageView)pane.getChildren().get(0)).setVisible(true);
    };

    //////////////////////////////////////////////////////////////////////////
    void displayActual();

    //////////////////////////////////////////////////////////////////////////
    void play();

    //////////////////////////////////////////////////////////////////////////
    void stop();

    //////////////////////////////////////////////////////////////////////////
    void pause();

    //////////////////////////////////////////////////////////////////////////
    void fire(Object oldVal, Object newVal);

    //////////////////////////////////////////////////////////////////////////
    void setMediaPlayer(MediaPlayer mediaPlayer);
}
