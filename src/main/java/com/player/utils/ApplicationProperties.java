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

    @Value( "${forward.rate.increment}" )
    private double stdRateIncrease;

    @Value( "${standard.height}" )
    private int stdHeight;

    @Value( "${standard.width}" )
    private int stdWidth;

    @Value("${app.title}")
    private String mainTitle;

    @Value("${css.file}")
    private String cssSubdir;

    @Value("${hbox.height}")
    private int hboxHeight;

    @Value("${hbox.width}")
    private int hboxWidth;

    @Value("${open.btn.tooltip}")
    private String openBtnToolTip;

    @Value("${image.dir}")
    private String imageSubdir;

    @Value("${icon.btn.fit.height}")
    private int btnFitHeight;

    @Value("${clear.btn.tooltip}")
    private String clearBtnToolTip;

    @Value("${playlist.dir}")
    private String playlistSubdir;

    @Value("${output.to.console}")
    private boolean outToConsole;

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

    //////////////////////////////////////////////////////////////////////////
    public int getHboxHeight(){
        return hboxHeight;
    }

    //////////////////////////////////////////////////////////////////////////
    public int getHboxWidth() {
        return hboxWidth;
    }

    //////////////////////////////////////////////////////////////////////////
    public String getOpenBtnToolTip() {
        return openBtnToolTip;
    }

    //////////////////////////////////////////////////////////////////////////
    public int getBtnFitHeight(){ return btnFitHeight; }

    //////////////////////////////////////////////////////////////////////////
    public String getLogo(String logoName){

        StringBuilder sb = new StringBuilder();
        sb.append("file:///")
          .append(System.getProperty("user.dir"))
          .append("/")
          .append(imageSubdir);

        switch(logoName){
            case "pause": sb.append("pause.png");
                          break;

            case "stop": sb.append("stop.png");
                         break;

            case "forward": sb.append("forward.png");
                            break;

            case "back": sb.append("back.png");
                         break;

            case "pending": sb.append("pending.png");
                         break;

            default: sb.append("play.png");
                     break;
        }

        return sb.toString();
    }

    //////////////////////////////////////////////////////////////////////////
    public String getThumbNail( String thumbnail){

        StringBuilder sb = new StringBuilder();
        sb.append(System.getProperty("user.dir"))
          .append("/")
          .append(imageSubdir);

        switch(thumbnail){

            default: sb.append("camera.png");
                break;
        }

        return sb.toString();
    }

    //////////////////////////////////////////////////////////////////////////
    public String getClearBtnToolTip(){
        return clearBtnToolTip;
    }

    //////////////////////////////////////////////////////////////////////////
    public double getStdRateIncrease(){
        return stdRateIncrease;
    }

    //////////////////////////////////////////////////////////////////////////
    public String getPlaylistDir(){
        String currDir = System.getProperty("user.dir");
        return currDir + playlistSubdir;
    }

    //////////////////////////////////////////////////////////////////////////
    public boolean isOutToConsole() {
        return outToConsole;
    }
}
