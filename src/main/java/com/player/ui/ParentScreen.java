package com.player.ui;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    default VBox buildVbox(String id, int height,int width, Pos position){
        VBox vBox = new VBox();
        vBox.setId(id);
        vBox.setMinHeight(height);
        vBox.setMinWidth(width);
        vBox.setAlignment(position);
        return vBox;
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

    //////////////////////////////////////////////////////////////////////////
    default Slider builStandardSlider( String id, double min, double max, double value,
                                       double height, double width ){
        Slider slider = new Slider(min, max, value);
        slider.setId(id);
        slider.setOrientation(Orientation.HORIZONTAL);
        slider.setMinHeight(height);
        slider.setMaxWidth(width);
        slider.setBlockIncrement(1000);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);

        return slider;
    }

    //////////////////////////////////////////////////////////////////////////
    default Alert generateAlert(String message, Alert.AlertType alertType){
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        return alert;
    }

}
