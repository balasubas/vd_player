package com.player.ui;

import com.player.utils.ApplicationProperties;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * TODO:
 *  1. Start styling this
 *  2. improve the layout
 *
 *
 * **/

public class MainScreen {

    //////////////////////////////  DECLARATIONS  /////////////////////////////

    @Autowired
    @Qualifier("appProperties")
    private ApplicationProperties appProperties;

    private double leftPaneWidth = 250;

    //////////////////////////////////////////////////////////////////////////
    public Stage buildMainStage(){

        Stage primaryStage = new Stage();
        Button closeBtn = new Button("Close");


        VBox leftSide = new VBox(new Label("Left Side"));
        leftSide.setMaxWidth(leftPaneWidth);
        leftSide.setMinWidth(leftPaneWidth);

        VBox rightSide = new VBox(new Label("Right Side"));
        HBox hBox = new HBox();
        hBox.setId("hbox-main");
        hBox.getChildren().addAll(closeBtn);


        GridPane gridPane = new GridPane();
        gridPane.setMinHeight(430);
        gridPane.addRow(0,new Label("Media"));
        gridPane.addRow(1,new Label("Media"));
        gridPane.addRow(2,new Label("Media"));
        gridPane.addRow(3,new Label("Media"));
        gridPane.addRow(4,new Label("Media"));

        rightSide.getChildren().addAll(gridPane,hBox);

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(leftSide,rightSide);

        Scene scene =  new Scene(splitPane,appProperties.getStdWidth(), appProperties.getStdHeight());
        scene.getStylesheets().add(appProperties.getStyleSheet());
        primaryStage.setScene(scene);
        primaryStage.setTitle(appProperties.getMainTitle());
        return primaryStage;
    }

}
