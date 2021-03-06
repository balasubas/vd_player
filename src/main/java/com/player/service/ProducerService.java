package com.player.service;

import com.player.entity.VideoFileWrapper;

import java.util.List;

public interface ProducerService {

    //////////////////////////////////////////////////////////////////////////
    void add( VideoFileWrapper videoFileWrapper );

    //////////////////////////////////////////////////////////////////////////
    VideoFileWrapper get();

    //////////////////////////////////////////////////////////////////////////
    VideoFileWrapper get( int index );

    //////////////////////////////////////////////////////////////////////////
    void load(List<VideoFileWrapper> videoFileWrapperList);

    //////////////////////////////////////////////////////////////////////////
    boolean isEmpty();

    //////////////////////////////////////////////////////////////////////////
    void clear();

}
