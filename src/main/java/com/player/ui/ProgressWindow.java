package com.player.ui;

import com.player.utils.ApplicationProperties;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

//TODO:
// now that thr progress indicator shows and updates
// organize it in this window so that it's won indicator per line
// also polish the display.
public class ProgressWindow implements ParentScreen {

    @Autowired
    @Qualifier("appProperties")
    private ApplicationProperties appProperties;

    //////////////////////////////  DECLARATIONS  /////////////////////////////

    private final String PROG_TITLE_CONST = "Video Loading Progress";
    private Map<Future<MediaPlayer>, ProgressIndicator> indicatorMap;
    private Stage mainStage;
    private  HBox hBox;

    //////////////////////////////////////////////////////////////////////////
    public ProgressWindow(){
        indicatorMap = new HashMap<>();
    }

    //////////////////////////////////////////////////////////////////////////
    public Stage buildMainStage(){
        Stage stage = new Stage();

        Button hide = new Button("Hide");
        hBox = buildHbox("progr-box",100,200, Pos.BASELINE_CENTER);
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
    public void addNewIndicator( Future<MediaPlayer> fileName ){
        if(!indicatorMap.containsKey(fileName)) {
            ProgressIndicator indicator = new ProgressIndicator();
            indicator.setProgress(0.0);
            indicatorMap.putIfAbsent(fileName, indicator);
            hBox.getChildren().add(indicator);
        }
    }

    //////////////////////////////////////////////////////////////////////////
    @Scheduled(fixedRate = 500)
    public void updateProgress(){
        if(!indicatorMap.isEmpty()){
            indicatorMap.entrySet().forEach((entry)->{
                if(!entry.getKey().isDone()){
                    Platform.runLater(()->{
                        double current = entry.getValue().getProgress();
                        entry.getValue().setProgress(current + 0.05);
                    });
                }
            });

            List<Future<MediaPlayer>> removable =
                    indicatorMap.keySet()
                            .stream()
                            .filter(Future::isDone)
                            .collect(Collectors.toList());

            if(!removable.isEmpty()) {
                removable.forEach(indicatorMap::remove);
            }
        }
    }

}
