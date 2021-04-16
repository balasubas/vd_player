package com.player;

import com.player.ui.MainScreen;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;

@Configuration
@ComponentScan
public class MainPlayerWindow extends Application {

    //////////////////////////////  DECLARATIONS  /////////////////////////////

    private static AbstractApplicationContext applicationContext =
            new AnnotationConfigApplicationContext(MainPlayerWindow.class);

    private static MainScreen mainScreen =
            (MainScreen) applicationContext.getBean("mainScreen");

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void start(Stage stage) throws Exception {
        Stage mainStage = mainScreen.buildMainStage();
        mainStage.setOnCloseRequest(shutdownSequence());
        mainStage.show();
    }

    //////////////////////////////////////////////////////////////////////////
    public static void main(String ... args){
            launch(args);
    }

    //////////////////////////////////////////////////////////////////////////
    private EventHandler<WindowEvent> shutdownSequence(){
        return (event)-> Platform.runLater(()->{
            try {
                applicationContext.close();
                this.stop();
                Platform.exit();
                System.exit(0);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
