package com.draft.draftlunch.Services;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserRepository {

    private static final String COLLECTION_USERS = "users";
    private static final String USERNAME_FIELD = "username";
    private static final String RESERVATION_FIELD = "reservation";
    private static final String RESTAURANT_LIKED_FIELD = "restaurantLiked";
    private static final String PICTURE_FIELD = "urlPicture";
    private static final String ID_FIELD = "uid";
    private static volatile UserRepository instance;
    private static final MutableLiveData<List<User>> liveUsers = new MutableLiveData<List<User>>(){};
    private static final List<User> users = new ArrayList<>();
    private static Location location = new Location("location");
    private static String userId;
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
        return FirebaseFirestore.getInstance().collection(COLLECTION_USERS);
    }

    // Create User in Firestore
    public void createUser() {
        FirebaseUser user = getCurrentUser();
        if(user != null){
            String urlPicture = (user.getPhotoUrl() != null) ? user.getPhotoUrl().toString() : null;
            String username = user.getDisplayName();
            String email = user.getEmail();
            String uid = user.getUid();
            String reservation = "";


            User userToCreate = new User(uid, username, email, urlPicture, reservation,null);

            Task<DocumentSnapshot> userData = getUserData();


            // If the user already exist in Firestore, we get his data
            userData.addOnSuccessListener(documentSnapshot ->
                    this.getUsersCollection().document(uid).set(userToCreate)
            );
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
    // Get User Reservation from Firestore
    public Task<DocumentSnapshot> getReservation(){
        String uid = this.getCurrentUserUID();
        if(uid != null){
            return this.getUsersCollection().document(uid).get(Source.valueOf(RESERVATION_FIELD));
        }else{
            return null;
        }
    }
    // Get User's favorite Restaurant from Firestore
    public Task<DocumentSnapshot> getlikedRestaurant(){
        String uid = this.getCurrentUserUID();
        if(uid != null){
            return this.getUsersCollection().document(uid).get(Source.valueOf(RESTAURANT_LIKED_FIELD));
        }else{
            return null;
        }
    }
    // Delete the User from Firebase
    public Task<Void> deleteUser(Context context){
        userId = this.getCurrentUserUID();
        return AuthUI.getInstance().delete(context);
    }

    public void deleteUser(){
        String uid = this.getCurrentUserUID();
        Log.e(TAG, "delete: "+uid );
        Objects.requireNonNull(getCurrentUser()).delete().addOnCompleteListener(task -> {
            Log.e(TAG, "delete from firebase" );
            if (task.isSuccessful()) {
                Log.e(TAG, "delete from firestore in progress: " );
                this.getUsersCollection().document(uid).delete();
            }
        });
    }

    // Delete the User from Firestore
    public void deleteUserFromFirestore() {
        if(userId != null){
            this.getUsersCollection().document(userId).delete();
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
                        user.setUid(documentSnapshot.getString(ID_FIELD));
                        users.add(user);
                        setLiveUsers(users);
                }
            }
        }).addOnSuccessListener(aVoid -> RestaurantRepository.FetchRestaurants(getLocation().getLatitude() + "," + getLocation().getLongitude()));
    }

    // Set a reservation in Firestore
    public void addReservation(String reservation) {
        String uid = this.getCurrentUserUID();
        if(uid != null) {

            this.getUsersCollection().document(uid).update(RESERVATION_FIELD, reservation);
        }
    }

    // Add a liked restaurant in Firestore
    public void addRestaurantLiked(String restaurantLiked) {
        String uid = this.getCurrentUserUID();
        if(uid != null){

            this.getUsersCollection().document(uid).update(RESTAURANT_LIKED_FIELD, FieldValue.arrayUnion(restaurantLiked));
        }
    }

    public List<Result> CrossDataUsersAndRestaurant(MutableLiveData<List<Result>> allRestaurants){
        if (liveUsers.getValue() != null){
            for(int i = 0 ; i < liveUsers.getValue().size() ; i++){
                if(allRestaurants != null){
                    for(int y = 0; y < Objects.requireNonNull(allRestaurants.getValue()).size() ; y++){
                        if (liveUsers.getValue().get(i).getReservation().equals(allRestaurants.getValue().get(y).getName())){
                            Log.e(TAG, "LOOP : " + liveUsers.getValue().get(i).getUsername() + " -- " + allRestaurants.getValue().get(y).getName() );
                            allRestaurants.getValue().get(i).addHasBeenReservedBy(liveUsers.getValue().get(i));
                        }
                    }
                }
            }
        }
        return Objects.requireNonNull(allRestaurants).getValue();
    }

    public List<User> getJoiningUsers(String restaurant) {
        List<User> joiningUsers = new ArrayList<>();
        for(int i = 0; i< Objects.requireNonNull(liveUsers.getValue()).size() ; i++){
            if(liveUsers.getValue().get(i).getReservation()==null){
                continue;
            }
            if (liveUsers.getValue().get(i).getReservation().equals(restaurant)){
                joiningUsers.add(liveUsers.getValue().get(i));
            }

        }
        return joiningUsers;
    }

    public LiveData<List<User>> getUsers() {
        return liveUsers;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        UserRepository.location = location;
    }

    public static MutableLiveData<List<User>> getLiveUsers() {
        return liveUsers;
    }

    public static void setLiveUsers(List<User> users) {
        liveUsers.setValue(users);
    }
}
