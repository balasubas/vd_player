package com.player.entity;

import java.util.HashSet;
import java.util.Set;

public class PlayList {

    //////////////////////////////  DECLARATIONS  /////////////////////////////

    private String name;
    private Set<PlayListItem> playListItems;

    //////////////////////////////////////////////////////////////////////////
    public PlayList(String name){
        this.setName(name);
        this.setPlayListItems(new HashSet<>());
    }

    //////////////////////////////////////////////////////////////////////////
    public String getName() {
        return name;
    }

    //////////////////////////////////////////////////////////////////////////
    public Set<PlayListItem> getPlayListItems() {
        return playListItems;
    }

    //////////////////////////////////////////////////////////////////////////
    public void addItem(PlayListItem item){
        this.playListItems.add(item);
    }

    //////////////////////////////////////////////////////////////////////////
    public void setName(String name) {
        this.name = name;
    }

    //////////////////////////////////////////////////////////////////////////
    public void setPlayListItems(Set<PlayListItem> playListItems) {
        this.playListItems = playListItems;
    }

}
