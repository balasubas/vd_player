package com.player;

import com.player.entity.LogSpecification;
import com.player.ui.MainScreen;
import com.player.utils.ApplicationProperties;
import com.player.utils.LoggingUtils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static ApplicationProperties applicationProperties =
            (ApplicationProperties) applicationContext.getBean("appProperties");

    private static final String applicationLogsName = "";
    private static Logger logger;

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void start(Stage stage) throws Exception {

        // TODO: encapsulate into it's own method
        LogSpecification logSpecification =
                new LogSpecification("application.log", "logs", Level.INFO, Level.INFO,
        true, "ApplicationLogAppender");

        LogSpecification logSpecificationErr =
                new LogSpecification("error.log", "logs", Level.ERROR, Level.ERROR,
                        true, "ErrorLogAppender");

        LoggingUtils.initializeLogger(logSpecificationErr);
        LoggingUtils.initializeLogger(logSpecification);

        logger = LogManager.getLogger(MainPlayerWindow.class);

        Stage mainStage = mainScreen.buildMainStage();
        mainStage.setOnCloseRequest(shutdownSequence());
        logger.info("Started the application... ");
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
                logger.info("Closing application... ");
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
