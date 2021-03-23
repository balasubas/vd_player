package com.player.ui;

import com.player.utils.ApplicationProperties;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

//TODO:
// polish the display and layout.
public class ProgressWindow implements ParentScreen {

    @Autowired
    @Qualifier("appProperties")
    private ApplicationProperties appProperties;

    //////////////////////////////  DECLARATIONS  /////////////////////////////

    private final String PROG_TITLE_CONST = "Video Loading Progress";
    private Map<Future<MediaPlayer>, ProgressIndicator> indicatorMap;
    private Stage mainStage;
    private VBox vBox;

    //////////////////////////////////////////////////////////////////////////
    public ProgressWindow(){
        indicatorMap = new HashMap<>();
    }

    //////////////////////////////////////////////////////////////////////////
    public Stage buildMainStage(){
        Stage stage = new Stage();

//        Button hide = new Button("Hide");
        vBox = buildVbox("progr-box",200,100, Pos.CENTER);
        vBox.setSpacing(5);
        vBox.setPadding(new Insets(10, 50, 50, 50));
        vBox.getChildren().addAll(new Label("Loading"));

        StackPane gridpane = new StackPane();
        gridpane.getChildren().add(vBox);

        Scene scene = new Scene(gridpane,appProperties.getStdWidth() - 650, appProperties.getStdHeight() - 300);
        scene.getStylesheets().add(appProperties.getStyleSheet());
        stage.setScene(scene);
        stage.setTitle(PROG_TITLE_CONST);
        stage.setResizable(false);

//        hide.setOnMouseClicked((evt)->{
//            stage.hide();
//        });

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
    public void addNewIndicator( Future<MediaPlayer> fileName, String fileActual ){
        if(!indicatorMap.containsKey(fileName)) {
            ProgressIndicator indicator = new ProgressIndicator();
            indicator.setProgress(0.0);
            indicatorMap.putIfAbsent(fileName, indicator);
            HBox hBox = new HBox();
            hBox.setMinWidth(100);
            hBox.setMinHeight(25);
            hBox.getChildren().addAll(new Label(fileActual + ": "), indicator);
            vBox.getChildren().addAll(hBox, new Label(""));
        }
    }

    //////////////////////////////////////////////////////////////////////////
    @Scheduled(fixedRate = 500)
    public void updateProgress(){
        if(!indicatorMap.isEmpty()){
            indicatorMap.forEach((key, value) -> {
                if (!key.isDone()) {
                    Platform.runLater(() -> {
                        double current = value.getProgress();
                        value.setProgress(current + 0.05);
                    });
                }
            });

            List<Future<MediaPlayer>> removable =
                    indicatorMap.keySet()
                            .stream()
                            .filter(Future::isDone)
                            .collect(Collectors.toList());

            if(!removable.isEmpty()) {
                Platform.runLater(()->{
                    removable.forEach((toRemove)->{
                        ProgressIndicator indicator = indicatorMap.remove(toRemove);
                        Optional<Node> filteredHbox =  vBox.getChildren()
                                                           .stream()
                                                           .filter((hb)-> hb.getClass().getSimpleName().equals("HBox"))
                                                           .filter((hb)-> ((HBox) hb).getChildren().contains(indicator))
                                                           .findFirst();

                        filteredHbox.ifPresent(node -> vBox.getChildren().remove(node));
                    });
                });
            }
        }
    }

}
