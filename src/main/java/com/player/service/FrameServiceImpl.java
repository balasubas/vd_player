package com.player.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
        Double precise = BigDecimal.valueOf(playbackPoint).setScale(2, RoundingMode.DOWN).doubleValue();
        frameStack.push(precise);
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
