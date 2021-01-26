package com.player.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class ApplicationProperties {

    //////////////////////////////  DECLARATIONS  /////////////////////////////

    @Value( "${standard.height}" )
    private int stdHeight;

    @Value( "${standard.width}" )
    private int stdWidth;

    //////////////////////////////////////////////////////////////////////////
    public int getStdHeight() {
        return stdHeight;
    }

    //////////////////////////////////////////////////////////////////////////
    public int getStdWidth() {
        return stdWidth;
    }
}
