package com.player.service;

import com.player.entity.Player;
import com.player.entity.VideoFileWrapper;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
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

    private Player currentPlayer;
    private ChangeListener<? super Player> listener;
    private PropertyChangeSupport pcs;
    private Pane pane;
    private ProducerService producerService;
    private boolean isPlayingFromQueue = false;
    private final Duration REWIND_CONST = new Duration(100);

    //////////////////////////////////////////////////////////////////////////
    public PlayerServiceImpl(){
        pcs = new PropertyChangeSupport(this);
        pcs.addPropertyChangeListener(this);
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
            currentPlayer = new Player(file);
            //Register this service to the player so it
            //can update the events
            currentPlayer.register(this);
            if(currentPlayer.getMediaPlayer() != null){
                currentPlayer.getMediaPlayer().currentTimeProperty().addListener((durs)->{
                    System.out.println(currentPlayer.getMediaPlayer().getCurrentTime() + " <<<<< ");
                });

                MediaView mediaView = new MediaView(currentPlayer.getMediaPlayer());
                mediaView.setFitWidth(450);
                mediaView.setFitHeight(350);
                gridPane.setAlignment(Pos.CENTER);
                gridPane.add(mediaView,1,1);
                currentPlayer.getMediaPlayer().play();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void playFromQueue(ProducerService producerService, Pane pane) {
        // This only fires off the first
        // video in the queue. The rest of the videos are fired off
        // via the property change listener.
        // see the propertyChange method.

        if(!Objects.nonNull(this.pane)){
            this.pane = pane;
        }

        if(!Objects.nonNull(this.producerService)){
            this.producerService = producerService;
        }

        if(!producerService.isEmpty()) {
            isPlayingFromQueue = true;
            VideoFileWrapper videoFileWrapper = producerService.get();
            consume(videoFileWrapper, this.pane);
        }
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void stop() {
        if(currentPlayer != null){
            if(currentPlayer.getMediaPlayer().getStatus().equals(MediaPlayer.Status.PLAYING) ||
                    currentPlayer.getMediaPlayer().getStatus().equals(MediaPlayer.Status.PAUSED) ){
                currentPlayer.getMediaPlayer().stop();
            }
        }
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void pause() {
        if(currentPlayer != null){
            if(currentPlayer.getMediaPlayer().getStatus().equals(MediaPlayer.Status.PLAYING)){
                currentPlayer.getMediaPlayer().pause();
            }
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
            if(currentPlayer.getMediaPlayer().getStatus().equals(MediaPlayer.Status.PAUSED) ){
                currentPlayer.getMediaPlayer().seek(currentPlayer.getPauseTime());
                currentPlayer.getMediaPlayer().play();
            }
        }
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void rewind() {

        if(currentPlayer != null){
            currentPlayer.getMediaPlayer().pause();
            Thread th = (new Thread(()->{
                // TODO: you will need a new class to store the frames.
                // remove this dummy value once done
                currentPlayer.getMediaPlayer().setStartTime(Duration.millis(2360.930978));
                currentPlayer.getMediaPlayer().seek(currentPlayer.getMediaPlayer().getStartTime());
            }));

            th.start();
            try {
                th.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            currentPlayer.getMediaPlayer().play();
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
    public void propertyChange(PropertyChangeEvent evt) {
        final String stopped = "STOPPED";

        if(evt.getNewValue().equals(stopped)){
            if(!this.producerService .isEmpty() && isPlayingFromQueue) {
                VideoFileWrapper videoFileWrapper = this.producerService .get();
                consume(videoFileWrapper, this.pane);
            }else{
                isPlayingFromQueue = false;
            }
        }
    }
}
