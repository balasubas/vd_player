package com.player.service;

import com.player.entity.VideoFileWrapper;
import javafx.scene.layout.Pane;

public interface ConsumerService {

    //////////////////////////////////////////////////////////////////////////
    void consume(VideoFileWrapper file, Pane pane);

    //////////////////////////////////////////////////////////////////////////
    void playFromQueue(ProducerService producerService, Pane pane);
}
