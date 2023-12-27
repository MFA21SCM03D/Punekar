package com.example.punekar.Models;



public class Data {

    private String Name;
    private int Thumbnail;
    private String Description;

    public Data(String name, int thumbnail, String description) {
        Name = name;
        Thumbnail = thumbnail;
        Description = description;
    }

    public Data() {
    }

    public String getName() {
        return Name;
    }

    public int getThumbnail() {
        return Thumbnail;
    }

    public String getDescription() {
        return Description;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setThumbnail(int thumbnail) {
        Thumbnail = thumbnail;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
