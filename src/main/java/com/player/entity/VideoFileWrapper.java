package com.player.entity;

import javafx.scene.media.MediaPlayer;
import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class VideoFileWrapper {

    ///////////////////////////     DECLARATIONS   ////////////////////////////

    private File icon;
    private File videoFile;
    private MediaPlayer mediaPlayer;

    //////////////////////////////////////////////////////////////////////////
    public VideoFileWrapper(File icon, File videoFile, Future<MediaPlayer> mediaPlayer )  {
        this.setIcon(icon);
        this.setVideoFile(videoFile);
        try {
            this.mediaPlayer = mediaPlayer.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    //////////////////////////////////////////////////////////////////////////
    public File getIcon() {
        return icon;
    }

    //////////////////////////////////////////////////////////////////////////
    public void setIcon(File icon) {
        this.icon = icon;
    }

    //////////////////////////////////////////////////////////////////////////
    public File getVideoFile() {
        return videoFile;
    }

    //////////////////////////////////////////////////////////////////////////
    public void setVideoFile(File videoFile) {
        this.videoFile = videoFile;
    }

    //////////////////////////////////////////////////////////////////////////
    public MediaPlayer getMediaPlayer(){
        return mediaPlayer;
    }
}
