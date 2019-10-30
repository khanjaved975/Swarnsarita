package com.project.jewelmart.swarnsarita.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.ArrayList;

/**
 * Created by t3d on 13/06/16.
 */
public class Collection implements Parcelable, ParentListItem {

    public String id;
    public String col_name;
    public String short_code;
    public String position;
    public String count;
    public ArrayList<Collection> subcol;
    public String image_type;
    public String col_image;
    public String default_sort;
    public String showonHomescreen;

    public Collection(Parcel in) {
        id = in.readString();
        col_name = in.readString();
        short_code = in.readString();
        position = in.readString();
        col_image = in.readString();
        count = in.readString();
        default_sort = in.readString();
        image_type = in.readString();
        subcol = in.createTypedArrayList(Collection.CREATOR);
    }

    @Override
    public ArrayList<Collection> getChildItemList() {
        return subcol;
    }

    public void setChildItemList(ArrayList<Collection> list) {
        subcol = list;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    public static final Creator<Collection> CREATOR = new Creator<Collection>() {
        @Override
        public Collection createFromParcel(Parcel in) {
            return new Collection(in);
        }

        @Override
        public Collection[] newArray(int size) {
            return new Collection[size];
        }
    };

    public Collection() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(col_name);
        dest.writeString(short_code);
        dest.writeString(position);
        dest.writeString(col_image);
        dest.writeString(count);
        dest.writeString(default_sort);
        dest.writeString(image_type);
        dest.writeTypedList(subcol);
    }

}
