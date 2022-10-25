package com.emran.groceryapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.emran.groceryapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //make fullscreen
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        //firebase auth for authentication related tasks
        firebaseAuth = FirebaseAuth.getInstance();

        //start authenticating user status after 1sec
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //check if user logged in or not
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user==null){
                    //user not logged in start LoginActivity
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
                else {
                    //user is logger in, check user type
                    checkUserType();
                }
            }
        }, 1000);
    }



    private void checkUserType() {
        //if user is seller, start seller main screen
        //if user is buyer, start user main screen

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String accountType = ""+dataSnapshot.child("accountType").getValue();
                        if (accountType.equals("Seller")){
                            //user is seller
                            startActivity(new Intent(SplashActivity.this, MainSellerActivity.class));
                            finish();
                        }
                        else {
                            //user is buyer
                            startActivity(new Intent(SplashActivity.this, MainUserActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }



}
