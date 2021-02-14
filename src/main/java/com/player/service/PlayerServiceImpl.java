package com.player.service;

import com.player.entity.Player;
import com.player.entity.VideoFileWrapper;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaView;

import java.io.IOException;


public class PlayerServiceImpl implements ConsumerService {

    //////////////////////////////  DECLARATIONS  /////////////////////////////


    //////////////////////////////////////////////////////////////////////////
    public PlayerServiceImpl(){

    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void consume(VideoFileWrapper file, Pane pane) {
        try {
            Player player = new Player(file);
            if(player.getMediaPlayer() != null){
                MediaView mediaView = new MediaView(player.getMediaPlayer());
                ((GridPane) pane).add(mediaView,1,1);
                player.getMediaPlayer().play();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void playFromQueue(ProducerService producerService, Pane pane) {
        while(!producerService.isEmpty()){
            consume(producerService.get(),pane);
        }
    }

}
