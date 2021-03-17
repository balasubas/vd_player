package com.player;

import com.player.ui.MainScreen;
import javafx.application.Application;
import javafx.stage.Stage;
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
        mainStage.setOnCloseRequest((event)->{
            // TODO: this does not completely shutdown everything
            applicationContext.close();
        });
        mainStage.show();
    }

    //////////////////////////////////////////////////////////////////////////
    public static void main(String ... args){
            launch(args);
    }

}
