package com.player.service;

import com.player.entity.VideoFileWrapper;


public class PlayerServiceImpl implements ConsumerService {

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void consume(VideoFileWrapper file) {

    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void playFromQueue(ProducerService producerService) {
        while(!producerService.isEmpty()){
            consume(producerService.get());
        }
    }

}
