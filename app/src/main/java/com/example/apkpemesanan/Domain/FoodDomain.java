package com.example.apkpemesanan.Domain;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class FoodDomain implements Serializable {
    private String title = "", pic = "", description = "";
    private double fee = 0.0;
    private int numberInCart = 0, id_menu = 0;



//    public FoodDomain(String title, String pic, String description, double fee, int star, int time, int calories) {
//        this.title = title;
//        this.pic = pic;
//        this.description = description;
//        this.fee = fee;
//        this.star = star;
//        this.time = time;
//        this.calories = calories;
//    }

    public FoodDomain(String title, String pic, double fee, int id_menu, String description) {
        this.title = title;
        this.fee = fee;
        this.pic = pic;
        this.id_menu = id_menu;
        this.description = description;
    }

    public int getNumberInCart() {
        return numberInCart;
    }

    public void setNumberInCart(int numberInCart) {
        this.numberInCart = numberInCart;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getId_menu() {
        return id_menu;
    }

    public void setId_menu(int id_menu) {
        this.id_menu = id_menu;
    }

    private int star, time, calories;

    @NonNull
    @Override
    public String toString() {
        return this.title;
    }

}
