package com.player.ui;

import com.player.utils.ApplicationProperties;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.HashMap;
import java.util.Map;

//TODO: Implement
public class ProgressWindow implements ParentScreen {

    @Autowired
    @Qualifier("appProperties")
    private ApplicationProperties appProperties;

    //////////////////////////////  DECLARATIONS  /////////////////////////////

    private final String PROG_TITLE_CONST = "Video Loading Progress";
    private Map<String, ProgressIndicator> indicatorMap;
    private Stage mainStage;

    //////////////////////////////////////////////////////////////////////////
    public ProgressWindow(){
        indicatorMap = new HashMap<>();
    }

    //////////////////////////////////////////////////////////////////////////
    public Stage buildMainStage(){
        Stage stage = new Stage();

        Button hide = new Button("Hide");
        HBox hBox = buildHbox("progr-box",100,200, Pos.BASELINE_CENTER);
        hBox.getChildren().add(hide);

        StackPane gridpane = new StackPane();
        gridpane.getChildren().add(hBox);

        Scene scene = new Scene(gridpane,appProperties.getStdWidth() - 550, appProperties.getStdHeight() - 300);
        scene.getStylesheets().add(appProperties.getStyleSheet());
        stage.setScene(scene);
        stage.setTitle(PROG_TITLE_CONST);
        stage.setResizable(false);

        hide.setOnMouseClicked((evt)->{
            stage.hide();
        });

        return stage;
    }

    //////////////////////////////////////////////////////////////////////////
    public void show(){
        if(mainStage != null){
            mainStage.show();
        }
    }

    //////////////////////////////////////////////////////////////////////////
    public void init(){
        if(mainStage == null){
            mainStage = buildMainStage();
        }
    }

    //////////////////////////////////////////////////////////////////////////
    public boolean isShowing(){
        return mainStage != null && mainStage.isShowing();
    }

    //////////////////////////////////////////////////////////////////////////
    public void hide(){
        if(mainStage != null){
            mainStage.hide();
        }
    }

    //////////////////////////////////////////////////////////////////////////
    public void addNewIndicator( String fileName ){
        ProgressIndicator indicator = new ProgressIndicator();
        indicator.setProgress(0.0);
        indicatorMap.put(fileName, indicator);
    }

    //////////////////////////////////////////////////////////////////////////
    public void updateProgress( String fileName ){

    }

}
