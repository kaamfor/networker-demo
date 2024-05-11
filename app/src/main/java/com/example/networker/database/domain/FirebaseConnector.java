package com.example.networker.database.domain;

import com.google.firebase.database.FirebaseDatabase;

public class FirebaseConnector {
    private FirebaseDatabase firebaseDatabase;

    public FirebaseConnector() {
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    public FirebaseDatabase getDBInstance() {
        return firebaseDatabase;
    }
}
