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

    private long pid;

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void start(Stage stage) throws Exception {
        Stage mainStage = mainScreen.buildMainStage();
        pid = ProcessHandle.current().pid();
        mainStage.setOnCloseRequest(shutdownSequence());
        mainStage.show();
    }

    //////////////////////////////////////////////////////////////////////////
    public static void main(String ... args){
            launch(args);
    }

    //////////////////////////////////////////////////////////////////////////
    private EventHandler<WindowEvent> shutdownSequence(){
        return (event)->{
            String os = System.getProperty("os.name");
            String command = "kill -9 " + pid;
            try {
                applicationContext.close();
                this.stop();
                Platform.exit();

                if(os.contains("Mac OS")){
                    Runtime.getRuntime().exec(command);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        };
    }

}
