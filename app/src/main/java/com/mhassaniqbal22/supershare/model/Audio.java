package com.mhassaniqbal22.supershare.model;

public class Audio {

    private String title;
    private int icon;
    private String size;
    private String path;
    private boolean isSelected = false;

    public Audio(String title, int icon, String size, String path) {
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

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
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
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
