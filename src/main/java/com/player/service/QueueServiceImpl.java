package com.player.service;

import com.player.entity.VideoFileWrapper;
import com.player.utils.ApplicationProperties;
import javafx.scene.media.MediaPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;

public class QueueServiceImpl implements QueueService {

    //////////////////////////////  DECLARATIONS  /////////////////////////////

    @Autowired
    @Qualifier("preloadService")
    private Preloader preloaderService;

    @Autowired
    @Qualifier("appProperties")
    private ApplicationProperties appProperties;

    private Queue<VideoFileWrapper> mainQueue;
    private Map<File, Future<MediaPlayer>> mediaQueue;
    private Map<File, Future<MediaPlayer>> tempQueue;
    private boolean mediaQueueIsBusy = false;
    private static Logger logger;

    //////////////////////////////////////////////////////////////////////////
    public QueueServiceImpl(){
        mainQueue = new ConcurrentLinkedQueue<>();
        mediaQueue = new ConcurrentHashMap<>();
        tempQueue = new HashMap<>();
        logger = LogManager.getLogger(QueueService.class);
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public int getSize() {
        return mainQueue.size();
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void push(VideoFileWrapper videoFileWrapper) {
        mainQueue.add(videoFileWrapper);
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public VideoFileWrapper pop() {
        return mainQueue.remove();
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public boolean isEmpty() {
        return mainQueue.isEmpty();
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void clear() {
        mainQueue.clear();
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void addAll(List<VideoFileWrapper> videoFileWrapperList) {
        mainQueue.addAll(videoFileWrapperList);
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public Map<File, Future<MediaPlayer>> getMediaQueue() {
        return mediaQueue;
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void addToMediaQueue(File file) {
        if(!mediaQueueIsBusy){
            mediaQueue.put(file,wrapFile(file));
        }else{
            tempQueue.put(file,wrapFile(file));
        }
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public List<VideoFileWrapper> cleanQueue() {
        mediaQueueIsBusy = true;
        List<VideoFileWrapper> done = new ArrayList<>();
        Iterator<File> fileIterator = mediaQueue.keySet().iterator();
        while(fileIterator.hasNext()){
            File file = fileIterator.next();
            if(mediaQueue.get(file).isDone()) {
                Future<MediaPlayer> mediaPlayerFuture = mediaQueue.remove(file);
                VideoFileWrapper videoFileWrapper =
                        new VideoFileWrapper(new File(appProperties.getThumbNail("camera")),
                                file, mediaPlayerFuture);
                logger.info("Loading video: " + file.getName());
                done.add(videoFileWrapper);
            }
        }

        mediaQueueIsBusy = false;
        return done;
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public Map<File, Future<MediaPlayer>> getPending() {
        Map<File, Future<MediaPlayer>> pending = new HashMap<>();

        Iterator<File> fileIterator = mediaQueue.keySet().iterator();
        while(fileIterator.hasNext()) {
            File file = fileIterator.next();
            if (!mediaQueue.get(file).isDone()) {
                Future<MediaPlayer> mediaPlayerFuture = mediaQueue.get(file);
                pending.put(file, mediaPlayerFuture);
            }
        }

        return pending;
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void transfer() {
        if(!tempQueue.isEmpty() && !mediaQueueIsBusy){
            Iterator<File> fileIterator = tempQueue.keySet().iterator();
            while(fileIterator.hasNext()){
                File file = fileIterator.next();
                Future<MediaPlayer> mediaPlayerFuture = mediaQueue.remove(file);
                mediaQueue.put(file,mediaPlayerFuture);
            }
        }
    }

    //////////////////////////////////////////////////////////////////////////
    private Future<MediaPlayer> wrapFile(File file){
        preloaderService.load(file);
        return preloaderService.getMediaPlayer();
    }

}
