package com.player.service;

public interface FrameService {

    //////////////////////////////////////////////////////////////////////////
    void addPlaybackPoint(double playbackPoint);

    //////////////////////////////////////////////////////////////////////////
    double back(int numberOfPoints);

    //////////////////////////////////////////////////////////////////////////
    void clearPlaybackPoints();

    //////////////////////////////////////////////////////////////////////////
    boolean queueIsEmpty();

}
