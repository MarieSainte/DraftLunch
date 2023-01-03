package com.draft.draftlunch.Services;

import android.content.Context;
import android.location.Location;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.draft.draftlunch.Models.Result;
import com.draft.draftlunch.Models.User;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private static final String COLLECTION_NAME = "users";
    private static final String USERNAME_FIELD = "username";
    private static final String RESERVATION_FIELD = "reservation";
    private static final String RESTAURANT_LIKED_FIELD = "restaurantLiked";
    private static final String PICTURE_FIELD = "urlPicture";
    private static volatile UserRepository instance;
    private static List<User> users = new ArrayList<>();
    private static LiveData<List<Result>> allRestaurants;
    private static Location location;

    public UserRepository() { }

    public static UserRepository getInstance() {
        UserRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized(UserRepository.class) {
            if (instance == null) {
                instance = new UserRepository();
            }
            return instance;
        }
    }

    @Nullable
    public FirebaseUser getCurrentUser(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public String getCurrentUserUID(){
        FirebaseUser user = getCurrentUser();
        return (user != null)? user.getUid() : null;
    }

    public Task<Void> signOut(Context context){
        return AuthUI.getInstance().signOut(context);
    }

    // Get the Collection Reference
    private CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // Create User in Firestore
    public void createUser() {
        FirebaseUser user = getCurrentUser();
        if(user != null){
            String urlPicture = (user.getPhotoUrl() != null) ? user.getPhotoUrl().toString() : null;
            String username = user.getDisplayName();
            String email = user.getEmail();
            String uid = user.getUid();
            String reservation = null;
            List<String> restaurantLiked = null;

            User userToCreate = new User(uid, username, email, urlPicture, null,null);

            Task<DocumentSnapshot> userData = getUserData();
            // If the user already exist in Firestore, we get his data (isMentor)
            userData.addOnSuccessListener(documentSnapshot -> this.getUsersCollection().document(uid).set(userToCreate));
        }
    }

    // Get User Data from Firestore
    public Task<DocumentSnapshot> getUserData(){
        String uid = this.getCurrentUserUID();
        if(uid != null){
            return this.getUsersCollection().document(uid).get();
        }else{
            return null;
        }
    }

    // Delete the User from Firebase
    public Task<Void> deleteUser(Context context){
        return AuthUI.getInstance().delete(context);
    }

    // Delete the User from Firestore
    public void deleteUserFromFirestore() {
        String uid = this.getCurrentUserUID();
        if(uid != null){
            this.getUsersCollection().document(uid).delete();
        }
    }

    // Get all users in Firestore
    public void fetchUsers(){
        String uid = this.getCurrentUserUID();
        getUsersCollection()
                .get()
                .addOnCompleteListener(task -> {

            if (task.isSuccessful() && task.getResult()!=null){

                for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                    if(uid == null){
                        continue;
                    }
                    if (uid.equals(documentSnapshot.getId())) {
                        continue;
                    }
                        User user = new User();
                        user.setUsername(documentSnapshot.getString(USERNAME_FIELD));
                        user.setUrlPicture(documentSnapshot.getString(PICTURE_FIELD));
                        user.setReservation(documentSnapshot.getString(RESERVATION_FIELD));
                        users.add(user);

                }
            }
        });
    }

    // Update User Username
    public Task<Void> updateUsername(String username) {
        String uid = this.getCurrentUserUID();
        if(uid != null){
            return this.getUsersCollection().document(uid).update(USERNAME_FIELD, username);
        }else{
            return null;
        }
    }

    // Set a reservation in Firestore
    public Task<Void> addReservation(String reservation) {
        String uid = this.getCurrentUserUID();
        if(uid != null){
            return this.getUsersCollection().document(uid).update(RESERVATION_FIELD, reservation);
        }else{
            return null;
        }
    }

    // Add a liked restaurant in Firestore
    public Task<Void> addRestaurantLiked(String restaurantLiked) {
        String uid = this.getCurrentUserUID();
        if(uid != null){
            FirebaseUser user = getCurrentUser();

            return this.getUsersCollection().document(uid).update(RESTAURANT_LIKED_FIELD, FieldValue.arrayUnion(restaurantLiked));
        }else{
            return null;
        }
    }

    public LiveData<List<Result>> CrossDataUsersAndRestaurant(LiveData<List<Result>> allRestaurants){
        this.allRestaurants = allRestaurants;
        for(int i = 0 ; i < users.size() ; i++){
            for(int y = 0 ; y < allRestaurants.getValue().size() ; y++){
                if (users.get(i).getReservation().equals(allRestaurants.getValue().get(y).getName())){
                    allRestaurants.getValue().get(i).addHasBeenReservedBy(users.get(i));
                }
            }
        }
        return allRestaurants;
    }

    public LiveData<List<User>> getJoiningUsers(String restaurant) {
        LiveData<List<User>> joiningUsers = null;
        for(int i=0; i<users.size() ; i++){
            if (users.get(i).getReservation().equals(restaurant)){
                joiningUsers.getValue().add(users.get(i));
            }

        }
        return joiningUsers;
    }

    public LiveData<List<User>> getUsers() {
        return (LiveData<List<User>>) users;
    }

    public LiveData<List<Result>> getAllRestaurants() {
        return allRestaurants;
    }

    public void setAllRestaurants(LiveData<List<Result>> allRestaurants) {
        this.allRestaurants = allRestaurants;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
