package com.draft.draftlunch.Models;

import androidx.annotation.Nullable;

import java.util.List;

public class User {

    private String uid;
    private String username;
    private String email;
    private String reservation;
    private List<String> restaurantLiked;
    @Nullable
    private String urlPicture;
    private List<User> allUsers;
    private List<Result> allRestaurants;

    public User() {
    }

    public User(String username, @Nullable String urlPicture ,@Nullable String reservation) {
        this.username = username;
        this.urlPicture = urlPicture;
        this.reservation = reservation;
    }

    public User(String uid, String username, String email, @Nullable String urlPicture,String reservation,List<String> restaurantLiked) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.urlPicture = urlPicture;
        this.reservation = reservation;
        this.restaurantLiked = restaurantLiked;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Nullable
    public String getUrlPicture() {
        return urlPicture;
    }

    public void setUrlPicture(@Nullable String urlPicture) {
        this.urlPicture = urlPicture;
    }

    public String getReservation() {
        return reservation;
    }

    public void setReservation(String reservation) {
        this.reservation = reservation;
    }

    public List<String> getRestaurantLiked() {
        return restaurantLiked;
    }

    public void setRestaurantLiked(List<String> restaurantLiked) {
        this.restaurantLiked = restaurantLiked;
    }


}
