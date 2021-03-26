package com.player.ui;

import com.player.utils.ApplicationProperties;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;
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

        vBox = buildVbox("progr-box",200,100, Pos.CENTER);
        vBox.setSpacing(2);
        vBox.setPadding(new Insets(10, 50, 50, 50));
        vBox.getChildren().addAll(new Label(""));

        Image img = new Image(appProperties.getLogo("pending"));
        BackgroundImage backgroundImage = new BackgroundImage(img,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO,
                        BackgroundSize.AUTO,
                        false,
                        false,
                        false,
                        true )
        );

        Background background = new Background(backgroundImage);

        StackPane stackPane = new StackPane();
        vBox.setBackground(background);
        stackPane.getChildren().add(vBox);


        Scene scene = new Scene(stackPane,appProperties.getStdWidth() - 650, appProperties.getStdHeight() - 300);
        scene.getStylesheets().add(appProperties.getStyleSheet());
        stage.setScene(scene);
        stage.setTitle(PROG_TITLE_CONST);
        stage.setResizable(false);

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
            indicator.setMinWidth(100);
            indicator.setMinHeight(35);
            indicatorMap.putIfAbsent(fileName, indicator);

            HBox hBox = new HBox();
            hBox.setMinWidth(200);
            hBox.setMinHeight(35);
            hBox.setSpacing(2);
            hBox.setPadding(new Insets(5, 10, 10, 10));
            hBox.setId("prog-ind");

            Label label = new Label(fileActual + ": ");
            label.setWrapText(true);

            hBox.getChildren().addAll(label, indicator);
            vBox.getChildren().addAll(hBox, new Label(""));
        }
    }

    //////////////////////////////////////////////////////////////////////////
    @Scheduled(fixedRate = 500)
    public void updateProgress(){
        if(!indicatorMap.isEmpty()){
            Platform.runLater(() -> {
                        if(!((Label)(vBox.getChildren().get(0))).getText().equals("Loading")){
                            ((Label)(vBox.getChildren().get(0))).setText("Loading");
                        }
                    });

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
        }else{
           if(Objects.nonNull(vBox)) {
               Platform.runLater(() -> ((Label) (vBox.getChildren().get(0))).setText(""));
           }
        }
    }

}
