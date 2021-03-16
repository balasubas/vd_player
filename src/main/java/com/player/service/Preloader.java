package com.player.service;

import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.concurrent.Future;

public interface Preloader {

    //////////////////////////////////////////////////////////////////////////
    void load(File file);

    //////////////////////////////////////////////////////////////////////////
    Future<MediaPlayer> getMediaPlayer();
}
