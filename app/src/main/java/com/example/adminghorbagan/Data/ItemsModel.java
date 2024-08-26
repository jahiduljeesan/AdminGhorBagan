package com.example.adminghorbagan.Data;

public class ItemsModel {
    String title,intro,description,process,imageUrl,category,tag;

    public ItemsModel() {
    }

    public ItemsModel(String title, String intro, String description, String process, String imageUrl, String category,String tag) {
        this.title = title;
        this.intro = intro;
        this.description = description;
        this.process = process;
        this.imageUrl = imageUrl;
        this.category = category;
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
