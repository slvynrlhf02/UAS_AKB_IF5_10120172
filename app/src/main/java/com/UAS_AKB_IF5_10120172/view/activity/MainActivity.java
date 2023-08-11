package com.UAS_AKB_IF5_10120172.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.UAS_AKB_IF5_10120172.R;
import com.UAS_AKB_IF5_10120172.view.fragment.InfoFragment;
import com.UAS_AKB_IF5_10120172.view.fragment.NoteFragment;
import com.UAS_AKB_IF5_10120172.view.fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        auth = FirebaseAuth.getInstance();

        //menampilkan halaman yang pertama muncul
        getFragmentPage(new InfoFragment());

        //insialisasi bottom nav
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                switch (item.getItemId()){
                    case
                            R.id.info:
                        fragment = new InfoFragment();
                        break;
                    case
                            R.id.note:
                        fragment = new NoteFragment();
                        break;
                    case
                            R.id.profile:
                        fragment = new ProfileFragment();
                        break;
                }
                return getFragmentPage(fragment);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            // Not signed in, launch the Login activity
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    private boolean getFragmentPage(Fragment fragment){
        if (fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.page_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}

// 10120172
// Silvyani Nurlaila Husnina Fajrin
// IF5
