package com.player.ui;

import com.player.utils.ApplicationProperties;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

public class MainScreen implements ParentScreen {

    //////////////////////////////  DECLARATIONS  /////////////////////////////

    @Autowired
    @Qualifier("appProperties")
    private ApplicationProperties appProperties;

    private double leftPaneWidth = 220;

    //////////////////////////////////////////////////////////////////////////
    public Stage buildMainStage(){

        Stage primaryStage = new Stage();
        Button pause = new Button("Pause");
        Button stop = new Button("Stop");
        Button rewind = new Button("Back");
        Button play = new Button("Play");
        Button fastForward = new Button("Forward");

        VBox leftSide = configureLeftPanel(new VBox(new Label("Video Files")));
        Button open = new Button("+");
        open.setId("open-btn");
        open.setTooltip(buildToolTip(appProperties.getOpenBtnToolTip(),null));

        leftSide.getChildren().add(open);
        leftSide.setMaxWidth(leftPaneWidth);
        leftSide.setMinWidth(leftPaneWidth);

        VBox rightSide = new VBox(new Label("Now Playing ... "));
        HBox hBox = buildHbox("hbox-main", appProperties.getHboxHeight(),appProperties.getHboxWidth(),Pos.BASELINE_CENTER);
        hBox.getChildren().addAll(pause,rewind,play,fastForward,stop);


        GridPane gridPane = new GridPane();
        gridPane.setMinHeight(430);

        rightSide.getChildren().addAll(gridPane,hBox);

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(leftSide,rightSide);

        Scene scene =  new Scene(splitPane,appProperties.getStdWidth(), appProperties.getStdHeight());
        scene.getStylesheets().add(appProperties.getStyleSheet());
        primaryStage.setScene(scene);
        primaryStage.setTitle(appProperties.getMainTitle());
        primaryStage.setResizable(false);

        return primaryStage;
    }

    //////////////////////////////////////////////////////////////////////////
    private VBox configureLeftPanel(VBox vBox){
        TableView<String> tableView = new TableView<>();
        tableView.setMinHeight(250.00);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<String,String> column = new TableColumn<>();
        column.setCellValueFactory((cellData)->{ return new ReadOnlyStringWrapper(cellData.getValue());});
        tableView.getColumns().add(column);

        vBox.getChildren().add(tableView);
        vBox.setSpacing(10);

        return vBox;
    }

}
