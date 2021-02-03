package com.player.ui;

import com.player.utils.ApplicationProperties;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * TODO:
 *   1. For the ui control buttons, you have to add back the css style for hover
 *   and pressed. These disappear when adding background colors.
 *
 *
 * **/

public class MainScreen implements ParentScreen {

    //////////////////////////////  DECLARATIONS  /////////////////////////////

    @Autowired
    @Qualifier("appProperties")
    private ApplicationProperties appProperties;

    private final double leftPaneWidth = 220;
    private final double gridPaneHeight = 430;
    private final double tableViewHeight = 250;
    private final double spacing = 10;
    private  FileChooser chooser;
    private List<File> vidFiles;

    //////////////////////////////////////////////////////////////////////////
    public Stage buildMainStage(){
        Stage primaryStage = new Stage();
        chooser = new FileChooser();
        vidFiles = new ArrayList<>();

        VBox leftSide = configureLeftPanel(new VBox(new Label("Video Files")));
        Button open = new Button("+");
        open.setOnAction((actionEvent)->{
            List<File> temp = chooser.showOpenMultipleDialog(primaryStage);
            if(Objects.nonNull(temp)) {
                temp.stream().filter(Objects::nonNull)
                            .forEach((file) -> vidFiles.add(file));
            }
        });

        open.setId("open-btn");
        open.setTooltip(buildToolTip(appProperties.getOpenBtnToolTip(),null));

        leftSide.getChildren().add(open);
        leftSide.setMaxWidth(leftPaneWidth);
        leftSide.setMinWidth(leftPaneWidth);

        VBox rightSide = new VBox(new Label("Now Playing ... "));
        HBox hBox = configureVidControls();

        GridPane gridPane = new GridPane();
        gridPane.setMinHeight(gridPaneHeight);

        rightSide.getChildren().addAll(gridPane,hBox);

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(leftSide,rightSide);

        Scene scene = new Scene(splitPane,appProperties.getStdWidth(), appProperties.getStdHeight());
        scene.getStylesheets().add(appProperties.getStyleSheet());
        primaryStage.setScene(scene);
        primaryStage.setTitle(appProperties.getMainTitle());
        primaryStage.setResizable(false);

        return primaryStage;
    }

    //////////////////////////////////////////////////////////////////////////
    private VBox configureLeftPanel(VBox vBox){
        //TODO: This needs refactored to display an icon and filename in each row.
        TableView<String> tableView = new TableView<>();
        tableView.setMinHeight(tableViewHeight);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<String,String> column = new TableColumn<>();
        column.setCellValueFactory((cellData)-> new ReadOnlyStringWrapper(cellData.getValue()));
        tableView.getColumns().add(column);

        vBox.getChildren().add(tableView);
        vBox.setSpacing(spacing);

        return vBox;
    }

    //////////////////////////////////////////////////////////////////////////
    private HBox configureVidControls(){
        Button pause = setImage(new Button(), "pause","ui-control");
        Button stop = setImage(new Button(), "stop","ui-control");
        Button rewind = setImage(new Button(), "back", "ui-control");
        Button play = setImage(new Button(), "play","ui-control");
        play.setOnAction((actionEvent)->{
            System.out.println("Now Playing ... ");
        });

        Button fastForward = setImage(new Button(), "forward","ui-control");

        HBox hBox = buildHbox("hbox-main", appProperties.getHboxHeight(),
                                                appProperties.getHboxWidth(),
                                                Pos.BASELINE_CENTER);
        hBox.getChildren().addAll(pause,rewind,play,fastForward,stop);

        return hBox;
    }

    //////////////////////////////////////////////////////////////////////////
    private Button setImage(Button button, String imageName, String id){
        Image img = new Image(appProperties.getLogo(imageName));
        ImageView view = new ImageView(img);
        view.setFitHeight(appProperties.getBtnFitHeight());
        view.setPreserveRatio(true);
        button.setGraphic(view);
        button.setId(id);
        return button;
    }
}
