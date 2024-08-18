package com.mhassaniqbal22.supershare.model;

import android.graphics.drawable.Drawable;

public class App {

    private String title;
    private Drawable icon;
    private String size;
    private String path;
    private boolean selected;

    public App(String title, Drawable icon, String size, String path) {
        this.title = title;
        this.icon = icon;
        this.size = size;
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
