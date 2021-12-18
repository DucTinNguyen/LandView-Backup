package com.example.landview.HomeFragmentSection.Services;

public class ItemService {
    private int bg_Resource;//background item
    private int rsID;//hình ảnh icon các chức năng
    private String name;

    public ItemService() {
    }

    public ItemService(int bg_Resource, int rsID, String name) {
        this.bg_Resource = bg_Resource;
        this.rsID = rsID;
        this.name = name;
    }

    public int getBg_Resource() {
        return bg_Resource;
    }

    public void setBg_Resource(int bg_Resource) {
        this.bg_Resource = bg_Resource;
    }

    public int getRsID() {
        return rsID;
    }

    public void setRsID(int rsID) {
        this.rsID = rsID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
