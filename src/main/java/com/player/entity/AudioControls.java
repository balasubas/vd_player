package com.player.entity;

import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;

public class AudioControls {

    //////////////////////////////  DECLARATIONS  /////////////////////////////

    private Slider volumeSlider;
    private RadioButton muteBtn;

    //////////////////////////////////////////////////////////////////////////
    public AudioControls(Slider volumeSlider, RadioButton muteBtn ){
        this.volumeSlider = volumeSlider;
        this.muteBtn = muteBtn;
        this.muteBtn.selectedProperty().addListener((changed)->{
            if(this.muteBtn.isSelected()){
                this.muteBtn.setText("Unmute");
            }else{
                this.muteBtn.setText("Mute");
            }
        });
    }

    //////////////////////////////////////////////////////////////////////////
    public Slider getVolumeSlider(){
        return this.volumeSlider;
    }

    //////////////////////////////////////////////////////////////////////////
    public double getCurrentSliderValue(){
        return this.volumeSlider.getValue();
    }

    //////////////////////////////////////////////////////////////////////////
    public void setSliderValue(double value){
        this.volumeSlider.setValue(value);
    }

    //////////////////////////////////////////////////////////////////////////
    public boolean isMute(){
        return muteBtn.isSelected();
    }

    //////////////////////////////////////////////////////////////////////////
    public RadioButton getMuteBtn(){
        return this.muteBtn;
    }

    //////////////////////////////////////////////////////////////////////////
    public void setMutBtnText(String text){
        this.muteBtn.setText(text);
    }

}
