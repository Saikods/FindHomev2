package com.joseluis.findhomev2.pojo;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class Rents implements Parcelable {
    private String street_name;
    private String province;
    private String town;
    private String description;
    private float rating;
    private float price;
    private List<String> images;
    private List<String> extras;


    public Rents(String street_name, String province, String town, String description, float rating, float price, List<String> images, List<String> extras) {
        this.street_name = street_name;
        this.province = province;
        this.town = town;
        this.description = description;
        this.rating = rating;
        this.price = price;
        this.images = images;
        this.extras = extras;
    }

    public Rents(String street_name, String province, String town, String description, float rating, float price, List<String> images) {
        this.street_name = street_name;
        this.province = province;
        this.town = town;
        this.description = description;
        this.rating = rating;
        this.price = price;
        this.images = images;
    }

    public Rents() {
    }


    protected Rents(Parcel in) {
        street_name = in.readString();
        province = in.readString();
        town = in.readString();
        description = in.readString();
        rating = in.readFloat();
        price = in.readFloat();
        images = in.createStringArrayList();
        extras = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(street_name);
        dest.writeString(province);
        dest.writeString(town);
        dest.writeString(description);
        dest.writeFloat(rating);
        dest.writeFloat(price);
        dest.writeStringList(images);
        dest.writeStringList(extras);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Rents> CREATOR = new Creator<Rents>() {
        @Override
        public Rents createFromParcel(Parcel in) {
            return new Rents(in);
        }

        @Override
        public Rents[] newArray(int size) {
            return new Rents[size];
        }
    };

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getStreet_name() {
        return street_name;
    }

    public void setStreet_name(String street_name) {
        this.street_name = street_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public List<String> getExtras() {
        return extras;
    }

    public void setExtras(ArrayList<String> extras) {
        this.extras = extras;
    }
}
