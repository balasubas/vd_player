package com.player.service;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
            Media media = null;
            MediaPlayer mediaPlayer = null;
            try {
                media = new Media(file.toURI().toURL().toExternalForm());
                mediaPlayer = new MediaPlayer(media);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return mediaPlayer;
        });
    }
}
