package com.player.entity;

import java.io.File;

public class VideoFileWrapper {

    ///////////////////////////     DECLARATIONS   ////////////////////////////

    private File icon;
    private File videoFile;

    //////////////////////////////////////////////////////////////////////////
    public VideoFileWrapper(File icon, File videoFile ){
        this.setIcon(icon);
        this.setVideoFile(videoFile);
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
}
