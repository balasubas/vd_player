package com.player.service;

import com.player.entity.VideoFileWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

public class ProducerServiceImpl implements ProducerService {

    //////////////////////////////  DECLARATIONS  /////////////////////////////

    @Autowired
    @Qualifier("queueService")
    private QueueService queueService;

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void add(VideoFileWrapper videoFileWrapper) {
        queueService.push(videoFileWrapper);
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public VideoFileWrapper get() {
        return queueService.isEmpty() ? null: queueService.pop();
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public VideoFileWrapper get(int index) {
        return null;
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void load(List<VideoFileWrapper> videoFileWrapperList) {
        queueService.addAll(videoFileWrapperList);
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public boolean isEmpty() {
        return queueService.isEmpty();
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void clear() {
        queueService.clear();
    }

}
