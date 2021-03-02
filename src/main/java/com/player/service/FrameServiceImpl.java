package com.player.service;

import java.util.Stack;

public class FrameServiceImpl implements FrameService {

    //////////////////////////////  DECLARATIONS  /////////////////////////////

    // Stacks are already synchronized.
    private Stack<Double> frameStack;

    //////////////////////////////////////////////////////////////////////////
    public FrameServiceImpl(){
        frameStack = new Stack<>();
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void addPlaybackPoint(double playbackPoint) {
        System.out.println(playbackPoint + " <<<<< ");
        frameStack.push(playbackPoint);
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public double back(int numberOfPoints) {
        double frameNum = 0;
        if(numberOfPoints <= 0){ numberOfPoints = 1; }

        while (numberOfPoints > 0 && !queueIsEmpty()){
            frameNum = frameStack.pop();
            numberOfPoints -= 1;
        }

        System.out.println(frameNum + " ***** ");

        return frameNum;
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void clearPlaybackPoints() {
        frameStack.clear();
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public boolean queueIsEmpty() {
        return frameStack.isEmpty();
    }

}
