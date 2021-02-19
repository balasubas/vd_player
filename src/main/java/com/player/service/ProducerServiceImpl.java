package com.player.service;

import com.player.entity.VideoFileWrapper;

import java.util.LinkedList;
import java.util.List;

public class ProducerServiceImpl implements ProducerService {

    //////////////////////////////  DECLARATIONS  /////////////////////////////

    private LinkedList<VideoFileWrapper> playQueue;

    //////////////////////////////////////////////////////////////////////////
    public ProducerServiceImpl(){
        playQueue = new LinkedList<>();
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void add(VideoFileWrapper videoFileWrapper) {
        playQueue.push(videoFileWrapper);
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public VideoFileWrapper get() {
        return playQueue.isEmpty() ? null: playQueue.pop();
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public VideoFileWrapper get(int index) {
        return playQueue.get(index);
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void load(List<VideoFileWrapper> videoFileWrapperList) {
        playQueue.addAll(videoFileWrapperList);
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public boolean isEmpty() {
        return playQueue.isEmpty();
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void clear() {
        playQueue.clear();
    }

}
