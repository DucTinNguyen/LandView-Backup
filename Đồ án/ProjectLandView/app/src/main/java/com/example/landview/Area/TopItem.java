package com.example.landview.Area;

import java.io.Serializable;

public class TopItem implements Serializable {
    private int background;
    private int icon;
    private String name,textDescription,mLink;

    public TopItem() {
    }

    public String getTextDescription() {
        return textDescription;
    }

    public void setTextDescription(String textDescription) {
        this.textDescription = textDescription;
    }

    public String getmLink() {
        return mLink;
    }

    public void setmLink(String mLink) {
        this.mLink = mLink;
    }

    public TopItem(int background, int icon, String name, String textDescription, String mLink) {
        this.background = background;
        this.icon = icon;
        this.name = name;
        this.textDescription = textDescription;
        this.mLink = mLink;
    }

    public TopItem(int background, int icon, String name, String des) {
        this.background = background;
        this.icon = icon;
        this.name = name;
        this.textDescription = des;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
