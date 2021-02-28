package com.player.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FrameServiceImpl implements FrameService {

    //////////////////////////////  DECLARATIONS  /////////////////////////////

    private Map<Integer, Double> mapQueue;

    //////////////////////////////////////////////////////////////////////////
    public FrameServiceImpl(){
        mapQueue = new ConcurrentHashMap<>();
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void addPlaybackPoint(double playbackPoint) {

    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public double back(int numberOfPoints) {

        if(numberOfPoints <= 0){ numberOfPoints = 1; }

        return 0;
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void clearPlaybackPoints() {
        mapQueue.clear();
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public boolean queueIsEmpty() {
        return mapQueue.isEmpty();
    }

}
