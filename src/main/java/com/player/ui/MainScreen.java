package com.player.ui;

import com.player.utils.ApplicationProperties;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


/**
 * Start styling this and improve the layout
 *
 * **/

public class MainScreen {

    //////////////////////////////  DECLARATIONS  /////////////////////////////

    @Autowired
    @Qualifier("appProperties")
    private ApplicationProperties appProperties;

    //////////////////////////////////////////////////////////////////////////
    public Stage buildMainStage(){
        Stage primaryStage = new Stage();
        Button closeBtn = new Button("Close");
        StackPane pane = new StackPane();
        pane.getChildren().add(closeBtn);
        Scene scene =  new Scene(pane,appProperties.getStdWidth(), appProperties.getStdHeight());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Main Video Player");
        return primaryStage;
    }

}
