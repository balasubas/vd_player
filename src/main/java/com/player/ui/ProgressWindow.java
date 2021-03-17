package com.player.ui;

import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

//TODO: Implement
public class ProgressWindow implements ParentScreen {

    //////////////////////////////  DECLARATIONS  /////////////////////////////

    private Map<String, ProgressIndicator> indicatorMap;

    //////////////////////////////////////////////////////////////////////////
    public ProgressWindow(){
        indicatorMap = new HashMap<>();
    }
    //////////////////////////////////////////////////////////////////////////
    public Stage buildMainStage(){
        return null;
    }

    //////////////////////////////////////////////////////////////////////////
    public void addNewIndicator( String fileName ){
        ProgressIndicator indicator = new ProgressIndicator();
        indicator.setProgress(0.0);
        indicatorMap.put(fileName, indicator);
    }

}
