package com.player.service;

import com.player.entity.VideoFileWrapper;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueServiceImpl implements QueueService {

    //////////////////////////////  DECLARATIONS  /////////////////////////////

    private Queue<VideoFileWrapper> mainQueue;

    //////////////////////////////////////////////////////////////////////////
    public QueueServiceImpl(){
        mainQueue = new ConcurrentLinkedQueue<>();
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

}
