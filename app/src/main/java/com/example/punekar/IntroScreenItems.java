package com.example.punekar;

public class IntroScreenItems {

    String Title, Description;
    int Image;

    public IntroScreenItems(String title, String description, int image) {
        Title = title;
        Description = description;
        Image = image;
    }

    public String getTitle() {
        return Title;
    }

    public String getDescription() {
        return Description;
    }

    public int getImage() {
        return Image;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setImage(int image) {
        Image = image;
    }
}
