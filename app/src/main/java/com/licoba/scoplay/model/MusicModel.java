package com.licoba.scoplay.model;

public class MusicModel {
    private String filename;
    private boolean isPlaying;

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public MusicModel(String filename) {
        this.filename = filename;
    }

    public MusicModel() {
    }

    @Override
    public String toString() {
        return "MusicModel{" +
                "filename='" + filename + '\'' +
                ", isPlaying=" + isPlaying +
                '}';
    }
}
