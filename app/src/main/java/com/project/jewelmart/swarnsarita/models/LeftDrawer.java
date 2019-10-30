package com.project.jewelmart.swarnsarita.models;

public class LeftDrawer {

    String Name;
    String Count;
    int imgResID;

    public String catId;
    public String catName;
    public String catCount;
    public String whichMaster;

    public LeftDrawer(String Name, String Count) {
        super();
        this.Name = Name;
        this.Count = Count;

        //this.imgResID = imgResID;
    }

    public LeftDrawer() {

    }

    public String getName() {
        return Name;
    }

    public void setName(String itemName) {
        Name = itemName;
    }

    public String getCount() {
        return Count;
    }

    public void setCount(String itemCount) {
        Count = itemCount;
    }




}
