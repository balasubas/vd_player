package com.player.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.util.Duration;


public interface ParentScreen {

    //////////////////////////////////////////////////////////////////////////
    default HBox buildHbox(String id, int height,int width, Pos position){
        HBox hBox = new HBox();
        hBox.setId(id);
        hBox.setMinHeight(height);
        hBox.setMinWidth(width);
        hBox.setAlignment(position);
        return hBox;
    }

    //////////////////////////////////////////////////////////////////////////
    default Tooltip buildToolTip(String message, Duration duration){
        if(null == duration){
            duration = new Duration(3000);
        }

        Tooltip tooltip = new Tooltip(message);
        tooltip.setShowDuration(duration);
        return tooltip;
    }

}
