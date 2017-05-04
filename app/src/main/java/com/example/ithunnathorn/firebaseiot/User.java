package com.example.ithunnathorn.firebaseiot;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by ithunnathorn on 5/4/2017 AD.
 */
@IgnoreExtraProperties
public class User {
    public String name;
    public String email;

    public User(){

    }


    public User(String name, String email) {
        this.name = name;
        this.email= email;

    }
}
