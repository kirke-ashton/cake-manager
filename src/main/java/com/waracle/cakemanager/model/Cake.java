package com.waracle.cakemanager.model;

public class Cake {

    private Long id;

    private String title;

    private String desc;

    private String image;

    public Cake() {}

    public Cake(Long id, String title, String desc, String image) {

        this.id = id;
        this.title = title;
        this.desc = desc;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}