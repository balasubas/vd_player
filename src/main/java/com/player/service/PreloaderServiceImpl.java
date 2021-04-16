package com.player.service;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


import java.io.File;
import java.net.MalformedURLException;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/*********************************************************************
*
*   This preloads the instantiation of the MediaPlayer
*   as these objects take some time to create causing the user
*   to have to wait. What this does is create the media player via
*   a Future.
*
* ********************************************************************/
public class PreloaderServiceImpl implements Preloader {

    //////////////////////////////  DECLARATIONS  /////////////////////////////

    private Stack<Future<MediaPlayer>> mediaPlayerStack;
    private ExecutorService executor;

    //////////////////////////////////////////////////////////////////////////
    public PreloaderServiceImpl(){
        mediaPlayerStack = new Stack<>();
        executor = Executors.newSingleThreadExecutor();
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void load(File file) {
        Future<MediaPlayer> promise = loadFuture(file);
        mediaPlayerStack.push(promise);
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public Future<MediaPlayer> getMediaPlayer(){
        return mediaPlayerStack.pop();
    }

    //////////////////////////////////////////////////////////////////////////
    private Future<MediaPlayer> loadFuture(File file){
        return executor.submit(()->{
            Media media = new Media(file.toPath().toUri().toURL().toExternalForm());
            return new MediaPlayer(media);
        });
    }
}
