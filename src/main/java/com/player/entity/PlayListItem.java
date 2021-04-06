package com.player.entity;

import java.io.File;

public class PlayListItem {

    //////////////////////////////  DECLARATIONS  /////////////////////////////

    private File location;
    private String name;

    ///////////////////////////////////////////////////////////////////////////
    public PlayListItem( File location ){
        this.location = location;
        this.name = location.getName();
    }

    ///////////////////////////////////////////////////////////////////////////
    public File getLocation() {
        return location;
    }

    ///////////////////////////////////////////////////////////////////////////
    public String getName() {
        return name;
    }

}
