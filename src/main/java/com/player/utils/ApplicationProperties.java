package com.player.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@PropertySource("classpath:application.properties")
public class ApplicationProperties {

    //////////////////////////////  DECLARATIONS  /////////////////////////////

    @Value( "${standard.height}" )
    private int stdHeight;

    @Value( "${standard.width}" )
    private int stdWidth;

    @Value("${app.title}")
    private String mainTitle;

    @Value("${css.file}")
    private String cssSubdir;

    //////////////////////////////////////////////////////////////////////////
    public int getStdHeight() {
        return stdHeight;
    }

    //////////////////////////////////////////////////////////////////////////
    public int getStdWidth() {
        return stdWidth;
    }

    //////////////////////////////////////////////////////////////////////////
    public String getMainTitle(){ return mainTitle; }

    //////////////////////////////////////////////////////////////////////////
    public String getStyleSheet(){
        Path path = Paths.get(System.getProperty("user.dir"),cssSubdir);
        return "file:///" + path.toAbsolutePath().toString();
    }

}
