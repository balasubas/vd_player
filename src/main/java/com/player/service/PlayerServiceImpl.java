package com.player.service;

import com.player.entity.AudioControls;
import com.player.entity.Player;
import com.player.entity.VideoFileWrapper;
import com.player.utils.ApplicationProperties;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;


public class PlayerServiceImpl implements ConsumerService, PropertyChangeListener {

    //////////////////////////////  DECLARATIONS  /////////////////////////////

    @Autowired
    @Qualifier("frameService")
    private  FrameService frameService;

    @Autowired
    @Qualifier("queueService")
    private QueueService queueService;

    @Autowired
    @Qualifier("appProperties")
    private ApplicationProperties appProperties;

    private Player currentPlayer;
    private ChangeListener<? super Player> listener;
    private PropertyChangeSupport pcs;
    private Pane pane;
    private Slider slider;
    private  AudioControls audioControls;
    private boolean isPlayingFromQueue = false;
    private final Duration REWIND_CONST = new Duration(100);
    private static Logger logger;

    //////////////////////////////////////////////////////////////////////////
    public PlayerServiceImpl(){
        pcs = new PropertyChangeSupport(this);
        pcs.addPropertyChangeListener(this);
        logger = LogManager.getLogger(ConsumerService.class);
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void consume(VideoFileWrapper file, Pane pane) {
        GridPane gridPane = (GridPane) pane;

        Optional<Node> found =
                gridPane.getChildren().stream()
                                      .filter((node)-> node.getClass().equals(MediaView.class))
                                      .findFirst();

        found.ifPresent(node -> pane.getChildren().remove(node));

        try {
            currentPlayer = new Player(file,this,
                                       frameService, appProperties.getLogo("pending"),gridPane);

            if(currentPlayer.getMediaPlayer() != null){
                currentPlayer.getMediaPlayer()
                             .currentTimeProperty()
                             .addListener((evt)-> updateSlider());


                slider.valueProperty().addListener( observable -> {
                    if(slider.isValueChanging()){
                        Duration duration =
                                currentPlayer.getMediaPlayer().getTotalDuration();

                        currentPlayer.getMediaPlayer()
                                     .seek(duration.multiply( slider.getValue() / 100 ));
                    }
                });

                currentPlayer.play();

                if(audioControls.isMute()){
                    currentPlayer.getMediaPlayer().setVolume(0.0);
                }

                audioControls.setSliderValue(currentPlayer.getMediaPlayer().getVolume());
                audioControls.getVolumeSlider().valueProperty().addListener((changed)-> {
                    if(!audioControls.isMute()) {
                        currentPlayer.getMediaPlayer().setVolume(audioControls.getCurrentSliderValue());
                    }
                });

                audioControls.getMuteBtn().selectedProperty().addListener((changed)->{
                    if(audioControls.isMute()){
                        audioControls.setMutBtnText("Unmute");
                        currentPlayer.getMediaPlayer().setVolume(0.0);
                    }else{
                        audioControls.setMutBtnText("Mute");
                        currentPlayer.getMediaPlayer().setVolume(audioControls.getVolumeSlider().getValue());
                    }
                });

            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void playFromQueue(Pane pane) {
        // This only fires off the first video in the queue. The rest of the videos are fired off
        // via the property change listener. See the propertyChange method.

        if(!Objects.nonNull(this.pane)){
            this.pane = pane;
        }

        if(!queueService.isEmpty()) {
            isPlayingFromQueue = true;
            VideoFileWrapper videoFileWrapper = queueService.pop();
            consume(videoFileWrapper, this.pane);
        }
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void stop() {
        if(currentPlayer != null){
            if(currentPlayer.getMediaPlayer().getStatus().equals(MediaPlayer.Status.PLAYING) ||
                    currentPlayer.getMediaPlayer().getStatus().equals(MediaPlayer.Status.PAUSED) ){
                currentPlayer.stop();
                frameService.clearPlaybackPoints();
            }
        }
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void pause() {
        if(currentPlayer != null){
            currentPlayer.pause();
        }
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void play(VideoFileWrapper file, Pane pane, ProducerService producerService) {
        if(currentPlayer != null){

            if(currentPlayer.isPaused()){
                resume();
            }else{
                currentPlayer.getMediaPlayer().stop();
                producerService.clear();
                consume(file,pane);
            }

        }else{
            producerService.clear();
            consume(file,pane);
        }
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void resume() {
        if(currentPlayer != null){
            currentPlayer.resume();
        }
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void rewind() {
        // This is not as precise. From research, it appears this is the best it gets using the standard
        // JavaFX library
        if(currentPlayer != null){
            if(currentPlayer.isPlaying()) {
                currentPlayer.pause();
            }
            Thread th = (new Thread(() -> {
                currentPlayer.getMediaPlayer().setStartTime(Duration.seconds(frameService.back(2)/1000));
                currentPlayer.getMediaPlayer().seek(currentPlayer.getMediaPlayer().getStartTime());
            }));

            th.start();
            try {
                th.join();
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
        }
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void forward(double rate) {
        if(currentPlayer.getMediaPlayer().getRate() < 1.90) {
            currentPlayer.setPlayerRate(rate);
        }
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public boolean isPlaying() {
        return currentPlayer != null &&
                currentPlayer.isPlaying();
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public boolean isPaused() {
        return currentPlayer != null && currentPlayer.isPaused();
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void fire(String oldVal, String newVal){
        pcs.firePropertyChange("Player", oldVal, newVal);
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void setSlider(Slider slider) {
        this.slider = slider;
    }

    //////////////////////////////////////////////////////////////////////////s
    @Override
    public void setAudioControls(AudioControls audioControls) {
        this.audioControls = audioControls;
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final String stopped = "STOPPED";

        if(evt.getNewValue().equals(stopped)){
            if(!queueService.isEmpty() && isPlayingFromQueue) {
                VideoFileWrapper videoFileWrapper = queueService.pop();
                consume(videoFileWrapper, this.pane);
            }else{
                isPlayingFromQueue = false;
            }
        }
    }

    //////////////////////////////////////////////////////////////////////////
    private void updateSlider(){
        Platform.runLater(()->{
            Duration currentTime = currentPlayer.getMediaPlayer().getCurrentTime();
            slider.setValue((currentTime.toMillis() /
                    currentPlayer.getMediaPlayer().getTotalDuration().toMillis())
                    * 100.0 );
        });
    }
}
