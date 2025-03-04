package com.mhassaniqbal22.supershare.model;

public class Device {

    private String title;
    private int icon;

    public Device(int icon, String title) {
        this.title = title;
        this.icon = icon;
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
}
